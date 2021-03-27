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
 * TestCase para probar la clase Genotipo
 */
package es.uned.simda.acidge.ge;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;
import es.uned.simda.acidge.ge.random.RandomGeneratorFactory;
import es.uned.simda.acidge.ge.random.RandomJava;
import es.uned.simda.acidge.ge.random.RandomMock;



/**
 * @author fcastejo
 *
 */
public class TestGenotipo 
{
	private final static Logger logger = Logger.getLogger(TestGenotipo.class.getName());

	static RandomMock randomMock;
	
	static byte genes1[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	static byte genes2[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	static byte genes3[] = { 0, 1, 2, 3, 4, 15, 16, 17, 18, 19 };
		
	@BeforeClass
	public static void before()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
		Genotipo g1 = new Genotipo(genes1);
		g1.setLongExp(5);
		Genotipo g2 = new Genotipo(genes1);
		g2.setLongExp(5);
			
		motor(g1, g2, true, true);
	}

	@Test
	public void test2() 
	{
		logger.info("test2");
		
		Genotipo g1 = new Genotipo(genes1);
		g1.setLongExp(5);
		Genotipo g2 = new Genotipo(genes2);
		g2.setLongExp(5);
			
		motor(g1, g2, false, true);
	}
	
	@Test
	public void test3() 
	{
		logger.info("test3");
		
		Genotipo g1 = new Genotipo(genes1);
		g1.setLongExp(5);
		Genotipo g2 = new Genotipo(genes3);
		g2.setLongExp(5);
			
		motor(g1, g2, false, true);
	}
	
	@Test
	public void test4() 
	{
		logger.info("test4");
		
		Genotipo g1 = new Genotipo(genes1);
		g1.setLongExp(6);
		Genotipo g2 = new Genotipo(genes3);
		g2.setLongExp(6);
			
		motor(g1, g2, false, false);
	}
	
	@Test
	public void test5() 
	{
		logger.info("test5");
		
		Genotipo g1 = new Genotipo(genes1);
		g1.setLongExp(3);
		Genotipo g2 = new Genotipo(genes3);
		g2.setLongExp(4);
			
		motor(g1, g2, false, false);
	}
	
	@Test
	public void test6() 
	{
		logger.info("test6");
		
		Genotipo g1 = new Genotipo(genes1);
		g1.setLongExp(0);
		Genotipo g2 = new Genotipo(genes1);
		g2.setLongExp(0);
			
		motor(g1, g2, true, true);
	}
	
	@Test
	public void test7() 
	{
		logger.info("test7");
		
		Genotipo g1 = new Genotipo(genes1);
		g1.setLongExp(0);
		Genotipo g2 = new Genotipo(genes3);
		g2.setLongExp(0);
			
		motor(g1, g2, false, true);
	}
	
	
	void motor(Genotipo g1, Genotipo g2, boolean expected1, boolean expected2)
	{
		boolean res[] = g1.esIgual(g2);
		
		assertEquals(res[0], expected1);
		assertEquals(res[1], expected2);
	}
}
