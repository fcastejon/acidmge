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
 * Clase que implementa el cruce con un paso 
 * 
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;



public class Cruce1Paso extends OperadorRecombinacion 
{
	private final static Logger logger = Logger.getLogger(Cruce1Paso.class.getName());

	int maxPunto, minPunto;
	
	public Cruce1Paso(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(geproperties, randomGenerator);
		
		maxPunto = Integer.MIN_VALUE;
		minPunto = Integer.MAX_VALUE;
			
		logger.info("Cruce de 1 Paso");
		
		if(CrossoverPointsNumber != 1)
			throw new AssertionError("Número de puntos de cruce erróneo:" + CrossoverPointsNumber);
	}
		
	public Cruce1Paso(int CrossoverType, double CrossoverRate, 
			int CrossoverPointsNumber, int CrossoverBlockSize, 
			double ExchangeProbability, 
			int LimitMaxGenesNumber, RandomGenerator randomGenerator) 
	{
		super(CrossoverType, CrossoverRate, CrossoverPointsNumber, CrossoverBlockSize, 
				ExchangeProbability, LimitMaxGenesNumber, randomGenerator);
		
		maxPunto = Integer.MIN_VALUE;
		minPunto = Integer.MAX_VALUE;
			
		logger.info("Cruce de 1 Paso");
		
		if(CrossoverPointsNumber != 1)
			throw new AssertionError("Número de puntos de cruce erróneo:" + CrossoverPointsNumber);
	}

	//Crossover de 1 punto con genotipos de longitud variable
	public Genotipo cruza(Genotipo padre1, Genotipo padre2)
	{
		//Genotipo hijo1 = null;
		//Genotipo hijo2 = null;
		Genotipo hijo1, hijo2;
		byte [] genes1 = padre1.getGenes();
		byte [] genes2 = padre2.getGenes();
		byte [] aux;
		
		//logger.info("padre1 " + padre1);
		//logger.info("padre2 " + padre2);
		
		if(randomGenerator.random() <= CrossoverRate)
			{
			//Hecho para que siempre se copie al menos 1 valor
			//Se genera un número entre 0 y genesNumber - 1 (tener en cuenta que la función random < 1)
			int punto1 = randomGenerator.randomInt(genes1.length);
			int punto2 = randomGenerator.randomInt(genes2.length);
			
			actPunto(punto1);
			actPunto(punto2);
			
			//logger.info("punto1:" + punto1 + " punto2:" +  punto2);
			
			assert((punto1 >= 0) && (punto1 <= (genes1.length - 1)));
			assert((punto2 >= 0) && (punto2 <= (genes2.length - 1)));
			
			num_cruza++;
			
			aux = fusiona(genes1, punto1, genes2, punto2);
			hijo1 = new Genotipo(aux);

			aux = fusiona(genes2, punto2, genes1, punto1);
			hijo2 = new Genotipo(aux);
			}
		else
			{
			hijo1 = new Genotipo(padre1);
			hijo2 = new Genotipo(padre2);
			}

		tot_cruza++;

		//logger.info("hijo1 " + hijo1);
		//logger.info("hijo2 " + hijo2);	
		
		gen_aux = hijo2;
		return hijo1;
	}
	

	
	//Crossover de 1 punto
	/*
	Genotipo cruza(Genotipo padre1, Genotipo padre2)
	{
		Genotipo hijo1, hijo2;

		hijo1 = new Genotipo(padre1);
		hijo2 = new Genotipo(padre2);
		
		//logger.info("padre1 " + padre1);
		//logger.info("padre2 " + padre2);
		
		if(Math.random() <= CrossoverRate)
			{
			//Hecho para que siempre se copie al menos 1 valor
			//Se genera un número entre 1 y GenesNumber - 1 (tener en cuenta que la función random < 1)
			int punto = (int) (Math.random() * (GenesNumber - 1)) + 1;
			
			actPunto(punto);
			
			//logger.info("punto " + punto);
			
			//assert(punto <= (GenesNumber - 2));
			assert((punto >= 1) & (punto <= (GenesNumber - 1)));
			
			num_cruza++;
			
			for(int i = punto; i < GenesNumber; i++)
				swap(hijo1.getGenes(), hijo2.getGenes(), i);		
			}

		tot_cruza++;

		//logger.info("hijo1 " + hijo1);
		//logger.info("hijo2 " + hijo2);	
		
		gen_aux = hijo2;
		return hijo1;
	}
	*/
	
	private void actPunto(int punto)
	{
		if(punto > maxPunto)
			maxPunto = punto;
		
		if(punto < minPunto)
			minPunto = punto;
	}
	
	@Override
	public void stat()
	{
		logger.info("minPunto:" + minPunto + " maxPunto:" + maxPunto);
		
		super.stat();
	}
}
