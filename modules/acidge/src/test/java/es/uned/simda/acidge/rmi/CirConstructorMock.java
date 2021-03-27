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
 * Clase CirConstructorMock
 * 
 * Mock for testing
 */

package es.uned.simda.acidge.rmi;

import java.util.logging.Logger;

import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.CirConstructor;


public class CirConstructorMock implements CirConstructor 
{
	private final static Logger logger = Logger.getLogger(CirConstructorMock.class.getName());
	
	public CirConstructorMock()
	{	

	}
	
	public void setVtran(double vtran) {  }
	public int getSimulaciones() { return 0; }

	@Override
	public boolean creaCircuitoExp(String exp) throws InviableException {
		return false;
	}

	@Override
	public void adaptaCircuitoAnalisis(int analisis, String netfile) throws InviableException 
	{
	}

	@Override
	public int getNivelInviable() 
	{
		return 0;
	}

	@Override
	public String getCircuit(int analisis) throws InviableException 
	{
		return null;
	}
}
