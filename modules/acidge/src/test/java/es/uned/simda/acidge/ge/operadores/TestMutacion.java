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
 * TestCase para probar la clase Mutación
 */
package es.uned.simda.acidge.ge.operadores;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
//import junit.framework.TestCase;
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
public class TestMutacion 
{
	private final static Logger logger = Logger.getLogger(TestMutacion.class.getName());

	//final static String newLine = String.format("%n");
	final static String SalidaFileTemp = "salida_%.txt";
	static RandomMock randomMock;
	static OperadorMutacion mutacion;
	
	static byte genes[] = { 0, 1, 2, 3, 4, 5 };
	
	@BeforeClass
	public static void checkEA()
	{
		es.uned.simda.acidge.util.Util.checkEA();
	     
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	     
		//RandomGeneratorFactory rgf = RandomGeneratorFactory.getRandomGeneratorFactory();
		randomMock = new RandomMock();
	    //Genotipo.inicia_clase(1, 10, randomMock);
	    
	    //OperatorFactory opf = OperatorFactory.getOperatorFactory();
	    
	    //mutacion = opf.creaOperadorMutacion(1, 0.7, randomMock, 0.0);
		mutacion = new Mutacion(1, 0.7, randomMock);
	}

	@Test
	public void test1() 
	{
		logger.info("test1");
		
		double dbl[] = { 0.9, 0.8, 0.7, 0.6, 0.5, 0.4 };
		int in[] = { 100, 101, 102, 103 };
		byte expected[] = { 0, 1, 100, 101, 102, 103 };
		
		motor(dbl, in, expected);
	}

	@Test
	public void test2() 
	{
		logger.info("test2");
		
		double dbl[] = { 0.9, 0.8, 0.75, 0.74, 0.73, 0.71 };
		int in[] = { 0 };
		byte expected[] = { 0, 1, 2, 3, 4, 5 };
		
		motor(dbl, in, expected);
	}
	
	@Test
	public void test3() 
	{
		logger.info("test3");
		
		double dbl[] = { 0.1, 0.2, 0.7, 0.6, 0.5, 0.4 };
		int in[] = { 100, 101, 102, 103, 104, 105 };
		byte expected[] = { 100, 101, 102, 103, 104, 105 };
		
		motor(dbl, in, expected);
	}
	
	byte [] copia(byte [] genes)
	{
		byte [] sal = new byte[genes.length];
		
		for(int i = 0; i < genes.length; i++)
			sal[i] = genes[i];
		
		return sal;
	}
	
	void motor(double [] dbl, int [] in, byte [] expected)
	{
		randomMock.setDouble(dbl);
		randomMock.setInteger(in);
		
		byte [] genes_aux = copia(genes);
		Genotipo genotipo = new Genotipo(genes_aux);
		
		mutacion.muta(genotipo);
		
		logger.info(genotipo.toString());
		
		assertArrayEquals(expected, genotipo.getGenes());
	}
}
