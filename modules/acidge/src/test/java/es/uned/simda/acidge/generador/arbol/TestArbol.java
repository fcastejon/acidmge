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
 * TestCase para probar las clases Nodo, Terminal y NoTerminal
 */
package es.uned.simda.acidge.generador.arbol;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.generador.GenException;
import es.uned.simda.acidge.generador.gramatica.TestGramatica;



/**
 * @author fcastejo
 *
 */
public class TestArbol 
{
	private final static Logger logger = Logger.getLogger(TestArbol.class.getName());
	final static String newLine = String.format("%n");

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
		
		NoTerminal arbol1 = construyeArbol1();
		
		motor(arbol1, expected1());
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
		NoTerminal arbol2 = construyeArbol2();
		
		motor(arbol2, expected2());
	}	

	@Test
	public void test3() 
	{
		logger.info("test3");
		
		NoTerminal arbol3 = construyeArbol3();
		
		motor(arbol3, expected3());
	}	
	
	void motor(NoTerminal arbol, String expected)
	{
		Visitor v = new ExpVisitor();
		
		String actual = (String) v.visita(arbol);		

		logger.info("actual:" + newLine + actual);
		logger.info("expected:" + newLine + expected);
		
		assertEquals(expected, actual);
	}
	
	// S->A
	NoTerminal construyeArbol1()
	{
		String comienzo = "S";
		NoTerminal raiz = new NoTerminal(comienzo);
		
		Terminal t1 = new Terminal(raiz, "A");
		raiz.addHijo(t1);
	
		return raiz;
	}
	
	String expected1()
	{
		return "A";
	}

	// 
	NoTerminal construyeArbol2()
	{
		String comienzo = "S";
		NoTerminal raiz = new NoTerminal(comienzo);
		
		Terminal t1 = new Terminal(raiz, "A");
		NoTerminal n1 = new NoTerminal(raiz, "S");
		raiz.addHijo(t1);
		raiz.addHijo(n1);
		
		Terminal t2 = new Terminal(n1, "A");
		n1.addHijo(t2);
	
		return raiz;
	}
	
	String expected2()
	{	
		return "AA";
	}
	
	// 
	NoTerminal construyeArbol3()
	{
		String comienzo = "S";
		NoTerminal raiz = new NoTerminal(comienzo);
		
		Terminal t1 = new Terminal(raiz, "A");
		NoTerminal n1 = new NoTerminal(raiz, "S");
		raiz.addHijo(t1);
		raiz.addHijo(n1);
		
		Terminal t2 = new Terminal(n1, "A");
		NoTerminal n2 = new NoTerminal(n1, "S");
		n1.addHijo(t2);
		n1.addHijo(n2);
		
		Terminal t3 = new Terminal(n2, "A");
		n2.addHijo(t3);
	
		return raiz;
	}
	
	String expected3()
	{	
		return "AAA";
	}
	

}
