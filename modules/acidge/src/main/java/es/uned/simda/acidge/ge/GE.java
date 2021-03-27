/*
**  Analog Circuit Design using Grammatical Evolution (ACID-GE)
**  Analog Circuit Design using Multigrammatical Evolution (ACID-MGE)
**
**  Copyright (c) 2021 Federico Castejon, Enrique J. Carmona
**
**  This program is free software: you can redistribute it and/or modify
**  it under the terms of the GNU General Public License as published by
**  the Free Software Foundation, either version 3 of the License, or
**  (at your option) any later version.
**
**  This program is distributed in the hope that it will be useful,
**  but WITHOUT ANY WARRANTY; without even the implied warranty of
**  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
**  GNU General Public License for more details.
**
**  You should have received a copy of the GNU General Public License
**  along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
/*
** Clase GE
*
* implementa grammatical evolution
*/
package es.uned.simda.acidge.ge;

import java.util.Iterator;
import java.util.logging.Logger;

import es.uned.simda.acidge.ge.operadores.OperadorDuplicacion;
import es.uned.simda.acidge.ge.operadores.OperadorInicializacion;
import es.uned.simda.acidge.ge.operadores.OperadorMutacion;
import es.uned.simda.acidge.ge.operadores.OperadorRecombinacion;
import es.uned.simda.acidge.ge.operadores.OperadorReemplazo;
import es.uned.simda.acidge.ge.operadores.OperatorFactory;
import es.uned.simda.acidge.ge.operadores.Selector;
import es.uned.simda.acidge.ge.random.RandomGenerator;
import es.uned.simda.acidge.ge.random.RandomGeneratorFactory;
import es.uned.simda.acidge.generador.Generador;
import es.uned.simda.acidge.generador.GeneradorFactory;
import es.uned.simda.acidge.problema.ProblemFactory;
import es.uned.simda.acidge.problema.Problema;
import es.uned.simda.acidge.util.LoggerStart;


public class GE
{
	private final static Logger logger = Logger.getLogger(GE.class.getName());

	final private GEProperties geproperties;
	private Poblacion poblacion;
	private int generacion;
	private Problema problema;
	private Problema problemaConv;
	private TerminationCondition terminationCondition;
	private Selector selector;
	private OperadorInicializacion inicializacion;
	private OperadorMutacion mutacion;
	private OperadorRecombinacion recombinacion;
	private OperadorDuplicacion duplicacion;
	private OperadorReemplazo reemplazo;
	private RandomGenerator randomGenerator;
	
	private int individualsNumber;

	double initialBestFitness;
	double finalBestFitness;
	
	//medidas de tiempo
	long tInit;
	long tTotal;


	/*
	 * Constructor
	 * 
	 * Realiza las inicializaciones necesarias
	 */
	GE(GEProperties geprop)
	{	
		logger.severe("TFM: Grammatical Evolution");
		logger.severe("--------------------------");
		
		geproperties = geprop;
		
		RandomGeneratorFactory randomGeneratorFactory = 
			RandomGeneratorFactory.getRandomGeneratorFactory();
		randomGenerator = randomGeneratorFactory.createRandomGenerator(
				geproperties.getRandomGeneratorClassName());

		GeneradorFactory generadorFactory = GeneradorFactory.getFactory();
		generadorFactory.initClass(geproperties.getGeneratorClassName(), geproperties, geproperties.getGrammarFileName());
		
		//Crea problema en caso de ejecución no paralela

		ProblemFactory probFactory = ProblemFactory.getProblemFactory();
		problemaConv = probFactory.createProblema(geproperties.getProblemClassName(), geproperties);
		
		//Conveniencia para creación de directorios sólo en caso de desarrollo de circuitos
		problemaConv.setNombreFecha(LoggerStart.getNombreFecha());

		//Use this problema instance in case of non-parallelism
		if(geproperties.isParalelismo() == false)
		{
			problema = problemaConv;
		}
		
		terminationCondition = new TerminationCondition(geproperties.getTerminationConditionType(),
				geproperties.getGenerationsNumber(), geproperties.isHasGoal(), geproperties.getGoalFitness());

		OperatorFactory opFactory = OperatorFactory.getOperatorFactory();
		
		inicializacion = opFactory.createOperadorInicializacion(geproperties.getPopulationInitializationClassName(), 
				geproperties, randomGenerator);
				
		Fenotipo.inicia_clase(geproperties.isParalelismo(), geproperties.isHashCache());
		
		recombinacion = opFactory.createOperadorRecombinacion(geproperties.getCrossoverClassName(), geproperties, randomGenerator);
			
		mutacion = opFactory.createOperadorMutacion(geproperties.getMutationClassName(), geproperties, randomGenerator);

		duplicacion = opFactory.createOperadorDuplicacion(geproperties.getDuplicationClassName(), geproperties, randomGenerator);
		
		reemplazo = opFactory.createOperadorReemplazo(geproperties.getSurvivorSelectionClassName(), geproperties); 
		
		selector = opFactory.createSelector(geproperties.getParentSelectionClassName(),	geproperties, randomGenerator);
		
		individualsNumber = geproperties.getIndividualsNumber();
	}
	
