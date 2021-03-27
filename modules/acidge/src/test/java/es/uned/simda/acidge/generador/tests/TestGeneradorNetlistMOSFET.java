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
 * TestCase para probar la clase Generador y la gramática testNetlistMOSFET.ebnf
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


public class TestGeneradorNetlistMOSFET 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorNetlistBloquesMOSFET2.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testNetlistMOSFET.ebnf";

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
			"R 2 3 1.0e0" + newLine +
			"R 3 2 2.4e5" + newLine +
			"M 4 2 3 5 NMOS1 L=10u W=10u" + newLine +
			"M 4 5 3 4 PMOS1 L=10u W=123u" + newLine +
			"M 6 7 0 5 NMOS1 L=10u W=55u" + newLine +
			"M 7 10 2 4 PMOS1 L=10u W=199u" + newLine +
			"R 8 10 3.4e9" + newLine +
			"R 9 10 3.8e9" + newLine;
			
		byte genes[] = { 0, 0, 0, 0, 0, 0, 0, 1, 
						 0, 1, 2, 0, 0, 0, 
						 0, 2, 1, 1, 4, 5, 
						 1, 3, 1, 2, 0, 0, 0, 
						 2, 3, 4, 2, 1, 2, 3, 
						 1, 5, 6, 0, 2, 4, 5, 
						 2, 6, 9, 1, 3, 9, 9, 
						 3, 7, 9, 2, 4, 9, 
						 3, 8, 9, 2, 8, 9, };
		
		motor(genes, 0, 100, expected);
	}
	
	//Genes de prueba, se obtuvieron con otra gramática pero decodifican bien con ésta
	static int genesTest1[] = { 196, 146, 86, 70, 94, 144, 10, 36, 16, 138, 24, 114, 134, 38, 24, 182, 
		40, 10, 102, 16, 66, 142, 62, 80, 216, 190, 156, 33, 152, 114, 142, 248, 48, 227, 233, 63, 
		108, 207, 36, 243, 99, 30, 224, 16, 126, 107, 191, 240, 237, 61, 56, 3, 66, 125, 132, 22, 
		167, 117, 6, 13, 57, 36, 248, 165, 12, 63, 39, 108, 108, 195, 168, 30, 152, 0, 28, 17, 125, 
		190, 239, 39, 138, 163, 10, 80, 192, 32, 227, 153, 144, 214, 244, 43, 66, 231, 229, 207, 227, 
		152, 16, 144, 156, 44, 195, 233, 94, 26, 39, 56, 23, 147, 214, 68, 90, 44, 236, 44, 99, 201, 28, 
		216, 91, 19, 186, 38, 152, 31, 45, 108, 99, 38, 199, 139, 145, 246, 204, 102, 32, 18, 206, 184, 
		60, 227, 233, 63, 108, 207, 36, 227, 99, 30, 162, 16, 126, 107, 189, 242, 253, 61, 56, 1, 66, 93, 
		132, 22, 166, 117, 70, 13, 179, 36, 120, 165, 12, 62, 7, 108, 108, 195, 168, 10, 152, 0, 23, 85, 
		125, 174, 255, 39, 138, 163, 3, 81, 201, 32, 227, 153, 145, 214, 180, 42, 66, 231, 229, 207, 227, 
		152, 16, 145, 156, 44, 195, 233, 94, 30, 39, 56, 23, 151, 132, 65, 90, 44, 236, 44, 99, 201, 156, 
		252, 217, 27, 150, 164, 152, 31, 45, 108, 107, 38, 199, 139, 17, 118, 76, 118, 32, 240, 125, 198, 
		195, 233 };	

	String expectedTes1 = 
		"M 5 11 7 4 PMOS1 L=10u W=33u" + newLine + 
		"R 10 10 1.3e9" + newLine + 
		"R 5 6 1.7e1" + newLine + 
		"R 7 7 3.3e6" + newLine + 
		"M 0 0 3 4 PMOS1 L=10u W=163u" + newLine + 
		"R 4 7 4.2e3" + newLine + 
		"R 10 10 7.8e0" + newLine + 
		"M 0 7 7 4 PMOS1 L=10u W=109u" + newLine + 
		"R 7 10 2.0e2" + newLine + 
		"M 8 11 2 4 PMOS1 L=10u W=23u" + newLine + 
		"R 0 10 1.7e2" + newLine + 
		"M 2 3 0 5 NMOS1 L=10u W=134u" + newLine + 
		"M 7 2 2 4 PMOS1 L=10u W=148u" + newLine + 
		"R 0 6 9.9e1" + newLine + 
		"M 8 4 9 5 NMOS1 L=10u W=32u" + newLine + 
		"M 2 10 0 5 NMOS1 L=10u W=29u" + newLine + 
		"M 5 7 4 5 NMOS1 L=10u W=16u" + newLine + 
		"M 6 8 3 5 NMOS1 L=10u W=187u" + newLine + 
		"R 8 0 4.2e6" + newLine + 
		"R 9 3 9.3e1" + newLine + 
		"M 2 0 6 4 PMOS1 L=10u W=56u" + newLine + 
		"R 5 3 9.6e0" + newLine + 
		"R 2 8 8.8e8" + newLine + 
		"R 4 11 9.0e3" + newLine + 
		"M 5 10 3 5 NMOS1 L=10u W=183u" + newLine + 
		"R 5 4 6.7e3" + newLine + 
		"M 6 5 10 5 NMOS1 L=10u W=79u" + newLine + 
		"R 8 10 8.5e6" + newLine;

	@Test
	public void TestGenes1() 
	{
		logger.info("TestGenes1");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGrammar.cargaGrama(is);
				
		motor(genesTest1, 0, 100, expectedTes1);
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
