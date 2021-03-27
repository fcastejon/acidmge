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
 * TestCase para probar la clase MutacionBitwiseGE2
 */
package es.uned.simda.acidge.ge.operadores;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;
import es.uned.simda.acidge.ge.random.RandomGeneratorFactory;
import es.uned.simda.acidge.ge.random.RandomJava;
import es.uned.simda.acidge.ge.random.RandomMock;



/**
 * @author fcastejo
 *
 */
public class TestMutacionBitwiseGE2 
{
	private final static Logger logger = Logger.getLogger(TestMutacionBitwiseGE2.class.getName());

	//final static String newLine = String.format("%n");
	final static String SalidaFileTemp = "salida_%.txt";
	static RandomMock randomMock;
	static OperadorMutacion mutacion;
	
	static byte genes[] = { 0x0, 0x10, 0x8, 0x4 };
	
	@BeforeClass
	public static void inicia()
	{
		es.uned.simda.acidge.util.Util.checkEA();
	     
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	     
		//RandomGeneratorFactory rgf = RandomGeneratorFactory.getRandomGeneratorFactory();
		randomMock = new RandomMock();
	    //Genotipo.inicia_clase(1, 10, randomMock);
	    
	    //OperatorFactory opf = OperatorFactory.getOperatorFactory();
	    
	    //Crea un operador de mutación bitwiseGE2
	    //mutacion = opf.creaOperadorMutacion(3, 0.7, randomMock, 0.1);
		mutacion = new MutacionBitwiseGE2(3, 0.7, randomMock, 0.1);
	}
	
	@AfterClass
	public static void finaliza()
	{
		mutacion.stat();
	}

	@Test
	public void test1() 
	{
		logger.info("test1");
		
		double dbl[] = {
				0.01,
				0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,  
				0.8, 0.4, 0.7, 0.3, 0.6, 0.2, 0.5, 0.1/*,
				0.1, 0.2, 0.8, 0.9, 0.7, 0.6, 0.5, 0.4*/
				};
		int in[] = { 0 };
		byte expected[] = { (byte) 0xfe, (byte) 0x6f, (byte) 0x8, (byte) 0x4 };
		
		motor(dbl, in, expected, 2);
	}

	@Test
	public void test2() 
	{
		logger.info("test2");
			
		double dbl[] = {
				0.2, 
				0.9, 0.8, 0.71, 0.8, 0.9, 0.95, 0.97, 0.84,  
				0.9, 0.69, 0.71, 0.9, 0.85, 0.72, 0.5, 0.99,
				0.9, 0.8, 0.3, 0.9, 0.99, 0.1, 0.71, 0.2 };
		int in[] = { 0 };
		byte expected[] = { 0, (byte) 0x10, (byte) 0x4a, (byte) 0x21 };
		
		motor(dbl, in, expected, 1);
	}
	
	@Test
	public void test3() 
	{
		logger.info("test3");
			
		double dbl[] = {
				0.9, 0.8, 0.71, 0.8, 0.9, 0.95, 0.97, 0.84,  
				0.9, 0.69, 0.71, 0.9, 0.85, 0.72, 0.5, 0.99,
				0.9, 0.8, 0.3, 0.9, 0.99, 0.1, 0.71, 0.2,
				0.9, 0.3, 0.9, 0.99, 0.1, 0.71, 0.2, 0.5
				};
		
		int in[] = { 0 };
		byte expected[] = { (byte) 0x0, (byte) 0x52, (byte) 0x2d, (byte) 0x4f };
		
		motor(dbl, in, expected, 0);
	}
	
	byte [] copia(byte [] genes)
	{
		byte [] sal = new byte[genes.length];
		
		for(int i = 0; i < genes.length; i++)
			sal[i] = genes[i];
		
		return sal;
	}
	
	void motor(double [] dbl, int [] in, byte [] expected, int igl)
	{
		randomMock.setDouble(dbl);
		randomMock.setInteger(in);
		
		byte [] genes_aux = copia(genes);
		Genotipo genotipo = new Genotipo(genes_aux);
		genotipo.setIndGE2(igl);
		
		mutacion.muta(genotipo);
		
		logger.info(genotipo.toString());
		
		assertArrayEquals(expected, genotipo.getGenes());
	}
}
