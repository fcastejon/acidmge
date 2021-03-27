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
 * Clase SteadyState
 * 
 *  Implementa el operador de reemplazo de estado estacionario
 *  
 *  La nueva población es de tamaño GenerationalGap y se sustituye a este tamaño de la 
 *  población anterior
 *  
 *  Tiene dos sabores: por edad y por adaptación
 */

package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;

public abstract class SteadyState extends OperadorReemplazo 
{
	private final static Logger logger = Logger.getLogger(SteadyState.class.getName());
	
	public SteadyState(GEProperties geproperties)
	{
		super(geproperties); 
		
		check();
		
		logger.info("Reemplazo SteadyState");
	}
	
	public SteadyState(int SurvivorSelectionType, int IndividualsNumber,
			int Elitism, int GenerationalGap) 
	{
		super(SurvivorSelectionType, IndividualsNumber, Elitism, GenerationalGap);
		
		check();
		
		logger.info("Reemplazo SteadyState");
	}
	
	private void check()
	{
		//Caso de Steady State con sus dos variantes
		if(IndividualsNumber < GenerationalGap)
			throw new AssertionError(
					"Valor de parámetro GenerationalGap debe ser menor que IndividualsNumber:" + 
					GenerationalGap);
			
		if(GenerationalGap == 0)
			throw new AssertionError(
					"Valor de parámetro GenerationalGap debe ser mayor que 0 :" + 
					GenerationalGap);
		
		if(GenerationalGap % 2 != 0)
			throw new AssertionError(
					"Valor de parámetro GenerationalGap debe ser par :" + 
					GenerationalGap);
		
		if(IndividualsNumber % GenerationalGap != 0)
			throw new AssertionError("Valor de parámetro GenerationalGap debe ser divisor de IndividualsNumber :" + 
					GenerationalGap);
	}

	@Override
	public int getMatingPoolSize() 
	{
		return GenerationalGap;
	} 
}
