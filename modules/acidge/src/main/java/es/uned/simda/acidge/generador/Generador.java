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
 * Clase abstracta Generador
 */

package es.uned.simda.acidge.generador;

import java.util.ArrayList;
import java.util.logging.Logger;

import es.uned.simda.acidge.ge.Genotipo;
import es.uned.simda.acidge.ge.GEProperties;


public abstract class Generador 
{
	private final static Logger logger = Logger.getLogger(Generador.class.getName());

	protected final int maxWrappingNumber;
	protected final int maxRecursionLevel;
	protected final int genesNumber;
	protected final byte[] genes;

	protected int indice;
	protected int wrapping;
	protected int longExp;
	protected ArrayList<Integer> xopoints;
	protected int recursionLevel;
	protected static int statMaxRecursionLevel = 0;
	
	public Generador(byte[] genes, GEProperties geproperties)
	{
		maxWrappingNumber = geproperties.getMaxWrappingNumber();
		maxRecursionLevel = geproperties.getMaxRecursionLevel();
		
		if(maxWrappingNumber < 0)
			throw new AssertionError("Valor de maxWrappingNumber no válido:" + maxWrappingNumber);
		
		if(maxRecursionLevel <= 0)
			throw new AssertionError("Valor de MaxRecursionLevel no válido:" + maxRecursionLevel);	
		
		this.genes = genes;
		genesNumber = genes.length;
		
		indice = 0;

		wrapping = 0;
		longExp = 0;

		xopoints = new ArrayList<Integer>();
		recursionLevel = 0;
	}
	
	protected void incRecursionLevel() throws GenException 
	{ 
		recursionLevel++;
		
		if(recursionLevel > statMaxRecursionLevel)
			statMaxRecursionLevel = recursionLevel;
		
		if(recursionLevel > maxRecursionLevel)
			throw new GenException("Alcanzado máximo nivel de recursion:" + recursionLevel);
	}
	
	protected void decRecursionLevel() 
	{
		recursionLevel--;
		
		assert(recursionLevel >= 0);
	}
	
	public static int getStatMaxRecursionLevel() { return statMaxRecursionLevel; }
	
	protected int getCodon() throws GenException
	{
		//Vuelta del índice para evitar contar un wrapping de más si termina justo al final de los genes
		if(indice >= genesNumber)
		{
			indice = 0;
			wrapping++;
		}

		if(wrapping > maxWrappingNumber)
			throw new GenException("Alcanzado máximo número de wrapping:" + wrapping);
		
		//Se gestiona como int para evitar el problema del signo
		int codon = ((int) genes[indice++] & Genotipo.maskByte);
		assert((codon >= 0) && (codon < Genotipo.valoresByte));
		
		if(longExp < genesNumber)
			longExp++;			
	
		//logger.fine("codon:" + Integer.toString((int) codon & Genotipo.maskByte));
		
		return codon;
	}
	
	public int getLongExp() { return longExp; }
	public int getWrapping() { return wrapping; }
	public ArrayList<Integer> getXOPoints() { return xopoints; }
	
	public abstract String genera() throws GenException;
	
}
