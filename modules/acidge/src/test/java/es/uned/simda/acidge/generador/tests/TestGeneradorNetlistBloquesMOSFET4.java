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
 * TestCase para probar la clase Generador y la gramática testNetlistMOSFETBloques4.ebnf
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


public class TestGeneradorNetlistBloquesMOSFET4 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorNetlistMOSFET.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testNetlistBloquesMOSFET4.ebnf";

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
			"R 2 3 nulo1 1.0e0 nulo1" + newLine +
			"R 3 2 nulo2 2.4e5 nulo2" + newLine +
			"M 4 2 3 0 NMOS1 L=10u W=10u" + newLine +
			"M 4 5 3 4 PMOS1 L=10u W=123u" + newLine +
			"M 6 7 0 0 NMOS1 L=10u W=55u" + newLine +
			"M 7 10 2 4 PMOS1 L=10u W=199u" + newLine +
			"R 8 10 nulo1 3.4e9 nulo2" + newLine +
			"R 9 10 nulo2 3.8e9 nulo1" + newLine;
			
		byte genes[] = {  
						 1, 1, 2, 0, 0, 0, 0, 0, 
						 1, 2, 1, 1, 1, 4, 5, 1, 
						 3, 3, 1, 2, 0, 0, 0, 0,
						 3, 3, 4, 2, 1, 1, 2, 3, 
						 3, 5, 6, 0, 0, 2, 4, 5, 
						 3, 6, 9, 1, 1, 3, 9, 9, 
						 1, 7, 9, 2, 2, 4, 9, 1,  
						 0, 8, 9, 3, 2, 8, 9, 0 
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
