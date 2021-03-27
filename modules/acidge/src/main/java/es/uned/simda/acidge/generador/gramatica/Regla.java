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
 * Clase Regla
 * 
 * Codifica una regla de producción de una gramática
 * Una regla es conjunto de subreglas que son opcionales
 */
package es.uned.simda.acidge.generador.gramatica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class Regla 
{
	private final static Logger logger = Logger.getLogger(Regla.class.getName());

	final static char DEFINE = '=';
	final static char SEPARATOR = '|';
	final static char END = ';'; 
	
	final private String noTerminal;
	
	private ArrayList<Subregla> subreglas;
	
	public Regla(String noTerminal)
	{
		this.noTerminal = noTerminal;
		
		subreglas = new ArrayList<Subregla>();
	}
	
	public int getNumSubreglas() { return subreglas.size(); }
	public String getNoTerminal() { return noTerminal; }
	public Subregla getSubregla(int i) { return subreglas.get(i); }
	public Iterator<Subregla> iterator() { return subreglas.iterator(); }
	
	public void add(Subregla subregla)
	{
		//logger.fine("Regla.add:" + subregla);

		subreglas.add(subregla);
	}
	
	//Optimizado con StringBuilder
	public String toString()
	{
		StringBuilder cad = new StringBuilder(Gramatica.BUILDERLENGTH);
		cad.append(noTerminal).append(" ").append(DEFINE).append(" ");
		
		for(int i = 0; i < subreglas.size(); i++)
		{
			cad.append(subreglas.get(i).toString());
			
			if(i < subreglas.size() - 1)
				cad.append(" ").append(SEPARATOR).append(" ");
			else
				cad.append(END);
		}
		
		return cad.toString();
	}
	
	/*
	public String toString()
	{
		String cad = noTerminal + " " + DEFINE + " ";
		
		for(int i = 0; i < subreglas.size(); i++)
		{
			cad += subreglas.get(i).toString();
			
			if(i < subreglas.size() - 1)
				cad += " " + SEPARATOR + " ";
			else
				cad += END;
		}
		
		return cad;
	}
	*/
}
