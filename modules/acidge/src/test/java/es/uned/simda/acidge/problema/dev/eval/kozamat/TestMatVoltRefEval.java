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
 * TestCase para probar la VoltRefEval
 * 
 */
package es.uned.simda.acidge.problema.dev.eval.kozamat;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

//import junit.framework.TestCase;
import org.junit.BeforeClass;
//import org.junit.Before;
import org.junit.Test;

import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.Signal;


public class TestMatVoltRefEval
{
	private final static Logger logger = Logger.getLogger(TestMatVoltRefEval.class.getName());
	
	//private final static int NumeroSumandos = 9;

	private final static String VoutName = "v(2)i";
	
	//Requisitos
	private final static int puntos = 105; 
	public final static double TKOZA = 0.02;
	public final static double TKOZAFACTOR1 = 1.0;
	public final static double TKOZAFACTOR2 = 10.0;
	private final static double Vobj = 2.0;
	//private final static double Vinicial = 4.0;
	//private final static double Vfinal = 6.0;
	//private final static int vpuntos = 21;
	
	private static double [][] v;
	//private static double [] vin;
	
	private static double expected[] = { 0.0, 0.0, 0.0, 0.0505385 };
	private static int expectedHits[] = { 105, 105, 105, 51 };
	private static final int tests = 4;
	
	@BeforeClass
	public static void beforeClass()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
		calculaVectores();
	}
	
	public static void calculaVectores()
	{
		v = new double[tests][];
		
		for(int i = 0; i < tests; i++)
			v[i] = new double[puntos];
		
		//vin = new double[puntos];
		
		for(int i = 0; i < puntos; i++)
		{
			v[0][i] = Vobj;
			v[1][i] = Vobj + Math.pow(-1, i)*TKOZA * 0.99;
			v[2][i] = Vobj - TKOZA*0.99 + 2*TKOZA*0.99*i/(puntos - 1);
			v[3][i] = Vobj - TKOZA * 2 + 4*TKOZA * i/(puntos - 1);
			
			//vin[i] = Vinicial + (Vfinal - Vinicial)/(vpuntos - 1) * (i % vpuntos);
		}
		
		logger.info("calculaVectores");
	}
	
	@Test
	public void testEvalua() 
	{	
		
		for(int i = 0; i < tests; i++)
		{
			logger.info("testEvalua:" + i);
			
			motor(i);
		}
		
	}
		
	public void motor(int ind) 
	{	
		VoltRefEval eval = new MatVoltRefEval(null);
		
		HashMap<String, Signal> signals = construye(ind);
		List<HashMap<String, Signal>> listaSignals = new ArrayList<HashMap<String, Signal>>();
		listaSignals.add(signals);
		
		try
		{
			KozaEvalRes evalRes = (KozaEvalRes) eval.evalua(listaSignals);
			
			logger.info(evalRes.toString());
			assertEquals(expected[ind], evalRes.getFitness(), 1e-6);
			assertEquals(expectedHits[ind], evalRes.getHits());
			assertEquals(puntos, evalRes.getGoalHits());
		}
		catch(InviableException ie)
		{
			fail();
		}
	}
	
	HashMap<String, Signal> construye(int ind)
	{	
		HashMap<String, Signal> signals = new HashMap<String, Signal>();
		Signal Vout = new Signal(VoutName);
		
		//assert(vin.length == puntos);
		for(int i = 0; i < puntos; i++)
		{
			logger.info("i:" + i + " medida:" + v[ind][i]);
			Vout.putMP(i, v[ind][i], 0);
		}

		signals.put(Vout.getNombre(), Vout);
		
		logger.info("Señales:" + signals.size());
		
		return signals;
	}
}
