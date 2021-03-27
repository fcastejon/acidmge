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
 * TestCase para probar ServerProperties
 * 
 */
package es.uned.simda.acidge.rmi;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Logger;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;



public class TestServerProperties 
{
	private final static Logger logger = Logger.getLogger(TestServerProperties.class.getName());

	private static final String PropertiesFile = "testServer.properties";
	
	@BeforeClass
	public static void beforeClass()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	@Before
	public void before()
	{
		InputStream is = getClass().getResourceAsStream(PropertiesFile);
	}
	
	@Test
	public void test0()
	{
		InputStream is = getClass().getResourceAsStream(PropertiesFile);
		
		ServerProperties serverProperties = new ServerProperties(is);
		
		serverProperties.printProperties();
		
		ArrayList<String> names = serverProperties.getNames();
				
		String[] actual = names.toArray(new String[names.size()]);
		
		String expected[] = { "ce03/0", "ce04/0", "ce04/1" };
		
		assertArrayEquals(expected, actual);
	}
	
	ArrayList<String> expectedNames()
	{
		ArrayList<String> names = new ArrayList<String>();
		
		names.add("ce03");
		names.add("ce04");
		
		return names;
	}
}
