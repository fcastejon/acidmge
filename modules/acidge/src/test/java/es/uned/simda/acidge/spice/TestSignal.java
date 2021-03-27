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
 * TestCase para probar TestSignal
 * 
 * 
 */
package es.uned.simda.acidge.spice;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;

//import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;




public class TestSignal
{	
	private final static Logger logger = Logger.getLogger(TestSignal.class.getName());

	@BeforeClass
	public static void before()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	@Test
	public void testdB()
	{
		double db;  
		
		db = Signal.AToDB(0.5);
		assertEquals(-6, db, 3e-2);
		
		db = Signal.AToDB(1/Math.sqrt(2));
		assertEquals(-3, db, 3e-2);
	}
	
	@Test
	public void testMagnitud()
	{
		double mag;
		
		mag = Signal.convertMagnitud(1, 0);
		assertEquals(1, mag, 1e-6);
		
		mag = Signal.convertMagnitud(0, 1);
		assertEquals(1, mag, 1e-6);
		
		mag = Signal.convertMagnitud(1, 1);
		assertEquals(Math.sqrt(2), mag, 1e-6);

		mag = Signal.convertMagnitud(2, 0);
		assertEquals(2, mag, 1e-6);
		
		mag = Signal.convertMagnitud(0, 2);
		assertEquals(2, mag, 1e-6);
		
		mag = Signal.convertMagnitud(Math.sqrt(2), Math.sqrt(2));
		assertEquals(2, mag, 1e-6);
	}
	
	@Test
	public void testFase()
	{
		double fase;
		
		fase = Signal.convertFase(1, 0);
		assertEquals(0, fase, 1e-6);
		
		fase = Signal.convertFase(1, 1);
		assertEquals(Math.PI/4, fase, 1e-6);
		
		fase = Signal.convertFase(0, 1);
		assertEquals(Math.PI/2, fase, 1e-6);
		
		fase = Signal.convertFase(-1, 1);
		assertEquals(3*Math.PI/4, fase, 1e-6);
		
		fase = Signal.convertFase(-1, 0);
		assertEquals(Math.PI, fase, 1e-6);
		
		fase = Signal.convertFase(-1, -1);
		assertEquals(-3*Math.PI/4, fase, 1e-6);
		
		fase = Signal.convertFase(0, -1);
		assertEquals(-Math.PI/2, fase, 1e-6);
		
		fase = Signal.convertFase(1, -1);
		assertEquals(-Math.PI/4, fase, 1e-6);
	}
	
	
}