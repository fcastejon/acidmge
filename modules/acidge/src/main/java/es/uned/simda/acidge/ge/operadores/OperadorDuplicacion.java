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
 * Clase abstracta OperadorDuplicacion
 *  
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public abstract class OperadorDuplicacion 
{
	private final static Logger logger = Logger.getLogger(OperadorDuplicacion.class.getName());

	//protected int DuplicationType;
	protected double DuplicationRate;
	
	//Valores para obtener estadísticas
	protected int num_dup;
	protected int tot_dup;
	
	protected RandomGenerator randomGenerator;
	
	//Constructor
	public OperadorDuplicacion(GEProperties geproperties, RandomGenerator randomGenerator)

	{
		//this.DuplicationType = geproperties.getDuplicationType();
		this.DuplicationRate = geproperties.getDuplicationRate();
		this.randomGenerator = randomGenerator;
		
		//inicialización de las variables estadísticas
		num_dup = 0;
		tot_dup = 0;
	}
	
	//Constructor
	public OperadorDuplicacion(int DuplicationType, 
			double DuplicationRate, RandomGenerator randomGenerator)
	{
		//this.DuplicationType = DuplicationType;
		this.DuplicationRate = DuplicationRate;
		this.randomGenerator = randomGenerator;
		
		//inicialización de las variables estadísticas
		num_dup = 0;
		tot_dup = 0;
	}
		
	public abstract void duplica(Genotipo genotipo);	
	
	public void stat()
	{
		logger.info("Duplicaciones: " + num_dup + "/" + 
				tot_dup + " = " + (double) num_dup/tot_dup);
	}
	
	public double getStat()
	{
		return (double) num_dup/tot_dup;
	}
}
