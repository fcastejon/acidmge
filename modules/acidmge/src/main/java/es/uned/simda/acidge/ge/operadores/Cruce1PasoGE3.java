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
 * Cruce1PasoGE3
 * 
 * Clase para Multigramar con separación en tres partes. 
 * Considera que hay dos xopoints que dividen el cromosoma en tres partes
 * 
 * Cruza con 1 paso la primera parte, segunda o tercera
 * 
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class Cruce1PasoGE3 extends OperadorRecombinacion 
{
	private final static Logger logger = Logger.getLogger(Cruce1PasoGE3.class.getName());

	int maxPunto, minPunto;
	private double CrossoverRateGE2;
	private double CrossoverRateGE3;
	private int num_primer;
	private int num_segun;
	
	public Cruce1PasoGE3(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(geproperties, randomGenerator);
		
		maxPunto = Integer.MIN_VALUE;
		minPunto = Integer.MAX_VALUE;
		num_primer = 0;
		
		logger.info("Cruce 1 paso GE2");
		
		this.CrossoverRateGE2 = geproperties.getCrossoverRateGE2();
		this.CrossoverRateGE3 = geproperties.getCrossoverRateGE3();
		
		if(CrossoverRateGE2 + CrossoverRateGE3 > 1.0)
			throw new AssertionError("Configuración errónea CrossoverRateGE2:" + CrossoverRateGE2 + 
					":CrossoverRateGE3:" + CrossoverRateGE3);		
		if(CrossoverPointsNumber != 1)
			throw new AssertionError("Número de puntos de cruce erróneo:" + CrossoverPointsNumber);
	}
	
	public Cruce1PasoGE3(int CrossoverType, double CrossoverRate, 
			int CrossoverPointsNumber, int CrossoverBlockSize, 
			double ExchangeProbability, double CrossoverRateGE2, double CrossoverRateGE3, 
			int LimitMaxGenesNumber, RandomGenerator randomGenerator) 
	{
		super(CrossoverType, CrossoverRate, CrossoverPointsNumber, CrossoverBlockSize, 
				ExchangeProbability, LimitMaxGenesNumber, randomGenerator);
		
		maxPunto = Integer.MIN_VALUE;
		minPunto = Integer.MAX_VALUE;
		num_primer = 0;
		
		logger.info("Cruce 1 paso GE2");
		
		this.CrossoverRateGE2 = CrossoverRateGE2;
		this.CrossoverRateGE3 = CrossoverRateGE3;
		
		if(CrossoverRateGE2 + CrossoverRateGE3 > 1.0)
			throw new AssertionError("Configuración errónea CrossoverRateGE2:" + CrossoverRateGE2 + 
					":CrossoverRateGE3:" + CrossoverRateGE3);		
		if(CrossoverPointsNumber != 1)
			throw new AssertionError("Número de puntos de cruce erróneo:" + CrossoverPointsNumber);
	}

	//Crossover por bloques, con cromosoma con dos partes diferentes
	public Genotipo cruza(Genotipo padre1, Genotipo padre2)
	{
		Genotipo hijo1 = null;
		Genotipo hijo2 = null;
		byte [] genes1 = padre1.getGenes();
		byte [] genes2 = padre2.getGenes();
		byte [] aux;
		int punto1, punto2;
		
		//logger.info("padre1 " + padre1);
		//logger.info("padre2 " + padre2);
		
		if(randomGenerator.random() <= CrossoverRate)
			{
			//Los padres deben ser expresables y sin wrapping
			if((padre1.getExpresable()) && (padre2.getExpresable()) && 
					(padre1.getWrapping() == 0) && (padre2.getWrapping() == 0) &&
					(padre1.getXOPoints().get(0) < padre1.getXOPoints().get(1)) && 
					(padre2.getXOPoints().get(0) < padre2.getXOPoints().get(1)) &&
					(padre1.getXOPoints().get(1) < genes1.length) && 
					(padre2.getXOPoints().get(1) < genes2.length))
				{
				assert(padre1.getXOPoints().size() >= 2);
				assert(padre2.getXOPoints().size() >= 2);

				double rnd = randomGenerator.random();
				if(rnd <= CrossoverRateGE2)	
				{
					//Se genera un número entre 0 y primer xopoint
					//Hecho para que al menos quede un codón por copia
					punto1 = randomGenerator.randomInt(padre1.getXOPoints().get(0));
					punto2 = randomGenerator.randomInt(padre2.getXOPoints().get(0));
					num_primer++;
				}
				else if((rnd - CrossoverRateGE2) <= CrossoverRateGE3)
				{
					//Se genera un número entre primer xopoint y segundo xpoint
					//Hecho para que al menos quede un codón por copia
					punto1 = randomGenerator.randomInt(padre1.getXOPoints().get(1) - padre1.getXOPoints().get(0)) + 
						padre1.getXOPoints().get(0);
					punto2 = randomGenerator.randomInt(padre2.getXOPoints().get(1) - padre2.getXOPoints().get(0)) + 
						padre2.getXOPoints().get(0);
					num_segun++;
				}
				else
				{
					//Se genera un número entre primer xopoint y genesNumber
					//Hecho para que al menos quede un codón por copia
					punto1 = randomGenerator.randomInt(padre1.getGenesNumber() - padre1.getXOPoints().get(1)) + 
						padre1.getXOPoints().get(1);
					punto2 = randomGenerator.randomInt(padre2.getGenesNumber() - padre2.getXOPoints().get(1)) + 
						padre2.getXOPoints().get(1);
				}

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
						
				logger.fine("p1l:" + padre1.getGenesNumber() + 
						":p1xo1:" + padre1.getXOPoints().get(0) + ":p1xo2:" + padre1.getXOPoints().get(1) +
						":p2l:" + padre2.getGenesNumber() + 
						":p2xo1:" + padre2.getXOPoints().get(0) + ":p2xo2:" + padre2.getXOPoints().get(1) +
						":punto1:" + punto1 + ":punto2:" +  punto2);
				}
			else
				{
				logger.fine("No_Cruce: Cromosomas con wrapping o no expresables");
				}
			}

		if((hijo1 == null) && (hijo2 == null))
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
		logger.info("CrucesPrimer: " + num_primer + "/" + 
				tot_cruza + " = " + (double) num_primer/tot_cruza);
		logger.info("CrucesSegun: " + num_segun + "/" + 
				tot_cruza + " = " + (double) num_segun/tot_cruza);
		
		super.stat();
	}
}
