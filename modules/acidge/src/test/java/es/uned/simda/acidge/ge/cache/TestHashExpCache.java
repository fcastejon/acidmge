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
 * TestCase para probar la clase ExpCacheBasico
 */
package es.uned.simda.acidge.ge.cache;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.problema.EvalRes;

public class TestHashExpCache 
{
	private final static Logger logger = Logger.getLogger(TestHashExpCache.class.getName());
	final static String newLine = String.format("%n");

	static ExpCache expCache;
	
	@BeforeClass
	public static void checkEA()
	{
		es.uned.simda.acidge.util.Util.checkEA();
	     
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
		//Creación de HashExpCache, con patrón decorador
		expCache = new ExpCacheBasico();
		expCache = new HashExpCache(expCache);
	}
	
	@Test
	//public void testAdd() 
	public void testAll()
	{
		logger.info("testAdd");
		boolean ret;
		
		EvalRes evalRes = new EvalRes(10);
		ret = expCache.add(exp1, evalRes);
		
		assertTrue(ret);
		
		ret = expCache.add(exp2, null);
		assertTrue(ret);
		
		evalRes = new EvalRes(5);
		ret = expCache.add(exp2, evalRes);
		
		assertFalse(ret);
		
		ret = expCache.add(exp3, null);
		
		assertTrue(ret);
		/*
	}
	
	@Test
	public void testGetEvalItem() 
	{
	*/
		logger.info("testGetEvalItem");
		//boolean ret;
		EvalItem evalItem;
		
		try
		{
			evalItem = expCache.getEvalItem(exp1);
			assertEquals(10, evalItem.getFitness(), 1e-6);
			
			for(int i = 0; i < 4;i++)
			{
				evalItem = expCache.getEvalItem(exp2);
				assertEquals(5, evalItem.getFitness(), 1e-6);
			}
		}
		catch(Exception exp)
		{
			fail();
		}
		
		try
		{
			evalItem = expCache.getEvalItem(exp3);
			fail();
		}
		catch(ExpCacheSyncYaSolicitadoException exp)
		{
			assertTrue(true);
		}
		catch(Exception exp)
		{
			fail();
		}
		
		try
		{
			evalItem = expCache.getEvalItem(exp4);
			fail();
		}
		catch(ExpCacheNotFoundException exp)
		{
			assertTrue(true);
		}
		catch(Exception exp)
		{
			fail();
		}
		/*
	}

	@Test
	public void testMetodosConRetorno() 
	{
	*/
		int ret1;
		
		ret1 = expCache.getTotalHits();
		
		logger.info("Hits:" + ret1);
		
		assertEquals(5, ret1);
		
		ret1 = expCache.getEntradas();
		
		logger.info("Entradas:" + ret1);
		
		assertEquals(3, ret1);
		
		double res = expCache.getEficiencia();
		
		logger.info("Eficiencia:" + res);
		
		assertEquals(0.4, res, 1e-6);
		
		ret1 = expCache.getOcupacion();
	
		logger.info("Ocupación:" + ret1);
		
		assertEquals(132, ret1);
	/*
	}

	@Test
	public void testResto() 
	{
	*/
		expCache.esperaAsinc(null);
		
		//Necesario para que dump no dé error
		//EvalRes 
		evalRes = new EvalRes(1);
		//boolean 
		ret = expCache.add(exp3, evalRes);
		
		assertFalse(ret);
				
		expCache.dump();
		
		assertTrue(true);
	}

/*	
	void motor()
	{

		
		assertArrayEquals(expected1, hijo1.getGenes());
		assertArrayEquals(expected2, hijo2.getGenes());
	}
	*/
	
	String exp1 = 
		"R 1 2 1.0e0" + newLine +
		"R 2 1 2.4e5" + newLine +
		"Q 3 1 2 2N2907" + newLine +
		"R 3 4 3.8e9" + newLine +
		"Q 5 6 0 2N2222" + newLine +
		"C 6 9 2.2e-10" + newLine +
		"C 7 9 3.4e-3" + newLine +
		"C 8 9 3.8e-3" + newLine;
	
	String exp2 = 
		"Q 4 2 8 2N2222" + newLine +
		"Q 3 3 8 2N2907" + newLine +
		"R 3 9 4.4e6" + newLine +
		"R 7 1 7.7e1" + newLine +
		"Q 3 6 5 2N2222" + newLine +
		"C 7 7 7.3e-5" + newLine +
		"R 8 5 4.3e9" + newLine +
		"R 8 5 7.0e2" + newLine +
		"R 8 7 9.0e9" + newLine +
		"R 8 3 2.0e2" + newLine +
		"Q 7 3 4 2N2222" + newLine +
		"C 3 6 7.9e-5" + newLine +
		"Q 2 6 4 2N2222" + newLine +
		"Q 5 3 4 2N2222" + newLine +
		"R 6 3 4.4e8" + newLine +
		"R 4 6 9.9e1" + newLine +
		"C 6 1 2.6e-4" + newLine +
		"Q 1 5 8 2N2907" + newLine +
		"Q 9 9 5 2N2222" + newLine +
		"R 2 2 1.6e4" + newLine +
		"R 7 3 1.8e7" + newLine +
		"R 7 9 4.2e6" + newLine +
		"R 7 9 9.3e1" + newLine +
		"Q 1 6 3 2N2222" + newLine +
		"C 6 7 8.3e-3" + newLine +
		"R 0 5 4.2e7" + newLine +
		"R 8 5 7.0e2" + newLine +
		"R 3 5 9.4e5" + newLine;
	
	String exp3 = 
		"C 3 5 9900p" + newLine +
		"C 6 4 5900n" + newLine +	
		"R 1 5 10" + newLine +
		"R 5 1 550k" + newLine +
		"R 6 1 6700Meg" + newLine +
		"R 6 7 9900G" + newLine + 
		"C 8 9 10m" + newLine +
		"C 9 5 320u" + newLine +
		"C 0 5 5900n" + newLine +
		"C 1 5 9900p" + newLine;
	
	String exp4 = 
		"R 1 1 10K" + newLine;
}
