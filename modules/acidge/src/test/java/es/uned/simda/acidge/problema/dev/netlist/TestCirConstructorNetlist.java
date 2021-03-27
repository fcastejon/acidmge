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
 * TestCirConstructorDevExp
 * 
 * Prueba la generación desde expresiones hasta netlist, con énfasis en probar la validación y simplificación
 * Son pruebas de integración y de funcionamiento conjunto
 */
package es.uned.simda.acidge.problema.dev.netlist;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

//import junit.framework.TestCase;
import org.junit.BeforeClass;
//import org.junit.Before;
import org.junit.Test;

import es.uned.simda.acidge.problema.InviableException;



public class TestCirConstructorNetlist
{	
	private final static Logger logger = Logger.getLogger(TestCirConstructorNetlist.class.getName());

	private final static String newLine = String.format("%n");

	//Parámetros para la creación de la instancia
	private final static int Simulaciones = 3;
	
    public static final Integer[] dummy = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    public static final Set<Integer> NodosProtegidos = new HashSet<Integer>(Arrays.asList(dummy));
	
	private final static String NetlistHeader1="Generación de Netlist con gramática"  + newLine +
		"V1 1 0 dc 20.0" + newLine +
		"VAC1 2 0 0.0 ac 0.0010 sin(0 %VTRAN 10000)" + newLine +
		"R1 2 3 600.0" + newLine +
		"R2 4 0 100.0K" + newLine +
		"Q1 6 5 7 2N2222" + newLine;
		
	private final static String NetlistHeader3="Generación de Netlist con gramática"  + newLine +
	"V1 1 0 dc 20.0" + newLine +
	"VAC1 4 0 0.0 ac 0.0010 sin(0 %VTRAN 10000)" + newLine +
	"R1 0 3 600.0" + newLine +
	"Q1 6 5 7 2N2222" + newLine;
	
	private final static String NetlistModelo=".MODEL 2N2222 NPN (is=19f bf=150 vaf=100 ikf=0.18 ise=50p ne=2.5 br=7.5 " + newLine +
		"+ var=6.4 ikr=12m isc=8.7p nc=1.2 rb=50 re=0.4 rc=0.3 cje=26p tf=0.5n " + newLine + 
		"+ cjc=11p tr=7n xtb=1.5 kf=0.032f af=1)" + newLine;
	private final static String NetlistAnalisis1 = newLine + 
			".op" + newLine +
			".ac DEC 1 1 100meg" + newLine +
			".print ac vm(4), vp(4), i(VAC1), v(3)" + newLine +
			".end" + newLine;
	private final static String NetlistAnalisis2 = newLine + 
			".temp 127.0" + newLine +
			".option TNOM=127" + newLine +
			".op" + newLine +
			".ac DEC 1 10k 10k" + newLine +
			".print ac vm(4), vp(4), v(3)" + newLine +
			".end" + newLine;
	
	private final static String NetlistAnalisis3 = newLine + 
			".ac DEC 1 10k 10k" + newLine +
			".print ac vm(4), vp(4), i(VAC1)" + newLine +
			".end" + newLine;
	
	//No existe NetlistHeader2, se toma el 1
	private final static String NetlistHeader[] = 
		{ NetlistHeader1, NetlistHeader1, NetlistHeader3 };
	
	private final static String NetlistAnalisis[] = 
		{ NetlistAnalisis1, NetlistAnalisis2, NetlistAnalisis3 };
	
	private final static int EvitarComponentesColgando = 0;
	private final static String ResistenciaElevada = "1G";
	
	private final static double VTRAN = 1.9;		//amplitud de señal para transitorio
	
	private static CirConstructorNetlist cirConstructor;
	
	@BeforeClass
	public static void beforeClass()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
		//Permite cambiar la implementación cambiando la clase creada!
		cirConstructor = new CirConstructorNetlist(Simulaciones, NodosProtegidos, 
				NetlistHeader, NetlistModelo,
				NetlistAnalisis, EvitarComponentesColgando, ResistenciaElevada);
		
