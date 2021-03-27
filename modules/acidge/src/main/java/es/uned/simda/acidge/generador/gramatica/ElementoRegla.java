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
 * ElementoRegla
 * 
 * Codifica un no terminal o una cadena terminal dentro de una regla
 * 
 */
package es.uned.simda.acidge.generador.gramatica;

import java.util.logging.Logger;

public class ElementoRegla 
{
	private final static Logger logger = Logger.getLogger(ElementoRegla.class.getName());

	public final static int TERMINAL = 0;
	public final static int NOTERMINAL = 1;
	public final static int EXPANSION = 2;
	public final static int VARIABLE = 3;
	final static char comillas = '\'';
	public final static String VAR = "%";
	
	final protected int tipo;
	final protected String elemento;
		
	public ElementoRegla(int tipo, String elemento)
	{
		this.tipo = tipo;
		this.elemento = elemento;
	}
	
	public int getTipo() { return tipo; }
	public String getElemento() { return elemento; }
	
	public String toString() 
	{
		if(tipo == TERMINAL)
			return comillas + elemento + comillas;
		else if(tipo == NOTERMINAL)
			return elemento;
		else if(tipo == VARIABLE)
			return elemento;
		else
			throw new AssertionError("ElementoRegla toString"); 
	}
}
