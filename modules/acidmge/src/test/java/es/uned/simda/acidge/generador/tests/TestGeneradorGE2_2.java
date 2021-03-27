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
 * 
 * Con gramática completa de generación
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
public class TestGeneradorGE2_2 
{
	private final static Logger logger = Logger.getLogger(TestGeneradorGE2_2.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testUniversalNetlistSensorBloquesBJT4.ebnf";

	Gramatica gramatica;
	
	@BeforeClass
	public static void init()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.trivialSetup(Level.FINEST);
	}
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
	    InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);
			
		String expected = "* MNN:6" + newLine + 
			"R 1 2 nulo2 5.5e6" + newLine; 

		byte genes[] = { 0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 };
		
		motor(genes, 0, 100, expected);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test1");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGrammar.cargaGrama(is);
			
		String expected = "* MNN:15" + newLine +
				"Q 11 15 1 Q2N3904 nulo1 nulo2" + newLine; 

		int genes1[] = { 59, 241, 8, 160, 58, 47, 57, 34, 239, 72, 210, 48, 188, 177, 163, 200, 
				242, 244, 118, 85, 175, 210, 157, 32, 199, 135, 133, 75, 206, 12, 50, 30, 131, 216, 
				218, 195, 110, 47, 108, 153, 58, 77, 189, 175, 39, 99, 4, 84, 187, 15, 221, 170, 5, 
				67, 31, 80, 140, 176, 88, 56, 139, 47, 254, 159, 7, 114, 233, 140, 101, 142, 177, 
				99, 246, 165, 77, 1, 53, 61, 211, 234, 161, 172, 36, 34, 108, 74, 184, 121, 158, 
				108, 95, 46, 203, 141, 230, 231, 187, 11, 231, 56, 43, 132, 90, 243, 65, 147, 73, 
				248, 104, 3, 124, 172, 97, 220, 86, 155, 30, 4, 81, 44, 137, 65, 26, 41, 25, 178, 
				35, 206, 79, 59, 239, 210, 133, 108, 239, 52, 222, 35, 205, 53, 192, 23, 160, 229, 
				233, 191, 49, 60, 17, 127, 255, 220, 255, 186, 46, 196, 43, 160, 19, 158, 127, 253, 
				71, 96, 212, 50, 193, 212, 29, 157, 127, 240, 48, 10, 70, 153, 240, 102, 152, 2, 
				194, 219, 58, 54, 31, 231, 236, 214, 56, 150, 100, 236, 97, 244, 184, 5, 180, 64, 
				127, 69, 175		
		};
		
		byte genes[] = new byte[genes1.length];
		
		for(int i = 0;i < genes1.length; i++)
			genes[i] = (byte) (genes1[i] & 0xff);
		
		//logger.info(gramatica.toString());
		
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
