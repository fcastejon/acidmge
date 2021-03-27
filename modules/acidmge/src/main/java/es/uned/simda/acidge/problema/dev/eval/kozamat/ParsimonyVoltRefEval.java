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
 * ParsimonyVoltRefEval
 * 
 * Evaluates a voltage reference circuit including parsimony
 * 
 * Fitness function is Koza's when not all hits are accomplished
 * 
 * When all hits are accomplished, fitness is function of component number 
 */
package es.uned.simda.acidge.problema.dev.eval.kozamat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.SigItem;
import es.uned.simda.acidge.spice.Signal;

public class ParsimonyVoltRefEval extends KozaVoltRefEval 
{
	private final static Logger logger = Logger.getLogger(ParsimonyGaussianEval.class.getName());
	private final static String NAMECOMPONENTCOUNT = "ComponentCount";
	public final static int MAXCOMPONENT = 168;  //42 componentes/chromosome * 4 wrapping

	public ParsimonyVoltRefEval(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("ParsimonyGaussianEval");
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
		
		assert(total == puntos);

		//Though componentCount is an integer, storing as a double saves a later casting
		double componentCount = signals.get(NAMECOMPONENTCOUNT).getSigItem(0).getMagnitud();
		
		if(componentCount > MAXCOMPONENT)
			componentCount = MAXCOMPONENT;
				
		//medidas = componentCount + ";" + medidas;
		
		//If all hits are accomplished then change to parsimony part
		if(hits == puntos)
		{
			fit = componentCount * TKOZA * TKOZAFACTOR2 / MAXCOMPONENT;
		}
		
		EvalRes evalRes = new KozaEvalRes(fit, fit, hits, puntos); 
		
		logger.info(evalRes.toString());
		
		//logger.info(medidas);
		
		return evalRes;
	}
	

}
