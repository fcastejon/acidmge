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
 * Clase SteadyStateAge
 * 
 *  Implementa el operador de reemplazo de estado estacionario
 *  
 *  La nueva población es de tamaño GenerationalGap y se sustituye a este tamaño de la 
 *  población anterior
 *  
 *  Variante reemplazando a los genotipos más antiguos
 */

package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Poblacion;

public class SteadyStateFitness extends SteadyState 
{
	private final static Logger logger = Logger.getLogger(SteadyStateFitness.class.getName());
	
	public SteadyStateFitness(GEProperties geproperties)
	{
		super(geproperties); 
		
		logger.info("Reemplazo SteadyState por fitness");
	}
	
	public SteadyStateFitness(int SurvivorSelectionType, int IndividualsNumber,
			int Elitism, int GenerationalGap) 
	{
		super(SurvivorSelectionType, IndividualsNumber, Elitism, GenerationalGap);

		logger.info("Reemplazo SteadyState por fitness");
	}

	@Override
	public Poblacion nuevaPoblacion(Poblacion poblacion, Poblacion offspring) 
	{	
		//steady-state con reemplazo por adaptación
		//Borra los GenerationalGap fenotipos con peor adaptación 
		//añade los hijos y finalmente ordena por conveniencia del programa
		
		//Borramos los GenerationalGap peores individuos
		poblacion.trim(IndividualsNumber - GenerationalGap);
		poblacion.addPoblacion(offspring);
		poblacion.ordena();
	
		return poblacion;
	}
}
