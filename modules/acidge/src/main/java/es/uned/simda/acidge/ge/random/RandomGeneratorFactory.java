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
 * SelectorFactory
 * 
 * Utiliza el patrón Simple Factory para la creación de la clase concreta a realizar
 */
package es.uned.simda.acidge.ge.random;

import java.util.logging.Logger;


public class RandomGeneratorFactory 
{
	private final static Logger logger = Logger.getLogger(RandomGeneratorFactory.class.getName());

	static RandomGeneratorFactory factory = null;
		
	//Patrón singleton (no multithread safe)
	public static RandomGeneratorFactory getRandomGeneratorFactory()
	{
		if(factory == null)
			factory = new RandomGeneratorFactory();
		
		return factory;
	}
		
	private RandomGeneratorFactory() {}
	
	public RandomGenerator createRandomGenerator(String type)  
	{	
	    Class<?> randomGenerator;
	    RandomGenerator instancia; 
	    
		try 
		{
			randomGenerator = Class.forName(type);
			instancia = (RandomGenerator) randomGenerator.newInstance();
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
	
	/*
	public RandomGenerator creaRandomGenerator(int RandomGenerator) 
	{
		RandomGenerator randomGenerator = null;
		
		switch(RandomGenerator)
		{
			case 0:
				randomGenerator = new RandomMock();
				break;
			case 1:
				randomGenerator = new RandomJava();
				break;
			case 2:
				randomGenerator = new RandomSimple();
				break;
			case 3:
				randomGenerator = new RandomMersenne();
				break;
				
			default:
				//código no alcanzable
				throw new AssertionError("Valor RandomGenerator no soportado:" + 
						RandomGenerator);
		}
		
		return randomGenerator;
	}
	*/
}
