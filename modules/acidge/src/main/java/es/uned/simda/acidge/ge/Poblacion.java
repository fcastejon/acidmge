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
** Clase Poblacion
**
*/
package es.uned.simda.acidge.ge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.logging.Logger;

import es.uned.simda.acidge.ge.operadores.OperadorInicializacion;
import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.Problema;


import es.uned.simda.acidge.stats.Stats;

public class Poblacion implements Iterable<Fenotipo>
{
	private final static Logger logger = Logger.getLogger(Poblacion.class.getName());

	ArrayList<Fenotipo> fenotipos;
	
	Stats statFitness;
	Stats statGenesNumber;
	Stats statLongExp;
	Stats statWrapping;
	Stats statCanonicalFitness;
	int viables;
	int expresables;
	
	public Poblacion()
	{
		fenotipos = new ArrayList<Fenotipo>();
		
		viables = 0;
		expresables = 0;
	}
	
	/*
	//@SuppressWarnings("unchecked")
	Poblacion(Poblacion pob)
	{
		//fenotipos = (ArrayList<Fenotipo>) pob.fenotipos.clone();
		
		fenotipos = new ArrayList<Fenotipo>();
		
		for (Fenotipo f : pob.fenotipos)
			fenotipos.add(f);
		
		viables = 0;
		expresables = 0;
	}
	*/	
	
	//Crea población utilizando el operador de inicialización facilitado 
	public void Inicializa(int IndividualsNumber, OperadorInicializacion inicializacion)
	{
		for(int i = 0; i < IndividualsNumber; i++)
			{
			Genotipo genotipo = inicializacion.creaGenotipo();
			
			Fenotipo ind = new Fenotipo(genotipo);
	
			addFenotipo(ind);
			}
	}
	
	public Iterator<Fenotipo> iterator() 
	{
		return fenotipos.iterator();	
	}
	
	public void addFenotipo(Fenotipo fenotipo)
		{
		//assert(fenotipos.size() < IndividualsNumber);
		
		fenotipos.add(fenotipo);
		}
	
	public void addPoblacion(Poblacion pob)
		{
		fenotipos.addAll(pob.fenotipos);
		}

	public Fenotipo getFenotipo(int i)
	{
		return fenotipos.get(i);
	}
	/*
	Fenotipo getRandom()
	{
		int indice = (int) (Math.random() * fenotipos.size());
		
		assert((indice >= 0) && (indice < fenotipos.size()));
		
		return getFenotipo(indice);
	}
	*/	
	
	public int getSize()
	{
		return fenotipos.size();
	}
	
	//Mantiene el número de fenotipos indicado en size y borra el resto
	//Hecho para añadir los hijos en el caso de steady-state 
	public void trim(int size)
	{
		Iterator<Fenotipo> iterator = fenotipos.iterator();
		
		assert(fenotipos.size() > size);
		
		for(int i = 0; (i < size); i++)
			iterator.next();
		
		while(iterator.hasNext())
		{
			iterator.next();
			iterator.remove();			
		}

		assert(fenotipos.size() == size);
	}
	
	//Borra los últimos num elementos de la generación gen
	public void borraGen(int gen, int num)
	{
		for(int i = fenotipos.size() - 1; (i >= 0) && (num > 0); i--)
		{
			if(fenotipos.get(i).getGeneracion() == gen)
			{
				fenotipos.remove(i);
				num--;
			}
		}
		
		assert(num == 0);
	}

	public void evalua(Problema problema)
	{
		Iterator<Fenotipo> iterator = iterator();
		Fenotipo fenotipo; 
		
		//El primer bucle lanza las operaciones de evaluación
		while(iterator.hasNext())
		{
			//logger.info("hago iterator.next()");
			
			fenotipo = iterator.next();
			
			//logger.info("llama a fenotipo.evalua(problema)");
			
			fenotipo.evalua(problema);
			
			//logger.info("Poblacion.evalua bucle1");
		}
		
		logger.info("Poblacion.evalua: entro en esperaAsinc");
		//En el caso de que no haya paralelismo, ni esperaAsinc, ni el segundo bucle tienen efecto
		Fenotipo.esperaAsinc(problema);
		
		//El segundo bucle obtiene los datos de las operaciones lanzadas
		iterator = iterator();
		
		//logger.info("Poblacion.evalua: entro en bucle2");
		
		while(iterator.hasNext())
		{
			fenotipo = iterator.next();
	
			//logger.info("bucle recoger:" + fenotipo.getFitness());
			
			//Se busca la adaptación si no se recuperó anteriormente
			//if(fenotipo.getFitness() == -1.0)
			if(fenotipo.getEvalRes() == null)
			{
				fenotipo.getFitnessFromCache();
				//logger.info("recojo:" + fenotipo.getFitness());
			}
		}
		
		ordena();
	}
	
	/*
	void evalua(Problema problema)
	{
		Iterator<Fenotipo> iterator = iterator();
		Fenotipo fenotipo; 
		
		while(iterator.hasNext())
		{
			fenotipo = iterator.next();
			
			fenotipo.evalua(problema, false);	
		}
		
		ordena();
	}
	*/
	
	//Ordena la población en orden ascendente de fitness (problema de minimización)
	//El mejor individuo es de índice 0
	public void ordena()
	{
		Collections.sort(fenotipos);
		//Collections.reverse(fenotipos);
	}
	
