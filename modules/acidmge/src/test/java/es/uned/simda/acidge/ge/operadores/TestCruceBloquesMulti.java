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
 * TestCase para probar la clase CruceBloquesMulti
 */
package es.uned.simda.acidge.ge.operadores;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomMock;



/**
 * @author fcastejo
 *
 */
public class TestCruceBloquesMulti 
{
	private final static Logger logger = Logger.getLogger(TestCruceBloquesMulti.class.getName());

	static RandomMock randomMock;
	static OperadorRecombinacion cruce;
	
	//static byte genes1[] = { 0, 1, 2, 3, 4, 5 };
	//static byte genes2[] = { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
	
	//Para límite máximo
	//static byte genes3[] = { 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32 };

	static byte genes1[] = { 
		51, 51, 51, 51, 51, 51,
		1, 1, 1, 1, 1, 1,
		2, 2, 2, 2, 2, 2,
		3, 3, 3, 3, 3, 3,
		4, 4, 4, 4, 4, 4,
		5, 5, 5, 5, 5, 5,
		6, 6, 6, 6, 6, 6,
		7, 7, 7, 7, 7, 7,
		8, 8, 8, 8, 8, 8,
		9, 9, 9, 9, 9, 9
		};
	
	static byte genes2[] = { 
			52, 52, 52, 52, 52, 52,
			2, 1, 2, 0, 0, 0,
			2, 1, 2, 0, 0, 0,
			2, 1, 2, 0, 0, 0,
			0, 0, 0, 0, 0, 0,
			2, 3, 1, 0, 0, 0,
			1, 3, 1, 0, 0, 0,
			2, 1, 2, 3, 9, 0,
			2, 1, 2, 3, 9, 0,
			0, 1, 2, 3, 4, 5
			};
	
	static byte genes3[] = { 
			53, 53, 53, 53, 53, 53, 
			1, 1, 1, 1, 1, 1,
			2, 2, 2, 2, 2, 2,
			3, 3, 3, 3, 3, 3,
			4, 4, 4, 4, 4, 4,
			5, 5, 5, 5, 5, 5,
			6, 6, 6, 6, 6, 6,
			7, 7, 7, 7, 7, 7,
			8, 8, 8, 8, 8, 8,
			9, 9, 9, 9, 9, 9,
			2, 1, 2, 0, 0, 0,
			2, 2, 1, 1, 4, 5,
			1, 3, 4, 2, 8, 9,
			2, 6, 9, 1, 2, 2,
			2, 7, 9, 2, 4, 9,
			2, 8, 9, 2, 8, 9,
			0, 0, 0, 1, 1, 0,
			2, 3, 1, 2, 0, 0,
			1, 5, 6, 0, 1, 1
			};
	
	static byte genes4[] = { 
		54, 54, 54, 54, 54, 54,
		1, 1, 1, 1, 1, 1,
		2, 2, 2, 2, 2, 2,
		3, 3, 3, 3, 3, 3,
		4, 4, 4, 4, 4, 4,
		5, 5, 5, 5, 5, 5,
		6, 6, 6, 6, 6, 6,
		7, 7, 7, 7, 7, 7,
		8, 8, 8, 8, 8, 8,
		9, 9, 9, 9, 9, 9,
		0, 0, 0, 0, 0, 0,
		1, 1, 1, 1, 1, 1,
		2, 2, 2, 2, 2, 2,
		3, 3, 3, 3, 3, 3,
		4, 4, 4, 4, 4, 4,
		5, 5, 5, 5, 5, 5,
		6, 6, 6, 6, 6, 6,
		7, 7, 7, 7, 7, 7,
		8, 8, 8, 8, 8, 8,
		9, 9, 9, 9, 9, 9,
		0, 0, 0, 0, 0, 0
		};
	
