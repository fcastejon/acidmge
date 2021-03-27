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
 * Clase MutacionGE2
 * 
 *  Implementa un operador de mutación bitwise para GE2
 *  
 *  Utiliza un nuevo atributo para seleccionar la frecuencia con que actúa sobre la parte de topología (primera parte) 
 *  
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class MutacionBitwiseGE2 extends OperadorMutacion
{
	private final static Logger logger = Logger.getLogger(MutacionBitwiseGE2.class.getName());

	private int num_bits_muta;
	private int num_topology;
	private int num_llamadas;

	private double MutationRateGE2;
	
	MutacionBitwiseGE2(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(geproperties, randomGenerator); 
		
		num_bits_muta = 0;
		num_topology = 0;
		num_llamadas = 0;
		this.MutationRateGE2 = geproperties.getMutationRateGE2();

		logger.info("Mutación Bitwise GE2");
	}
	
	MutacionBitwiseGE2(int MutationType, 
			double MutationRate, RandomGenerator randomGenerator, 
			double MutationRateGE2)
	{
		super(MutationType, MutationRate, randomGenerator); 
		
		num_bits_muta = 0;
		num_topology = 0;
		num_llamadas = 0;
		this.MutationRateGE2 = MutationRateGE2;
		
		logger.info("Mutación Bitwise GE2");
	}
	
	public void muta(Genotipo genotipo)
	{
		//logger.info("muta antes  :" + genotipo);
		int ini, fin;
		byte [] genes = genotipo.getGenes();
		
		num_llamadas++;

		if(genotipo.getIndGE2() <= 0)
		{
			logger.fine("MutacionBitwiseGE2: Cromosoma con wrapping o no expresables");
			ini = 0;
			fin = genes.length;
		}
		else if(randomGenerator.random() < MutationRateGE2)
		{
			ini = 0;
			fin = genotipo.getIndGE2();
			num_topology++;
		}
		else
		{
			ini = genotipo.getIndGE2();
			fin = genes.length;
		}
		
		int i, j;
		int mask;
		boolean flag;
		
		for(i = ini; i < fin; i++)
			{
			flag = false;
			
			for(j = 0, mask = 128; j < 8; j++, mask = mask >> 1)
				{
				//logger.info("mask:" + (byte) mask + " " + Integer.toBinaryString(mask));
				
				if(randomGenerator.random() <= MutationRate)
					{
					num_bits_muta++;

					genes[i] ^= (byte) mask;
					flag = true;
					}
				}
			
			//Cuenta cuando haya al menos una mutación en el byte
	    	if(flag)
	    		num_muta++;
			
			//La cuenta es de bytes 
			tot_muta++;
			}
		
		//logger.info("muta despues:" + genotipo);
	}
	
	@Override
	public void stat()
	{
		logger.info("Mutaciones (bytes): " + num_muta + "/" + 
			tot_muta + " = " + (double) num_muta/tot_muta +
			" (bits):" + num_bits_muta + "/" + tot_muta * 8 + " = " + 
			(double) num_bits_muta/tot_muta/8 + " GE2Rate:"
			+ num_topology + "/" + num_llamadas + " = " + 
			(double) num_topology/num_llamadas);
	}
}
