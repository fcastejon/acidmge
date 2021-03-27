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
 * Clase RandomMersenne
 * 
 * Recubre la clase MersenneTwisterFast para implementar la interfaz RandomGenerator
 */
package es.uned.simda.acidge.ge.random;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.random.mersenne.MersenneTwisterFast;


public class RandomMersenne implements RandomGenerator 
{
	private final static Logger logger = Logger.getLogger(RandomMersenne.class.getName());

	MersenneTwisterFast mtf;

	public RandomMersenne() 
	{
		logger.info("Generador de números aleatorios MersenneTwister");
		
		mtf = new MersenneTwisterFast();
		
		assert(mtf != null);
	}
		
	@Override
	public double random() 
	{
		assert(mtf != null);
		
	    return mtf.nextDouble();
	}
	
	//Devuelve valor entre 0 y maxValue - 1
	@Override
	public int randomInt(int maxValue)
	{
		return (int) (random() * maxValue);
	}
}