	@BeforeClass
	public static void checkEA()
	{
		es.uned.simda.acidge.util.Util.checkEA();
	     
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	     
		//RandomGeneratorFactory rgf = RandomGeneratorFactory.getRandomGeneratorFactory();
		randomMock = new RandomMock();
	    //Genotipo.inicia_clase(1, 10, randomMock);
	    
	    //OperatorFactory opf = OperatorFactory.getOperatorFactory();
	    
	    //cruce = opf.creaOperadorRecombinacion(12, 0.7, 1, 6, 0.0, 0.0, 0.0, 120, randomMock);
		cruce = new CruceBloquesMulti(12, 0.7, 1, 6, 0.0, 120, randomMock);
	}
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
		double dbl[] = { 0.7 };
		int in[] = { 2, 1 };

		byte expected1[] = { 
			51, 51, 51, 51, 51, 51, 
			1, 1, 1, 1, 1, 1,
			2, 2, 2, 2, 2, 2,
			2, 1, 2, 0, 0, 0,
			2, 1, 2, 0, 0, 0,		
			5, 5, 5, 5, 5, 5,
			6, 6, 6, 6, 6, 6,
			2, 3, 1, 0, 0, 0,
			1, 3, 1, 0, 0, 0,
			2, 1, 2, 3, 9, 0,
			2, 1, 2, 3, 9, 0,
			0, 1, 2, 3, 4, 5
			};
		
		byte expected2[] = { 
				52, 52, 52, 52, 52, 52,
				2, 1, 2, 0, 0, 0,
				3, 3, 3, 3, 3, 3,
				4, 4, 4, 4, 4, 4,
				0, 0, 0, 0, 0, 0,
				7, 7, 7, 7, 7, 7,
				8, 8, 8, 8, 8, 8,
				9, 9, 9, 9, 9, 9
				};
		
