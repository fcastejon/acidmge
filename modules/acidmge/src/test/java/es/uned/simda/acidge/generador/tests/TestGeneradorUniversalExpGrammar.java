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
 * TestCase para probar la clase Generador y la gramática testUniversalExpGrammar.ebnf
 */
package es.uned.simda.acidge.generador.tests;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;
import es.uned.simda.acidge.ge.random.RandomGeneratorFactory;
import es.uned.simda.acidge.generador.GenException;
import es.uned.simda.acidge.generador.Generador;
import es.uned.simda.acidge.generador.GeneradorGE2;
import es.uned.simda.acidge.generador.GeneradorGrammar;
import es.uned.simda.acidge.generador.gramatica.Gramatica;
import es.uned.simda.acidge.generador.parser.ASTSyntax;
import es.uned.simda.acidge.generador.parser.EBNFGrammar;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitor;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitorAGramatica;
import es.uned.simda.acidge.generador.parser.ParseException;


public class TestGeneradorUniversalExpGrammar 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorUniversalExpGrammar.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testUniversalExpGrammar.ebnf";

	Gramatica gramatica;
	
	@BeforeClass
	public static void init()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.trivialSetup(Level.FINE);
	}
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);	
		
		String expected = "X/1.0";
		byte genes[] = { 0, 3, 3, 2, 2, 1 };
		
		motor(genes, 0, 100, expected);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);	
				
		String expected = "(LOG(X)*X)";
		byte genes[] = { 1, 2, 3, 3, 3, 2, 3, 2 }; // 3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3 };
		
		motor(genes, 0, 100, expected);
	}

	@Test
	public void test3() 
	{
		logger.info("test3");

		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);	
		
		String expected = "LOG((1.0*1.0))+LOG((1.0*1.0))";
		byte genes[] = { 0, 2, 1, 3, 3, 2, 1, 3, 3, 3, 3, 3, 1, 0, 3, 3, 3, 1 };
		
		motor(genes, 1, 100, expected);
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
			Generador gen = new GeneradorGE2(genes, geproperties);
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

	String expected1 = 
		"R 1 0 nulo2 3.3e4 nulo1" + newLine +
		"* Fin" + newLine;

	String expected2 = 
		"R 1 2 nulo2 1.1e2 nulo2" + newLine +
		"C 5 6 nulo2 5.5e-6 nulo2" + newLine +
		"Q 8 9 10 Q2N3904 nulo2 nulo1 nulo2" + newLine +
		"R 3 2 nulo2 3.3e4 nulo1" + newLine +
		"* Fin" + newLine;
}
