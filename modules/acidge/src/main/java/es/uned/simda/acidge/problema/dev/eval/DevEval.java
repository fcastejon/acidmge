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
 * Superclase DevEval
 * 
 * Define como debe ser un evaluador de dispositivos
 */

package es.uned.simda.acidge.problema.dev.eval;

//import java.io.File;
import java.util.HashMap;
import java.util.List;
//import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.ge.GEProperties;

public abstract class DevEval 
{
	protected GEProperties geproperties;
	
	public DevEval(GEProperties geproperties)
	{
		this.geproperties = geproperties;
	}
	
	abstract public EvalRes evalua(List<HashMap<String, Signal>> listaSignals) throws InviableException;

	//FIXME: paso de parámetros dificil de hacer de manera más limpia
	abstract public double getVtran();
	abstract public boolean getTempSweepIndex();
	
	//Función de utilidad
	public static double ajusta(double valor, int decimales)
	{
		double d = Math.pow(10,decimales);
		return Math.round(valor * d)/d;
	}
}
