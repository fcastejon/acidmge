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
 * TestCase para probar la clase GeneradorGE2
 */
package es.uned.simda.acidge.generador.tests;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.generador.GenException;
import es.uned.simda.acidge.generador.Generador;
import es.uned.simda.acidge.generador.GeneradorGE2;
import es.uned.simda.acidge.generador.GeneradorGrammar;
import es.uned.simda.acidge.generador.gramatica.ElementoRegla;
import es.uned.simda.acidge.generador.gramatica.Gramatica;
import es.uned.simda.acidge.generador.gramatica.Subregla;
import es.uned.simda.acidge.generador.parser.ASTSyntax;
import es.uned.simda.acidge.generador.parser.EBNFGrammar;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitor;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitorAGramatica;
import es.uned.simda.acidge.generador.parser.ParseException;


/**
 * @author fcastejo
 *
 */
public class TestGeneradorGE2 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorGE2.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testUniversalSimple.ebnf";

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
		
		//No se chequea, sólo referencia
		String expectedGrammar = 
			"(* Comentario *)"  + newLine +
			"LIST = LINEA;"  + newLine +
			"LINEA = \"FU\";"  + newLine +
			newLine +
			"(* 6 nodos nuevos, 4 nodos de parte fija *)" + newLine +
			"nodo = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\";"  + newLine +
			"(*nulo1*)" + newLine;
			
		String expected = "FU 9"; 

		byte genes[] = { 0, 0, 9 };
		
		motor(genes, 0, 100, expected);
	}

	@Test
	public void test2() 
	{
		logger.info("test2");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);
		
		//No se chequea, sólo referencia
		String expectedGrammar = 
			"(* Comentario *)"  + newLine +
			"LIST = LINEA;"  + newLine +
			"LINEA = \"FU \", nodo;"  + newLine +
			newLine +
			"(* 7 nodos nuevos, 4 nodos de parte fija *)" + newLine +
			"nodo = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\" | \"10\";"  + newLine +
			"(*nulo1*)" + newLine;
		
		String expected = "FU 9"; 

		byte genes[] = { 1, 1, 9 };
		
		motor(genes, 0, 100, expected);
	}
		
	@Test
	public void test3() 
	{
		logger.info("test3");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);
		
		//No se chequea, sólo referencia
		String expectedGrammar = 
			"(* Comentario *)"  + newLine +
			"LIST = LINEA;"  + newLine +
			"LINEA = \"FU\";"  + newLine +
			newLine +
			"(* 6 nodos nuevos, 4 nodos de parte fija *)" + newLine +
			"nodo = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\";"  + newLine +
			"(*nulo1*)" + newLine;
			
		String expected = "FU 0"; 

		byte genes[] = { 0, 0, 10 };
		
		motor(genes, 0, 100, expected);
	}

	@Test
	public void test4() 
	{
		logger.info("test4");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);
		
		//No se chequea, sólo referencia
		String expectedGrammar = 
			"(* Comentario *)"  + newLine +
			"LIST = LINEA;"  + newLine +
			"LINEA = \"FU \", nodo;"  + newLine +
			newLine +
			"(* 7 nodos nuevos, 4 nodos de parte fija *)" + newLine +
			"nodo = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\" | \"10\";"  + newLine +
			"(*nulo2*)" + newLine;
		
		String expected = "FU 10"; 

		byte genes[] = { 1, 1, 10 };
		
		motor(genes, 0, 100, expected);
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
}
