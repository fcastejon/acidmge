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
import es.uned.simda.acidge.generador.GenException;
import es.uned.simda.acidge.generador.Generador;
import es.uned.simda.acidge.generador.GeneradorGrammar;
import es.uned.simda.acidge.generador.gramatica.Gramatica;
import es.uned.simda.acidge.generador.parser.ASTSyntax;
import es.uned.simda.acidge.generador.parser.EBNFGrammar;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitor;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitorAGramatica;
import es.uned.simda.acidge.generador.parser.ParseException;


public class TestGeneradorNetlistBloquesBJT 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorNetlistBloquesBJT.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testNetlistBloquesBJT.ebnf";

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
			"R 1 2 1.0e0" + newLine +
			"R 1 2 1.0e0" + newLine +
			"R 1 2 1.0e0" + newLine +
			"nulo1 nulo1 nulo1 nulo1 nulo1" + newLine +
			"C 3 1 1.0e-12" + newLine +
			"C 3 1 1.0e-12" + newLine +
			"Q 1 2 3 Q2N3906 nulo1" + newLine +
			"Q 1 2 3 Q2N3906 nulo1" + newLine +
			"nulo2 nulo1 nulo2 nulo1 nulo2" + newLine;
			
		byte genes[] = { 
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
		
		motor(genes, 0, 100, expected);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGrammar.cargaGrama(is);
		
		String expected = 
			"R 1 2 1.0e0" + newLine +
			"R 2 1 2.4e5" + newLine +
			"R 3 4 3.8e9" + newLine +
			"C 6 9 2.2e-10" + newLine +
			"C 7 9 3.4e-3" + newLine +
			"C 8 9 3.8e-3" + newLine +
			"nulo1 nulo1 nulo2 nulo2 nulo1" + newLine +
			"Q 3 1 2 Q2N3904 nulo1" + newLine +
			"Q 5 6 0 Q2N3906 nulo2" + newLine;
		
			
		byte genes[] = { 
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
		
		motor(genes, 0, 100, expected);
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
