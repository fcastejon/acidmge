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
 * Ruleta
 * 
 * Implementa el método de la ruleta de selección de padres (modificada como se indica)
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.Iterator;
import java.util.logging.Logger;

import es.uned.simda.acidge.ge.Fenotipo;
import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Poblacion;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class Ruleta extends Selector 
{
	private final static Logger logger = Logger.getLogger(Ruleta.class.getName());

	double totalFitness;
	
	public Ruleta(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(randomGenerator);
		logger.info("Selección de padres por ruleta");
		totalFitness = 0;
	} 
	
	public Ruleta(RandomGenerator randomGenerator)
	{
		super(randomGenerator);
		logger.info("Selección de padres por ruleta");
		totalFitness = 0;
	}
	
	/*
	 * En el libro de la asignatura se indica que la selección de padres se hace
	 * de forma proporcional a la adaptación. 
	 * 
	 * Como en esta implementación, el problema es de minimización, la mejor adaptación 
	 * es la más pequeña. 
	 * 
	 * Se ha optado por utilizar un método inversamente proporcional a la adaptación. 
	 * Esto es, a 1/fitness
	 */
	//Calcula la suma total de las inversas de las fitness
	public void inicia(Poblacion pobVal) 
	{
		pob = pobVal;
		
		Iterator<Fenotipo> iterator = pob.iterator();
		Fenotipo fenotipo; 
		
		totalFitness = 0;

		while(iterator.hasNext())
		{
			fenotipo = iterator.next();
			
			totalFitness += 1/fenotipo.getFitness();
		}
	}

	//Obtiene un número aleatorio y calcula el fenotipo correspondiente
	public Fenotipo select() 
	{
		double bola = randomGenerator.random() * totalFitness;
		double acum = 0;
		Iterator<Fenotipo> iterator = pob.iterator();
		Fenotipo fenotipo; 
		
		while(iterator.hasNext())
		{
			fenotipo = iterator.next();
			acum += 1/fenotipo.getFitness();
			if(acum >= bola)
				{
				return fenotipo;
				}
		}
		assert(false);
		return null;
	}

}
