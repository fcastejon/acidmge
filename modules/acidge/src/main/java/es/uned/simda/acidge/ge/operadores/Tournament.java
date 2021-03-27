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
 * Tournament
 * 
 * Implementa el método de selección de padres por torneo
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.Fenotipo;
import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Poblacion;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class Tournament extends Selector 
{
	private final static Logger logger = Logger.getLogger(Tournament.class.getName());

	private int TournamentSize; 
	
	public Tournament(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(randomGenerator);
		this.TournamentSize = geproperties.getTournamentSize();
		logger.info("Selección de padres por Tournament, size:" + this.TournamentSize);
	}
	
	public Tournament(int TournamentSize, RandomGenerator randomGenerator) 
	{
		super(randomGenerator);
		this.TournamentSize = TournamentSize;
		logger.info("Selección de padres por Tournament, size:" + this.TournamentSize);
	}

	//Sólo apunta la Poblacion sobre la que actúa
	public void inicia(Poblacion pobVal) 
	{
		pob = pobVal;
	}

	//Elige tantos fenotipos como participantes en el torneo
	//Gana el que tiene la mejor fitness, correspondiente a la más baja
	public Fenotipo select() 
	{
		double minFitness;
		Fenotipo elegido;
		Fenotipo aux;
		int iElegido;
			
		//El primero obtenido marca el valor mínimo inicial y si los demás lo mejoran lo sustituyen
		//Nunca puede haber un resultado desierto
		int indice = randomGenerator.randomInt(pob.getSize());
		assert(indice < pob.getSize());
			
		elegido = pob.getFenotipo(indice);
		minFitness = pob.getFenotipo(indice).getFitness();
		iElegido = indice;		
		
		for(int i = 0; i < (TournamentSize - 1); i++)
		{
			//Genera un número entre 0 y tamaño de población - 1 
			indice = randomGenerator.randomInt(pob.getSize());
			assert(indice < pob.getSize());
				
			aux = pob.getFenotipo(indice);
			if(aux.getFitness() < minFitness)
				{
				elegido = aux;
				minFitness = aux.getFitness();
				iElegido = indice;
				}
			
			//cadena +=" indice:" + indice + " Fitness:" + aux.getFitness();
		}
		
		//logger.info(" Elegido:" + iElegido);
		
		assert(elegido != null);
		return elegido;
	}	

}
