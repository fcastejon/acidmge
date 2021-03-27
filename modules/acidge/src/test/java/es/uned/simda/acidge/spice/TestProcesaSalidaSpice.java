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
 * TestCase para probar TestProcesaSalidaSpice
 * 
 * 
 */
package es.uned.simda.acidge.spice;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

//import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.problema.InviableException;



public class TestProcesaSalidaSpice 
{
	private final static Logger logger = Logger.getLogger(TestProcesaSalidaSpice.class.getName());

	private final static String outFileOk = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaSpice.txt";
	private final static String outFileError = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaSpiceError.txt";
	private final static String outFileOk2 = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaSpice2.txt";
	private final static String outFileOk3 = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaSpice3.txt";
	private final static String outFileError2 = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaSpiceError2.txt";
	private final static String outFileTemp = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaTemp.txt";
	private final static String outFileTempIndex = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaTempIndex.txt";
	private final static String outFileVSweep = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaVSweep.txt";
	private final static String outFileError3 = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaSpiceError3.txt";
	private final static String outFileCero = "./src/test/resources/es/uned/simda/acidge/spice/salidaCero.txt";
	private final static String outFileError4 = "./src/test/resources/es/uned/simda/acidge/spice/testSalidaSpiceError4.txt";
	
	private static String FileSeparator;
	private static String UserDir;
	
	private ProcesaSalidaSpice pss;
		
