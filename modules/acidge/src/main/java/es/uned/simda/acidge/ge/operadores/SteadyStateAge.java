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

public class SteadyStateAge extends SteadyState 
{
	private final static Logger logger = Logger.getLogger(SteadyStateAge.class.getName());

	//private int generacionBorrar;
	//private int generacionesArranque;
	
	public SteadyStateAge(GEProperties geproperties)
	{
		super(geproperties); 
		
		logger.info("Reemplazo SteadyState por edad con Generational Gap " + GenerationalGap + 
				" y elitismo " + Elitism);
	}
	
	public SteadyStateAge(int SurvivorSelectionType, int IndividualsNumber,
			int Elitism, int GenerationalGap) 
	{
		super(SurvivorSelectionType, IndividualsNumber, Elitism, GenerationalGap);

		//Sólo para el caso de steady state con selección por edad 
		//generacionBorrar = 1;
		//generacionesArranque = IndividualsNumber / GenerationalGap;
			
		//assert(generacionesArranque > 0);
		
		logger.info("Reemplazo SteadyState por edad con Generational Gap " + GenerationalGap + 
				" y elitismo " + Elitism);
	}

	@Override
	public Poblacion nuevaPoblacion(Poblacion poblacion, Poblacion offspring) 
	{	
		//steady-state con reemplazo por edad
		//Borra los GenerationalGap fenotipos más antiguos de la población 
		//añade los hijos y finalmente ordena por conveniencia del programa
		//En las primeras generaciones se borran los individuos de peor fitness
	
		//TODO esto es lo que hay que añadir
		//En caso de elitismo, se añade el mejor individuo de la generación anterior
		//en lugar del de peor fitness de la nueva
		
		/*
		//Cálculo de la generación a borrar
		if(generacionesArranque > 0)
		{
			logger.info("generacionesArranque:" + generacionesArranque);
			generacionesArranque--;
			poblacion.trim(IndividualsNumber - GenerationalGap);
		}
		else
		{
			//Ordena por edad y fitness y elimina los GenerationalGap más viejos o de peor fitness
			poblacion.ordenaAge();
			poblacion.trim(IndividualsNumber - GenerationalGap);
			//generacionBorrar++;
			//poblacion.checkGen();
			//logger.info("BorramosGeneracion:" + generacionBorrar);
			//poblacion.borraGen(generacionBorrar, GenerationalGap);
		}
		*/
		
		//Ordena por edad y fitness y elimina los GenerationalGap más viejos o de peor fitness
		//Funciona también para generaciones bajas, comportándose como SS por fitness
		poblacion.ordenaAge(Elitism);
		poblacion.trim(IndividualsNumber - GenerationalGap);
		poblacion.addPoblacion(offspring);
		poblacion.ordena();
	
		return poblacion;
	}
}
