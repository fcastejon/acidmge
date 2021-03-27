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
 * IncrementalVoltRefEval
 * 
 * Evaluates a voltage reference circuit using incremental evolution
 * 
 * Fitness function has two parts: first only hits for 25º are used 
 * 
 * second: the same as Koza's when all hits for 25º are accomplished
 *  
 */
package es.uned.simda.acidge.problema.dev.eval.kozamat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.SigItem;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.ge.GEProperties;

public class IncrementalVoltRefEval extends KozaVoltRefEval
{
	private final static Logger logger = Logger.getLogger(IncrementalVoltRefEval.class.getName());
	
	//public final static int vt25start = 21; 
	//public final static int vt25end = 42;
	public final static int puntosPorFase = 21;
	public final static int fases = 5;
	public final static double offset[] = { 20000, 15000, 10000, 5000, 0 };

	public IncrementalVoltRefEval(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("ParsimonySensorEval");
	}
	
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
		
		double medida;
		double fit = 0;
		//double fit1 = 0;
		double abs;
		int hits = 0;
		//int hits1 = 0;
		//int total = 0;
		int fase = 0;
		
		Signal signal = signals.get(VoutName);

		//Iterator<SigItem> iterator = signals.get(VoutName).iterator();
		
		//while(iterator.hasNext())
		int j;
		
		for(j = 1; j <= puntos; j++)
		{
			//logger.info("v[" + j+ "]=" + v[j]);
			//medida = sig.getSigItem(v[j]).getMagnitud();
			SigItem item =signal.getSigItem(j-1);			
			
			//total++;
						
			//SigItem item =iterator.next();
			
			if(item == null)
				throw new InviableException("No se puede procesar la salida");
			
			medida = item.getMagnitud();
			
			abs = Math.abs(medida - Vobj);
			
			if(abs <= TKOZA)
				hits++;
			
			fit += fitnessPunto(abs);
						
			if(j % puntosPorFase == 0)
			{
				if(hits != j)
				{
					fit += offset[fase];
					//logger.info("salgo total:" + j + " :hits:" + hits + " :fase:" + fase);
					break;
				}
				else
				{
					fase++;
					//logger.info("sigo total:" + j + " :hits:" + hits + " :fase:" + fase);
				}
			}
		}
		
		//logger.info("total:" + j + " :fase:" + fase);
		
		EvalRes evalRes = new KozaEvalRes(fit, fit, hits, puntos); 
		
		logger.info(evalRes.toString());
		
		return evalRes;
	}
	
}
