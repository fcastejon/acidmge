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
 * Clase RandomJava
 * 
 * Implementa el generador de números aleatorios mediante funciones de la librería
 * de Java. java.util.Random
 */

package es.uned.simda.acidge.ge.random;

import java.util.logging.Logger;



public class RandomJava implements RandomGenerator
{
	private final static Logger logger = Logger.getLogger(RandomJava.class.getName());

	public RandomJava()
	{
		logger.info("Generador de números aleatorios Java");
	}
	
	@Override
	public double random()
	{
		return Math.random();
	}
	
	//Devuelve valor entre 0 y maxValue - 1
	@Override
	public int randomInt(int maxValue)
	{
		return (int) (Math.random() * maxValue);
	}
}
