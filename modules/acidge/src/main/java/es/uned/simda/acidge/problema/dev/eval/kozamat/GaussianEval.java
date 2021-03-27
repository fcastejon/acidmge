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
 * GaussianEval
 * 
 * Evalua un circuito de función gaussiana
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



public abstract class GaussianEval extends DevEval 
{
	private final static Logger logger = Logger.getLogger(GaussianEval.class.getName());

	//private final static int NumeroSumandos = 1;
	
	protected final static String IoutName = "v2#brancht";
	
	private final static double VTRAN = 0;		//No utilizado
	//private final static double Ke = 0;		//Margen de tolerancia de las funciones de ponderación
	
	//Requisitos
	protected final static int puntos = 101;
	public final static double Ipico = 80e-9;
	public final static double TKOZA = 5e-9;
	public final static double TKOZAFACTOR1 = 1e6;
	public final static double TKOZAFACTOR2 = 1e7;
	private final static double Vinicial = 2;
	private final static double Vfinal = 3;
	private final static double Vmed = 2.5;
	private final static double Vdes = 0.1;
	
	protected static double [] v;
	protected static double [] i;
	
	private static boolean inicializado = false;

	public GaussianEval(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("SensorEval");
		
		calculaVectores();
	}	
	
	public static void calculaVectores()
	{
		if(inicializado)
			return;

		logger.info("calculaVectores");
		
		inicializado = true;
		
		v = new double[puntos];
		i = new double[puntos];
		
		for(int j = 0; j < puntos; j++)
		{
			v[j] = ajusta(Vinicial + (Vfinal - Vinicial)/(puntos - 1) * j, 2);
			i[j] = Ipico *
			//(Vdes*Math.sqrt(2*Math.PI)) * 
				Math.exp(-((v[j] - Vmed) * (v[j] - Vmed))/(2*Vdes*Vdes));
			
			logger.finer(Double.toString(i[j]));
		}
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
		if(signals.get(IoutName) == null) 
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
		Signal sig = signals.get(IoutName);
		
		for(int j = 0; j < puntos; j++)
		{
			//logger.info("v[" + j+ "]=" + v[j]);
			//medida = sig.getSigItem(v[j]).getMagnitud();
			SigItem item =sig.getSigItem(v[j]);
			if(item == null)
				throw new InviableException("No se puede procesar la salida");
			
			medida = item.getMagnitud();
			
			abs = Math.abs(medida - i[j]);
			
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
