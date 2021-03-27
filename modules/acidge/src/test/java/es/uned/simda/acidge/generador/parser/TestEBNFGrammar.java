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
 * TestEBNFEBNFGrammar
 * 
 * Tests muy simples de parseado
 */
package es.uned.simda.acidge.generador.parser;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.StringReader;
import java.util.logging.Logger;
import es.uned.simda.acidge.generador.gramatica.Gramatica;

/**
 * @author fcastejo
 *
 */
public class TestEBNFGrammar
{
	private final static Logger logger = Logger.getLogger(TestEBNFGrammar.class.getName());
	final static String newLine = String.format("%n");
	
	@BeforeClass
	public static void init()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.trivialSetup();
	}
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		String gram = gramatica1();
		String expected = expected1();

		motor(gram, expected);
	}

	@Test
	public void test2() 
	{
		logger.info("test2");
		String gram = gramatica2();
		String expected = expected2();

		motor(gram, expected);
	}

	void motor(String gram, String expected)
	{
		StringReader sr = new StringReader(gram);
		
		logger.info("gramatica:" + newLine + gram);
		
		try
		{
		    EBNFGrammar parser = new EBNFGrammar(sr);
		    
		    //parser.setParseOnly();
		        
			ASTSyntax ast = parser.Syntax();
			
			EBNFGrammarVisitor vd = new EBNFGrammarVisitorDump();
			
			ast.jjtAccept(vd, null);

			EBNFGrammarVisitor v = new EBNFGrammarVisitorAGramatica();
			
			Gramatica gramatica = (Gramatica) ast.jjtAccept(v, null);
			
			String actual = gramatica.toString();
								
			logger.info("actual:" + actual);
			logger.info("actual.length:" + actual.length());
			logger.info("expected:" + expected);
			logger.info("expected.length:" + expected.length());

			assertEquals(expected, actual);

		}
		catch(ParseException e)
		{
			e.printStackTrace();
			fail("ParseException");
		}
	}
	
	private String gramatica1()
	{
		String gram =
			"(* Gramática de prueba 1 *)" + newLine + 
			"S = T | 'D';" + newLine +
			"T = '(', T, ')'; (* esta línea es un comentario *)" + newLine +
			"T = 'AA' | \"BBB\" | 'C';" + newLine;
		
		return gram;
	}
	
	
	private String expected1()
	{
		String expected = 
			"S = T | 'D';" + newLine +
			"T = '(', T, ')' | 'AA' | 'BBB' | 'C';" + newLine;

		return expected;
	}
	
	private String gramatica2()
	{
		String gram = 
			"(* Ejemplo *)" + newLine +
			"S = \"A\" | T, \"A\";" + newLine +
			"T = S;" + newLine;

		return gram;
	}
	
	private String expected2()
	{
		String expected = 
			"S = 'A' | T, 'A';" + newLine +
			"T = S;" + newLine;

		return expected;
	}

}
