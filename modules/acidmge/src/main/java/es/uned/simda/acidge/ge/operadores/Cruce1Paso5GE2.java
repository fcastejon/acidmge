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
 * Cruce1Paso5GE2
 * 
 * Clase para GE2 con separación en dos partes. Considera que el cromosoma está partido en dos mitades
 * 
 * Cruza con 1.5 pasos la primera parte y la segunda
 * 
 * En el caso de mutar la primera parte, tiene 2 pasos y en la segunda sólo 1
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class Cruce1Paso5GE2 extends OperadorRecombinacion 
{
	private final static Logger logger = Logger.getLogger(Cruce1Paso5GE2.class.getName());

	int maxPunto, minPunto;
	private double CrossoverRateGE2;
	private int num_primer;
	
	public Cruce1Paso5GE2(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(geproperties, randomGenerator);
		
		maxPunto = Integer.MIN_VALUE;
		minPunto = Integer.MAX_VALUE;
		num_primer = 0;
		
		logger.info("Cruce 1.5 pasos GE2");
		
		this.CrossoverRateGE2 = geproperties.getCrossoverRateGE2();
		
		if(CrossoverPointsNumber != 1)
			throw new AssertionError("Número de puntos de cruce erróneo:" + CrossoverPointsNumber);
	}
	
	public Cruce1Paso5GE2(int CrossoverType, double CrossoverRate, 
			int CrossoverPointsNumber, int CrossoverBlockSize, 
			double ExchangeProbability, double CrossoverRateGE2, 
			int LimitMaxGenesNumber, RandomGenerator randomGenerator) 
	{
		super(CrossoverType, CrossoverRate, CrossoverPointsNumber, CrossoverBlockSize, 
				ExchangeProbability, LimitMaxGenesNumber, randomGenerator);
		
		maxPunto = Integer.MIN_VALUE;
		minPunto = Integer.MAX_VALUE;
		num_primer = 0;
		
		logger.info("Cruce 1.5 pasos GE2");
		
		this.CrossoverRateGE2 = CrossoverRateGE2;
		
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
		//TODO usar getXOPoints en vez de getIndGE2
		
		if(randomGenerator.random() <= CrossoverRate)
			{
			//Los padres deben ser expresables y sin wrapping
			if((padre1.getExpresable()) && (padre2.getExpresable()) && 
					(padre1.getWrapping() == 0) && (padre2.getWrapping() == 0) &&
					(padre1.getXOPoints().get(0) < genes1.length) && 
					(padre2.getXOPoints().get(0) < genes2.length))
				{
				assert(padre1.getIndGE2() > 0);
				assert(padre2.getIndGE2() > 0);
				
				if(randomGenerator.random() <= CrossoverRateGE2)
				{
					//Se genera un número entre 0 e indGE2
					//Hecho para que al menos quede un codón por copia
					punto1 = randomGenerator.randomInt(padre1.getIndGE2());
					punto2 = randomGenerator.randomInt(padre2.getIndGE2());
					int ig1 = padre1.getIndGE2();
					int ig2 = padre2.getIndGE2();
					num_primer++;
					
					actPunto(punto1);
					actPunto(punto2);
				
					//logger.info("punto1:" + punto1 + " punto2:" +  punto2);
				
					assert((punto1 >= 0) && (punto1 <= (genes1.length - 1)));
					assert((punto2 >= 0) && (punto2 <= (genes2.length - 1)));
							
					int t1 = ig1 - punto1;
					int t2 = ig2 - punto2;
					assert((t1 >= 0) && (t2 >= 0));
					
					aux = cruzaCromosomas(genes1, punto1, ig1, genes2, punto2, ig2);
					hijo1 = new Genotipo(aux);

					aux = cruzaCromosomas(genes2, punto2, ig2, genes1, punto1, ig1);
					hijo2 = new Genotipo(aux);
				}
				else
				{
					//Se genera un número entre segundo xopoint y genesNumber
					//Hecho para que al menos quede un codón por copia
					punto1 = randomGenerator.randomInt(padre1.getGenesNumber() - padre1.getIndGE2()) + padre1.getIndGE2();
					punto2 = randomGenerator.randomInt(padre2.getGenesNumber() - padre2.getIndGE2()) + padre2.getIndGE2();
				
					actPunto(punto1);
					actPunto(punto2);
				
					//logger.info("punto1:" + punto1 + " punto2:" +  punto2);
				
					assert((punto1 >= 0) && (punto1 <= (genes1.length - 1)));
					assert((punto2 >= 0) && (punto2 <= (genes2.length - 1)));
							
					aux = fusiona(genes1, punto1, genes2, punto2);
					hijo1 = new Genotipo(aux);

					aux = fusiona(genes2, punto2, genes1, punto1);
					hijo2 = new Genotipo(aux);
				}
				
				num_cruza++;
				
				logger.fine("p1l:" + padre1.getGenesNumber() +
						" p2l:" + padre2.getGenesNumber() + 
						" ig1:" + padre1.getIndGE2() + " ig2:" + padre2.getIndGE2() +
						" punto1:" + punto1 + " punto2:" +  punto2);
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
	
	//Realiza el intercambio de cromosomas para el caso de 1.5 pasos
	byte [] cruzaCromosomas(byte [] g1, int punto1, int ig1, byte [] g2, int punto2, int ig2)
	{
		int l1 = g1.length - (ig1 - punto1) + (ig2 - punto2);
		//int l2 = g1.length - 2 * (punto1 - punto2);
		
		//int t1 = ig1 - punto1;
		int t2 = ig2 - punto2;

		//TODO retirar esta traza
		if(l1 == 0)
		{
			logger.info("cruzaCromosomas: l1:"+l1+" g2.length:" + g2.length + " ig2:" + ig2 + " punto1:" + punto1 + " punto2:" + punto2);
			throw new AssertionError("l1==0");
		}
		
		if(l1 > LimitMaxGenesNumber)
			l1 = LimitMaxGenesNumber;
		
		byte hijo[] = new byte[l1];
		int pos = 0;

		System.arraycopy(g1, 0, hijo, pos, punto1);
		pos += punto1;
		System.arraycopy(g2, punto2, hijo, pos, t2);
		pos += t2;
		
		int nc = g1.length - ig1;

		if((pos + nc) > LimitMaxGenesNumber)
			nc = LimitMaxGenesNumber - pos;

		//logger.fine("pos1:" + pos + " nc:" + nc);

		System.arraycopy(g1, ig1, hijo, pos, nc);
		pos += nc;
					
		assert(pos == hijo.length);
		
		return hijo;
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
		
		super.stat();
	}
}
