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
 * TerminationCondition
 * 
 * implementa la condición de terminación del GE
 */
package es.uned.simda.acidge.ge;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Logger;



import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.Problema;

public class TerminationCondition 
{
	private final static Logger logger = Logger.getLogger(TerminationCondition.class.getName());

	int TerminationConditionType;
	int GenerationsNumber;
	protected double GoalFitness;
	protected boolean HasGoal;
	LinkedList<Double> lista;
	
	//En caso de condición de terminación de problema y opción de no parar hasta máximo número 
	//de generaciones, avance indica si se alcanzó el umbral
	final static int NOALCANZADO = 0;
	final static int ALCANZADO = 1;
	final static int ALCANZADOYCONSULTADO = 2;
	
	int estadoAvance;
	
	TerminationCondition(int TerminationConditionType, int GenerationsNumber,
			boolean HasGoal, double GoalFitness)
	{
		if((TerminationConditionType != 1) && (TerminationConditionType != 2) && 
				(TerminationConditionType != 4) && (TerminationConditionType != 5))
			throw new AssertionError("Valor de parámetro TerminationConditionType no soportado " + 
					TerminationConditionType);
		
		this.GoalFitness = GoalFitness;
		this.HasGoal = HasGoal;
		this.TerminationConditionType = TerminationConditionType;
		this.GenerationsNumber = GenerationsNumber;
		estadoAvance = NOALCANZADO;
		
		lista = new LinkedList<Double>();
	}
	
	boolean compruebaBestFitness(double bestFitness)
	{
		assert(lista.size() <= GenerationsNumber);
		
		lista.addFirst(new Double(bestFitness));
		
		if(lista.size() > GenerationsNumber)
		{
			lista.removeLast();

			Iterator<Double> iterator = lista.iterator();
			Double valor = bestFitness;
		
			String cadena = "Valores bestFitness almacenados:";
		
			while(iterator.hasNext() == true)
			{
				double listaVal = iterator.next().doubleValue();
			
				cadena += listaVal + " ";
			
				if(listaVal != valor)
				{
					logger.info(cadena);
					return false;
				}
			}
			
			logger.info(cadena);
		
			logger.info("bestFitness:" + bestFitness + " sin cambios durante " + lista.size() + 
				" generaciones");
		
			return true;
		}
		else
			return false;
	}
	
	boolean isProblemCondition()
	{
		if(TerminationConditionType != 5)
			return false;
		else if(estadoAvance == ALCANZADO)
		{
			estadoAvance = ALCANZADOYCONSULTADO;
			return true;
		}
		else
			return false;
	}
	
	//FIXME: una vez superada la paranoia, quitar parámetro bestFitness y obtener de evalRes
	boolean isTerminationCondition(int generacion, double bestFitness, EvalRes evalRes)
	{
		boolean ret = false;

		assert(bestFitness == evalRes.getFitness());
		
		//Comprobar si se alcanza el valor de éxito del problema (si existe)
		if(HasGoal && (bestFitness <= GoalFitness))
		{
			logger.info("Alcanzado objetivo bestFitness:" + bestFitness + 
					" goalFitness:" + GoalFitness);
			
			ret = true;
		}
		else 
		{
			//Diferentes condiciones de terminación 
			switch(TerminationConditionType)
			{
				case 1:		
					if(generacion >= GenerationsNumber)
					{
						logger.info("Alcanzada máxima generación:" + generacion);
						ret = true;
					}
				
					break;
				case 2:
					ret = compruebaBestFitness(bestFitness);		
				
					break;
				case 4:
					//Considera el limite de generaciones y una terminación del problema
					if(generacion >= GenerationsNumber)
					{
						logger.info("Alcanzada máxima generación:" + generacion);
						ret = true;
					}
					else
						ret = evalRes.isFinished();
					break;
				case 5:
					//Considera el limite de generaciones y una terminación del problema
					if(generacion >= GenerationsNumber)
					{
						logger.info("Alcanzada máxima generación:" + generacion);
						ret = true;
					}
					else
					{
						if(evalRes.isFinished() && (estadoAvance == NOALCANZADO))
						{
							logger.info("Encontrado primer individuo que cumple hits");
							estadoAvance = ALCANZADO;
						}
					}
					break;
				default:
					assert(false); //código no alcanzable
			}
		}

		return ret;
	}

}
