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
 * TestCase para probar la clase Generador y la gramática testUniversalNetlistSensorBloquesBJT4.ebnf
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


public class TestGeneradorUniversalNetlistBJT 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorUniversalNetlistBJT.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testUniversalNetlistSensorBloquesBJT4.ebnf";

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
				
		byte genes[] = { 0, 1, 0, 1, 2, 3, 4 };
		
		motor(genes, 0, 100, expected1);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);	
				 
		String expected = 
			"(* Comentario *)"  + newLine +
			"LIST = LINEA;"  + newLine +
			"LINEA = \"FU\";"  + newLine +
			newLine +
			"(* 7 nodos nuevos, 4 nodos de parte fija *)" + newLine +
			"nodo = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\" | \"10\";"  + newLine;
		
		byte genes[] = { 1, 1, 2, 3, 4, 5, 7 };
		
		motor(genes, 0, 100, expected2);
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

	String expected1 = 
		"(*" + newLine +
		" * netlistSensorBloquesBJT4.ebnf" + newLine +
		" *" + newLine +
		" * Para el circuito sensor de temperatura" + newLine +
		" *" + newLine +
		" * Gramática de generación de netlist utilizando bloques de componentes" + newLine +
		" * Considera que va a haber una cabecera y un pie, por lo que sólo se centra en componentes ajenos a la parte fija.  " + newLine +
		" * Requiere filtrar parámetros con valor nulo en los componentes, también renumerado de componentes" + newLine +
		" *" + newLine +
		" * Introduce resistencias, condensadores, transistores PNP y NPN" + newLine +
		" * " + newLine +
		" * Bloques de tamaño 7 bytes, 4 nodos de parte fija" + newLine +
		" *" + newLine +
		" * Dos partes por componentes: tipo + continuación, nodo1, nodo2, nodo3 (opcional)" + newLine +
		" * segunda parte: valor resistencia o condensador, dummy o  tipo transistor" + newLine +
		" *)" + newLine +
		newLine +
		"LIST = HEADER, LINEA; " + newLine +
		newLine +
		"LINEA = RESISTENCIA | RESISTENCIA, LINEA | CONDENSADOR | CONDENSADOR, LINEA | BJT | BJT, LINEA;" + newLine +
		newLine +
		"RESISTENCIA =  \"R\", SEP, nodo, SEP, nodo, SEP, dummy, SEP, ValorResistencia, EOL; " + newLine +
		newLine +
		"CONDENSADOR =  \"C\", SEP, nodo, SEP, nodo, SEP, dummy, SEP, ValorCondensador, EOL;" + newLine +
		newLine +
		"BJT = \"Q\", SEP, nodo, SEP, nodo, SEP, nodo, SEP, TipoBJT, SEP, dummy, SEP, dummy, EOL;" + newLine +
		newLine +
		"(* 2N3904 es NPN y 2N3906 es PNP *)" + newLine +
		"TipoBJT = \"Q2N3904\" | \"Q2N3906\";" + newLine +
		newLine +
		"dummy = \"nulo1\" | \"nulo2\"; " + newLine +
		newLine +
		"HEADER = \"* MNN:6" + newLine +
		"\";" + newLine +
		"nodo = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\";" + newLine +
		newLine +
		"ValorResistencia = digitonocero, DECIMAL, digito, EXP, digito; " + newLine +
		newLine +
		"ValorCondensador = digitonocero, DECIMAL, digito, EXP, expCondensador;" + newLine +
		newLine +
		"digito = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\";" + newLine +
		newLine +
		"digitonocero = \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\";" + newLine +
		newLine +
		"expCondensador = \"-12\" | \"-11\" | \"-10\" | \"-9\" | \"-8\" | \"-7\" | \"-6\" | \"-5\" | \"-4\" | \"-3\";" + newLine +
		newLine +
		"SEP = \" \";" + newLine +
		newLine +
		"EXP = \"e\";" + newLine +
		newLine +
		"DECIMAL = \".\";" + newLine +
		newLine +
		"EOL = ?EOL?;" + newLine +
		newLine +
		"CR = ?Carriage return?;" + newLine +
		"(*nulo2*)" + newLine +
		"(*nulo1*)" + newLine +
		"(*nulo2*)" + newLine +
		"(*nulo1*)" + newLine +
		"(*nulo2*)" + newLine +
		"(*nulo1*)" + newLine;

	String expected2 = 
		"(*" + newLine +
		" * netlistSensorBloquesBJT4.ebnf" + newLine +
		" *" + newLine +
		" * Para el circuito sensor de temperatura" + newLine +
		" *" + newLine +
		" * Gramática de generación de netlist utilizando bloques de componentes" + newLine +
		" * Considera que va a haber una cabecera y un pie, por lo que sólo se centra en componentes ajenos a la parte fija.  " + newLine +
		" * Requiere filtrar parámetros con valor nulo en los componentes, también renumerado de componentes" + newLine +
		" *" + newLine +
		" * Introduce resistencias, condensadores, transistores PNP y NPN" + newLine +
		" * " + newLine +
		" * Bloques de tamaño 7 bytes, 4 nodos de parte fija" + newLine +
		" *" + newLine +
		" * Dos partes por componentes: tipo + continuación, nodo1, nodo2, nodo3 (opcional)" + newLine +
		" * segunda parte: valor resistencia o condensador, dummy o  tipo transistor" + newLine +
		" *)" + newLine +
		newLine +
		"LIST = HEADER, LINEA; " + newLine +
		newLine +
		"LINEA = RESISTENCIA | RESISTENCIA, LINEA | CONDENSADOR | CONDENSADOR, LINEA | BJT | BJT, LINEA;" + newLine +
		newLine +
		"RESISTENCIA =  \"R\", SEP, nodo, SEP, nodo, SEP, dummy, SEP, ValorResistencia, EOL; " + newLine +
		newLine +
		"CONDENSADOR =  \"C\", SEP, nodo, SEP, nodo, SEP, dummy, SEP, ValorCondensador, EOL;" + newLine +
		newLine +
		"BJT = \"Q\", SEP, nodo, SEP, nodo, SEP, nodo, SEP, TipoBJT, SEP, dummy, SEP, dummy, EOL;" + newLine +
		newLine +
		"(* 2N3904 es NPN y 2N3906 es PNP *)" + newLine +
		"TipoBJT = \"Q2N3904\" | \"Q2N3906\";" + newLine +
		newLine +
		"dummy = \"nulo1\" | \"nulo2\"; " + newLine +
		newLine +
		"HEADER = \"* MNN:7" + newLine +
		"\";" + newLine +
		"nodo = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\" | \"10\";" + newLine +
		newLine +
		"ValorResistencia = digitonocero, DECIMAL, digito, EXP, digito; " + newLine +
		newLine +
		"ValorCondensador = digitonocero, DECIMAL, digito, EXP, expCondensador;" + newLine +
		newLine +
		"digito = \"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\";" + newLine +
		newLine +
		"digitonocero = \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\";" + newLine +
		newLine +
		"expCondensador = \"-12\" | \"-11\" | \"-10\" | \"-9\" | \"-8\" | \"-7\" | \"-6\" | \"-5\" | \"-4\" | \"-3\";" + newLine +
		newLine +
		"SEP = \" \";" + newLine +
		newLine +
		"EXP = \"e\";" + newLine +
		newLine +
		"DECIMAL = \".\";" + newLine +
		newLine +
		"EOL = ?EOL?;" + newLine +
		newLine +
		"CR = ?Carriage return?;" + newLine +
		"(*nulo2*)" + newLine +
		"(*nulo1*)" + newLine +
		"(*nulo2*)" + newLine +
		"(*nulo1*)" + newLine +
		"(*nulo2*)" + newLine +
		"(*nulo2*)" + newLine;
}
