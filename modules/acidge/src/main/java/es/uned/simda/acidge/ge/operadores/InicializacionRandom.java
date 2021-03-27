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
 * Clase InicializacionRandom
 * 
 * Genera un Genotipo aleatoriamente
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class InicializacionRandom extends OperadorInicializacion 
{
	private final static Logger logger = Logger.getLogger(InicializacionRandom.class.getName());

	//Mínimo y máximo tamaño de genotipo a crear. Se obtiene del fichero de parámetros
	private int MinGenesNumber;
	private int MaxGenesNumber;

	
	public InicializacionRandom(GEProperties geproperties, RandomGenerator randomGenerator) 
	{
		super(geproperties, randomGenerator);
		
		this.MinGenesNumber = geproperties.getMinGenesNumber();
		this.MaxGenesNumber = geproperties.getMaxGenesNumber();
	}
	
	public InicializacionRandom(int InicializacionType, int MinGenesNumber, int MaxGenesNumber, 
			RandomGenerator randomGenerator) 
	{
		super(InicializacionType, randomGenerator);
		
		this.MinGenesNumber = MinGenesNumber;
		this.MaxGenesNumber = MaxGenesNumber;
	}

	private byte getRandomGen() { return (byte) (randomGenerator.randomInt(Genotipo.valoresByte)); }
	
	@Override
	public Genotipo creaGenotipo() 
	{
		tot_ini++;
		
		//Recordar que random genera números entre 0 y max, lo que hace necesario el +1
		int genesNumber = randomGenerator.randomInt(MaxGenesNumber - MinGenesNumber + 1) + MinGenesNumber;
		
		assert((genesNumber >= MinGenesNumber) && (genesNumber <= MaxGenesNumber));
		
		byte [] genes = new byte[genesNumber];

	    for(int i = 0; i < genesNumber; i++)
		{    	
	    	genes[i] = getRandomGen();
		}
	    
	    Genotipo genotipo = new Genotipo(genes);
	   
	    //logger.info(genotipo.toString());
		
		return genotipo;
	}

	//No hay información de interés en este caso
	@Override
	public void stat()	
	{
		logger.info("InicializacionRandom.stat: " + tot_ini + " inicializados");
	}

}
