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
** Clase Fenotipo
**
*/
package es.uned.simda.acidge.ge;

import java.util.logging.Logger;


import es.uned.simda.acidge.ge.cache.EvalItem;
import es.uned.simda.acidge.ge.cache.ExpCache;
import es.uned.simda.acidge.ge.cache.ExpCacheBasico;
import es.uned.simda.acidge.ge.cache.HashExpCache;
import es.uned.simda.acidge.ge.cache.ExpCacheNotFoundException;
import es.uned.simda.acidge.ge.cache.ExpCacheSync;
import es.uned.simda.acidge.ge.cache.ExpCacheSyncYaSolicitadoException;
import es.uned.simda.acidge.generador.GenException;
import es.uned.simda.acidge.generador.Generador;
import es.uned.simda.acidge.generador.GeneradorFactory;
import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.Problema;

public class Fenotipo implements Comparable<Fenotipo>
{
	private final static Logger logger = Logger.getLogger(Fenotipo.class.getName());

	//static int parsimony;
	//static double parsimonyValue = 0;
	//static int maxWrappingNumber = 0;
	//static int maxRecursionLevel = 0;
	//static int numRPRList = 0;
	//static int SurvivorSelectionType = 0;
	static int orden = 0;
	
	private Genotipo genotipo;
	private double fitness;
	//private double canonicalFitness;
	private EvalRes evalRes;
	private String expresion;
	
	public static ExpCache expCache;	//Cache de evaluaciones
	private static GeneradorFactory generadorFactory;
	//Contador de evaluaciones de fitness para el cálculo del AES
	private static int evaluationCount;
	private static boolean paralelismo;
	
	//Inicialización de la clase Fenotipo
	//obtiene los valores del fichero de parámetros que afectan al Fenotipo	
	static void inicia_clase(
			//int ParsimonyVal, double parsimonyValueVal, 
			boolean paralelismoVal,	boolean hashCache)
	{
		//parsimony = ParsimonyVal;
		//parsimonyValue = parsimonyValueVal;
		paralelismo = paralelismoVal;
		//SurvivorSelectionType = SurvivorSelectionTypeVal;
		//orden = OrdenFitness;
			
		/*
		if((parsimony != 1) && (parsimony != 2) && (parsimony != 3))
			throw new AssertionError("Valor de Parsimony no válido:" + parsimony);
		
		if(parsimonyValue < 0.0)
			throw new AssertionError("Valor de ParsimonyValue no válido:" + parsimonyValue);
		*/
		
		generadorFactory = GeneradorFactory.getFactory();
		
		expCache = new ExpCacheBasico();
		
		if(hashCache)
		{
			logger.info("Creación de HashExpCache");
			expCache = new HashExpCache(expCache);
		}
		
		if(paralelismo == true)
		{
			logger.info("Creación de ExpCacheSync");
			expCache = new ExpCacheSync(expCache);
		}
		
		/*
		switch(parsimony)
		{
		case 1:
			logger.info("Parsimony pressure:OFF");
			break;
		case 2:
			logger.info("Parsimony pressure:lineal limitada con par:" + parsimonyValue);
			break;
		case 3:
			logger.info("Parsimony pressure:exponencial con par:" + parsimonyValue);
			break;
		default:
			assert(false);
			break;
		}
		*/
	}
	
	//Conveniencia
	public static ExpCache getExpCache() { return expCache; }

	public Fenotipo(Genotipo gen)
	{
		genotipo = new Genotipo(gen);
			
		fitness = -1.0;
		//canonicalFitness = -1.0;
		expresion = null;
		evalRes = null;
	}

	public Genotipo getGenotipo() { return genotipo; }
	public int getGeneracion() { return genotipo.getGeneracion(); }
	
	public static int getEvaluationCount() { return evaluationCount; }
	private static void incrementEvaluationCount() { evaluationCount++;}
	
	//Comprobación adicional
	void checkFitVal(double fit)
	{
		if((fit < 0) || Double.isNaN(fit) || Double.isInfinite(fit))
			logger.severe("checkFitVal:" + fit);
		
		//No deben ponerse fitness negativas, puede valer -1.0 inicialmente
		//Fitness negativa señala que no se ha calculado aún
		assert(fit >= 0.0);
		assert(!(Double.isNaN(fit)) && !(Double.isInfinite(fit)));
	}
/*
	void setFitness(double fit) 
	{	
		checkFitVal(fit);
		
		fitness = fit; 
	}

	//Para la comparación entre distintos modelos de fitness
	void setCanonicalFitness(double fit) 
	{	
		checkFitVal(fit);
		
		canonicalFitness = fit; 
	}
*/
	public double getFitness() { return fitness; }
	public double getCanonicalFitness() { return evalRes.getCanonicalFitness(); }
	public EvalRes getEvalRes() { return evalRes; }
	
