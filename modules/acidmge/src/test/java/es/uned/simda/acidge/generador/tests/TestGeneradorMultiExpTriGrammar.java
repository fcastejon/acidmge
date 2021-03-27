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
 * TestCase para probar la clase GeneradorMultiGrammar y las gramáticas testMultiExpTri1.ebnf, testMultiExpTri2.ebnf y testMultiExpTri3.ebnf
 */
package es.uned.simda.acidge.generador.tests;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.io.InputStream;
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


public class TestGeneradorMultiExpTriGrammar 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorMultiExpTriGrammar.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testMultiExpTri1.ebnf";
	final static String GRAMMARTEST2 = "testMultiExpTri2.ebnf";
	final static String GRAMMARTEST3 = "testMultiExpTri3.ebnf";
	final static int ngrama = 3;

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
		is = getClass().getResourceAsStream(GRAMMARTEST3);
		gramas[2] = GeneradorGrammar.cargaGramatica(is);

		GeneradorMultiGrammar.setGramaticas(gramas);
	}
		
	@Test
	public void test1() 
	{
		logger.info("test1");
				
		String expected = "X/1.0";
		byte genes[] = //{ 0, 3, 3, 2, 2, 1 };
			{ 0, 3, 3, 2, 2, 1, 1 }; //, 3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3 };
		
		motor(genes, 0, 100, expected);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
				
		String expected = "LOG(X)*X";
		byte genes[] = { 0, 1, 2, 3, 3, 3, 2, 2, 2 }; //3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3 };
		
		motor(genes, 0, 100, expected);
	}

	@Test
	public void test3() 
	{
		logger.info("test3");

		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGrammar.cargaGrama(is);
		
		String expected = "LOG((1.0*1.0))+LOG((1.0*1.0))";
		byte genes[] = { 0, 3, 0, 3, 4, 2, 3, 1, 2, 1, 3, 2, 1, 1, 2, 1, 3, 2, 1 };
		//byte genes[] = { 1, 0, 0, 0, 2, 2, 2, 2, 3 };
		
		motor(genes, 1, 100, expected);
	}

	@Test
	public void test4() 
	{
		logger.info("test4");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGrammar.cargaGrama(is);
		
		String expected = "COS(X-1.0)+COS(X-1.0)";
		byte genes[] = { 0, 3, 0, 1, 4, 2, 1, 0, 2, 0, 1, 2, 1, 0, 2, 2, 1, 2, 1 };
		
		motor(genes, 1, 100, expected);
	}	

	public void motor(int [] genes, int MaxWrapping, int MaxRecursionLevel, String expected)
	{	
		//Aprovechamos el constructor que ademite cadenas de int
		Genotipo dummy = new Genotipo(genes);
		
		motor(dummy.getGenes(), MaxWrapping, MaxRecursionLevel, expected);
	}
	
	
	public void motor(byte [] genes, int MaxWrapping, int MaxRecursionLevel, String expected)
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
		}
		catch(GenException e)
		{
			//e.printStackTrace();
			logger.severe(e.getMessage());
			fail(e.getMessage());
		}
	}
}
