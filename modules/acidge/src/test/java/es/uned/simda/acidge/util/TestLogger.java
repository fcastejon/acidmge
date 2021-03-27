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
 * TestLogger
 * 
 * TestCase para probar las clases de trazas
 * 
 * No se puede automatizar pues requiere revisión de la salida manual
 */
package es.uned.simda.acidge.util;

import static org.junit.Assert.*;

import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestLogger 
{
	private final static Logger logger = Logger.getLogger(TestLogger.class.getName());

	final static String newLine = String.format("%n");
	
	
	@BeforeClass
	public static void beforeClass()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		//es.uned.simda.acidge.util.LoggerStart.setupFromFile();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	@Test
	public void test1() 
	{	
		for(int i = 0; i < 10000; i++)
			logger.info("Esto es una traza de orden: " + i);
		
		assertEquals(1, 1);
	}
}
