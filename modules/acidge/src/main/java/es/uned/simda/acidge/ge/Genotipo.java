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
** Clase Genotipo
**
*/
package es.uned.simda.acidge.ge;

import java.util.ArrayList;
import java.util.logging.Logger;


import es.uned.simda.acidge.ge.random.RandomGenerator;

public class Genotipo
{
	private final static Logger logger = Logger.getLogger(Genotipo.class.getName());

	public final static int valoresByte = 256;
	public final static int maskByte = 0xff;
	
	static RandomGenerator randomGenerator = null;
	static int generacionClase = -1;				//se saca de un método estático de inicialización

	//Variables del objeto
	//Cadena binaria que contiene el genotipo
	private byte[] genes;
	final private int generacion;
	private boolean expresable;	
	private boolean viable;
	private int longExp;	//Longitud expresada
	private int wrapping;	//wrapping expresado
	protected ArrayList<Integer> xopoints;
	//private int indGE2;		//índice siguiente a donde termina la decodificación de la gramática universal
	
			
	static public void setGeneracionClase(int generacion) { assert(generacion > 0); generacionClase = generacion; }
	
	//Constructor llamado por los demás, rellena los campos comunes
	private Genotipo()
	{
		//Se pospone su rellenado a los constructores específicos
		genes = null;
		
		//En todos los casos el genotipo creado pertenece a la generación actual
		//Incluso en caso de copia
		generacion = generacionClase;
		
		//Se resetean los valores de resultados
		viable = true;
		expresable = true;		//Se considera expresable hasta que se vea que no 
		wrapping = -1;
		longExp = -1;
		//indGE2 = -1;
		xopoints = null;
	}
	
	public Genotipo(Genotipo g_org)	//Crea un nuevo Genotipo copiando uno existente
	{
		//Constructor común
		this();						//TODO Duda de que copie los campos escalares 
		
		int genesNumber = g_org.genes.length;
	
		//No tiene sentido comprobar un máximo número de genesNumber, pues en la ejecución podrían crecer
		//assert((genesNumber >= MinGenesNumber) && (genesNumber <= MaxGenesNumber));
		assert(genesNumber > 0);
	
		genes = new byte[genesNumber];
		
		for(int k = 0; k <= g_org.genes.length - 1; k++)
			genes[k] = g_org.genes[k];
	}
	
	//Creación de un Genotipo a partir de la cadena binaria
	//Es el constructor que todos llaman
	public Genotipo(byte[] genes)	
	{
		//Constructor común
		this();
		
		assert(genes.length > 0);	
		this.genes = genes;
	}
	
	//Creación de genotipo a partir de una cadena de enteros
	public Genotipo(int[] intGen)	
	{
		//Constructor común
		this();
		
		int genesNumber = intGen.length;
		
		assert(genesNumber > 0);
		genes = new byte[genesNumber];
		
		for(int k = 0; k <= intGen.length - 1; k++)
			genes[k] = (byte) (intGen[k] & 0x0FF);
	}

	//Varios getters & setters
	public int getGenesNumber() { return genes.length; }
	int getGeneracion() { return generacion; }

	public byte [] getGenes() { return genes; }
	public void setGenes(byte [] aux) { genes = aux; }
	
	public boolean getViable() { return viable; }
	public void setViable(boolean viable) { this.viable = viable; }
	public boolean getExpresable() { return expresable; }
	public void setExpresable(boolean expresable) { this.expresable = expresable; }
	public int getLongExp() { return longExp; }
	public void setLongExp(int longExp) { this.longExp = longExp; }
	public int getWrapping() { return wrapping; }
	public void setWrapping(int wrapping) { this.wrapping = wrapping; }
	public int getIndGE2() 
		{
			assert(xopoints.size() == 1);
			if(xopoints == null) 
				return -1;
			else 
				return xopoints.get(0); 
		}
	//public void setIndGE2(int indGE2) { this.indGE2 = indGE2; }
	public void setIndGE2(int indGE2) 
	{ 
		xopoints = new ArrayList<Integer>();
		xopoints.add(indGE2); 
	}
	public void setXOPoints(ArrayList<Integer> xopoints) { this.xopoints = xopoints; }
	public ArrayList<Integer> getXOPoints() { return xopoints; }
	public int getXOPoint(int ind) { return xopoints.get(ind); } 
	public int getXOPointSize() { return xopoints.size(); }

	public String toString()
	{
		String cad = "gen:" + generacion + ":longExp:" + longExp + ":";
		
		/* añadido
		cad += ":wrapping:" + wrapping + ":";
		
		for(int i = 0; i < xopoints.size(); i++)
		{
			cad += "xop" + i + ":" + xopoints.get(i) + ":";
		}
		//Hasta aquí 
		*/
		
		for(int i = 0; i < genes.length; i++)
		{
			//Para obtener valores sin signo
			cad += Integer.toString((int) genes[i] & maskByte);
			
			//cad += Byte.toString(genes[i]);
			if(i < genes.length - 1)
				cad += ", ";
		}
			
		return "Genotipo:" + cad;
	}
	
	public String toStringShort()
	{
		String cad = "gen:" + generacion + ":length:" + genes.length + ":longExp:" + longExp + ":";
		
		cad += ":wrapping:" + wrapping + ":";
		
		for(int i = 0; i < xopoints.size(); i++)
		{
			cad += "xop" + i + ":" + xopoints.get(i) + ":";
		}
			
		return "Genotipo:" + cad;
	}
		
	//Comprueba igualdad entre genotipos e igualdad de la parte expresable
	//devuelve dos booleanos el primero es igualdad completa 
	//y el segundo de la parte expresable
	public boolean[] esIgual(Genotipo otro)
	{
		boolean res[] = new boolean[2];
		
		byte[] g1 = this.getGenes();
		byte[] g2 = otro.getGenes();
		
		//logger.info("g1:" + this.toString());		
		//logger.info("g2:" + otro.toString());
		
		assert(g1.length >= longExp);
		assert(g2.length >= otro.getLongExp());
		
		int i;
		
		if(longExp != otro.getLongExp())
		{
			res[0] = false;
			res[1] = false;
			return res;
		}
		else
		{
			for(i = 0; i < longExp; i++)
			{
				if(g1[i] != g2[i])
					break;
			}
			
			if(i < longExp)
			{	
				res[0] = false;
				res[1] = false;
				return res;
			}	
		}
		
		//A partir de aquí es igual en la parte expresable
		if(g1.length != g2.length)
		{
			res[0] = false;
			res[1] = true;
			return res;
		}
	
		for(i = longExp; i < g1.length; i++)
		{
			if(g1[i] != g2[i])
				break;
		}
		
		if(i < g2.length)
		{	
			res[0] = false;
			res[1] = true;
			return res;
		}
		else
		{
			res[0] = true;
			res[1] = true;
			return res;
		}
	}
}
