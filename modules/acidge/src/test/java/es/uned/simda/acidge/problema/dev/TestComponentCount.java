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
 * TestCase for ComponentCount
 * 
 * 
 */
package es.uned.simda.acidge.problema.dev;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.logging.Logger;

//import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.spice.Signal;




public class TestComponentCount
{	
	private final static Logger logger = Logger.getLogger(TestComponentCount.class.getName());
	final static String newLine = String.format("%n");

	@BeforeClass
	public static void before()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	String exp1 = 
		"R 1 0 nulo2 3.3e4 nulo2" + newLine +
		"* Fin" + newLine;

	String exp2 = 
		"R 1 2 nulo2 1.1e2 nulo2" + newLine +
		"Q 5 6 7 Q2N3904 nulo2 nulo1 nulo2" + newLine +
		"Q 8 9 10 Q2N3906 nulo1 nulo1 nulo2" + newLine +
		"R 3 2 nulo2 3.3e4 nulo1" + newLine +
		"* Fin" + newLine;
	
	String exp3 = 
		"R 1 2 1.0e0" + newLine +
		"R 1 2 1.0e0" + newLine +
		"R 1 2 1.0e0" + newLine +
		"*C 3 1 1.0e-12" + newLine +
		"C 3 1 1.0e-12" + newLine +
		"Q 1 2 3 Q2N3906 nulo1" + newLine +
		"Q 1 2 3 Q2N3906 nulo1" + newLine;
	

	String expressions[] = { exp1, exp2, exp3 };
	int expected[] = { 1, 4, 6 };
	
	@Test
	public void test1()
	{
		for(int i = 0; i < expressions.length; i++)
			motor(expressions[i], expected[i]);
	}
	
	public void motor(String exp, int expected)
	{		
		ComponentCount cc= new ComponentCount(exp);

		Signal signal = cc.count();

		assertEquals(ComponentCount.NAMECOMPONENTCOUNT, signal.getNombre());
		
		int numComponents = (int) signal.getMagnitud(0);
		
		assertEquals(expected, numComponents);		
	}
	

	
}