	void setProblema(Problema problema) { this.problema = problema; }
	
	/*
	 * ejecuta(): Ejecución del Algoritmo Genético
	 * 
	 * contiene los pasos del algoritmo
	 */
	void ejecuta()
	{		
		tInit = System.currentTimeMillis();
		
		problema.resetEvaluationCount();
		
		generacion = 1;
		Genotipo.setGeneracionClase(generacion);
		problema.setGeneracion(generacion);
		
		/*
		 * INICIALIZA POBLACIÓN
		 */
		poblacion = new Poblacion();
		poblacion.Inicializa(individualsNumber, inicializacion);
		inicializacion.stat();
		
		/*
		 * EVALUA CANDIDATOS
		 */
		
		poblacion.evalua(problema);
		poblacion.calculaStats();
		
		initialBestFitness = poblacion.getBestFitness();
		
		//poblacion.calculaStats();
		
		logger.info("-------------------------Generacion:" + generacion + "-----------------------------");
		logger.severe("Generacion:" + generacion);
		poblacion.pintaStats("Gen:" + generacion + ":");
		//GE.FitnessFile.println(generacion + "," + poblacion.StatToString());
		logger.info("evaluationCount:" + problema.getEvaluationCount());
		poblacion.pintaBest();

		//poblacion.pinta();
		//Genotipo.stat();
		recombinacion.stat();
		mutacion.stat();
		//assert(false);
		int res[] = poblacion.calculaDiversidadSimple();
		logger.info("evolFitness;gen;" + generacion +";fitness;" + poblacion.getFenotipo(0).getFitness() +
				";eq;" + res[0] + ";expEq;" + res[1] + ";oCache;" + Fenotipo.expCache.getOcupacion());			              					
		
		/*
		 * CONDICIÓN DE TERMINACIÓN
		 */
		while(terminationCondition.isTerminationCondition(generacion, poblacion.getBestFitness(),
					poblacion.getBestEvalRes()) == false)
		{	
			//En caso de no parar el algoritmo al alcanzar la condición de terminación del problema
			//Pintamos el primer individuo que alcanza dicha condición
			if(terminationCondition.isProblemCondition())
				logger.severe("MejorAvance:" + poblacion.getFenotipo(0).toString() +
						":evaluationCount:" + problema.getEvaluationCount());
			
			generacion++;
			Genotipo.setGeneracionClase(generacion);
			problema.setGeneracion(generacion);
			
			/*
			 * SELECCIÓN DE PADRES
			 */
			Poblacion matingPool = new Poblacion();
			int matingPoolSize = reemplazo.getMatingPoolSize();		
			logger.info("matingPoolSize:" + matingPoolSize);
			
			selector.inicia(poblacion);
			
			while(matingPool.getSize() < matingPoolSize)
			{
				matingPool.addFenotipo(selector.select());
			}
			
			//####
			logger.info("matingPool.getSize:" + matingPool.getSize());
			
			Poblacion offspring = new Poblacion();			
			
			/*
			 * RECOMBINACIÓN DE HIJOS
			 * 
			 * Recorre el conjunto de padres seleccionados y los cruza de dos en dos
			 * 
			 * Cada cruce provee dos nuevos individuos
			 */

			Iterator<Fenotipo> iterator = matingPool.iterator();
			
			while(iterator.hasNext() && (offspring.getSize() < individualsNumber))
			{
				Fenotipo padre1, padre2;
				Genotipo genHijo1, genHijo2;
				
				padre1 = iterator.next();
				padre2 = iterator.next();
				
				assert(padre1 != null);
				assert(padre2 != null);
				
				assert(recombinacion != null);
								
				genHijo1 = recombinacion.cruza(padre1.getGenotipo(), padre2.getGenotipo());
				genHijo2 = recombinacion.cruza();
				
				offspring.addFenotipo(new Fenotipo(genHijo1));
				offspring.addFenotipo(new Fenotipo(genHijo2));
				
				//logger.info("offspring.getSize:" + offspring.getSize());
			}
			
			/*
			 * MUTACIÓN
			 */
			iterator = offspring.iterator();
			
			while(iterator.hasNext())
			{
				mutacion.muta(iterator.next().getGenotipo());
			}
			
			/*
			 * DUPLICACIÓN
			 */
			iterator = offspring.iterator();
			
			while(iterator.hasNext())
			{
				duplicacion.duplica(iterator.next().getGenotipo());
			}
								
			/*
			 * Statistics
			 */
			//Comentar la línea siguiente para acelerar la ejecución del algoritmo
			//poblacion.pinta();
			//Genotipo.stat();
			recombinacion.stat();
			mutacion.stat();
			duplicacion.stat();

			/*
			 * Offspring evaluation
			 */
			offspring.evalua(problema);			
			
			/*
			 * SELECCIÓN DE SUPERVIVIENTES
			 */
			
			poblacion = reemplazo.nuevaPoblacion(poblacion, offspring);
		
			//La nueva población debe tener el mismo número de individuos
			logger.info("poblacion size:" + poblacion.getSize());
			assert(poblacion.getSize() == individualsNumber);

			/*
			 * Indicación de nueva generación y presentación de valores de la misma
			 */
			
			poblacion.calculaStats();
			logger.info("-------------------------Generacion:" + generacion + "-----------------------------");
			logger.severe("Generacion:" + generacion);
			poblacion.pintaStats("Gen:" + generacion + ":");
			//GE.FitnessFile.println(generacion + "," + poblacion.StatToString());
			logger.info("evaluationCount:" + problema.getEvaluationCount());
			
			//poblacion.pinta();
			//poblacion.pintaBest();
			res = poblacion.calculaDiversidadSimple();
			logger.info("evolFitness;gen;" + generacion +";fitness;" + poblacion.getFenotipo(0).getFitness() +
					";eq;" + res[0] + ";expEq;" + res[1]); // + ";oCache;" + Fenotipo.expCache.getOcupacion());
		}
		
		/*
		 * Alcanzada terminación, se imprimen datos finales
		 */
		
		//Fenotipo.ExpCacheDump();
		
		logger.severe("GeneradorExp statMaxRecursionLevel:" + Generador.getStatMaxRecursionLevel());
		
		//Best circuit, raw expression and stats 
		logger.severe("Mejor:" + poblacion.getFenotipo(0).toString() +
				":evaluationCount:" + problema.getEvaluationCount() + 
				":pcruce:" + geproperties.getCrossoverRate() + 
				":pmuta:" + geproperties.getMutationRate() +
				":pcrucege2:" + geproperties.getCrossoverRateGE2() +
				":pcrucege3:" + geproperties.getCrossoverRateGE3() +
				":medgenes:" + (geproperties.getMaxGenesNumber() + geproperties.getMinGenesNumber())/2 
				);

		finalBestFitness = poblacion.getBestFitness();
		tTotal = System.currentTimeMillis() - tInit;
		logger.info("Resumen:Generacion:" + generacion + ":evaluationCount:" + problema.getEvaluationCount() +
				":initialBestFitness:" + initialBestFitness + ":finalBestFitness:" + finalBestFitness +
				":entradas:" + Fenotipo.expCache.getEntradas() + ":totalHits:" + Fenotipo.expCache.getTotalHits() +
				":eficiencia:" + Fenotipo.expCache.getEficiencia() + ":ocupacion:" + Fenotipo.expCache.getOcupacion() +
				":Fenotipo.evaluationCount:" + Fenotipo.getEvaluationCount() +
				":tTotal:" + tTotal + ":CRate:" + recombinacion.getStat() + ":MRate:" + mutacion.getStat());
		
		//Por conveniencia se pintan de nuevo
		poblacion.pintaStats("Final:Gen:" + generacion + ":");
		
		//Complete netlist of best circuit
		//Uses a local instance of problema 
		logger.severe("BestCircuit:");
		logger.severe(problemaConv.build(poblacion.getFenotipo(0).getExpression()));
	}
}
