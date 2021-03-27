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
 * SensorEval
 * 
 * Evalua un circuito sensor de temperatura 
 * 
 * Común a Koza y Mattiussi
 * 
 */
package es.uned.simda.acidge.problema.dev.eval.kozamat;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.DevEval;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.SigItem;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.ge.GEProperties;


public abstract class SensorEval extends DevEval 
{
	private final static Logger logger = Logger.getLogger(SensorEval.class.getName());

	//private final static int NumeroSumandos = 1;
	
	public final static String VoutName = "v(3)t";
	
	private final static double VTRAN = 0;		//No utilizado
	//private final static double Ke = 0;		//Margen de tolerancia de las funciones de ponderación
	
	//Requisitos
	public final static int puntos = 21;	
	public final static double TKOZA = 0.1;
	public final static double TKOZAFACTOR1 = 1.0;
	public final static double TKOZAFACTOR2 = 10.0;
	private final static double TempInicial = 0.0;
	private final static double TempFinal = 100;
	private final static double Vinicial = 0;
	private final static double Vfinal = 10;
	
	protected static double [] v;
	protected static double [] t;
	
	private static boolean inicializado = false;

	public SensorEval(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("SensorEval");
		
		calculaVectores();
	}
	
	public static void calculaVectores()
	{
		if(inicializado)
			return;
		
		inicializado = true;
		
		v = new double[puntos];
		t = new double[puntos];
		
		for(int i = 0; i < puntos; i++)
		{
			v[i] = Vinicial + (Vfinal - Vinicial)/(puntos - 1) * i;
			t[i] = TempInicial + (TempFinal - TempInicial)/(puntos - 1) * i;
		}
		
		logger.info("calculaVectores");
	}
	
	@Override
	public double getVtran() { return VTRAN; }
	
	@Override
	public boolean getTempSweepIndex() { return false; };
	
	@Override
	public EvalRes evalua(List<HashMap<String, Signal>> listaSignals) throws InviableException
	{
		HashMap<String, Signal> signals = listaSignals.get(0);
		
		//En caso de no poder extraer los datos del fichero de salida
		//devolvemos una excepción de circuito inviable
		if(signals.get(VoutName) == null) 
			{
				logger.info("Error en ficheros de salida");
				logger.info("signals:" + Signal.signalsToString(signals));
				throw new InviableException("No se puede procesar la salida");
			}
		
		String medidas = "Medidas;";
		
		//funAdaptacion.reset();
		
		double medida;
		double fit = 0;
		double abs;
		int hits = 0;
		
		for(int i = 0; i < puntos; i++)
		{
			//medida = signals.get(VoutName).getSigItem(t[i]).getMagnitud();
			
			SigItem item = signals.get(VoutName).getSigItem(t[i]);
			
			if(item == null)
				throw new InviableException("No se puede procesar la salida");
			
			medida = item.getMagnitud();
			
			abs = Math.abs(medida - v[i]);
			
			if(abs <= TKOZA)
				hits++;
			
			fit += fitnessPunto(abs);
			
			medidas += medida + ";";
		}
		
		/* No necesario en este caso
		funAdaptacion.add(fit, Ke);				
		double fitness = funAdaptacion.getFitness();
		canonicalFitness = funAdaptacion.getCanonicalFitness();		//Almacena para devolver posteriormente
		*/
		EvalRes evalRes = new KozaEvalRes(fit, fit, hits, puntos); 
		
		//No necesario en este problema
		//logger.info(funAdaptacion.toString());
		//logger.info("fitness: " + fitness + " fitnessCanonica:" + canonicalFitness);
		logger.info(evalRes.toString());
		
		//medidas += ";" + fitness + ";" + canonicalFitness;
		
		logger.info(medidas);
		
		return evalRes;
	}
	
	abstract double fitnessPunto(double abs);

}
