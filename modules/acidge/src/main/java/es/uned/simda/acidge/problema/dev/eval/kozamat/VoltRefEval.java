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
 * VoltRefEval
 * 
 * Evalua un circuito de referencia de voltaje
 * 
 *  Común a Koza y Mattiussi
 * 
 */
package es.uned.simda.acidge.problema.dev.eval.kozamat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.DevEval;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.SigItem;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.ge.GEProperties;

public abstract class VoltRefEval extends DevEval 
{
	private final static Logger logger = Logger.getLogger(VoltRefEval.class.getName());

	//private final static int NumeroSumandos = 1;
	
	//private final static String VoutName = "v(3)i";
	protected final static String VoutName = "v(2)i";
	
	private final static double VTRAN = 0;		//No utilizado
	//private final static double Ke = 0;		//Margen de tolerancia de las funciones de ponderación
	
	//Requisitos
	protected final static int puntos = 105; 
	protected final static double TKOZA = 0.02;
	protected final static double TKOZAFACTOR1 = 1.0;
	protected final static double TKOZAFACTOR2 = 10.0;
	/*
	private final static double TempInicial = 0.0;
	private final static double TempFinal = 100;
	private final static double Vinicial = 4.0;
	private final static double Vfinal = 6.0;
	*/
	protected final static double Vobj = 2.0;
	
	//private static double [] v;
	
	//private static boolean inicializado = false;

	public VoltRefEval(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("VoltRefEval");
	}
	
	@Override
	public double getVtran() { return VTRAN; }
	
	//Este caso es necesario pues el volcado tiene claves duplicadas
	@Override
	public boolean getTempSweepIndex() { return true; };
	
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
		
		//String medidas = "Medidas;";
		
		//funAdaptacion.reset();
		
		double medida;
		double fit = 0;
		double abs;
		int hits = 0;
		int total = 0;
		
		Iterator<SigItem> iterator = signals.get(VoutName).iterator();
		
		while(iterator.hasNext())
		{
			total++;
			//medida = iterator.next().getMagnitud();
						
			SigItem item =iterator.next();
			
			if(item == null)
				throw new InviableException("No se puede procesar la salida");
			
			medida = item.getMagnitud();
			
			abs = Math.abs(medida - Vobj);
			
			if(abs <= TKOZA)
				hits++;
			
			fit += fitnessPunto(abs);
			
			//logger.info("medida:" + medida + " abs:" + abs);
			//medidas += medida + ";";
		}
		
		logger.info("puntos:" + puntos + " total:" + total);
		
		assert(total == puntos);
		
		EvalRes evalRes = new KozaEvalRes(fit, fit, hits, puntos); 
		
		logger.info(evalRes.toString());
		
		//medidas += ";" + fitness + ";" + canonicalFitness;
		
		//logger.info(medidas);
		
		return evalRes;
	}
	
	abstract double fitnessPunto(double abs);
}
