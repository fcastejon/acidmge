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
 * TestCase para probar la clase Cruce1Paso
 */
package es.uned.simda.acidge.ge.operadores;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomMock;



/**
 * @author fcastejo
 *
 */
public class TestDuplicacion 
{
	private final static Logger logger = Logger.getLogger(TestDuplicacion.class.getName());

	//final static String newLine = String.format("%n");
	static RandomMock randomMock;
	static OperadorDuplicacion duplicacion;
	
	static byte genes[] = { 0, 1, 2, 3, 4, 5, 6 };
	
	@BeforeClass
	public static void checkEA()
	{
		es.uned.simda.acidge.util.Util.checkEA();
	     
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	     
		//RandomGeneratorFactory rgf = RandomGeneratorFactory.getRandomGeneratorFactory();
		randomMock = new RandomMock();
	    //Genotipo.inicia_clase(1, 10, randomMock);
	    
	    //OperatorFactory opf = OperatorFactory.getOperatorFactory();
	    
	    //duplicacion = opf.creaOperadorDuplicacion(1, 0.5, randomMock);
		duplicacion = new Duplicacion(1, 0.5, randomMock);
	}
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
		double dbl[] = { 0.51 };
		int in[] = { 2, 5 };
		byte expected[] = { 0, 1, 2, 3, 4, 5, 6 }; 
		
		motor(dbl, in, expected);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
		double dbl[] = { 0.5 };
		int in[] = { 2, 5 };
		byte expected[] = { 0, 1, 2, 3, 4, 5, 6, 2, 3, 4, 5, 6 }; 
		
		motor(dbl, in, expected);
	}
	
	@Test
	public void test3() 
	{
		logger.info("test3");
		
		double dbl[] = { 0.4 };
		int in[] = { 0, 7 };
		byte expected[] = { 0, 1, 2, 3, 4, 5, 6, 0, 1, 2, 3, 4, 5, 6 }; 
		
		motor(dbl, in, expected);
	}
	
	@Test
	public void test4() 
	{
		logger.info("test4");
		
		double dbl[] = { 0.3 };
		int in[] = { 6, 1 };
		byte expected[] = { 0, 1, 2, 3, 4, 5, 6, 6 }; 
		
		motor(dbl, in, expected);
	}
	
	@Test
	public void test5() 
	{
		logger.info("test5");
		
		double dbl[] = { 0.2 };
		int in[] = { 3, 2 };
		byte expected[] = { 0, 1, 2, 3, 4, 5, 6, 3, 4 }; 
		
		motor(dbl, in, expected);
	}
	
	@Test
	public void test6() 
	{
		logger.info("test7");
		
		double dbl[] = { 0.001 };
		int in[] = { 1, 6 };
		byte expected[] = { 0, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6 }; 
		
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
		
		duplicacion.duplica(genotipo);
		
		logger.info(genotipo.toString());
		
		assertArrayEquals(expected, genotipo.getGenes());
	}
}
