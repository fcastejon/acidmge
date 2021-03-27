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
 * Tests de la expansión
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
public class TestEBNFGrammarExpansion
{
	private final static Logger logger = Logger.getLogger(TestEBNFGrammarExpansion.class.getName());
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

	@Test
	public void test3() 
	{
		logger.info("test3");
		String gram = gramatica3();
		String expected = expected3();

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
			"A = T | D;" + newLine +
			"T = '(', T, ')', <GEXOMarker>; (* esta línea es un comentario *)" + newLine +
			"V = 'AA' | \"BBB\" | 'C';" + newLine;
		
		return gram;
	}
	
	
	private String expected1()
	{
		String expected = 
			"A = T | D;" + newLine +
			"T = '(', T, ')', <GEXOMarker>;" + newLine +
			"V = 'AA' | 'BBB' | 'C';" + newLine;

		return expected;
	}
	
	private String gramatica2()
	{
		String gram = 
			"(* Ejemplo *)" + newLine +
			"A = <GEResult>;" + newLine +
			"B = \"A\" | T, \"A\";" + newLine +
			"T = B;" + newLine +
			"V = <GECodonValue: 1, 3>;" + newLine;

		return gram;
	}
	
	private String expected2()
	{
		String expected = 
			"A = <GEResult>;" + newLine +
			"B = 'A' | T, 'A';" + newLine +
			"T = B;" + newLine +
			"V = <GECodonValue:1,3>;" + newLine;

		return expected;
	}
	private String gramatica3()
	{
		String gram = 
			"%MNN = <GECodonValue: 10, 20 >;" + newLine +
			"A = <GEResult>;" + newLine +
			"B = \"A\" | T, \"A\";" + newLine +
			"T = B;" + newLine +
			"V = <GECodonValue: 1, %MNN + 3>;" + newLine;

		return gram;
	}
	
	private String expected3()
	{
		String expected =  
			"%MNN = <GECodonValue:10,20>;" + newLine +
			"A = <GEResult>;" + newLine +
			"B = 'A' | T, 'A';" + newLine +
			"T = B;" + newLine +
			"V = <GECodonValue:1,%MNN+3>;" + newLine;
		
		return expected;
	}


}
