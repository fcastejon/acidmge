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
 * Interfaz RandomGenerator
 * 
 * Permite el uso de varios generadores de números aleatorios con diferentes algoritmos
 * 
 * También permite el uso de una clase mock para las pruebas unitarias de las clases
 * que usan números aleatorios
 * 
 * Modificado de clase abstracta a interfaz
 */
package es.uned.simda.acidge.ge.random;

//import java.util.logging.Logger;


public interface RandomGenerator 
{	
	//private final static Logger logger = Logger.getLogger(RandomGenerator.class.getName());

	//private static RandomGenerator randomGenerator = null;
	
	//RandomGenerator()	{	}
	/*
	public static void setRandomGenerator(RandomGenerator rg) 
	{
		assert(randomGenerator == null);
		randomGenerator = rg;
	}
	public static RandomGenerator getRandomGenerator() 
	{
		assert(randomGenerator != null);
		return randomGenerator;
	}
	*/
	
	//Devuelve un double entre 0 y 1
	public double random();

	//Devuelve valor entre 0 y maxValue - 1
	public int randomInt(int maxValue);
}
