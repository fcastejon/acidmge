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
 * Clase abstracta OperadorRecombinacion 
 * 
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;

public abstract class OperadorRecombinacion 
{
	private final static Logger logger = Logger.getLogger(OperadorRecombinacion.class.getName());

	//protected int CrossoverType;
	protected double CrossoverRate;
	protected int CrossoverPointsNumber;
	protected int CrossoverBlockSize;
	//protected double ExchangeProbability;
	protected int LimitMaxGenesNumber;
	
	protected int num_cruza;
	protected int tot_cruza;
	
	protected RandomGenerator randomGenerator;
	protected Genotipo gen_aux;	//conveniencia para el cruce
	
	//Constructor
	public OperadorRecombinacion(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		//this.CrossoverType = geproperties.getCrossoverType();
		this.CrossoverRate = geproperties.getCrossoverRate();
		this.CrossoverPointsNumber = geproperties.getCrossoverPointsNumber();
		this.CrossoverBlockSize = geproperties.getCrossoverBlockSize();
		//this.ExchangeProbability = geproperties.getExchangeProbability();
		this.LimitMaxGenesNumber = geproperties.getLimitMaxGenesNumber();
		this.randomGenerator = randomGenerator;

		num_cruza = 0;
		tot_cruza = 0;
	}
	
	//Constructor
	public OperadorRecombinacion(int CrossoverType, 
			double CrossoverRate, int CrossoverPointsNumber, int CrossoverBlockSize,
			double ExchangeProbability, int LimitMaxGenesNumber, RandomGenerator randomGenerator)
	{
		//this.CrossoverType = CrossoverType;
		this.CrossoverRate = CrossoverRate;
		this.CrossoverPointsNumber = CrossoverPointsNumber;
		this.CrossoverBlockSize = CrossoverBlockSize;
		//this.ExchangeProbability = ExchangeProbability;
		this.LimitMaxGenesNumber = LimitMaxGenesNumber;
		this.randomGenerator = randomGenerator;

		num_cruza = 0;
		tot_cruza = 0;
	}
		
	//Llamada sin parámetros, devuelve el segundo hijo almacenado en gen_aux
	public Genotipo cruza()
	{
		return gen_aux;
	}
	
	public abstract Genotipo cruza(Genotipo padre1, Genotipo padre2);
	
	void swap(byte [] g1, byte [] g2, int indice)
	{
		assert(indice > 0);
		assert(indice < g1.length);
		assert(indice < g2.length);
		
		byte aux = g2[indice];
		g2[indice] = g1[indice];
		g1[indice] = aux;
	}
	
	//Devuelve una cadena de bytes con la primera parte de la primera cadena y el final de la segunda
	byte [] fusiona(byte [] g1, int punto1, byte [] g2, int punto2)
	{
		int genesNumber = punto1 + g2.length - punto2;
		
		if(genesNumber > LimitMaxGenesNumber)
			genesNumber = LimitMaxGenesNumber;
		
		assert((genesNumber > 0) && (genesNumber <= LimitMaxGenesNumber));
		/*
		logger.info("g1:" + g1.length + " p1:" + punto1 + " g2:" + g2.length +
				" p2:" + punto2 + " gNumber" + genesNumber);
		*/
		
		byte [] hijo = new byte[genesNumber];
		
		//int i;
		//for(i = 0; i < punto1; i++)
		//	hijo[i] = g1[i];
		
		System.arraycopy(g1, 0, hijo, 0, punto1);
/*		
		for(int j = punto2; j < g2.length; i++, j++)
			hijo[i] = g2[j];
*/		
		//Para incorporar la limitación máxima de cadena
		//for(int j = punto2; (j < g2.length) && (i < LimitMaxGenesNumber); i++, j++)
		//	hijo[i] = g2[j];
		
		int lon = g2.length - punto2;
		if((punto1 + lon) > LimitMaxGenesNumber)
			lon = LimitMaxGenesNumber - punto1;
		
		System.arraycopy(g2, punto2, hijo, punto1, lon);
		
		assert((punto1 + lon) == hijo.length);
		
		return hijo;
	}
	
	public void stat()
	{
		logger.info("Cruces: " + num_cruza + "/" + 
			tot_cruza + " = " + (double) num_cruza/tot_cruza);
	}
	
	public double getStat()
	{
		return (double) num_cruza/tot_cruza;
	}
	
}
