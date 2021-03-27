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
 * Clase Mutacion
 * 
 *  Implementa el operador de mutación random resetting
 *  
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class Mutacion extends OperadorMutacion
{
	private final static Logger logger = Logger.getLogger(Mutacion.class.getName());

	Mutacion(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(geproperties, randomGenerator); 
		
		logger.info("Mutación por Random Reset");
	}
	
	Mutacion(int MutationType, 
			double MutationRate, RandomGenerator randomGenerator)
	{
		super(MutationType, MutationRate, randomGenerator); 
		
		logger.info("Mutación por Random Reset");
	}
	
	private byte getRandomGen() { return (byte) (randomGenerator.randomInt(Genotipo.valoresByte)); }
	
	public void muta(Genotipo genotipo)
	{
		//logger.info("muta antes  :" + genotipo);
		
		byte [] genes = genotipo.getGenes();
		
		for(int i = 0; i < genes.length; i++)
			{
		    if(randomGenerator.random() <= MutationRate)
				{
		    	num_muta++;

		    	genes[i] = getRandomGen();
				}
		    	
		    tot_muta++;
		    }
		
		//logger.info("muta despues:" + genotipo);
	}
}
