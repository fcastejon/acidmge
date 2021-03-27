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
 * Subregla
 * 
 * 
 */
package es.uned.simda.acidge.generador.gramatica;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Subregla 
{
	private final static Logger logger = Logger.getLogger(Subregla.class.getName());

	final private String noTerminal;
	
	private ArrayList<ElementoRegla> exp;
	
	public Subregla(String noTerminal)
	{
		this.noTerminal = noTerminal;
		
		exp = new ArrayList<ElementoRegla>();
	}
	
	public int getSize() { return exp.size(); }
	public ElementoRegla getElemento(int i) { return exp.get(i); }
	public String getNoTerminal() { return noTerminal; }
	
	public void add(ElementoRegla elementoRegla)
	{
		//logger.fine("Subregla.add:" + elementoRegla);
		
		exp.add(elementoRegla);
	}
	
	//Optimizado con StringBuilder
	public String toString()
	{
		StringBuilder cad = new StringBuilder(Gramatica.BUILDERLENGTH);
		
		for(int i = 0; i < exp.size(); i++)
		{
			cad.append(exp.get(i).toString());
			
			if(i < exp.size()-1)
				cad.append(", ");
		}
		
		return cad.toString();
	}
	
	/*
	public String toString()
	{
		String cad = "";
		
		for(int i = 0; i < exp.size(); i++)
		{
			cad += exp.get(i).toString();
			
			if(i < exp.size()-1)
				cad += ", ";
		}
		
		return cad;
	}*/
}