	public int getGenesNumber() { return genotipo.getGenesNumber(); }
	public boolean getViable() { return genotipo.getViable(); }
	public boolean getExpresable() { return genotipo.getExpresable(); }
	public int getLongExp() { return genotipo.getLongExp(); }
	public int getWrapping() { return genotipo.getWrapping(); }
	public String getExpression() { return expresion; }
	
	//En el caso paralelo, espera la terminación de las ejecuciones de evaluación paralelas
	public static void esperaAsinc(Problema problema)
	{
		expCache.esperaAsinc(problema);
	}
	
	//Evaluación de la fitness total
	public void evalua(Problema problema)
	{
		//Si ya está evaluado volvemos inmediatamente
		//if(fitness != -1.0)
		if(evalRes != null)
			return;
		
		//Comprueba si se generó la expresión anteriormente
		if((expresion == null) && genotipo.getExpresable())
		{
			generaExpresion();
		}
		
		//Sólo obtiene la adaptación si es expresable
		if(genotipo.getExpresable())
			evaluaExpresion(problema, paralelismo);
	}
	
	//Evaluación de la fitness total
	private boolean generaExpresion()
	{
		try 
		{
			Generador generador = generadorFactory.createGenerador(genotipo.getGenes());
			
			expresion = generador.genera();
			
			genotipo.setExpresable(true);		//Marca como viable
			genotipo.setWrapping(generador.getWrapping());
			genotipo.setLongExp(generador.getLongExp());
			//genotipo.setIndGE2(generador.getIndGE2());
			genotipo.setXOPoints(generador.getXOPoints());
		}
		//captura las dos GenException
		catch(GenException exp)
		{		
			logger.info("evalua: GenException:" + exp.getMessage());
			//expresion = CadenaNoExpresable;
			evalRes = new EvalRes(EvalRes.PenalizacionMaxWrapping);
			//fitness = EvalRes.PenalizacionMaxWrapping;
			//canonicalFitness = EvalRes.PenalizacionMaxWrapping;
			fitness = evalRes.getFitness();
			//canonicalFitness = evalRes.getCanonicalFitness();
			genotipo.setExpresable(false);					//Marca como no expresable
			genotipo.setViable(false);						//los no expresables son también inviables
			genotipo.setWrapping(0);
			genotipo.setLongExp(0);
			//genotipo.setIndGE2(0);
			genotipo.setXOPoints(null);
		}
		
		return genotipo.getExpresable();
	}
	
	//Evaluación de la fitness total
	//Se llama despues de haber llamada a generaExpresion
	private void evaluaExpresion(Problema problema, boolean asincrono)
	{
		assert(genotipo.getExpresable() == true);
		
		//Consulta a la caché, en caso de que esté se devuelve el valor.
		try 
		{
			//Movido aquí para calcular las evaluaciones antes de la caché
			incrementEvaluationCount();
			
			//double fitval = expCache.getFitness(expresion);
			EvalItem item = expCache.getEvalItem(expresion); 

			//Guarda fitness y marca viable sí o no
			//setYMarcaViable(fitval);
			assert(item.getFitness() == item.getEvalRes().getFitness());
			setYMarcaViable(item.getEvalRes());
		}			
		catch(ExpCacheNotFoundException e)
		{	
			//No se encontró por lo que se debe evaluar la adaptación
			//El cálculo de número de evaluaciones se hace aquí
			//La caché reduce el número de evaluaciones
			//incrementEvaluationCount();
			
			try
			{
				if(asincrono)
				{
					//Marcamos la petición asíncrona
					expCache.add(expresion, null);
				}
				
				EvalRes evalResTmp = problema.evalua(expresion);
							
				if(!asincrono)
				{				
					//Ya vienen corregidas de evalRes
					//Corrige valor de fitness
					//fitval = corrige(fitval);
					//canFitVal = corrige(canFitVal);
			
					//Guarda fitness y marca viable sí o no
					setYMarcaViable(evalResTmp);
				
					//Escritura en la caché
					expCache.add(expresion, evalResTmp);
					
					//Incrementamos los hits llamando a getEvalItem
					//FIXME quedaría mejor modificación add de forma que de forma paramétrica se incrementen los hits
					incHitsExpSinc(expresion);
				}
				else
				{
					assert(evalResTmp == null);
				}
			}
			//captura InviableException
			catch(InviableException ie)
			{		
				assert(!asincrono);
				logger.info("evalua: InviableException:" + ie.getMessage());
			
				//Guarda fitness y marca viable sí o no
				EvalRes evalResTmp = new EvalRes(EvalRes.PenalizacionNaN);
				setYMarcaViable(evalResTmp);
				
				//Escritura en la caché
				expCache.add(expresion, evalResTmp);
				
				//Incrementamos los hits llamando a getEvalItem
				//FIXME quedaría mejor modificación add de forma que de forma paramétrica se incrementen los hits
				incHitsExpSinc(expresion);
			}
		} 
		catch (ExpCacheSyncYaSolicitadoException e) 
		{
			if(!asincrono)
				throw new AssertionError("Código no alcanzable");
		}
	}
	
