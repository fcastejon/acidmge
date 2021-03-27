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
 * ExpVisitor
 * 
 * Construye la expresión con el patrón visitor
 */
package es.uned.simda.acidge.generador.arbol;

import es.uned.simda.acidge.generador.gramatica.Gramatica;


public class ExpVisitor implements Visitor
{
	@Override
	public String visita(Nodo nodo)
	{
		//El patrón Visitor permite que se llame a las clases hijas en linkado en tiempo de ejecución
		//La clase abstracta no debe ser llamada nunca
		throw new AssertionError("Código no alcanzable");
	}
	
	@Override
	public String visita(NoTerminal noTerminal)
	{
		int numHijos = noTerminal.getNumeroHijos();
		StringBuilder cad = new StringBuilder(Gramatica.BUILDERLENGTH);
		
		assert(numHijos > 0);
		
		for(int i = 0; i < numHijos; i++)
		{
			cad.append((String) noTerminal.getHijo(i).acepta(this));	
		}
		
		return cad.toString();
	}
/*
	@Override
	public String visita(NoTerminal noTerminal)
	{
		int numHijos = noTerminal.getNumeroHijos();
		String cad = "";
		
		assert(numHijos > 0);
		
		for(int i = 0; i < numHijos; i++)
		{
			String aux = (String) noTerminal.getHijo(i).acepta(this);	
		
			cad += aux;
		}
		
		return cad;
	}
*/
	
	
	@Override
	public String visita(Terminal terminal)
	{
		return terminal.toString();
	}
}
