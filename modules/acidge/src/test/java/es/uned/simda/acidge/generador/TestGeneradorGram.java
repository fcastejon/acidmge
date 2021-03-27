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
 * TestCase para probar la clase GeneradorExp
 */
package es.uned.simda.acidge.generador;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.generador.gramatica.ElementoRegla;
import es.uned.simda.acidge.generador.gramatica.Gramatica;
import es.uned.simda.acidge.generador.gramatica.Subregla;


/**
 * @author fcastejo
 *
 */
public class TestGeneradorGram 
{
	final static String newLine = String.format("%n");
	private final static Logger logger = Logger.getLogger(TestGeneradorGram.class.getName());
	
	@BeforeClass
	public static void init()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup(Level.ALL);
	}
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
		Gramatica gram = construyeGramatica1();
		
		String expected = "AAAAA";
		byte genes[] = { 1, 3, 3, 1, 0 };
		
		String actual = motor(genes, 0, 100, gram);
		
		logger.info("actual:" + actual);
		logger.info("expected:" + expected);
		
		assertEquals(expected, actual);
	}

	@Test
	public void test2() 
	{
		logger.info("test2");
		
		Gramatica gram = construyeGramatica2();
		
		String expected = "ACB";
		byte genes[] = { 1, 2, 3, 3, 2, 3, 3, 2, 3, 3} ; //, 2, 3, 3, 2, 3, 3, 2, 3, 3 };
		
		String actual = motor(genes, 0, 100, gram);
		
		logger.info("actual:" + actual);
		logger.info("expected:" + expected);
		
		assertEquals(expected, actual);
	}
/*
	@Test
	public void test3() 
	{
		logger.info("test3");
		
		String expected = "LOG((1.0*1.0))+LOG((1.0*1.0))";
		byte genes[] = { 0, 2, 3, 1, 3, 3, 3, 3, 3 };
		
		String actual = motor(genes, 1, 100);
		
		assertEquals(expected, actual);
	}

	@Test
	public void test4() 
	{
		logger.info("test4");
		
		String expected = "COS(X-1.0)+COS(X-1.0)";
		byte genes[] = { 0, 2, 1, 0, 3, 2, 1, 3, 3 };
		
		String actual = motor(genes, 1, 100);
		
		assertEquals(expected, actual);
	}
*/
		
	public String motor(byte [] genes, int MaxWrapping, int MaxRecursionLevel, Gramatica gram)
	{	
		String exp = "";
		
		GEProperties geproperties = new GEProperties();
		geproperties.setMaxWrappingNumber(MaxWrapping);
		geproperties.setMaxRecursionLevel(MaxRecursionLevel);
		
		try
		{
			GeneradorGrammar.setGramatica(gram);
			Generador gen = new GeneradorGrammar(genes, geproperties);
			exp = gen.genera();
			logger.info(exp);
		}
		catch(GenException e)
		{
			//e.printStackTrace();
			logger.info(e.getMessage());
			fail(e.getMessage());
		}
		
		return exp;
	}
	
	// S := 'A' | 'A','S';
	Gramatica construyeGramatica1()
	{
		String comienzo = "S";
		Gramatica gram = new Gramatica();
		ElementoRegla el1 = new ElementoRegla(ElementoRegla.TERMINAL, "A");
		ElementoRegla el2 = new ElementoRegla(ElementoRegla.TERMINAL, "A");
		ElementoRegla el3 = new ElementoRegla(ElementoRegla.NOTERMINAL, "S");
		
		Subregla sub1 = new Subregla(comienzo);
		sub1.add(el1);
		Subregla sub2 = new Subregla(comienzo);
		sub2.add(el2);
		sub2.add(el3);
		
		gram.add(sub1);
		gram.add(sub2);
	
		return gram;
	}
	
	// S := 'A' | 'S','B' | 'S', 'C';
	Gramatica construyeGramatica2()
	{
		String comienzo = "S";
		Gramatica gram = new Gramatica();
		ElementoRegla el1 = new ElementoRegla(ElementoRegla.TERMINAL, "A");
		ElementoRegla el2 = new ElementoRegla(ElementoRegla.NOTERMINAL, "S");
		ElementoRegla el3 = new ElementoRegla(ElementoRegla.TERMINAL, "B");
		ElementoRegla el4 = new ElementoRegla(ElementoRegla.NOTERMINAL, "S");
		ElementoRegla el5 = new ElementoRegla(ElementoRegla.TERMINAL, "C");
		
		Subregla sub1 = new Subregla(comienzo);
		sub1.add(el1);
		Subregla sub2 = new Subregla(comienzo);
		sub2.add(el2);
		sub2.add(el3);
		Subregla sub3 = new Subregla(comienzo);
		sub3.add(el4);
		sub3.add(el5);
		
		gram.add(sub1);
		gram.add(sub2);
		gram.add(sub3);
	
		return gram;
	}

}
