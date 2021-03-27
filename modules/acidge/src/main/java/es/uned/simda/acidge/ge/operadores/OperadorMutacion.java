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
 * Clase abstracta OperadorMutacion
 *  
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;
import es.uned.simda.acidge.ge.random.RandomJava;

public abstract class OperadorMutacion 
{
	private final static Logger logger = Logger.getLogger(OperadorMutacion.class.getName());

	//protected int MutationType;
	protected double MutationRate;
	
	//Valores para obtener estadísticas 
	protected int num_muta;
	protected int tot_muta;
	
	protected RandomGenerator randomGenerator;
	
	//Constructor
	public OperadorMutacion(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		//this.MutationType = geproperties.getMutationType();
		this.MutationRate = geproperties.getMutationRate();
		this.randomGenerator = randomGenerator;
				
		//inicialización de las variables estadísticas
		num_muta = 0;
		tot_muta = 0;
	}
	
	//Constructor
	public OperadorMutacion(int MutationType, 
			double MutationRate, RandomGenerator randomGenerator)
	{
		//this.MutationType = MutationType;
		this.MutationRate = MutationRate;
		this.randomGenerator = randomGenerator;
				
		//inicialización de las variables estadísticas
		num_muta = 0;
		tot_muta = 0;
	}

	abstract public void muta(Genotipo genotipo);	
	
	public void stat()
	{
		logger.info("Mutaciones: " + num_muta + "/" + 
			tot_muta + " = " + (double) num_muta/tot_muta);
	}
	
	public double getStat()
	{
		return (double) num_muta/tot_muta;
	}
}
