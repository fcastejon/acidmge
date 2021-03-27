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
 * RandomSelector
 * 
 * Selección de padres aleatoria
 * 
 * Implementada para uso en conjunto con el algoritmo GAVaPS
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.Fenotipo;
import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Poblacion;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class RandomSelector extends Selector 
{
	private final static Logger logger = Logger.getLogger(RandomSelector.class.getName());

	public RandomSelector(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(geproperties, randomGenerator);
		logger.info("Selección de padres aleatoria");
	}
	
	public RandomSelector(RandomGenerator randomGenerator) 
	{
		super(randomGenerator);
		logger.info("Selección de padres aleatoria");
	}

	//Sólo apunta la Poblacion sobre la que actúa
	public void inicia(Poblacion pobVal) 
	{
		pob = pobVal;
	}

	public Fenotipo select() 
	{
		//Genera un número entre 0 y tamaño de población - 1 
		int indice = randomGenerator.randomInt(pob.getSize());
		
		assert(indice < pob.getSize());
			
		return pob.getFenotipo(indice);
	}
}
