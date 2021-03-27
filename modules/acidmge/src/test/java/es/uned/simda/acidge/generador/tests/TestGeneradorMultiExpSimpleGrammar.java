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
 * TestCase para probar la clase GeneradorMultiGrammar y las gramáticas testMultiExpSimple1.ebnf y testMultiExpSimple2.ebnf
 * 
 * Los casos de prueba son los mismos de testGeneradorUniversalExpGrammar
 */
package es.uned.simda.acidge.generador.tests;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.generador.GenException;
import es.uned.simda.acidge.generador.Generador;
import es.uned.simda.acidge.generador.GeneradorGE2;
import es.uned.simda.acidge.generador.GeneradorGrammar;
import es.uned.simda.acidge.generador.GeneradorMultiGrammar;
import es.uned.simda.acidge.generador.gramatica.Gramatica;


public class TestGeneradorMultiExpSimpleGrammar 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorMultiExpSimpleGrammar.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testMultiExpSimple1.ebnf";
	final static String GRAMMARTEST2 = "testMultiExpSimple2.ebnf";
	final static int ngrama = 2;

	@BeforeClass
	public static void init()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.trivialSetup(Level.FINE);
	}
	
	@Before
	public void before()
	{	
		
		
		Gramatica gramas[] = new Gramatica[ngrama];
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		gramas[0] = GeneradorGrammar.cargaGramatica(is);
		is = getClass().getResourceAsStream(GRAMMARTEST2);
		gramas[1] = GeneradorGrammar.cargaGramatica(is);

		GeneradorMultiGrammar.setGramaticas(gramas);
	}

	@Test
	public void test1() 
	{
		logger.info("test1");
				
		String expected = "X/1";
		byte genes[] = { 10, 0, 3, 3, 2, 2, 1, 1 };
		Integer expected_xopoints[] = { 1, 4 };
		
		motor(genes, 0, 100, expected, expected_xopoints);
	}

	@Test
	public void test2() 
	{
		logger.info("test2");
				
		String expected = "(LOG(X)*X)";
		byte genes[] = { 10, 1, 2, 3, 3, 3, 2, 3, 2 }; // 3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3 };
		Integer expected_xopoints[] = { 1, 5 };
		
		motor(genes, 0, 100, expected, expected_xopoints);
	}

	@Test
	public void test3() 
	{
		logger.info("test3");

		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGrammar.cargaGrama(is);
		
		String expected = "LOG((6*5))+LOG((1*1))";
		byte genes[] = { 4, 0, 2, 1, 3, 3, 2, 1, 3, 3, 3, 3, 6, 3, 1, 5, 0, 3, 3, 1, 3, 1, 11 };
		Integer expected_xopoints[] = { 1, 10 };
		
		motor(genes, 1, 100, expected, expected_xopoints);
	}
	
/*	
	@Test
	public void test4() 
	{
		logger.info("test4");
		
		carga(GRAMMARTEST1);
		
		String expected = "COS(X-1.0)+COS(X-1.0)";
		byte genes[] = { 0, 2, 1, 0, 3, 2, 1, 3, 3 };
		
		motor(genes, 1, 100, expected);
	}
	
/*
	@Test
	public void test1() 
	{
		logger.info("test1");
		
		carga(GRAMMARTEST1);
		
		byte genes[] = { 0, 1, 0, 1, 2, 3, 4 };
		
		motor(genes, 0, 100, expected1);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
		carga(GRAMMARTEST1);
		 
		byte genes[] = { 1, 1, 2, 3, 
						3, 5, 6, 7,
						5, 8, 9, 10,
						0, 3, 2, 1,
						0, 1, 2, 3, 
						4, 5, 6, 7,
						8, 9, 0, 1,
						2, 3, 4, 0
						};
		
		motor(genes, 0, 100, expected2);
	}
	*/
	public void motor(int [] genes, int MaxWrapping, int MaxRecursionLevel, String expected,
			Integer [] expected_xopoints)
	{	
		//Aprovechamos el constructor que ademite cadenas de int
		Genotipo dummy = new Genotipo(genes);
		
		motor(dummy.getGenes(), MaxWrapping, MaxRecursionLevel, expected, expected_xopoints);
	}
	
	
	public void motor(byte [] genes, int MaxWrapping, int MaxRecursionLevel, String expected, 
			Integer [] expected_xopoints)
	{	
		GEProperties geproperties = new GEProperties();
		geproperties.setMaxWrappingNumber(MaxWrapping);
		geproperties.setMaxRecursionLevel(MaxRecursionLevel);
		
		try
		{
			Generador gen = new GeneradorMultiGrammar(genes, geproperties);
			String actual = gen.genera();
			logger.info("actual: " + actual);
			logger.info("expected: " + expected);
			
			assertEquals(expected, actual);
			
			ArrayList<Integer> l = gen.getXOPoints();
			Integer xopoints[] = (Integer []) l.toArray(new Integer[l.size()]);
			
			assertArrayEquals(expected_xopoints, xopoints);
		}
		catch(GenException e)
		{
			//e.printStackTrace();
			logger.severe(e.getMessage());
			fail(e.getMessage());
		}
	}
}
