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
 * Clase Generacional
 * 
 *  Implementa el operador de reemplazo Generacional
 *  
 *  Sustituye por la nueva población y añade los individuos si hay elitismo
 */

package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Poblacion;

public class Generacional extends OperadorReemplazo 
{
	private final static Logger logger = Logger.getLogger(Generacional.class.getName());

	public Generacional(GEProperties geproperties)
	{
		super(geproperties); 
		
		logger.info("Reemplazo Generacional");
	}

	public Generacional(int SurvivorSelectionType, int IndividualsNumber,
			int Elitism, int GenerationalGap) 
	{
		super(SurvivorSelectionType, IndividualsNumber, Elitism, GenerationalGap);
		
		logger.info("Reemplazo Generacional");
	}

	@Override
	public int getMatingPoolSize() 
	{
		return IndividualsNumber - Elitism;
	}
	
	@Override
	public Poblacion nuevaPoblacion(Poblacion poblacion, Poblacion offspring) 
	{
		//En caso de elitismo, se añden los N mejores individuos de la generación anterior
		if(Elitism != 0)
		{
			//Ya se ha creado el número justo de nuevos individuos
			//offspring.trim(individualsNumber - 1);
			
			//Nos quedamos con los N mejores individuos y los añadimos a la nueva población
			poblacion.trim(Elitism);
			offspring.addPoblacion(poblacion);
			offspring.ordena();
		}
 
		return offspring;
	}
}