	@BeforeClass
	public static void before()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
		FileSeparator = System.getProperty("file.separator");
		UserDir = System.getProperty("user.dir");
	}
	
	//Normalmente quitado, vale para probar un fichero concreto
	/*
	@Test
	public void testAdHoc()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileAdHoc, false);
		
		try
		{
			pss.lee();
		
			HashMap<String, Signal> signals = pss.getSignals();
		
			logger.info("signals.size()=" + signals.size());
			logger.info(signals.toString());
			logger.info("v(3)t=" + signals.get("v(3)t").getSize());
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}
	}
	*/
	
	@Test
	public void testOk()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileOk, false);
		
		try
		{
			pss.lee();
		
			HashMap<String, Signal> signals = pss.getSignals();
		
			//assertTrue(signals.size() == 5);
			assertTrue(signals.size() == 4);
			assertTrue(signals.get("vm(5)f").getSize() == 36);
			assertTrue(signals.get("v1#branchf").getSize() == 36);
			assertTrue(signals.get("v(2)f").getSize() == 36);
			assertTrue(signals.get("ic").getSize() == 1);
			//assertTrue(signals.get("V(4)").getSize() == 1);
			
			double m1 = signals.get("vm(5)f").getSigItem(64).getMagnitud();
			double p1 = signals.get("vm(5)f").getSigItem(64).getFase();
			
			assertEquals(m1, 3.026804e+000, 1e-6);
			assertEquals(p1, -2.38486e+000, 1e-6);
		
			m1 = signals.get("v1#branchf").getSigItem(64).getMagnitud();
			p1 = signals.get("v1#branchf").getSigItem(64).getFase();
		
			assertEquals(m1, 8.282485628e-5, 1e-10);
			assertEquals(p1, -2.384745, 1e-6);
		
			m1 = signals.get("v(2)f").getSigItem(64).getMagnitud();
			p1 = signals.get("v(2)f").getSigItem(64).getFase();
		
			assertEquals(m1, 1, 1e-6);
			assertEquals(p1, 0, 1e-6);
			
			//m1 = signals.get("V(4)").getSigItem(0).getMagnitud();
			//assertEquals(m1, 2.519340e+000, 1e-6);
			
			m1 = signals.get("ic").getSigItem(0).getMagnitud();
			assertEquals(m1, 0.00157201, 1e-6);
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}
	}

	@Test
	public void testError()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileError, false);
		
		try
		{
			pss.lee();
			fail();
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			assertTrue(true);
		}	
	}
	
	@Test
	public void testOk2()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileOk2, false);
		
		try
		{
			pss.lee();
		
			HashMap<String, Signal> signals = pss.getSignals();
		
			//assertTrue(signals.size() == 6);
			assertTrue(signals.size() == 5);
			assertTrue(signals.get("vm(4)f").getSize() == 5);
			assertTrue(signals.get("vac1#branchf").getSize() == 5);
			assertTrue(signals.get("v(3)f").getSize() == 5);
			assertTrue(signals.get("v(4)t").getSize() == 51);
			//assertTrue(signals.get("V(4)").getSize() == 1);
			assertTrue(signals.get("ic").getSize() == 1);
			
			Signal v = signals.get("v(4)t");
			
			//double continua = signals.get("V(4)").getSigItem(0).getMagnitud();
			//assertEquals(continua, 0.0, 1e-6);
			
			double ic = signals.get("ic").getSigItem(0).getMagnitud();
			assertEquals(ic, 0.00108514, 1e-6);
			
			//v.calculaStats(continua);
			v.calculaStats(0);
			
			double media = v.getMedia();
			//double maxPicoPico = v.getMaxPicoPico();
			
			//logger.info("media:" + media + " maxPicoPico:" + maxPicoPico);
			logger.info("media:" + media);
			
			assertEquals(media, 0.279223, 1e-6);
			//assertEquals(maxPicoPico, 14.532102, 1e-6);
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}
	}
	
	@Test
	public void testOk3()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileOk3, false);
		
		try
		{
			pss.lee();
		
			HashMap<String, Signal> signals = pss.getSignals();
		
			logger.info(Signal.signalsToString(signals));
			
			//assertTrue(signals.size() == 6);
			assertTrue(signals.size() == 5);
			assertTrue(signals.get("vm(4)f").getSize() == 9);
			assertTrue(signals.get("vac1#branchf").getSize() == 9);
			assertTrue(signals.get("v(3)f").getSize() == 9);
			//assertTrue(signals.get("V(4)").getSize() == 1);
			assertTrue(signals.get("ic").getSize() == 1);
			assertTrue(signals.get("THD").getSize() == 1);
			
			//double continua = signals.get("V(4)").getSigItem(0).getMagnitud();
			//assertEquals(continua, 0.0, 1e-6);
			
			double ic = signals.get("ic").getSigItem(0).getMagnitud();
			assertEquals(ic, 4.56623e-06, 1e-6);
			
			double thd = signals.get("THD").getSigItem(0).getMagnitud();
			assertEquals(thd, 42.4534, 1e-6);
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}
	}
	
	@Test
	public void testError2()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileError2, false);
		
		try
		{
			pss.lee();
			fail();
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			assertTrue(true);
		}	
	}

	@Test
	public void testTemp()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileTemp, false);
		
		try
		{
			pss.lee();
		
			HashMap<String, Signal> signals = pss.getSignals();
		
			logger.info(Signal.signalsToString(signals));
			
			assertTrue(signals.size() == 2);
			assertTrue(signals.get("v(4)t").getSize() == 21);
			assertTrue(signals.get("ic").getSize() == 1);
			
			double ic = signals.get("ic").getSigItem(0).getMagnitud();
			assertEquals(ic, 0.010001, 1e-6);
			
			assertEquals(signals.get("v(4)t").getSigItem(0).getMagnitud() , 9.615936e-01, 1e-6);
			assertEquals(signals.get("v(4)t").getSigItem(100).getMagnitud() , 1.111513e+00, 1e-6);
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}
	}
	
	@Test
	public void testTempIndex()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileTempIndex, true);
		
		try
		{
			pss.lee();
		
			HashMap<String, Signal> signals = pss.getSignals();
		
			logger.info(Signal.signalsToString(signals));
			
			assertTrue(signals.size() == 2);
			assertTrue(signals.get("v(3)i").getSize() == 105);
			assertTrue(signals.get("ic").getSize() == 1);
			
			double ic = signals.get("ic").getSigItem(0).getMagnitud();
			assertEquals(ic, 1.52193e-11, 1e-15);
			
			assertEquals(signals.get("v(3)i").getSigItem(0).getMagnitud() , 7.936146e-13, 1e-17);
			assertEquals(signals.get("v(3)i").getSigItem(104).getMagnitud() , 1.851139e-07, 1e-12);
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}
	}
	
	@Test
	public void testVSweep()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileVSweep, true);
		
		try
		{
			pss.lee();
		
			HashMap<String, Signal> signals = pss.getSignals();
		
			logger.info(Signal.signalsToString(signals));
			
			assertTrue(signals.size() == 1);
			logger.info("size " + signals.get("v2#brancht").getSize());
			assertTrue(signals.get("v2#brancht").getSize() == 101);
			//assertTrue(signals.get("ic").getSize() == 1);
			
			//double ic = signals.get("ic").getSigItem(0).getMagnitud();
			//assertEquals(ic, 1.52193e-11, 1e-15);
			
			
			//logger.info(signals.get("v2#brancht").toString());
			assertEquals(-7.51e-12, signals.get("v2#brancht").getSigItem(2.0).getMagnitud(), 1e-14);
			assertEquals(-7.51e-12, signals.get("v2#brancht").getSigItem(3.0).getMagnitud(), 1e-14);
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}
	}
	
	@Test
	public void testError3()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileError3, false);
		
		try
		{
			pss.lee();
			HashMap<String, Signal> signals = pss.getSignals();
			
			String lista = Signal.signalsToString(signals);
			
			logger.info("Debe estar vacia (" + lista + ")");
			
			if(lista.isEmpty())
				assertTrue(true);
			else
				fail();
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}	
	}
	
	@Test
	public void testCero()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileCero, false);
		
		try
		{
			pss.lee();
		
			HashMap<String, Signal> signals = pss.getSignals();
		
			logger.info("signals.size()=" + signals.size());
			logger.info(signals.toString());
			logger.info("v(3)t=" + signals.get("v(3)t").getSize());
			
			assertEquals(signals.get("v(3)t").getSigItem(0.0).getMagnitud(), -4.91715e-24, 1e-17);
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			fail();
		}
	}

	@Test
	public void testError4()
	{
		pss = new ProcesaSalidaSpice(UserDir + FileSeparator + outFileError4, false);
		
		try
		{
			pss.lee();
			fail();
		}
		catch(InviableException ie)
		{
			logger.info(ie.getClass().toString() + ie.getMessage());
			assertTrue(true);
		}	
	}
}
