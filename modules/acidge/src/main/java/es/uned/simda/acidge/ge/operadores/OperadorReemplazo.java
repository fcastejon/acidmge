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
 * Clase abstracta OperadorReemplazo
 *
 * Implementa un operador de selección de supervivientes
 */

package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Poblacion;
import es.uned.simda.acidge.ge.random.RandomGenerator;

public abstract class OperadorReemplazo 
{
	private final static Logger logger = Logger.getLogger(OperadorReemplazo.class.getName());

	//protected int SurvivorSelectionType;
	protected int IndividualsNumber;
	protected int Elitism;
	protected int GenerationalGap;
	
	//Constructor
	public OperadorReemplazo(GEProperties geproperties)
	{
		IndividualsNumber = geproperties.getIndividualsNumber();
		Elitism = geproperties.getElitism();
		GenerationalGap = geproperties.getGenerationalGap();
		
		if((Elitism < 0) || (Elitism > IndividualsNumber))
			throw new AssertionError("Elitism parameter value not supported:" + 
			Elitism);
		
		if((IndividualsNumber < 0) || (IndividualsNumber % 2 != 0))
			throw new AssertionError("IndividualsNumber parameter value not supported: " + 
					IndividualsNumber);
		
		if((GenerationalGap < 0) || (GenerationalGap >= IndividualsNumber))
			throw new AssertionError("GenerationalGap parameter value not supported: " + 
					GenerationalGap);
	}
	
	//Constructor
	public OperadorReemplazo(int SurvivorSelectionType, int IndividualsNumber, int Elitism, int GenerationalGap)
	{
		this.IndividualsNumber = IndividualsNumber;
		this.Elitism = Elitism;	
		this.GenerationalGap = GenerationalGap;
		
		if((Elitism < 0) || (Elitism > IndividualsNumber))
			throw new AssertionError("Elitism parameter value not supported:" + 
			Elitism);
		
		if((IndividualsNumber < 0) || (IndividualsNumber % 2 != 0))
			throw new AssertionError("IndividualsNumber parameter value not supported: " + 
					IndividualsNumber);
		
		if((GenerationalGap < 0) || (GenerationalGap >= IndividualsNumber))
			throw new AssertionError("GenerationalGap parameter value not supported: " + 
					GenerationalGap);
	}
		
	public abstract int getMatingPoolSize();
	
	public abstract Poblacion nuevaPoblacion(Poblacion poblacion, Poblacion offspring);
	
	public void stat() {};
}
