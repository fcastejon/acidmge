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
 * TestCase para probar ProblemaServer
 * 
 */
package es.uned.simda.acidge.rmi;

import static org.junit.Assert.*;
import es.uned.simda.acidge.ge.GEProperties;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestProblemaServer 
{	
	private final static Logger logger = Logger.getLogger(TestProblemaServer.class.getName());

	static ProblemaServer server;
	
	private static String ProblemClassName = "es.uned.simda.acidge.problema.dev.ProblemaDev";
	private static String CircuitConstructorClassName = "es.uned.simda.acidge.rmi.CirConstructorMock";
	private static String DevEvalClassName = "es.uned.simda.acidge.problema.dev.eval.kozamat.KozaSensorEval";
	private static String FunAdaptacionClassName = "es.uned.simda.acidge.problema.dev.funciones.Fun1";
	static String hostname;
	final static int Numero = 0;
	
	@BeforeClass
	public static void before()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
	 	//public ProblemaServer(int FitnessType, double MinX, double MaxX, int NumberOfPoints, 
				//int TipoEmbrion, String NetlistHeader, String NetlistModelo, String NetlistAnalisis,
				//String fileName)
		
		//Obtenemos el nombre del host

		try 
		{
			hostname = InetAddress.getLocalHost().getHostName();
			
			logger.info("Hostname:" + hostname);
		} 
		catch (UnknownHostException e) 
		{
 			logger.info(e.getClass() + " " + e.getMessage());
 			throw new AssertionError("UnkownHostException");
		}
		
		logger.info("Creando ProblemaServer");
		
		GEProperties geproperties = new GEProperties();
		
		geproperties.setProblemClassName(ProblemClassName);
 		geproperties.setCircuitConstructorClassName(CircuitConstructorClassName);
 		geproperties.setDevEvalClassName(DevEvalClassName);
 		geproperties.setFunAdaptacionClassName(FunAdaptacionClassName);
		
		server = new ProblemaServer(hostname, Numero, geproperties);

		logger.info("ProblemaServer creado");
	}
	
	@AfterClass
	public static void after()
	{
		logger.info("Cerrando ProblemaServer");

		server.cerrar();
		
		logger.info("ProblemaServer cerrado");
	}
	
	@Test
	public void test0()
	{   
    	ClientFactory factory = ClientFactory.getFactory();
    	logger.info("Creando clienteProblema");
    	
		GEProperties geproperties = new GEProperties();
		
		geproperties.setProblemClassName(ProblemClassName);
 		geproperties.setCircuitConstructorClassName(CircuitConstructorClassName);
 		geproperties.setDevEvalClassName(DevEvalClassName);
 		geproperties.setFunAdaptacionClassName(FunAdaptacionClassName);

		ProblemaRemoto problema = factory.createClienteProblema(hostname, Numero,
				hostname, geproperties, false);
    	
    	assertTrue(problema != null);
	}
}
