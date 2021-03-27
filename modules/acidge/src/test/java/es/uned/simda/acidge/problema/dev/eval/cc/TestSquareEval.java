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
 * TestCase para probar la clase SquareEval
 * 
 */
package es.uned.simda.acidge.problema.dev.eval.cc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

//import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
//import org.junit.Before;
import org.junit.Test;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.DevEval;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.ge.GEProperties;


public class TestSquareEval
{
	private final static Logger logger = Logger.getLogger(TestSquareEval.class.getName());
	
	//private final static int NumeroSumandos = 9;

	private final static String VoutName = "v(3)t";
	
	//Requisitos
	private final static int puntos = 21;
	public final static double TKOZA = 1e-2;		//Porcentaje
	public final static double TKOZAFACTOR1 = 1.0;
	public final static double TKOZAFACTOR2 = 10.0;
	private final static double Vinicial = -250e-3;
	private final static double Vfinal = 250e-3;
	public final static double PRECISION = 1e-12;			//Menor que este valor se considera cero

	public static double tkozamin;	
	private static double [][] vo;
	private static double [] vi;
	private static double [] error;

	private static double expected[] = { 0.0, 0.00240938, 0.00477056, 0.096375, 0.0481250 };
	private static double expectedCan[] = { 0.0, 0.0001147321, 0.00022717, 0.00045893, 0.000229167 };
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
			
			vo[0][j] = vi[j] * vi[j];
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
			
			logger.finer("vi:" + Double.toString(vi[j]) + 
					" vo:" + Double.toString(vo[0][j]));
									
			vo[1][j] = vo[0][j] + error[j] / 2;
			vo[2][j] = vo[0][j] + error[j] * 0.99;
			vo[3][j] = vo[0][j] + error[j] * 2;
			vo[4][j] = vo[0][j] + (j>10 ? 1 : 0) * 2 * error[j];
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
		geproperties.setUmbralError(-1);
		
		DevEval eval = new SquareEval(geproperties);
		
		HashMap<String, Signal> signals = construye(ind);
		List<HashMap<String, Signal>> listaSignals = new ArrayList<HashMap<String, Signal>>();
		listaSignals.add(signals);
		
		try
		{
			KozaEvalRes evalRes = (KozaEvalRes) eval.evalua(listaSignals);
			
			logger.info(evalRes.toString());
			assertEquals(expected[ind], evalRes.getFitness(), 1e-8);
			assertEquals(expectedCan[ind], evalRes.getCanonicalFitness(), 1e-8);
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
