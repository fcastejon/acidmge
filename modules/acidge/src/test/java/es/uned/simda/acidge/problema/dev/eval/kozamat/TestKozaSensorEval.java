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
 * TestCase para probar la clase SensorEval
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


public class TestKozaSensorEval
{
	private final static Logger logger = Logger.getLogger(TestKozaSensorEval.class.getName());
	
	//private final static int NumeroSumandos = 9;

	private final static String VoutName = "v(3)t";
	
	//Requisitos
	private static final int puntos = 21;
	private static final double TempInicial = 0.0;
	private static final double TempFinal = 100;
	private static final double Vinicial = 0;
	private static final double Vfinal = 10;
	
	private static double [][] v;
	private static double [] t;
	
	private static double expected[] = { 0.0, 1.89, 105.0, 55.5 };
	private static int expectedHits[] = { 21, 21, 0, 10 };
	private static boolean goal[] = { true, true, false, false };
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

		t = new double[puntos];
		
		for(int i = 0; i < puntos; i++)
		{
			v[0][i] = Vinicial + (Vfinal - Vinicial)/(puntos - 1) * i;
			v[1][i] = Vinicial + (Vfinal - Vinicial)/(puntos - 1) * i + 0.09;
			v[2][i] = Vinicial + (Vfinal - Vinicial)/(puntos - 1) * i + 0.5;
			v[3][i] = Vinicial + (Vfinal - Vinicial)/(puntos - 1) * i + (i < puntos/2 ? 0.05 : 0.5);
			t[i] = TempInicial + (TempFinal - TempInicial)/(puntos - 1) * i;
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
		SensorEval eval = new KozaSensorEval(null);
		
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
			assertEquals(goal[ind], evalRes.isFinished());
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
		
		for(int i = 0; i < t.length; i++)
		{
			logger.info("i:" + i);
			Vout.putMP(t[i], v[ind][i], 0);
		}

		signals.put(Vout.getNombre(), Vout);
		
		logger.info("Señales:" + signals.size());
		
		return signals;
	}
}
