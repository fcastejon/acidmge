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
 * CruceBloquesMulti
 * 
 * Clase para Multigramática con separación de topología y valores. 
 * Considera que el cromosoma está partido en tres partes: MNN, topología y valores
 * 
 * Cruza por bloques la segunda parte y la tercera, el primer bloque queda sin modificar
 * 
 * Implementa el cruce por bloques y aunque considera un punto, cruza en las dos partes finales
 * 
 * Los puntos de cruce son múltiplos del tamaño de bloques
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;



public class CruceBloquesMulti extends OperadorRecombinacion 
{
	private final static Logger logger = Logger.getLogger(CruceBloquesMulti.class.getName());

	int maxPunto, minPunto;
	
	public CruceBloquesMulti(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(geproperties, randomGenerator);
	
		maxPunto = Integer.MIN_VALUE;
		minPunto = Integer.MAX_VALUE;
			
		logger.info("Cruce por bloques GE2:" + CrossoverBlockSize);
		
		if(CrossoverPointsNumber != 1)
			throw new AssertionError("Número de puntos de cruce erróneo:" + CrossoverPointsNumber);
	}
		
	public CruceBloquesMulti(int CrossoverType, double CrossoverRate, 
			int CrossoverPointsNumber, int CrossoverBlockSize, 
			double ExchangeProbability, 
			int LimitMaxGenesNumber, RandomGenerator randomGenerator) 
	{
		super(CrossoverType, CrossoverRate, CrossoverPointsNumber, CrossoverBlockSize, 
				ExchangeProbability, LimitMaxGenesNumber, randomGenerator);
		
		maxPunto = Integer.MIN_VALUE;
		minPunto = Integer.MAX_VALUE;
			
		logger.info("Cruce por bloques GE2:" + CrossoverBlockSize);
		
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
		
		//logger.info("padre1 " + padre1);
		//logger.info("padre2 " + padre2);
		
		if(randomGenerator.random() <= CrossoverRate)
			{
			//Los padres deben ser expresables y sin wrapping
			if((padre1.getExpresable()) && (padre2.getExpresable()) && 
					(padre1.getWrapping() == 0) && (padre2.getWrapping() == 0)) 
				{
				assert(padre1.getXOPointSize() == 2);
				assert(padre2.getXOPointSize() == 2);
				//assert(padre1.getIndGE2() > 0);
				//assert(padre2.getIndGE2() > 0);
				//assert(padre1.getIndGE2() <= padre1.getGenesNumber()/2);  //No es posible garantizar siempre la igualdad
				//assert(padre2.getIndGE2() <= padre2.getGenesNumber()/2);
				
				//Se leen los XOPoints
				int xoTop1 = padre1.getXOPoint(0);
				int xoTop2 = padre2.getXOPoint(0);
				int xoSiz1 = padre1.getXOPoint(1);
				int xoSiz2 = padre2.getXOPoint(1);
				assert((xoTop1 == CrossoverBlockSize) && (xoSiz1 % CrossoverBlockSize == 0));
				assert((xoTop2 == CrossoverBlockSize) && (xoSiz2 % CrossoverBlockSize == 0));
				
				//Se genera un número entre 0 y genes.length/bloque - 1(tener en cuenta que la función random < 1)
				//Hecho para que al menos quede un bloque por copiar
				int punto1 = randomGenerator.randomInt((xoSiz1 - xoTop1) / CrossoverBlockSize) * CrossoverBlockSize + xoTop1;
				int punto2 = randomGenerator.randomInt((xoSiz2 - xoTop2) / CrossoverBlockSize) * CrossoverBlockSize + xoTop2;
				
				assert(punto1 % CrossoverBlockSize == 0);
				assert(punto2 % CrossoverBlockSize == 0);

				
				//Estadísticas
				actPunto(punto1);
				actPunto(punto2);
				
				/*
				logger.fine("p1l:" + padre1.getGenesNumber() +
						" p2l:" + padre2.getGenesNumber() + 
						" xoTop1:" + xoTop1 + " xoTop2:" + xoTop2 +
						" xoSiz1:" + xoSiz1 + " xoSiz2:" + xoSiz2 +
						" punto1:" + punto1 + " punto2:" +  punto2);
				*/
				
				assert((punto1 >= xoTop1) && (punto1 <= xoSiz1));
				assert((punto2 >= xoTop2) && (punto2 <= xoSiz2));
								
				num_cruza++;

				int t1 = xoSiz1 - punto1;
				int t2 = xoSiz2 - punto2;
				assert((t1 >= 0) && (t2 >= 0));
				
				aux = cruzaCromosomas(genes1, xoTop1, punto1, xoSiz1, genes2, xoTop2, punto2, xoSiz2);
				hijo1 = new Genotipo(aux);

				aux = cruzaCromosomas(genes2, xoTop2, punto2, xoSiz2, genes1, xoTop1, punto1, xoSiz1);
				hijo2 = new Genotipo(aux);
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
	
	//Realiza el intercambio de cromosomas
	byte [] cruzaCromosomas(byte [] g1, int xoTop1, int punto1, int xoSiz1, byte [] g2, int xoTop2, int punto2, int xoSiz2)
	{
		int l1 = g2.length - 2 * (punto2 - punto1);
		//int l2 = g1.length - 2 * (punto1 - punto2);
		
		//int t1 = xoSiz1 - punto1;
		int t2 = xoSiz2 - punto2;

		/*
		if(l1 == 0)
		{
			logger.info("cruzaCromosomas: l1:"+l1+" g2.length:" + g2.length + " ig2:" + ig2 + " punto1:" + punto1 + " punto2:" + punto2);
			throw new AssertionError("l1==0");
		}
		*/
		
		if(l1 > LimitMaxGenesNumber)
			l1 = LimitMaxGenesNumber;
		
		byte hijo[] = new byte[l1];
		int pos = 0;

		// Se copia desde el comienzo para llevar el parámetro MNN
		System.arraycopy(g1, 0, hijo, pos, punto1);
		pos += punto1;
		System.arraycopy(g2, punto2, hijo, pos, t2);
		pos += t2;
		
		int nc = punto1 - xoTop1;

		if((pos + nc) > LimitMaxGenesNumber)
			nc = LimitMaxGenesNumber - pos;

		//logger.fine("pos1:" + pos + " nc:" + nc);

		System.arraycopy(g1, xoSiz1, hijo, pos, nc);
		pos += nc;
		
		nc = g2.length - xoSiz2 - punto2 + xoTop2;
		if((pos + nc) > LimitMaxGenesNumber)
			nc = LimitMaxGenesNumber - pos;
		
		//logger.info("pos:" + pos + " nc:" + nc);
		System.arraycopy(g2, xoSiz2 + punto2 - xoTop2, hijo, pos, nc);
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
		
		super.stat();
	}
}
