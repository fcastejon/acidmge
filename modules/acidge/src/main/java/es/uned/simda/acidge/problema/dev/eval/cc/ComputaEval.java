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
package es.uned.simda.acidge.problema.dev.eval.cc;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.DevEval;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.Linearize;
import es.uned.simda.acidge.spice.SigItem;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.ge.GEProperties;


public abstract class ComputaEval extends DevEval 
{
	private final static Logger logger = Logger.getLogger(ComputaEval.class.getName());

	//private final static int NumeroSumandos = 1;
	
	protected final static String VoutName = "v(3)t";
	
	protected final static double VTRAN = 0;		//No utilizado
	//private final static double Ke = 0;		//Margen de tolerancia de las funciones de ponderación
	
	//Requisitos
	protected final static int puntos = 21;
	//public final static double TKOZA = 1e-2;		//Porcentaje
	public final static double TKOZAFACTOR1 = 1.0;
	public final static double TKOZAFACTOR2 = 10.0;
	public double Vinicial;
	public double Vfinal;
	public final static double PRECISION = 1e-12;			//Menor que este valor se considera cero
	
	public static double tkozamin;
	protected static double [] vi;
	protected static double [] vo;
	protected static double [] error;
	protected double UmbralError;
	
	protected static boolean inicializado = false;
	protected static Linearize linearize;
	
	//Para cálculo en función de clase hija
	protected double fit;
	protected double canFit;
	protected int hits;

	public ComputaEval(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("ComputaEval");
		
		UmbralError = getUmbralError();
		Vinicial = getVinicial();
		Vfinal = getVfinal();

		if((UmbralError > 1) || (UmbralError < 0))
			throw new AssertionError("UmbralError no valido:" + UmbralError);
		
		calculaVectores();
	}

	abstract double getUmbralError();
	abstract double getVinicial();
	abstract double getVfinal();

	public void calculaVectores()
	{
		if(inicializado)
			return;

		logger.info("calculaVectores");
		
		logger.finer("Vinicial:" + Vinicial + ":Vfinal:" + Vfinal);
		
		inicializado = true;
		
		vi = new double[puntos];
		vo = new double[puntos];
		error = new double[puntos];;
		
		tkozamin = Double.MAX_VALUE;
		
		for(int j = 0; j < puntos; j++)
		{
			vi[j] = ajusta(Vinicial + (Vfinal - Vinicial)/(puntos - 1) * j, 3);		//ATENCION deben ser 3 decimales!!
			
			vo[j] = funcion(vi[j]);
			
			error[j] = Math.abs(vo[j] * UmbralError);			//umbral para este valor
			
			//Determinamos el menor umbral distinto de cero, se usará como valor umbral cuando vo valga cero
			//Se considera cero si es menor que precisión
			if((error[j] > PRECISION) && (error[j] < tkozamin))
				tkozamin = error[j];
			
			logger.finer("vi:" + Double.toString(vi[j]) + 
					" vo:" + Double.toString(vo[j]) + "error" + error[j]);
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
		}
		
		linearize = new Linearize(puntos, vi);
	}
	
	public static void reset()
	{
		inicializado = false;
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
		
		fit = 0;
		canFit = 0;
		hits = 0;
		double abs;
		double medida;
		Signal sig = signals.get(VoutName);
		
		if(sig.getSize() != puntos)
		{
			//try 
			{
				sig = linearize.linearize(sig);
			}
			/*
			catch(NoSuchElementException e)
			{
				logger.severe("NoSuchElementException");
				throw new InviableException("NoSuchElementException");
			}	
			*/
		}
		
		for(int j = 0; j < puntos; j++)
		{
			logger.fine("vi[" + j+ "]=" + vi[j]);
			//medida = sig.getSigItem(v[j]).getMagnitud();
			SigItem item =sig.getSigItem(vi[j]);
			if(item == null)
				throw new InviableException("No se puede procesar la salida");
			
			medida = item.getMagnitud();
			
			abs = Math.abs(medida - vo[j]);
			
			calculaFitnessHitsPunto(abs, j);
			
			/*
			if(abs <= error[j])
				hits++;
			
			fit += fitnessPunto(abs, j);
			canFit += abs;
			*/
			
			medidas += medida + ";";
		}
		
		/* No necesario en este caso
		funAdaptacion.add(fit, Ke);				
		double fitness = funAdaptacion.getFitness();
		canonicalFitness = funAdaptacion.getCanonicalFitness();		//Almacena para devolver posteriormente
		*/
		
		canFit /= puntos;
		
		EvalRes evalRes = new KozaEvalRes(fit, canFit, hits, puntos); 
		
		//No necesario en este problema
		//logger.info(funAdaptacion.toString());
		//logger.info("fitness: " + fitness + " fitnessCanonica:" + canonicalFitness);
		logger.info(evalRes.toString());
		
		//medidas += ";" + fitness + ";" + canonicalFitness;
		
		logger.info(medidas);
		
		return evalRes;
	}
	
	abstract void calculaFitnessHitsPunto(double abs, int j);
	abstract double funcion(double vi);
}

