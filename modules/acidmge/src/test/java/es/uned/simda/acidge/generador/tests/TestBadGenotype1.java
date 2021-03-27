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
 * TestCase Bad Genotype 1
 */
package es.uned.simda.acidge.generador.tests;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.generador.GenException;
import es.uned.simda.acidge.generador.Generador;
import es.uned.simda.acidge.generador.GeneradorGE2;
import es.uned.simda.acidge.generador.GeneradorGrammar;
import es.uned.simda.acidge.generador.GeneradorMultiGrammar;
import es.uned.simda.acidge.generador.gramatica.Gramatica;


/**
 * @author fcastejo
 *
 */
public class TestBadGenotype1 
{
	private final static Logger logger = Logger.getLogger(TestBadGenotype1.class.getName());
	final static String newLine = String.format("%n");
	final static String GRAMMARTEST1 = "testMultiNetlistSensorTopology1.ebnf";
	final static String GRAMMARTEST2 = "testMultiNetlistComunTop2.ebnf";
	final static int ngrama = 2;

	Gramatica gramatica;
	
	
	static int genes[] = { 
		47, 122, 14, 98, 79, 250, 120, 1, 59, 127, 27, 199, 193, 22, 237, 82, 221, 226, 92, 10, 59, 127, 27, 199, 193, 237, 192, 
		202, 221, 226, 92, 10, 197, 197, 109, 65, 185, 60, 144, 99, 143, 223, 31, 178, 221, 230, 92, 142, 185, 71, 141, 116, 221, 
		230, 92, 142, 95, 197, 208, 15, 67, 94, 145, 221, 149, 31, 21, 73, 223, 174, 115, 140, 113, 173, 114, 140, 181, 205, 61, 
		83, 91, 170, 72, 197, 67, 94, 17, 227, 151, 144, 216, 146, 67, 92, 17, 159, 49, 6, 129, 162, 43, 187, 152, 136, 175, 219, 
		63, 68, 161, 0, 25, 237, 57, 241, 78, 62, 161, 167, 44, 138, 59, 179, 208, 200, 141, 244, 141, 93, 137, 31, 63, 178, 179, 
		17, 209, 214, 173, 15, 46, 32, 51, 66, 192, 44, 143, 95, 63, 178, 165, 16, 174, 55, 57, 201, 215, 4, 51, 112, 230, 191, 153, 
		96, 204, 101, 237, 82, 143, 201, 136, 113, 151, 255, 252, 234, 57, 39, 118, 233, 15, 252, 39, 30, 232, 169, 28, 37, 119, 
		135, 208, 43, 15, 89, 241, 49, 210, 84, 126, 215, 134, 164, 220, 165, 230, 27, 83, 114, 250, 142, 15, 169, 88, 22, 150, 
		238, 244, 49, 16, 139, 115, 120, 110, 188, 58, 237, 130, 72, 204, 73, 31, 38, 135, 78, 58, 100, 101, 56, 84, 201, 177, 95, 
		181, 140, 17, 167, 107, 187, 199, 189, 119, 31, 185, 177, 60, 8, 229, 99, 11, 159, 101, 0, 161, 119, 23, 141, 83, 238, 101, 
		203, 24, 42, 214, 77, 183, 89, 150, 85, 18, 31, 154, 53, 162, 87, 8, 58, 61, 63, 185, 158, 176, 148, 34, 62, 63, 226, 53, 
		15, 212, 252, 67, 18, 163, 119, 231, 155, 153, 26, 220, 121, 41, 85, 47, 107, 97, 106, 42, 72, 235, 33, 169, 100, 63, 
		115, 143, 163, 142, 62, 34, 175, 221, 196, 238, 22, 157, 77, 63, 121
	};
			
	@BeforeClass
	public static void beforeClass()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	@Before
	public void before()
	{
		Gramatica gramas[] = new Gramatica[ngrama];
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		gramas[0] = GeneradorGrammar.cargaGramatica(is);
		is = getClass().getResourceAsStream(GRAMMARTEST2);
		gramas[1] = GeneradorGrammar.cargaGramatica(is);

		GeneradorMultiGrammar.setGramaticas(gramas);
	}

	@Test
	public void test1() 
	{
		logger.info("test1");
		
		InputStream is = getClass().getResourceAsStream(GRAMMARTEST1);
		GeneradorGE2.cargaGramaGE2(is);	
				
		motor(genes, 4, 100);
	}
	
	public void motor(int [] genes, int MaxWrapping, int MaxRecursionLevel) 
	{	
		GEProperties geproperties = new GEProperties();
		geproperties.setMaxWrappingNumber(MaxWrapping);
		geproperties.setMaxRecursionLevel(MaxRecursionLevel);
		
		try
		{
			//Aprovechamos el constructor que ademite cadenas de int
			Genotipo genotype = new Genotipo(genes);
			
			Generador gen = new GeneradorMultiGrammar(genotype.getGenes(), geproperties);
			String actual = gen.genera();
			logger.info("actual: " + actual);
		
			ArrayList<Integer> l = gen.getXOPoints();
			Integer xopoints[] = (Integer []) l.toArray(new Integer[l.size()]);
			
			assertEquals(1, 1);
		}
		catch(GenException e)
		{
			//e.printStackTrace();
			logger.severe(e.getMessage());
			fail(e.getMessage());
		}
	}

}
