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
 * TestCase para probar la clase Poblacion
 */
package es.uned.simda.acidge.ge;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.operadores.OperatorFactory;
import es.uned.simda.acidge.ge.random.RandomGenerator;
import es.uned.simda.acidge.ge.random.RandomGeneratorFactory;
import es.uned.simda.acidge.ge.random.RandomJava;
import es.uned.simda.acidge.ge.random.RandomMock;
import es.uned.simda.acidge.problema.EvalRes;



/**
 * @author fcastejo
 *
 */
public class TestPoblacion 
{
	private final static Logger logger = Logger.getLogger(TestPoblacion.class.getName());
	
	static byte genes[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		
	static int IndividualsNumber = 10;

	
	@BeforeClass
	public static void before()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}
	
	int fit[] = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
	int age[] = { 2, 1, 2, 1, 2, 1, 2, 1, 2, 1 };
	
	Poblacion creaPoblacion(int [] fit, int [] age)
	{
		Poblacion pob = new Poblacion();
		
		for(int i = 0; i < fit.length; i++)
		{
			Genotipo.setGeneracionClase(age[i]);
			Genotipo gen = new Genotipo(genes);
			Fenotipo fen = new Fenotipo(gen);
			EvalRes evalRes = new EvalRes(fit[i]);
			fen.setYMarcaViable(evalRes);
			pob.addFenotipo(fen);
		}
		
		return pob;
	}
	
	int exp1[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	
	int exp2[] = { 1, 3, 5, 7, 9, 0, 2, 4, 6, 8 };
	int age2[] = { 2, 2, 2, 2, 2, 1, 1, 1, 1, 1 };
	
	int exp3[] = { 0, 1, 2, 3, 5, 7, 9, 4, 6, 8 };
	int age3[] = { 1, 2, 1, 2, 2, 2, 2, 1, 1, 1 };
	
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
    	Poblacion pob1 = creaPoblacion(fit, age);
    	
    	pob1.ordena();	    
		int actual[] = getFitness(pob1);	
		
		Assert.assertArrayEquals(exp1, actual);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
    	Poblacion pob1 = creaPoblacion(fit, age);

    	//Necesario ordenar previamente
    	pob1.ordena();
		pob1.ordenaAge(0);
		int actual[] = getFitness(pob1);
		int actage[] = getAge(pob1);
		
		Assert.assertArrayEquals(exp2, actual);
		Assert.assertArrayEquals(age2, actage);
	}
	
	@Test
	public void test3() 
	{
		logger.info("test3");
		
    	Poblacion pob1 = creaPoblacion(fit, age);
    			
    	//Necesario ordenar previamente
    	pob1.ordena();
		pob1.ordenaAge(4);
		int actual[] = getFitness(pob1);
		int actage[] = getAge(pob1);
		
		Assert.assertArrayEquals(exp3, actual);
		Assert.assertArrayEquals(age3, actage);
	}

	
	int [] getFitness(Poblacion pob)
	{
		int [] fi = new int[pob.getSize()];
		Iterator<Fenotipo> it = pob.iterator();
		int i = 0;
		
		while(it.hasNext())
		{
			fi[i++] = (int) it.next().getFitness();
		}
		
		return fi;
	}
	
	int [] getAge(Poblacion pob)
	{
		int [] fi = new int[pob.getSize()];
		Iterator<Fenotipo> it = pob.iterator();
		int i = 0;
		
		while(it.hasNext())
		{
			fi[i++] = it.next().getGeneracion();
		}
		
		return fi;
	}
	
	void pintaArray(int [] p)
	{
		String c = "";
		
		for(int i = 0; i < p.length; i++)
		{
			c += p[i] + " ";
		}

		logger.info(c);
	}

}
