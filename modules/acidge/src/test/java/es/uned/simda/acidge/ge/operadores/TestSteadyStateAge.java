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
 * TestCase para probar la clase SteadyStateAge
 */
package es.uned.simda.acidge.ge.operadores;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.logging.Logger;
//import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.Fenotipo;
import es.uned.simda.acidge.ge.Poblacion;
import es.uned.simda.acidge.problema.EvalRes;


/**
 * @author fcastejo
 *
 */
public class TestSteadyStateAge 
{
	private final static Logger logger = Logger.getLogger(TestSteadyStateAge.class.getName());

	static OperadorReemplazo reemplazo;
	
	static byte genes[] = { 0, 1, 2, 3, 4, 5 };
	
	static int SurvivorSelectionType = 2;
	static int IndividualsNumber = 10;
	static int Elitism = 2;
	
	@BeforeClass
	public static void checkEA()
	{
		es.uned.simda.acidge.util.Util.checkEA();
	     
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
	}

	Poblacion creaPoblacion(int npob, int generation)
	{
		Genotipo.setGeneracionClase(generation);

		Poblacion pob = new Poblacion();
		
		for(int i = 0; i < npob; i++)
		{
			Genotipo gen = new Genotipo(genes);
			Fenotipo fen = new Fenotipo(gen);
			EvalRes evalRes = new EvalRes((i + 1) * generation);
			fen.setYMarcaViable(evalRes);
			pob.addFenotipo(fen);
		}
		
		return pob;
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

	int exp1[]    = { 10, 20, 9, 18, 8, 16, 7, 14, 6, 12 }; 
	int expage1[] = { 10, 10, 9, 9, 8, 8, 7, 7, 6, 6  };
	int exp2[]    = { 1, 2, 10, 20, 9, 18, 8, 16, 7, 14 }; 
	int expage2[] = { 1, 1, 10, 10, 9, 9, 8, 8, 7, 7  };
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
	    //OperatorFactory opf = OperatorFactory.getOperatorFactory();
	    
	    //SurvivorSelectionType, IndividualsNumber, Elitism, GenerationalGap)
	    //reemplazo = opf.creaOperadorReemplazo(SurvivorSelectionType, IndividualsNumber, 0, 2);
		reemplazo = new SteadyStateAge(SurvivorSelectionType, IndividualsNumber, 0, 2);

    	Poblacion pob1 = creaPoblacion(IndividualsNumber, 1);
    	
    	int p[] = new int[1];
    	int q[] = new int[1];
	    
	    for(int i = 2; i <= 10; i++)
	    {
	    	Poblacion pob2 = creaPoblacion(reemplazo.getMatingPoolSize(), i);
		
	    	pob1 = reemplazo.nuevaPoblacion(pob1, pob2);
	    	pob1.ordenaAge(0);
	    	p = getFitness(pob1);
	    	pintaArrayInt(p);
	    	q = getAge(pob1);
	    	pintaArrayInt(q);
	    }	
		
		Assert.assertArrayEquals(exp1, p);
		
		Assert.assertArrayEquals(expage1, q);

	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		int elitismo = 2;
		
	    //OperatorFactory opf = OperatorFactory.getOperatorFactory();
	    
	    //SurvivorSelectionType, IndividualsNumber, Elitism, GenerationalGap)
	    //reemplazo = opf.creaOperadorReemplazo(SurvivorSelectionType, IndividualsNumber, elitismo, 2);
	    reemplazo = new SteadyStateAge(SurvivorSelectionType, IndividualsNumber, elitismo, 2);

    	Poblacion pob1 = creaPoblacion(IndividualsNumber, 1);
    	
    	int p[] = new int[1];
    	int q[] = new int[1];
	    
	    for(int i = 2; i <= 10; i++)
	    {
	    	Poblacion pob2 = creaPoblacion(reemplazo.getMatingPoolSize(), i);
		
	    	pob1 = reemplazo.nuevaPoblacion(pob1, pob2);
	    	pob1.ordenaAge(elitismo);
	    	p = getFitness(pob1);
	    	pintaArrayInt(p);
	    	q = getAge(pob1);
	    	pintaArrayInt(q);
	    }	
		
		Assert.assertArrayEquals(exp2, p);
		
		Assert.assertArrayEquals(expage2, q);

	}
	
	void pintaArray(double [] p)
	{
		String c = "";
		
		for(int i = 0; i < p.length; i++)
		{
			c += p[i] + " ";
		}

		logger.info(c);
	}
	
	//No importa repetir código al ser una prueba
	void pintaArrayInt(int [] p)
	{
		String c = "";
		
		for(int i = 0; i < p.length; i++)
		{
			c += p[i] + " ";
		}

		logger.info(c);
	}
	
}
