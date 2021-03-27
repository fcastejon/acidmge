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
 * Clase abstracta OperadorInicializacion
 *  
 */

package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public abstract class OperadorInicializacion 
{
	private final static Logger logger = Logger.getLogger(OperadorInicializacion.class.getName());

	//protected int PopulationInitializationType;
	
	//Valores para obtener estadísticas
	protected int tot_ini;
	
	protected RandomGenerator randomGenerator;
	
	//Constructor
	public OperadorInicializacion(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		//this.PopulationInitializationType = geproperties.getPopulationInitializationType();
		this.randomGenerator = randomGenerator;
		
		//inicialización de la variable estadística
		tot_ini = 0;
	}
	
	//Constructor
	public OperadorInicializacion(int PopulationInitializationType, 
			RandomGenerator randomGenerator)
	{
		//this.PopulationInitializationType = PopulationInitializationType;
		this.randomGenerator = randomGenerator;
		
		//inicialización de la variable estadística
		tot_ini = 0;
	}
		
	public abstract Genotipo creaGenotipo();
	
	public abstract void stat();
}
