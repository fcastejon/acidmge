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
 * Clase Duplicación
 * 
 *  Implementa el operador de duplicación de Grammatical Evolution
 *  
 *  Copia una subcadena determinada aleatoriamente al final de la cadena original
 *  
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.random.RandomGenerator;


public class Duplicacion extends OperadorDuplicacion
{
	private final static Logger logger = Logger.getLogger(Duplicacion.class.getName());

	public Duplicacion(GEProperties geproperties, RandomGenerator randomGenerator)
	{
		super(geproperties, randomGenerator); 
		
		logger.info("Duplicación al final de la cadena");
	}
	
	public Duplicacion(int DuplicationType, double DuplicationRate, 
			RandomGenerator randomGenerator)
	{
		super(DuplicationType, DuplicationRate, randomGenerator); 
		
		logger.info("Duplicación al final de la cadena");
	}
	
	public void duplica(Genotipo genotipo)
	{
	    if(randomGenerator.random() <= DuplicationRate)
		{
	    	//logger.info("duplica antes  :" + genotipo);
			byte [] genes = genotipo.getGenes();
	    	
	    	//El siguiente número es entre 0 y genes.length -1
			int inicio = randomGenerator.randomInt(genes.length);
			//Tamaño entre inicio y genes.length - 1
			int longitud = randomGenerator.randomInt(genes.length - inicio + 1);
			
			assert((inicio + longitud) <= genes.length);
			
			//logger.info("genes:" + genes.length + " inicio:" + inicio + " long:" + longitud);
			
			byte [] genes2 = new byte[genes.length + longitud];

			int i;
	    	for(i = 0; i < genes.length; i++)
	    		genes2[i] = genes[i];
	    	
	    	for(int j = 0; j < longitud; i++, j++)
	    		genes2[i] = genes[inicio + j];
		
	    	num_dup++;
	    
	    	genotipo.setGenes(genes2);
	    	
	    	//logger.info("duplica despues:" + genotipo);
		}
		    	
		tot_dup++;
	}
}
