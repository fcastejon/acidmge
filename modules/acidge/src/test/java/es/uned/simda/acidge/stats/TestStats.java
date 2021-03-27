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
 * TestCase para probar la clase Stats
 */
package es.uned.simda.acidge.stats;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;




/**
 * @author fcastejo
 *
 */
public class TestStats 
{
	private final static Logger logger = Logger.getLogger(TestStats.class.getName());
	
	final static double Delta = 1e-9;
	static Stats stats;
	
	@BeforeClass
	public static void checkEA()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	//Validado contra la función de cuartiles de Excel cuyo método se usa en Stats
	
	@Test
	public void test1() 
	{
		logger.info("test 1: prueba con valor 9");
		stats = new Stats();
		
		valores(9);
		
		assertEquals(9, stats.getLength(), Delta);
		assertEquals(4.0, stats.getAvg(), Delta);
		assertEquals(2.5819888974716116, stats.getDes(), Delta);	
		assertEquals(8.0, stats.getMax(), Delta);
		assertEquals(6.0, stats.getTercerCuartil(), Delta);
		assertEquals(4.0, stats.getMediana(), Delta);
		assertEquals(2.0, stats.getPrimerCuartil(), Delta);
		assertEquals(0.0, stats.getMin(), Delta);
		
		//assertEquals(expected, actual, Delta);
		//linea = "size;max;tCuar;med;pCuar;min;avg;des";
	}
	
	@Test
	public void test2() 
	{
		logger.info("test 2: prueba con valor 10");
		stats = new Stats();
		
		valores(10);
		
		assertEquals(10, stats.getLength(), Delta);
		assertEquals(4.5, stats.getAvg(), Delta);
		assertEquals(2.8722813232690143, stats.getDes(), Delta);	
		assertEquals(9.0, stats.getMax(), Delta);
		assertEquals(6.75, stats.getTercerCuartil(), Delta);
		assertEquals(4.5, stats.getMediana(), Delta);
		assertEquals(2.25, stats.getPrimerCuartil(), Delta);
		assertEquals(0.0, stats.getMin(), Delta);
	}
	
	@Test
	public void test3() 
	{
		logger.info("test 3: prueba con valor 11");
		stats = new Stats();
		
		valores(11);
		
		assertEquals(11, stats.getLength(), Delta);
		assertEquals(5.0, stats.getAvg(), Delta);
		assertEquals(3.1622776601683795, stats.getDes(), Delta);	
		assertEquals(10.0, stats.getMax(), Delta);
		assertEquals(7.5, stats.getTercerCuartil(), Delta);
		assertEquals(5.0, stats.getMediana(), Delta);
		assertEquals(2.5, stats.getPrimerCuartil(), Delta);
		assertEquals(0.0, stats.getMin(), Delta);
	}
	
	@Test
	public void test4() 
	{
		logger.info("test 4: prueba con valor 12");
		stats = new Stats();
		
		valores(12);
		
		assertEquals(12, stats.getLength(), Delta);
		assertEquals(5.5, stats.getAvg(), Delta);
		assertEquals(3.452052529534663, stats.getDes(), Delta);	
		assertEquals(11.0, stats.getMax(), Delta);
		assertEquals(8.25, stats.getTercerCuartil(), Delta);
		assertEquals(5.5, stats.getMediana(), Delta);
		assertEquals(2.75, stats.getPrimerCuartil(), Delta);
		assertEquals(0.0, stats.getMin(), Delta);
	}
	
	@Test
	public void test5() 
	{
		logger.info("test 5: prueba con valor 3");
		stats = new Stats();
		
		valores(3);
		
		assertEquals(3, stats.getLength(), Delta);
		assertEquals(1.0, stats.getAvg(), Delta);
		assertEquals(0.81649658, stats.getDes(), Delta);	
		assertEquals(2.0, stats.getMax(), Delta);
		assertEquals(1.5, stats.getTercerCuartil(), Delta);
		assertEquals(1.0, stats.getMediana(), Delta);
		assertEquals(0.5, stats.getPrimerCuartil(), Delta);
		assertEquals(0.0, stats.getMin(), Delta);
		
		//assertEquals(expected, actual, Delta);
		//linea = "size;max;tCuar;med;pCuar;min;avg;des";
	}
	
	@Test
	public void test6() 
	{
		logger.info("test6 : prueba con valor 2");
		stats = new Stats();
		
		valores(2);
		
		assertEquals(2, stats.getLength(), Delta);
		assertEquals(0.5, stats.getAvg(), Delta);
		assertEquals(0.5, stats.getDes(), Delta);	
		assertEquals(1.0, stats.getMax(), Delta);
		assertEquals(0.75, stats.getTercerCuartil(), Delta);
		assertEquals(0.5, stats.getMediana(), Delta);
		assertEquals(0.25, stats.getPrimerCuartil(), Delta);
		assertEquals(0.0, stats.getMin(), Delta);
		
		//assertEquals(expected, actual, Delta);
		//linea = "size;max;tCuar;med;pCuar;min;avg;des";
	}

	@Test
	public void test7() 
	{
		logger.info("test7 : prueba con valor 1");
		stats = new Stats();
		
		valores(1);
		
		assertEquals(1, stats.getLength(), Delta);
		assertEquals(0.0, stats.getAvg(), Delta);
		assertEquals(0.0, stats.getDes(), Delta);	
		assertEquals(0.0, stats.getMax(), Delta);
		assertEquals(0.0, stats.getTercerCuartil(), Delta);
		assertEquals(0.0, stats.getMediana(), Delta);
		assertEquals(0.0, stats.getPrimerCuartil(), Delta);
		assertEquals(0.0, stats.getMin(), Delta);
		
		//assertEquals(expected, actual, Delta);
		//linea = "size;max;tCuar;med;pCuar;min;avg;des";
	}
	
	void valores(int num)
	{
		for(int i = 0; i < num; i++)
		{
			stats.add((double) i);
		}
		
		stats.calcula();
		
		logger.info(stats.valores());
		logger.info(stats.toString());
	}
}
