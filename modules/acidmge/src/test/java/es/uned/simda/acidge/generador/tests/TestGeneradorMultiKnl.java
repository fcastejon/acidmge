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


public class TestGeneradorMultiKnl 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorMultiKnl.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testMultiBlockKnl1.ebnf";
	final static String GRAMMARTEST2 = "testMultiBlockKnl2.ebnf";
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
				
		String expected = "f(x)=(-3.3e-5) * K3(+3.1e-8, +8.8e0) + (-8.7e-5) * K1(+8.3e-6, -4.4e-7) + " +
				"(+4.2e-5) * K1(+6.2e-8, -3.7e3) + (-5.5e-5) * K2(-4.2e4, 6.4e7, 8)";
		
		byte genes[] = { 5, 7, 13, 8, 
				31, 2, 3, 4, 2, 2, 1, 1, 6, 7, 8, 9, 15, 34, 7, 23, 92, 115, 3, 22, 31,
				48, 14, 78, 92, 21, 62, 23, 24, 32, 92, 1, 31, 56, 27, 88, 19, 31, 15, 23, 7, 84, 32, 13,
				32, 34, 73, 48, 51, 32
				};

		Integer expected_xopoints[] = { 4, 52 };
		
		logger.info("genes.length:" + genes.length);
		
		motor(genes, 10, 100, expected, expected_xopoints);
	}


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
			
			logger.info("XOPoints:" + xopoints[0] + " " + xopoints[1]);
			
			logger.info("fu");
			
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
