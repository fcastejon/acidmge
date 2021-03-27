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
import es.uned.simda.acidge.problema.dev.eval.DevEval;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.Signal;


public class TestKozaGaussianEval
{
	private final static Logger logger = Logger.getLogger(TestKozaGaussianEval.class.getName());
	
	//private final static int NumeroSumandos = 9;

	private final static String IoutName = "v2#brancht";
	
	//Requisitos
	private final static int puntos = 101;
	public final static double Ipico = 80e-9;
	public final static double TKOZA = 5e-9;
	public final static double TKOZAFACTOR1 = 1e6;
	public final static double TKOZAFACTOR2 = 1e7;
	private final static double Vinicial = 2;
	private final static double Vfinal = 3;
	private final static double Vmed = 2.5;
	private final static double Vdes = 0.1;
	
	private static double [][] i;
	private static double [] v;
	
	private static double expected[] = { 0.0, 0.2525, 19.715099, 5.225 };
	private static int expectedHits[] = { 101, 101, 54, 50 };
	private static final int tests = 4;
	
	private static double acum = 0;
	
	@BeforeClass
	public static void beforeClass()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
		calculaVectores();
	}
	
	public static void calculaVectores()
	{
		i = new double[tests][];
		
		for(int j = 0; j < tests; j++)
			i[j] = new double[puntos];

		v = new double[puntos];
		
		acum = 0;
		
		for(int j = 0; j < puntos; j++)
		{
			//v[j] = Vinicial + (Vfinal - Vinicial)/(puntos - 1) * j;
			v[j] = GaussianEval.ajusta(Vinicial + (Vfinal - Vinicial)/(puntos - 1) * j, 2);
			i[0][j] = Ipico *
			//(Vdes*Math.sqrt(2*Math.PI)) * 
				Math.exp(-((v[j] - Vmed) * (v[j] - Vmed))/(2*Vdes*Vdes));
			
			acum += i[0][j];
			
			i[1][j] = i[0][j] + TKOZA/2;
			i[2][j] = 0;
			i[3][j] = i[0][j] + (j < puntos/2 ? TKOZA/2 : TKOZA*2);
		}
		
		logger.info("calculaVectores: " + acum);
	}

	@Test
	public void testAcum()
	{
		double suma = acum / (Vdes*Math.sqrt(2*Math.PI)) / Ipico / puntos;
		
		logger.info("Suma normalizada:" + suma);
		
		assertEquals(1.0, suma, 1e-2);
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
		DevEval eval = new KozaGaussianEval(null);
		
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
		Signal Vout = new Signal(IoutName);
		
		for(int j = 0; j < v.length; j++)
		{
			//logger.info("i:" + j);
			Vout.putMP(v[j], i[ind][j], 0);
		}

		signals.put(Vout.getNombre(), Vout);
		
		logger.info("Señales:" + signals.size());
		
		return signals;
	}
}
