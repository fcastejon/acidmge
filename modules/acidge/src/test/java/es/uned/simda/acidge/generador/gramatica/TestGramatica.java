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
package es.uned.simda.acidge.generador.gramatica;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author fcastejo
 *
 */
public class TestGramatica 
{
	final static String newLine = String.format("%n");
	private final static Logger logger = Logger.getLogger(TestGramatica.class.getName());

	@BeforeClass
	public static void init()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
		Gramatica gram = construyeGramatica1();
		
		String actual = gram.toString();
		
		String expected = expected1();
		
		logger.info("actual:" + newLine + actual);
		logger.info("expected:" + newLine + expected);
		
		assertEquals(expected, actual);
	}
	
	// S := 'A' | 'A', S;
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
	
	String expected1()
	{
		String res = "";
		res += "S = 'A'" + " | " + "'A', S;" + newLine;
		
		return res;
	}
	

}