	private void incHitsExpSinc(String exp)
	{
		//La llamada a getEvalItem es para incrementar el número de hits
		//En el caso asíncrono se hace desde getFitnessFromCache
		try 
		{	
			expCache.getEvalItem(exp);
		}
		catch(ExpCacheNotFoundException e2)
		{
			throw new AssertionError("Código no alcanzable");
		}
		catch(ExpCacheSyncYaSolicitadoException e2)
		{
			throw new AssertionError("Código no alcanzable");
		}
	}

	//Obtiene los resultados de las llamadas asíncronas
	public void getFitnessFromCache()
	{
		assert(genotipo.getExpresable() == true);
		
		//logger.info("Buscando fitness de:" + expresion);
		
		//Consulta a la caché, en caso de que esté se devuelve el valor.
		try 
		{
			//double fitval = expCache.getFitness(expresion);
			EvalItem item = expCache.getEvalItem(expresion);
			
			//assert(fitval != -1.0);
			assert(item.getFitness() != -1.0);

			//Guarda fitness y marca viable sí o no
			//setYMarcaViable(fitval);
			assert(item.getFitness() == item.getEvalRes().getFitness());
			setYMarcaViable(item.getEvalRes());
		}			
		catch(ExpCacheNotFoundException e)
		{	
    		logger.info("ExpCacheNotFoundException: " + e.getMessage());
			throw new AssertionError("ExpCacheNotFoundException");
		} 
		catch (ExpCacheSyncYaSolicitadoException e) 
		{
    		logger.info("ExpCacheNotFoundException: " + e.getMessage());
			throw new AssertionError("ExpCacheNotFoundException");
		}
	}	
	
	//Se convierte a public para poder hacer pruebas
	public void setYMarcaViable(EvalRes evalResVal) 
	{
		//Posiblemente no sea necesario si llevamos este control a EvalRes
		checkFitVal(evalResVal.getFitness());
		checkFitVal(evalResVal.getCanonicalFitness());
		
		fitness = evalResVal.getFitness();
		//canonicalFitness = evalResVal.getCanonicalFitness();
		evalRes = evalResVal;
		
		//Si la fitness es mayor que PenalizacionGradual, la expresión cacheada es inviable
		if(fitness >= EvalRes.PenalizacionGradual)
			genotipo.setViable(false);		//Marca como inviable
		else
			genotipo.setViable(true);		//Marca como viable
	}
	
	/*
	public static double corrige(double val)
	{
		double res = val;
		
		if(Double.isNaN(val) || Double.isInfinite(val))
		{
			res = PenalizacionNaN;
		} 
				
		return res;	
	}
	*/
	//Comprueba igualdad entre genotipos e igualdad de la parte expresable
	//Llamada directa a su equivalente en Genotipo
	public boolean[] esIgual(Fenotipo otro)
	{
		return genotipo.esIgual(otro.getGenotipo());
	}

	/*
	private double corrigeParsimonia(double fit)
	{
		//modifica la fitness en caso de Parsimony Pressure
		if(parsimony != 1)
		{
			double factor = 1;
			
			int length = getGenesNumber();

			//penalizamos la fitness multiplicando por el tamaño, dividido por un valor
			switch(parsimony)
			{
				case 2:
					if(length > parsimonyValue)
						factor = length/parsimonyValue;
					else
						factor = 1;
					break;
				case 3:
					factor = Math.exp(length * parsimonyValue);
					break;
				case 1:
				default:
					throw new AssertionError("Código no alcanzable");
			}
					
			fit = fit * factor;		
		}
		
		return fit;
	}
	*/

	public String toStringResumido()
	{
		String linea = "";
		
		linea += "fitness;" + fitness + ";";
		linea += expresion;
		
		return linea;
	}
	
	public String toString()
	{
		assert(evalRes != null);
		assert(evalRes.getFitness() == fitness);
		String linea = evalRes.toString();
		
		//linea += "fitness," + fitness + "," + "canonicalFitness," + canonicalFitness + ",";
		linea += ";" + expresion;
		linea += ";" + genotipo.toString();
		
		
		return linea;
	}
	
	public static void ExpCacheDump()
	{
		expCache.dump();
	}
	
	//Comparación basada en el fitness del fenotipo para permitir la ordenación
	public int compareTo(Fenotipo obj) 
	{
		if(this.getFitness() > obj.getFitness())
			return 1;
		else if(this.getFitness() == obj.getFitness())
			return 0;
		else if(this.getFitness() < obj.getFitness())
			return -1;
		else 
			{
			logger.info("this.fitness:" + this.getFitness() + " obj.fitness:" + 
					obj.getFitness());
			
			throw new AssertionError("Código no alcanzable");
			}
	}
}
