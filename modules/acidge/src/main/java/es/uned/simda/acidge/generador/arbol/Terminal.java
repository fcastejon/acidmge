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
 * Clase Terminal
 * 
 */
package es.uned.simda.acidge.generador.arbol;


public class Terminal extends Nodo 
{
	private String terminal;
	
	public Terminal(Nodo padre, String terminal)
	{
		super(padre);
		this.terminal= terminal;
	}
	
	public void addHijo(Nodo nodo) { throw new AssertionError("addHijo: Operación inválida"); }
	
	public String getTerminal() { return terminal; }
	
	public String toString()
	{
		return terminal;
	}
	
	@Override
	public Object acepta(Visitor visitor)
	{
		return visitor.visita(this);
	}
}
