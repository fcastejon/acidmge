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
 * TestCase para probar la clases RandomGenerator
 */
package es.uned.simda.acidge.ge.random;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.random.RandomGenerator;
import es.uned.simda.acidge.ge.random.RandomGeneratorFactory;



public class TestRandom 
{
	private final static Logger logger = Logger.getLogger(TestRandom.class.getName());
	private final static int MAXTEST = 100;
	private final static int MAXRANDINT = 49;
	
	@BeforeClass
	public static void checkEA()
	{
		es.uned.simda.acidge.util.Util.checkEA();	     
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}

	@Test
	public void testRandomJava() 
	{
		logger.info("testRandomJava");
		
		motor("es.uned.simda.acidge.ge.random.RandomJava");
		
		motor2("es.uned.simda.acidge.ge.random.RandomJava");
	}
/* Este generador no funciona
	@Test
	public void testRandomSimple() 
	{
		logger.info("es.uned.simda.acidge.ge.random.RandomSimple");
		
		motor(RandomSimple);
	}
*/
	@Test
	public void testRandomMersenne() 
	{
		logger.info("testRandomMersenne");
		
		motor("es.uned.simda.acidge.ge.random.RandomMersenne");
		
		motor2("es.uned.simda.acidge.ge.random.RandomMersenne");
	}

	void motor(String type)
	{
		RandomGeneratorFactory factory = RandomGeneratorFactory.getRandomGeneratorFactory();
		
		RandomGenerator rng = factory.createRandomGenerator(type);
		
		double d;
		
		for(int i = 0; i < MAXTEST; i++)
		{
			d = rng.random();
			
			assert((d >= 0.0) && (d < 1.0));
			
			logger.info(Double.toString(d));
		}
	}
	
	void motor2(String type)
	{
		RandomGeneratorFactory factory = RandomGeneratorFactory.getRandomGeneratorFactory();
		
		RandomGenerator rng = factory.createRandomGenerator(type);
		
		int d;
		
		for(int i = 0; i < MAXTEST; i++)
		{
			d = rng.randomInt(MAXRANDINT);
			
			assert((d >= 0.0) && (d < MAXRANDINT));
			
			logger.info(Double.toString(d));
		}
	}
	
}
