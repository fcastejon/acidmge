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


public class TestGeneradorMultiIsoKnl 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorMultiIsoKnl.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testMultiIsoKnl1.ebnf";
	final static String GRAMMARTEST2 = "testMultiIsoKnl2.ebnf";
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
				
		String expected = "(+6.6e-03) * K1(+6.1e-04, +8.8e-01) + (-3.1e-02) * K3(-9.7e-03, -8.0e-07)";
		
		int genes[] = { 
				1, 154, 167, 116, 120, 0, 0, 0, 
				172, 165, 38, 41, 26, 0, 0, 0,  
				//231, 188, 76, 
				124, 50, 121, 195, 198, 196, 228, 
				141, 215, 125, 27, 101, 33, 178, 240, 116, 231, 
				102, 45, 162, 147, 179, 86, 28, 246, 224, 236, 
				68, 234, 163, 174, 254, 88, 87, 76, 227, 208, 
				140, 211, 105, 72, 63, 13, 164, 208, 7, 8, 
				34, 49
				};
		

		Integer expected_xopoints[] = { 16, 32 };
		
		logger.info("genes.length:" + genes.length);
		
		motor(genes, 0, 100, expected, expected_xopoints);
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
			
			logger.info("longExp:" + gen.getLongExp());
			
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
