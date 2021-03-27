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
 * CubeRootEvalUmbral
 * 
 * Evaluates a cube root function
 * 
 */
package es.uned.simda.acidge.problema.dev.eval.cc;

import java.util.logging.Logger;
import es.uned.simda.acidge.ge.GEProperties;


public class CubeRootEvalUmbral extends ComputaEval 
{
	private final static Logger logger = Logger.getLogger(CubeRootEvalUmbral.class.getName());
	
	private final static double VinicialDef = -250e-3;
	private final static double VfinalDef = 250e-3;
	private final static double TERCIO = (double) 1/3;
	private final static int iMax = 20;

	public CubeRootEvalUmbral(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("CubeRootEvalUmbral:" + TERCIO + ":" + error[iMax]);
	}
	
	double getVinicial() { return VinicialDef; }
	double getVfinal() { return VfinalDef; }
	double getUmbralError() { return geproperties.getUmbralError(); }
	
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
	
	/*
	@Override
	double fitnessPunto(double abs, int j)
	{
		double ret;
		
		if(abs <= error[j])
			ret = abs * TKOZAFACTOR1;
		else
			ret = abs * TKOZAFACTOR2;

		return ret;
	}
	*/
	
	@Override
	double funcion(double vi)
	{
		return Math.signum(vi) * Math.pow(Math.abs(vi), TERCIO);
	}
}
