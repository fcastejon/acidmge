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
 * KozaSensorEval
 * 
 * Evalua un circuito sensor de temperatura según definición de Koza
 * 
 */
package es.uned.simda.acidge.problema.dev.eval.kozamat;

import java.util.logging.Logger;
import es.uned.simda.acidge.ge.GEProperties;

public class KozaSensorEval extends SensorEval 
{
	private final static Logger logger = Logger.getLogger(KozaSensorEval.class.getName());

	public KozaSensorEval(GEProperties geproperties) 
	{
		super(geproperties);
		logger.info("KozaSensorEval");
	}
	
	double fitnessPunto(double abs)
	{
		double ret;
		
		if(abs <= TKOZA)
		{
			ret = abs * TKOZAFACTOR1;
		
		}
		else
			ret = abs * TKOZAFACTOR2;

		return ret;
	}
}
