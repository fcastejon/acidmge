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
 * TestCase para probar la clase SteadyStateFitness
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
public class TestSteadyStateFitness 
{
	private final static Logger logger = Logger.getLogger(TestSteadyStateFitness.class.getName());

	static OperadorReemplazo reemplazo;
	
	static byte genes[] = { 0, 1, 2, 3, 4, 5 };
	
	static int SurvivorSelectionType = 3;
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
	
	double [] getFitness(Poblacion pob)
	{
		double [] fi = new double[pob.getSize()];
		Iterator<Fenotipo> it = pob.iterator();
		int i = 0;
		
		while(it.hasNext())
		{
			fi[i++] = it.next().getFitness();
		}
		
		return fi;
	}
	

	double exp1[] = { 1, 2, 2, 3, 4, 4, 5, 6, 7, 8 };
	double exp2[] = { 1, 2, 2, 3, 3, 4, 4, 4, 10, 20 };
	
	@Test
	public void test1() 
	{
		logger.info("test1");
		
	    //OperatorFactory opf = OperatorFactory.getOperatorFactory();
	    
	    //SurvivorSelectionType, IndividualsNumber, Elitism, GenerationalGap)
	    //reemplazo = opf.creaOperadorReemplazo(SurvivorSelectionType, IndividualsNumber, 0, 2);
		reemplazo = new SteadyStateFitness(SurvivorSelectionType, IndividualsNumber, 0, 2);
		
		Poblacion pob1 = creaPoblacion(IndividualsNumber, 1);
		
		Poblacion pob2 = creaPoblacion(reemplazo.getMatingPoolSize(), 2);
		
		pob1 = reemplazo.nuevaPoblacion(pob1, pob2);
		double actual[] = getFitness(pob1);	
		
		Assert.assertArrayEquals(exp1, actual, 1e-6);
	}
	
	@Test
	public void test2() 
	{
		logger.info("test2");
		
	    //OperatorFactory opf = OperatorFactory.getOperatorFactory();
	    
	    //SurvivorSelectionType, IndividualsNumber, Elitism, GenerationalGap)
	    //reemplazo = opf.creaOperadorReemplazo(SurvivorSelectionType, IndividualsNumber, 0, 2);
	    reemplazo = new SteadyStateFitness(SurvivorSelectionType, IndividualsNumber, 0, 2);

    	Poblacion pob1 = creaPoblacion(IndividualsNumber, 1);
	    
	    for(int i = 2; i <= 10; i++)
	    {
	    	Poblacion pob2 = creaPoblacion(reemplazo.getMatingPoolSize(), i);
		
	    	pob1 = reemplazo.nuevaPoblacion(pob1, pob2);
	    	double p[] = getFitness(pob1);
	    	pintaArray(p);
	    }
	    
		double actual[] = getFitness(pob1);	
		
		Assert.assertArrayEquals(exp2, actual, 1e-6);
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
	
}
