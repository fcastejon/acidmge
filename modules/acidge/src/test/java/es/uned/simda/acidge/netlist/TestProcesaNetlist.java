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
 * TestCase para probar la clase ProcesaNetList
 */
package es.uned.simda.acidge.netlist;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.problema.InviableException;

public class TestProcesaNetlist 
{
	private final static Logger logger = Logger.getLogger(TestProcesaNetlist.class.getName());

	final static String newLine = String.format("%n");
	
	//Parámetros para la creación de la instancia
	private final static String NetlistHeaderAC="Generación de Netlist con gramática"  + newLine +
		"V1 1 0 dc 20.0" + newLine +
		"VAC1 2 0 0.0 ac 0.0010 sin(0 %VTRAN 10000)" + newLine +
		"R1 2 3 600.0" + newLine +
		"R2 4 0 1.0e5" + newLine +
		"Q1 6 5 7 2N2222" + newLine;
		
    public static final Integer[] dummy = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    public static final Set<Integer> NodosProtegidos = new HashSet<Integer>(Arrays.asList(dummy));
    
    private static final String ResistenciaElevada = "1G";
    
	static ProcesaNetlist procNetlist;
	
	@BeforeClass
	public static void beforeClass()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();

	}
	
	@Test
	public void test1() 
	{	
		String expresion = "R 3 5 1.0e4" + newLine +
			"R 1 6 6.0e3" + newLine +
			"R 7 0 1.0e3" + newLine +
			"C 6 4 1.0e-6" + newLine;

		String expected = expected1();
		
		try 
		{
			String actual = motor(expresion);
			
			logger.info("actual:" + actual);
			logger.info("expected:" + expected);
			
			assertEquals(expected, actual);
			
			int [] nivel = procNetlist.checkNodos();
			
			assertEquals(0, nivel[0]);
			assertEquals(0, nivel[1]);
		}
		catch(InviableException e)
		{
			fail();
		}
	}
	
	@Test
	public void testChecknodos() 
	{	
		
		String expresion = "R 1 3 10" + newLine +
			"R 3 1 5.5e4" + newLine;
		
		try 
		{
			String actual = motor(expresion);
			
			int [] nivel = procNetlist.checkNodos();
			
			assertEquals(4, nivel[0]);
		}
		catch(InviableException e)
		{
			fail();
		}
	}
	
	@Test
	public void testEsCorto() 
	{	
		
		String expresion = 
			"R 3 5 1.0e4" + newLine +
			"R 1 6 6.0e3" + newLine +
			"R 7 0 1.0e3" + newLine +
			"R 5 5 10" + newLine +
			"C 7 7 1u" + newLine + 
			"Q 9 9 9 Q2N2222" + newLine +
			"C 6 4 1.0e-6" + newLine;
		
		try 
		{
			String actual = motor(expresion);
			
			String expected = expected2();

			logger.info("actual:" + actual);
			logger.info("expected:" + expected);
			
			assertEquals(expected, actual);
			
			int [] nivel = procNetlist.checkNodos();
			
			assertEquals(0, nivel[0]);
			assertEquals(0, nivel[1]);
		}
		catch(InviableException e)
		{
			fail();
		}
	}
	
	@Test
	public void testMOSFET() 
	{	
		
		String expresion =  
			"M 0 4 6 4 PMOS1 L=10u W=91u" + newLine +
			"M 6 9 6 5 NMOS1 L=10u W=174u" + newLine + 
			"R 1 6 6.0e3" + newLine +
			"R 7 0 1.0e3" + newLine +
			"J 6 0 7 JM1" + newLine;		
		
		try 
		{
			String actual = motor(expresion);
			
			String expected = expected3();

			logger.info("actual:" + actual);
			logger.info("expected:" + expected);
			
			assertEquals(expected, actual);
			
			int [] nivel = procNetlist.checkNodos();
			
			assertEquals(1, nivel[0]);
			assertEquals(1, nivel[1]);
		}
		catch(InviableException e)
		{
			fail();
		}
	}
	
	@Test
	public void testEvitarComponentesColgando() 
	{	
		
		String expresion = "R 3 5 1.0e4" + newLine +
			"R 1 6 6.0e3" + newLine +
			"R 7 0 1.0e3" + newLine +
			"C 6 4 1.0e-6" + newLine +
			"R 6 8 1.0e3" + newLine + 
			"C 7 9 1.0e-6" + newLine +
			"Q 1 10 0 2N2222" + newLine;
			
		try 
		{
			String actual = motor(expresion, 1);
			
			String expected = expected4();

			logger.info("actual:" + actual);
			logger.info("expected:" + expected);
			
			assertEquals(expected, actual);
			
			int [] nivel = procNetlist.checkNodos();
			
			assertEquals(0, nivel[0]);
			assertEquals(0, nivel[1]);
		}
		catch(InviableException e)
		{
			fail();
		}
	}

	String motor(String expresion) throws InviableException
	{
		return motor(expresion, 0);
	}
	
	String motor(String expresion, int EvitarComponentesColgando) throws InviableException
	{
		
		procNetlist = new ProcesaNetlist(NodosProtegidos, EvitarComponentesColgando, ResistenciaElevada);
		
		procNetlist.inicia(NetlistHeaderAC);

		String actual = procNetlist.procesa(expresion);
		
		procNetlist.logGradoNodos();
		
		return actual;
	}
	
	String expected1()
	{
		return "R3 3 5 10.0K" + newLine +
		"R4 1 6 6.0K" + newLine +
		"R5 7 0 1.0K" + newLine +
		"C1 6 4 1.0u" + newLine;
	}
	
	String expected2()
	{
		return "R3 3 5 10.0K" + newLine +
		"R4 1 6 6.0K" + newLine +
		"R5 7 0 1.0K" + newLine +
		"*R 5 5 10" + newLine +
		"*C 7 7 1u" + newLine +
		"*Q 9 9 9 Q2N2222" + newLine +
		"C1 6 4 1.0u" + newLine;
	}
	
	String expected3()
	{
		return  
			"M1 0 4 6 4 PMOS1 L=10u W=91u" + newLine + 
			"M2 6 9 6 5 NMOS1 L=10u W=174u"	+ newLine +
			"R3 1 6 6.0K" + newLine +
			"R4 7 0 1.0K" + newLine +
			"J1 6 0 7 JM1" + newLine;
	}
	
	String expected4()
	{
		return "R3 3 5 10.0K" + newLine +
		"R4 1 6 6.0K" + newLine +
		"R5 7 0 1.0K" + newLine +
		"C1 6 4 1.0u" + newLine +
		"R6 6 8 1.0K" + newLine + 
		"C2 7 9 1.0u" + newLine +
		"Q2 1 10 0 2N2222" + newLine +
		"R7 8 0 1G" + newLine + 
		"R8 9 0 1G" + newLine +
		"R9 10 0 1G" + newLine;
	}
}
