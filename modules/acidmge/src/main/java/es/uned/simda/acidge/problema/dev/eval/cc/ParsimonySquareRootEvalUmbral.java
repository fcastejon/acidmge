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
 * ParsimonySquareRootEvalUmbral
 * 
 * Evaluates a square root function circuit including parsimony
 * 
 * Fitness function is Koza's when not all hits are accomplished
 * 
 * When all hits are accomplished, fitness is function of component number 
 */
package es.uned.simda.acidge.problema.dev.eval.cc;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.eval.KozaEvalRes;
import es.uned.simda.acidge.spice.SigItem;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.ge.GEProperties;

public class ParsimonySquareRootEvalUmbral extends ComputaEval 
{
	private final static Logger logger = Logger.getLogger(ParsimonySquareRootEvalUmbral.class.getName());
	private final static String NAMECOMPONENTCOUNT = "ComponentCount";
	public final static int MAXCOMPONENT = 168;  //42 componentes/chromosome * 4 wrapping
	
	
	private final static double VinicialDef = 0;
	private final static double VfinalDef = 500e-3;
	private final static int iMax = 20;

	public ParsimonySquareRootEvalUmbral(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("ParsimonySquareRootEvalUmbral:" + error[iMax]);
	}

	double getVinicial() { return VinicialDef; }
	double getVfinal() { return VfinalDef; }	
	double getUmbralError() { return geproperties.getUmbralError(); }
	
	
	@Override
	public EvalRes evalua(List<HashMap<String, Signal>> listaSignals) throws InviableException
	{
		HashMap<String, Signal> signals = listaSignals.get(0);
		
		//En caso de no poder extraer los datos del fichero de salida
		//devolvemos una excepción de circuito inviable
		if((signals.get(VoutName) == null) || (signals.get(NAMECOMPONENTCOUNT) == null))
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

			SigItem item =sig.getSigItem(vi[j]);
			if(item == null)
				throw new InviableException("No se puede procesar la salida");
			
			medida = item.getMagnitud();
			
			abs = Math.abs(medida - vo[j]);
			
			calculaFitnessHitsPunto(abs, j);
			
			medidas += medida + ";";
		}
		
		canFit /= puntos;
				
		//Though componentCount is an integer, storing as a double saves a later casting
		double componentCount = signals.get(NAMECOMPONENTCOUNT).getSigItem(0).getMagnitud();
		
		if(componentCount > MAXCOMPONENT)
			componentCount = MAXCOMPONENT;
		
		medidas = componentCount + ";" + medidas;
		
		//If all hits are accomplished then change to parsimony part
		if(hits == puntos)
		{
			fit = componentCount * error[iMax] * TKOZAFACTOR2 / MAXCOMPONENT;
		}
		
		EvalRes evalRes = new KozaEvalRes(fit, canFit, hits, puntos); 
		
		logger.info(evalRes.toString());
		
		logger.info(medidas);
		
		return evalRes;
	}
	

	//Actualiza fit, hits y canFit
	//Se ignora el punto y se utiliza siempre un valor fijo
	@Override
	void calculaFitnessHitsPunto(double abs, int j)
	{		
		if(abs <= error[iMax])
			hits++;

		canFit += abs;
		
		if(abs <= error[iMax])
			fit += abs * TKOZAFACTOR1;
		else
			fit += abs * TKOZAFACTOR2;
	}
	
	@Override
	double funcion(double vi)
	{
		return Math.sqrt(vi);
	}
}