		cirConstructor.setVtran(VTRAN);
	}
	
	@Test
	public void test1() 
	{	
		
		String expresion = "R 3 5 900" + newLine +
			"R 1 6 1.0e4" + newLine +
			"R 7 0 1.0e3" + newLine +
			"C 6 4 10.0e-6" + newLine;

		String expected1 = expected1();
		String expected2 = expected2();
		String expected3 = expected3();
		
		motor(expresion, expected1, expected2, expected3, true, 0);
	}
	
	@Test
	public void test2() 
	{		
		String expresion = "R 3 5 900.0" + newLine +
			"R 1 6 1.0e4" + newLine;

		motor(expresion, "", "", "", false, 2);
	}
	
	@Test
	public void test3() 
	{		
		String expresion = "R 3 5 900" + newLine +
			"R 1 6 1.0e4" + newLine +
			"R 7 0 1.0e3" + newLine +
			"C 6 4 10.0e-6" + newLine;

		String expected1 = expected1();
		
		motor2(expresion, expected1, true, 0);
	}
	

	public void motor(String expresion, String expected1, String expected2, String expected3, 
			boolean flag, int nivel) 
	{	
		logger.info(expresion);

		try
		{
			boolean res = cirConstructor.creaCircuitoExp(expresion);
			assertEquals(res, flag);
			
			if(nivel > 0)
			{
				int actual = cirConstructor.getNivelInviable();
				logger.info("nivel actual:" + actual);
				assertEquals(actual, nivel, 1e-8);
			}
			else
			{
				//hecho ad hoc
				assertEquals(Simulaciones, 3);
				String actual1 = cirConstructor.adaptaCircuitoAnalisis(0);
				String actual2 = cirConstructor.adaptaCircuitoAnalisis(1);
				String actual3 = cirConstructor.adaptaCircuitoAnalisis(2);
		
				logger.info("Actual1:" + actual1);
				logger.info("Expected1:" + expected1);
				logger.info("Actual2:" + actual2);
				logger.info("Expected2:" + expected2);
				logger.info("Actual3:" + actual3);
				logger.info("Expected3:" + expected3);
			
				assertEquals(expected1, actual1);
				assertEquals(expected2, actual2);
				assertEquals(expected3, actual3);
			}
		}
		catch(InviableException ie)
		{
			fail();
		}
	}
	
	public void motor2(String expresion, String expected1, boolean flag, int nivel) 
	{	
		logger.info(expresion);

		try
		{
			boolean res = cirConstructor.creaCircuitoExp(expresion);
			assertEquals(res, flag);

			String actual1 = cirConstructor.getCircuit(0);
			logger.info("Actual1:" + actual1);
			logger.info("Expected1:" + expected1);
			assertEquals(expected1, actual1);
		}
		catch(InviableException ie)
		{
			fail();
		}
	}
	
	
	String expected1()
	{
		return 	"Generación de Netlist con gramática" + newLine + 
			"V1 1 0 dc 20.0" + newLine + 
			"VAC1 2 0 0.0 ac 0.0010 sin(0 1.9 10000)" + newLine + 
			"R1 2 3 600.0" + newLine + 
			"R2 4 0 100.0K" + newLine + 
			"Q1 6 5 7 2N2222" + newLine +
			"R3 3 5 900.0" + newLine +
			"R4 1 6 10.0K" + newLine +
			"R5 7 0 1.0K" + newLine +
			"C1 6 4 10.0u" + newLine +
			".MODEL 2N2222 NPN (is=19f bf=150 vaf=100 ikf=0.18 ise=50p ne=2.5 br=7.5 " + newLine +  
			"+ var=6.4 ikr=12m isc=8.7p nc=1.2 rb=50 re=0.4 rc=0.3 cje=26p tf=0.5n " + newLine +  
			"+ cjc=11p tr=7n xtb=1.5 kf=0.032f af=1)" + newLine + newLine + 
			".op" + newLine + 
			".ac DEC 1 1 100meg" + newLine + 
			".print ac vm(4), vp(4), i(VAC1), v(3)" + newLine + 
			".end" + newLine;
	}	
	
	String expected2()
	{
		return 	"Generación de Netlist con gramática" + newLine + 
		"V1 1 0 dc 20.0" + newLine + 
		"VAC1 2 0 0.0 ac 0.0010 sin(0 1.9 10000)" + newLine + 
		"R1 2 3 600.0" + newLine + 
		"R2 4 0 100.0K" + newLine + 
		"Q1 6 5 7 2N2222" + newLine +
		"R3 3 5 900.0" + newLine +
		"R4 1 6 10.0K" + newLine +
		"R5 7 0 1.0K" + newLine +
		"C1 6 4 10.0u" + newLine +
		".MODEL 2N2222 NPN (is=19f bf=150 vaf=100 ikf=0.18 ise=50p ne=2.5 br=7.5 " + newLine +  
		"+ var=6.4 ikr=12m isc=8.7p nc=1.2 rb=50 re=0.4 rc=0.3 cje=26p tf=0.5n " + newLine +  
		"+ cjc=11p tr=7n xtb=1.5 kf=0.032f af=1)" + newLine + newLine +
		".temp 127.0" + newLine +
		".option TNOM=127" + newLine +
		".op" + newLine +
		".ac DEC 1 10k 10k" + newLine +
		".print ac vm(4), vp(4), v(3)" + newLine +
		".end" + newLine;
	}
	
	String expected3()
	{
		return 	"Generación de Netlist con gramática" + newLine + 
		"V1 1 0 dc 20.0" + newLine +
		"VAC1 4 0 0.0 ac 0.0010 sin(0 1.9 10000)" + newLine +
		"R1 0 3 600.0" + newLine +
		"Q1 6 5 7 2N2222" + newLine +
		"R3 3 5 900.0" + newLine +
		"R4 1 6 10.0K" + newLine +
		"R5 7 0 1.0K" + newLine +
		"C1 6 4 10.0u" + newLine +
		".MODEL 2N2222 NPN (is=19f bf=150 vaf=100 ikf=0.18 ise=50p ne=2.5 br=7.5 " + newLine +  
		"+ var=6.4 ikr=12m isc=8.7p nc=1.2 rb=50 re=0.4 rc=0.3 cje=26p tf=0.5n " + newLine +  
		"+ cjc=11p tr=7n xtb=1.5 kf=0.032f af=1)" + newLine + newLine + 
		".ac DEC 1 10k 10k" + newLine +
		".print ac vm(4), vp(4), i(VAC1)" + newLine +
		".end" + newLine;
	}	
}