	//Ordena la población en orden ascendente de edad y fitness
	//Para el SteadyStateAge
	public void ordenaAge(int elitismo)
	{
		//fácil
		if(elitismo == 0)
			Collections.sort(fenotipos, new FenotipoComparator());
		else
		{
			//Consideramos que viene ordenada
			//Ojo, subList indica primero y final+1
			ArrayList<Fenotipo> elite = new ArrayList<Fenotipo>(fenotipos.subList(0, elitismo));
			ArrayList<Fenotipo> resto = new ArrayList<Fenotipo>(fenotipos.subList(elitismo, 
					fenotipos.size()));
			
			Collections.sort(resto, new FenotipoComparator());
			fenotipos = elite;
			fenotipos.addAll(resto);
		}
	}

	public void pinta()
	{
		logger.info("***************** Población ****************");
		Iterator<Fenotipo> iterator = fenotipos.iterator();
		Fenotipo fenotipo; 
		
		while(iterator.hasNext())
		{
			fenotipo = iterator.next();
			
			logger.info(fenotipo.toString());
		}
		
		logger.info("*********************************************");
	}
	
	public void calculaStats()
	{
		statFitness = new Stats("statFitness");
		statGenesNumber = new Stats("statGenesNumber");
		statLongExp = new Stats("statLongExp");
		statWrapping = new Stats("statWrapping");
		statCanonicalFitness = new Stats("statCanonicalFitness");
		
		viables = 0;
		expresables = 0;

		Fenotipo fenotipo;

		Iterator<Fenotipo> iterator = fenotipos.iterator();
		
		while(iterator.hasNext())
		{
			fenotipo = iterator.next();
			
			statFitness.add(fenotipo.getFitness());
			statCanonicalFitness.add(fenotipo.getCanonicalFitness());
			statGenesNumber.add(fenotipo.getGenesNumber());

			if(fenotipo.getExpresable())
			{
				expresables++;
			
				statLongExp.add(fenotipo.getLongExp());
				statWrapping.add(fenotipo.getWrapping());
						
				if(fenotipo.getViable())
					viables++;
			}
			else
				assert(fenotipo.getViable() == false);
		}
		
		statFitness.calcula();
		statGenesNumber.calcula();
		statLongExp.calcula();
		statWrapping.calcula();
		statCanonicalFitness.calcula();
	}
	
	//Calcula el número de elementos de la población iguales al mejor
	//Devuelve dos valores: número de exactamente iguales y número de expresablemente iguales
	public int [] calculaDiversidadSimple()
	{
		int count[] = new int[2];
		boolean res[];
		
		Fenotipo mejor = fenotipos.get(0);
		
		for(int i = 1; i < fenotipos.size(); i++)
		{
			res = mejor.esIgual(fenotipos.get(i));
			
			if(res[0])
				count[0]++;
			
			if(res[1])
				count[1]++;
		}
		
		return count;
	}
	
	//Obtiene la mejor fitness, que corresponde al primer fenotipo de la población
	//Espera que la población esté ordenada
	public double getBestFitness()
	{
		return fenotipos.get(0).getFitness();
	}
	
	//Obtiene el evalRes del individuo con mejor fitness, que corresponde al primer fenotipo de la población
	//Espera que la población esté ordenada
	public EvalRes getBestEvalRes()
	{
		return fenotipos.get(0).getEvalRes();
	}
	
	public void pintaBest()
	{
		logger.info("pintaBest;" + fenotipos.get(0).toStringResumido());
	}	
	
	/*
	public String cabeceraStat()
	{	
		String linea = statFitness.cabecera();
		
		return linea;
	}
	*/
	
	public void pintaStats(String prefijo)
	{	
		logger.info(prefijo + statFitness.toString());
		logger.info(prefijo + statCanonicalFitness.toString());
		logger.info(prefijo + statGenesNumber.toString());
		logger.info(prefijo + statLongExp.toString());
		logger.info(prefijo + statWrapping.toString());
		logger.info(prefijo + "BestFitness:" + statFitness.getMin() + ":medGenesNumber:" + statGenesNumber.getAvg() + 
				":medLongExp:" + statLongExp.getAvg() + ":medWrapping:" + statWrapping.getAvg() +
				":Expresables:" + expresables + ":Viables:" + viables + ":BestCanonicalFitness:" + statCanonicalFitness.getMin());
	}
		
	//Comprueba que hay el mismo número de individuos de cada generación en la población
	//Sólo para steady-state
	public void checkGen()
	{
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		Integer v;
		
		for(Fenotipo fenotipo : fenotipos)
		{
			Integer I = new Integer(fenotipo.getGeneracion());
			
			if((v = hm.get(I)) == null)
			{
				v = new Integer(1);
				hm.put(I, v);
			}
			else
			{
				v = new Integer(v.intValue() + 1);
				hm.put(I, v);
			}
		}
/*		
		int c = -1;
		
		for(Integer I : hm.values())
		{
			if(c == -1)
				c = I.intValue();
			else
				if(c != I.intValue())
					throw new AssertionError("checkGen: número de generaciones inconsistente:" + 
							c + ":" + I.intValue());
		}
*/		
		int c = -1;
		
		for(Map.Entry<Integer,Integer> entry : hm.entrySet())
		{
			if(c == -1)
				c = entry.getValue().intValue();
			else
				if(c != entry.getValue().intValue())
					throw new AssertionError("checkGen: número de generaciones inconsistente:" + 
							c + ":" + entry.getValue().intValue());
			
			//logger.info("gen:" + entry.getKey().intValue() + " cuenta:" + entry.getValue().intValue());
		}
		
	}
}

