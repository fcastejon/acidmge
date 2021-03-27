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
 * TestCase para probar la clase SquareRootEval
 * 
 */
package es.uned.simda.acidge.problema.dev.eval.cc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

//import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.AfterClass;
//import org.junit.Before;
import org.junit.Test;

import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.DevEval;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.ge.GEProperties;


public class TestSquareRootEvalUmbral
{
	private final static Logger logger = Logger.getLogger(TestSquareRootEvalUmbral.class.getName());
	
	//private final static int NumeroSumandos = 9;

	private final static String VoutName = "v(3)t";
	
	//Requisitos
	private final static int puntos = 21;
	public final static double TKOZA = 1e-2;		//Porcentaje
	public final static double TKOZAFACTOR1 = 1.0;
	public final static double TKOZAFACTOR2 = 10.0;
	private final static double Vinicial = 0;
	private final static double Vfinal = 500e-3;
	public final static double PRECISION = 1e-12;			//Menor que este valor se considera cero
	public final static int iMax = 20;

	public static double tkozamin;	
	private static double [][] vo;
	private static double [] vi;
	private static double [] error;

	private static double expected[] = { 0.0, 7.42462e-2, 1.47007e-1, 2.9698484, 1.414213562 };
	private static double expectedCan[] = { 0.0, 3.53553e-3, 7.00036e-3, 1.41421e-2, 6.734350297e-3 };
	private static int expectedHits[] = { 21, 21, 21, 0, 11 };
	private static final int tests = 5;
		
	@BeforeClass
	public static void beforeClass()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
		calculaVectores();
	}
	
	@AfterClass
	public static void afterClass()
	{
		ComputaEval.reset();
	}
	
	public static void calculaVectores()
	{
		vo = new double[tests][];
		
		for(int j = 0; j < tests; j++)
			vo[j] = new double[puntos];

		vi = new double[puntos];
		error = new double[puntos];
		
		tkozamin = Double.MAX_VALUE;
		
		for(int j = 0; j < puntos; j++)
		{
			vi[j] = DevEval.ajusta(Vinicial + (Vfinal - Vinicial)/(puntos - 1) * j, 3);		//DEben ser 3 decimales
			logger.info("vi[" + j + "]=" + vi[j]);
			
			vo[0][j] = Math.sqrt(vi[j]);
			error[j] = Math.abs(vo[0][j] * TKOZA);			//umbral para este valor
			
			//Determinamos el menor umbral distinto de cero, se usará como valor umbral cuando vo valga cero
			//Se considera cero si es menor que precisión
			if((error[j] > PRECISION) && (error[j] < tkozamin))
				tkozamin = error[j];
		}
		
		//Corrección de valores cero de error
		for(int j = 0; j < puntos; j++)
		{
			//Se corrige si vale cero (menor que precisión)
			if(error[j] < PRECISION)
			{
				logger.finer("error[" + j + "]=" + error[j] + " corregido:" + tkozamin);
				error[j] = tkozamin;
			}
			
			vo[1][j] = vo[0][j] + error[iMax] / 2;
			vo[2][j] = vo[0][j] + error[iMax] * 0.99;
			vo[3][j] = vo[0][j] + error[iMax] * 2;
			vo[4][j] = vo[0][j] + (j>10 ? 1 : 0) * 2 * error[iMax];
			
			logger.finer("vi:" + Double.toString(vi[j]) + 
					" vo:" + Double.toString(vo[0][j]));
		}
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
		GEProperties geproperties = new GEProperties();
		geproperties.setUmbralError(TKOZA);
		
		DevEval eval = new SquareRootEvalUmbral(geproperties);
		
		HashMap<String, Signal> signals = construye(ind);
		List<HashMap<String, Signal>> listaSignals = new ArrayList<HashMap<String, Signal>>();
		listaSignals.add(signals);
		
		try
		{
			KozaEvalRes evalRes = (KozaEvalRes) eval.evalua(listaSignals);
			
			logger.info(evalRes.toString());
			assertEquals(expected[ind], evalRes.getFitness(), 1e-6);
			assertEquals(expectedCan[ind], evalRes.getCanonicalFitness(), 1e-6);
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
		
		logger.finer("vi.length:" + vi.length);
		for(int j = 0; j < vi.length; j++)
		{
			logger.finer("i:" + j + ":vi["+j+"]:" + vi[j] + ":vo["+ ind + "]["+j+"]:" + vo[ind][j]);
			Vout.putMP(vi[j], vo[ind][j], 0);
		}

		signals.put(Vout.getNombre(), Vout);
		
		logger.info("Señales:" + signals.size());
		
		return signals;
	}
}