		motor(genes1, 30, genes2, 24, dbl, in, expected1, expected2);
	}

	@Test
	public void test2() 
	{
		logger.info("test2");
		
		double dbl[] = { 0.7001 };
		int in[] = { 0 };
		byte expected1[] = { 
			51, 51, 51, 51, 51, 51,
			1, 1, 1, 1, 1, 1,
			2, 2, 2, 2, 2, 2,
			3, 3, 3, 3, 3, 3,
			4, 4, 4, 4, 4, 4,
			5, 5, 5, 5, 5, 5,
			6, 6, 6, 6, 6, 6,
			7, 7, 7, 7, 7, 7,
			8, 8, 8, 8, 8, 8,
			9, 9, 9, 9, 9, 9
			};
		
		byte expected2[] = { 
				52, 52, 52, 52, 52, 52, 
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				2, 3, 1, 0, 0, 0,
				1, 3, 1, 0, 0, 0,
				2, 1, 2, 3, 9, 0,
				2, 1, 2, 3, 9, 0,
				0, 1, 2, 3, 4, 5
				};
		
		motor(genes1, 6, genes2, 6, dbl, in, expected1, expected2);
	}

	@Test
	public void test3() 
	{
		logger.info("test3");
		
		double dbl[] = { 0.4 };
		int in[] = { 0, 0 };
		
		byte expected1[] = { 
				51, 51, 51, 51, 51, 51, 
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				2, 3, 1, 0, 0, 0,
				1, 3, 1, 0, 0, 0,
				2, 1, 2, 3, 9, 0,
				2, 1, 2, 3, 9, 0,
				0, 1, 2, 3, 4, 5
				};
		
		byte expected2[] = { 
				52, 52, 52, 52, 52, 52, 
				1, 1, 1, 1, 1, 1,
				2, 2, 2, 2, 2, 2,
				3, 3, 3, 3, 3, 3,
				4, 4, 4, 4, 4, 4,
				5, 5, 5, 5, 5, 5,
				6, 6, 6, 6, 6, 6,
				7, 7, 7, 7, 7, 7,
				8, 8, 8, 8, 8, 8,
				9, 9, 9, 9, 9, 9
				};
			
		motor(genes1, 30, genes2, 24, dbl, in, expected1, expected2);
	}

	@Test
	public void test4() 
	{
		logger.info("test4");
		
		double dbl[] = { 0.1 };
		int in[] = { 0, 2 };
		
		byte expected1[] = { 
				51, 51, 51, 51, 51, 51, 
				2, 1, 2, 0, 0, 0,
				1, 3, 1, 0, 0, 0,
				2, 1, 2, 3, 9, 0,
				2, 1, 2, 3, 9, 0,
				0, 1, 2, 3, 4, 5
				};
			
			byte expected2[] = { 
					52, 52, 52, 52, 52, 52, 
					2, 1, 2, 0, 0, 0,
					2, 1, 2, 0, 0, 0,
					1, 1, 1, 1, 1, 1,
					2, 2, 2, 2, 2, 2,
					3, 3, 3, 3, 3, 3,
					4, 4, 4, 4, 4, 4,
					0, 0, 0, 0, 0, 0,
					2, 3, 1, 0, 0, 0,			
					5, 5, 5, 5, 5, 5,
					6, 6, 6, 6, 6, 6,
					7, 7, 7, 7, 7, 7,
					8, 8, 8, 8, 8, 8,
					9, 9, 9, 9, 9, 9
					};
		
		motor(genes1, 30, genes2, 24, dbl, in, expected1, expected2);
	}

	@Test
	public void test5() 
	{
		logger.info("test5");
		
		double dbl[] = { 0.69999 };
		int in[] = { 2, 0 };

		byte expected1[] = { 
				51, 51, 51, 51, 51, 51, 
				1, 1, 1, 1, 1, 1,
				2, 2, 2, 2, 2, 2,
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				4, 4, 4, 4, 4, 4,
				5, 5, 5, 5, 5, 5,
				2, 3, 1, 0, 0, 0,
				1, 3, 1, 0, 0, 0,
				2, 1, 2, 3, 9, 0,
				2, 1, 2, 3, 9, 0,
				0, 1, 2, 3, 4, 5
				};
			
			byte expected2[] = { 
					52, 52, 52, 52, 52, 52,
					3, 3, 3, 3, 3, 3,
					6, 6, 6, 6, 6, 6,
					7, 7, 7, 7, 7, 7,
					8, 8, 8, 8, 8, 8,
					9, 9, 9, 9, 9, 9
					};
		
		
		motor(genes1, 24, genes2, 30, dbl, in, expected1, expected2);
	}

	@Test
	public void test6() 
	{
		logger.info("test6");
		
		double dbl[] = { 0.3 };
		int in[] = { 1, 1 };

		byte expected1[] = { 
				51, 51, 51, 51, 51, 51, 
				1, 1, 1, 1, 1, 1,
				2, 1, 2, 0, 0, 0,
				5, 5, 5, 5, 5, 5,				
				0, 0, 0, 0, 0, 0,
				2, 3, 1, 0, 0, 0,
				1, 3, 1, 0, 0, 0,
				2, 1, 2, 3, 9, 0,
				2, 1, 2, 3, 9, 0,
				0, 1, 2, 3, 4, 5
				};
			
			byte expected2[] = { 
					52, 52, 52, 52, 52, 52, 
					2, 1, 2, 0, 0, 0,
					2, 2, 2, 2, 2, 2,
					3, 3, 3, 3, 3, 3,
					4, 4, 4, 4, 4, 4,
					2, 1, 2, 0, 0, 0,
					6, 6, 6, 6, 6, 6,
					7, 7, 7, 7, 7, 7,
					8, 8, 8, 8, 8, 8,
					9, 9, 9, 9, 9, 9
					};
			
		motor(genes1, 30, genes2, 18, dbl, in, expected1, expected2);
	}

	@Test
	public void test7() 
	{
		logger.info("test7");
		
		double dbl[] = { 0.1 };
		int in[] = { 2, 2 };
		
		byte expected1[] = { 
			51, 51, 51, 51, 51, 51, 
			1, 1, 1, 1, 1, 1,
			2, 2, 2, 2, 2, 2,
			2, 1, 2, 0, 0, 0,
			4, 4, 4, 4, 4, 4,
			5, 5, 5, 5, 5, 5,
			1, 3, 1, 0, 0, 0,
			2, 1, 2, 3, 9, 0,
			2, 1, 2, 3, 9, 0,
			0, 1, 2, 3, 4, 5
			};
		
		byte expected2[] = { 
				52, 52, 52, 52, 52, 52, 
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				3, 3, 3, 3, 3, 3,
				0, 0, 0, 0, 0, 0,
				2, 3, 1, 0, 0, 0,
				6, 6, 6, 6, 6, 6,
				7, 7, 7, 7, 7, 7,
				8, 8, 8, 8, 8, 8,
				9, 9, 9, 9, 9, 9
				};
		
		motor(genes1, 24, genes2, 24, dbl, in, expected1, expected2);
	}

	@Test
	public void test8() 
	{
		logger.info("test8");
		
		double dbl[] = { 0.3 };
		int in[] = { 4, 1 };
		
		byte expected1[] = { 
			51, 51, 51, 51, 51, 51, 
			1, 1, 1, 1, 1, 1,
			2, 2, 2, 2, 2, 2,
			3, 3, 3, 3, 3, 3,
			4, 4, 4, 4, 4, 4,
			5, 5, 5, 5, 5, 5,
			6, 6, 6, 6, 6, 6,
			7, 7, 7, 7, 7, 7,
			8, 8, 8, 8, 8, 8,
			9, 9, 9, 9, 9, 9
			};
		
		byte expected2[] = { 
				52, 52, 52, 52, 52, 52,
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				2, 1, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				2, 3, 1, 0, 0, 0,
				1, 3, 1, 0, 0, 0,
				2, 1, 2, 3, 9, 0,
				2, 1, 2, 3, 9, 0,
				0, 1, 2, 3, 4, 5
				};
		
		motor(genes1, 6, 1, genes2, 6, 2, dbl, in, expected1, expected2);
	}

	@Test
	public void test9() 
	{
		logger.info("test9");
		
		//Prueba el límite máximo
		double dbl[] = { 0.3 };
		int in[] = { 1, 0 };
		
		byte expected1[]  = { 	
			52, 52, 52, 52, 52, 52, 
			2, 1, 2, 0, 0, 0,
			1, 1, 1, 1, 1, 1,
			2, 1, 2, 0, 0, 0,
			2, 2, 2, 2, 2, 2,
			3, 3, 3, 3, 3, 3,
			4, 4, 4, 4, 4, 4,
			5, 5, 5, 5, 5, 5,
			6, 6, 6, 6, 6, 6,
			7, 7, 7, 7, 7, 7,
			8, 8, 8, 8, 8, 8,
			9, 9, 9, 9, 9, 9,
			2, 1, 2, 0, 0, 0,
			2, 2, 1, 1, 4, 5,
			1, 3, 4, 2, 8, 9,
			2, 6, 9, 1, 2, 2,
			2, 7, 9, 2, 4, 9,
			2, 8, 9, 2, 8, 9,
			0, 0, 0, 1, 1, 0,
			2, 3, 1, 2, 0, 0
			//1, 5, 6, 0, 1, 1	
		};
	
		byte expected2[] = {
				53, 53, 53, 53, 53, 53, 
				2, 1, 2, 0, 0, 0,
				0, 0, 0, 0, 0, 0,
				2, 3, 1, 0, 0, 0,
				1, 3, 1, 0, 0, 0,
				2, 1, 2, 3, 9, 0,
				2, 1, 2, 3, 9, 0,
				0, 1, 2, 3, 4, 5
			};

		motor(genes2, 18, genes3, 12, dbl, in, expected1, expected2);
	}

	@Test
	public void test10() 
	{
		logger.info("test9");
		
		//Prueba el límite máximo
		double dbl[] = { 0.3 };
		int in[] = { 7, 1 };
		
		byte expected1[] = { 
				53, 53, 53, 53, 53, 53, 
				1, 1, 1, 1, 1, 1,
				2, 2, 2, 2, 2, 2,
				3, 3, 3, 3, 3, 3,
				4, 4, 4, 4, 4, 4,
				5, 5, 5, 5, 5, 5,
				6, 6, 6, 6, 6, 6,
				7, 7, 7, 7, 7, 7,
				2, 2, 2, 2, 2, 2,
				3, 3, 3, 3, 3, 3,
				4, 4, 4, 4, 4, 4,
				5, 5, 5, 5, 5, 5,
				6, 6, 6, 6, 6, 6,
				7, 7, 7, 7, 7, 7,
				8, 8, 8, 8, 8, 8,
				9, 9, 9, 9, 9, 9,
				9, 9, 9, 9, 9, 9,
				2, 1, 2, 0, 0, 0,
				2, 2, 1, 1, 4, 5,
				1, 3, 4, 2, 8, 9,
/*
				2, 6, 9, 1, 2, 2
				2, 7, 9, 2, 4, 9,
				2, 8, 9, 2, 8, 9,
				1, 1, 1, 1, 1, 1,
				2, 2, 2, 2, 2, 2,
				3, 3, 3, 3, 3, 3,
				4, 4, 4, 4, 4, 4,
				5, 5, 5, 5, 5, 5,
				6, 6, 6, 6, 6, 6,
				7, 7, 7, 7, 7, 7,
				8, 8, 8, 8, 8, 8,
				9, 9, 9, 9, 9, 9,
				0, 0, 0, 0, 0, 0
*/
				};
		
		byte expected2[]  = { 
				54, 54, 54, 54, 54, 54, 
				1, 1, 1, 1, 1, 1,
				8, 8, 8, 8, 8, 8,
				0, 0, 0, 0, 0, 0,
				0, 0, 0, 1, 1, 0,
				2, 3, 1, 2, 0, 0,
				1, 5, 6, 0, 1, 1				
			};
		
		motor(genes3, 54, genes4, 60, dbl, in, expected1, expected2);
	}
	

	
	void motor(byte [] g1, int ig1, byte [] g2, int ig2, double [] dbl, int [] in, byte [] expected1, byte [] expected2)
	{
		motor(g1, ig1, 0, g2, ig2, 0, dbl, in, expected1, expected2);
	}
	
	void motor(byte [] g1, int ig1, int wrapping1, byte [] g2, int ig2, int wrapping2, 
			double [] dbl, int [] in, byte [] expected1, byte [] expected2)
	{
		ArrayList<Integer> xopoints;
		
		randomMock.setDouble(dbl);
		randomMock.setInteger(in);
		
		Genotipo genotipo1 = new Genotipo(g1);
		genotipo1.setWrapping(wrapping1);
		xopoints = new ArrayList<Integer>();
		xopoints.add(6);
		xopoints.add(ig1);
		genotipo1.setXOPoints(xopoints);
		
		Genotipo genotipo2 = new Genotipo(g2);
		genotipo2.setWrapping(wrapping2);
		xopoints = new ArrayList<Integer>();
		xopoints.add(6);
		xopoints.add(ig2);
		genotipo2.setXOPoints(xopoints);
		
		Genotipo hijo1 = cruce.cruza(genotipo1, genotipo2);
		Genotipo hijo2 = cruce.cruza();
		
		logger.info("Hijo1:" + hijo1);
		logger.info("Hijo2:" + hijo2);
		
		assertArrayEquals(expected1, hijo1.getGenes());
		assertArrayEquals(expected2, hijo2.getGenes());
	}
}
