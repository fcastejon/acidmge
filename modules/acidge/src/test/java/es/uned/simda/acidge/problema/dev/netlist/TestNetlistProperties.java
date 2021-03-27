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


import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
//import org.junit.Before;
import org.junit.Test;




public class TestNetlistProperties
{	
	private final static Logger logger = Logger.getLogger(TestNetlistProperties.class.getName());

	//private final static String newLine = String.format("%n");

	private final static String PropertiesFile = "testNetlist.properties";

	NetlistProperties properties;
	
	@BeforeClass
	public static void init()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.trivialSetup(Level.FINEST);
	}
	
	@Test
	public void test() 
	{	
		logger.info("test");
	    InputStream is = getClass().getResourceAsStream(PropertiesFile);
	    
		NetlistProperties properties = new NetlistProperties(is);
		
		assertEquals(properties.getNodosProtegidos().size(), 3);
		assertEquals(properties.getSimulaciones(), 1);
		
		properties.printProperties();
	}
	

}
