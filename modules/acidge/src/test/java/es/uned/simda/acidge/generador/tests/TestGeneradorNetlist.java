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
 * TestCase para probar la clase Generador y la gramática testNetlistGrammar.ebnf
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
import es.uned.simda.acidge.generador.GeneradorGrammar;
import es.uned.simda.acidge.generador.gramatica.Gramatica;
import es.uned.simda.acidge.generador.parser.ASTSyntax;
import es.uned.simda.acidge.generador.parser.EBNFGrammar;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitor;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitorAGramatica;
import es.uned.simda.acidge.generador.parser.ParseException;


public class TestGeneradorNetlist 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorNetlist.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testNetlistGrammar.ebnf";
	final static String GRAMMARTEST2 = "testNetlistDesacoplo.ebnf";

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
		GeneradorGrammar.cargaGrama(is);
		
		String expected = 
			"R 1 3 10" + newLine +
			"R 3 1 550k" + newLine +
			"R 4 1 6700Meg" + newLine +
			"R 4 5 9900G" + newLine + 
			"C 6 7 10m" + newLine +
			"C 7 0 320u" + newLine +
			"C 8 0 5900n" + newLine +
			"C 9 0 9900p" + newLine;
			
		byte genes[] = { 0, 0, 0, 0, 0, 0, 0, 1, 
						 0, 1, 2, 0, 0, 0, 0, 
						 0, 2, 1, 1, 4, 5, 1,
						 0, 3, 1, 2, 5, 7, 2,
						 0, 3, 4, 2, 8, 9, 3, 
						 1, 5, 6, 0, 0, 0, 0,
						 1, 6, 9, 1, 2, 2, 1,
						 1, 7, 9, 2, 4, 9, 2,
						 1, 8, 9, 2, 8, 9, 3 };
		
		motor(genes, 0, 100, expected);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST2);
		GeneradorGrammar.cargaGrama(is);
		
		String expected = 
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
			
		byte genes[] = { 2, 8, 9, 3, 2, 4, 9, 2, 
						 0, 0, 0, 0, 0, 0, 0, 1, 
						 0, 1, 2, 0, 0, 0, 0, 
						 0, 2, 1, 1, 4, 5, 1,
						 0, 3, 1, 2, 5, 7, 2,
						 0, 3, 4, 2, 8, 9, 3, 
						 1, 5, 6, 0, 0, 0, 0,
						 1, 6, 9, 1, 2, 2, 1,
						 1, 7, 9, 2, 4, 9, 2,
						 1, 8, 9, 2, 8, 9, 3 };
		
		motor(genes, 0, 100, expected);
	}
	
/*
	@Test
	public void test2() 
	{
		logger.info("test2");
		
		String expected = 
			"C 2 1 11m" + newLine +
			"R 0 0 21k" + newLine;
		byte genes[] = { 0, 1, 1, 2, 1, 3, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1, 2, 3, 3 };
		
		motor(genes, 0, 100, expected);
	}
/*
	@Test
	public void test3() 
	{
		logger.info("test3");
		
		String expected = "LIST(END)(THREE_VIA0(CUT)(FLIP(END))(CUT))";
		byte genes[] = { 0, 2, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 1 }; //, 0, 0, 0, 1, 1, 1, 2, 3, 3 };
		
		motor(genes, 1, 100, expected);
	}

	@Test
	public void test4() 
	{
		logger.info("test4");
		
		String expected = "LIST(THREE_VIA1(FLIP(END))(FLIP(FLIP(END)))(NOP(WIRE(CUT))))(NOP(CUT))";
		byte genes[] = { 3, 1, 1, 1, 0, 0, 2, 1, 3, 1, 0, 0, 0, 1, 1, 1, 2, 0, 3, 1, 1, 0, 1}; 
		
		motor(genes, 1, 100, expected);
	}
	
	@Test
	public void test5() 
	{
		logger.info("test5");
		
		//La segunda repetición se debe al wrapping!!
		String expected = "LIST(THREE_VIA0(CUT)(NOP(END))(TWO_GROUND(WIRE(END))(CUT)))" +
				"(THREE_VIA0(CUT)(NOP(END))(TWO_GROUND(WIRE(END))(CUT)))";
		
		byte genes[] = { 3, 1, 0, 0, 1, 1, 1, 0, 0, 2, 1, 1, 2, 0, 0, 0, 1 };
		
		motor(genes, 1, 100, expected);
	}

	@Test
	public void test6() 
	{
		logger.info("test6");
		
		//La segunda repetición se debe al wrapping!!
		String expected = "LIST(C 1.0e-11(NOP(END)))(C 2.2e-12(END))";
		
		byte genes[] = { 4, 1, 0, 0, 1, 1, 1, 0, 0, 4, 1, 1, 2, 0, 0, 0, 1 };
		
		motor(genes, 1, 100, expected);
	}
	
	@Test
	public void test7() 
	{
		logger.info("test7");
		
		//La segunda repetición se debe al wrapping!!
		String expected = "LIST(R 9.1e7(END))(C 4.0e-12(R 2.1e1(TWO_VIA3(NOP(CUT))(R 9.1e7(END)))))";
		
		byte genes[] = { 4, 0, 8, 1, 7, 0, 0, 4, 1, 3, 0, 0, 9, 4, 1, 1, 1, 2, 0, 3, 1, 1, 0, 1 };
		
		motor(genes, 1, 100, expected);
	}
*/
	public void motor(byte [] genes, int MaxWrapping, int MaxRecursionLevel, String expected)
	{	
		GEProperties geproperties = new GEProperties();
		geproperties.setMaxWrappingNumber(MaxWrapping);
		geproperties.setMaxRecursionLevel(MaxRecursionLevel);
		
		try
		{
			Generador gen = new GeneradorGrammar(genes, geproperties);
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
