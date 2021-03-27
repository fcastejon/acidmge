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
 * TestCase para probar el acceso al programa externo NGSPICE
 * 
 * 
 */
package es.uned.simda.acidge.spice;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;




public class TestSpice 
{
	private final static Logger logger = Logger.getLogger(TestSpice.class.getName());

	//private static final int LOOP1 = 10;
	private static final int LOOP1 = 2;
	//private static final int LOOP2 = 100;
	//private static final int LOOP3 = 10000;
	//private static final int LOOP4 = 4;
	private static final int LOOP4 = 1;
	
	static Spice spice;
	private static final String SPICE_OUTFILE = "sal.txt";
	private static final String NETLIST = "./src/test/resources/es/uned/simda/acidge/spice/netlistOk.cir";
	//private static final String NETLISTERROR1 = "./src/test/resources/es/uned/simda/acidge/spice/netlistError.cir";
	//private static final String NETLISTERROR2 = "./src/test/resources/es/uned/simda/acidge/spice/netlist_cuelgue_ngspice.cir";
	private static final String LONG_SPICE_COMMAND = "./src/test/resources/es/uned/simda/acidge/spice/longspice.sh";
			
	@BeforeClass
	public static void before()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
		spice = new Spice();
	}
	
	@AfterClass
	public static void after()
	{
		spice.logStats();
		spice.cerrar();
	}
	
	@Test
	public void test0()
	{
		logger.info("test0");
		boolean ret = spice.lanza(NETLIST, SPICE_OUTFILE);
		
		assertTrue(ret);
	}
	
	@Test
	public void test1()
	{
		logger.info("test1");
		boolean ret = true;
		
		ret = motor(LOOP1);
		
		assertTrue(ret);
	}
/* No longer in error, for ngspice 27 ubuntu 16 and 64bits
	@Test
	public void testError1()
	{
		logger.info("testError1");
		boolean ret = spice.lanza(NETLISTERROR1, SPICE_OUTFILE);
		
		assertTrue(ret == false);
	}
	
	@Test
	public void testError2()
	{
		logger.info("testError2");
		boolean ret = spice.lanza(NETLISTERROR2, SPICE_OUTFILE);
		
		assertTrue(ret);
	}	
*/
	@Test
	public void testError3()
	{
		logger.info("testError3");
		String cad = "Reference value :  4.91675e-07";
	 	
		if(cad.contains(Spice.cadError2))
			assertTrue(true);
		else
			fail();
	}
	
	/*
	@Test
	public void test2()
	{
		logger.info("test2");
		boolean ret = true;
		
		ret = motor(LOOP2);
		
		assertTrue(ret);
	}
	*/
/*
	@Test
	public void test3()
	{
		logger.info("test3");
		boolean ret = true;
		
		ret = motor(LOOP3);
		
		assertTrue(ret);
	}*/

	@Test
	public void testTimeout()
	{
		logger.info("testTimeout");
		
		spice.setCommand(LONG_SPICE_COMMAND);
		
		boolean ret = true;
		
		ret = motor2(LOOP4);
		
		assertTrue(ret);
	}


	public boolean motor(int loop)
	{
		boolean ret = true;
		
		for(int i = 0; i < loop; i++)
		{
			if(i % 100 == 0)
				logger.info("loop i:" + i);
			//ret = spice.lanza(NETLISTERROR1, SPICE_OUTFILE);
			if((ret = spice.lanza(NETLIST, SPICE_OUTFILE)) == false)
			{
				logger.severe("Error en spice.lanza");
				break;
			}
			logger.info("Fin lanza");
		}

		return ret;
	}
	
	public boolean motor2(int loop)
	{
		boolean ret = true;
		
		for(int i = 0; i < loop; i++)
		{
			if(i % 100 == 0)
				logger.info("loop i:" + i);
			//ret = spice.lanza(NETLISTERROR1, SPICE_OUTFILE);
			if((ret = spice.lanza(new Integer(i).toString(), SPICE_OUTFILE)) == false)
			{
				logger.severe("Error en spice.lanza");
				break;
			}
			logger.info("Fin lanza");
		}

		return ret;
	}

/*
	@Test
	public void test1() 
	{
	 	InputStreamReader sr = null;
	 	BufferedReader input = null;
	 	
	 	String fileSeparator = System.getProperty("file.separator");
	 	String userDir = System.getProperty("user.dir");
	 	String commandline = userDir + fileSeparator + "dummy.bat";
	 	
	 	logger.info(commandline);
		
		try 
		{
		 	String line;
		 	//Process p = Runtime.getRuntime().exec(commandline);
		 	
		 	ProcessBuilder builder = new ProcessBuilder(commandline);
			builder.redirectErrorStream(true);
            Process p = builder.start();
		 	
		 	/*
		 	 * Alternativa de Stack Overflow para revisar
		 	 * ProcessBuilder builder = new ProcessBuilder("/bin/bash");
			 * builder.redirectErrorStream(true);
             * Process process = builder.start();
		 	 * 
		 	 *
            
            OutputStream os = p.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.println("prueba");
		 	
		 	try 
		 	{
		 		sr = new InputStreamReader(p.getInputStream());
		 		input = new BufferedReader(sr);
		 	
		 		while ((line = input.readLine()) != null) 
		 		{
		 			logger.info(line);
		 		}
		 	}
			finally
			{
				input.close();
			}
		}
		catch (Exception err) 
		{
		 	err.printStackTrace();
		}
	}
*/
}
