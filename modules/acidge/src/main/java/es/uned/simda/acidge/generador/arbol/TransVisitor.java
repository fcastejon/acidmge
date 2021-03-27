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
 * TransVisitor
 * 
 * Modifica un árbol de acuerdo a la siguiente gramática en un esquema multigramática
 * 
 * Cambia nodos terminales por no terminales si hay una regla en la gramática
 */
package es.uned.simda.acidge.generador.arbol;

import java.util.logging.Logger;

import es.uned.simda.acidge.generador.GeneradorGrammar;
import es.uned.simda.acidge.generador.gramatica.Gramatica;
import es.uned.simda.acidge.generador.gramatica.Regla;

public class TransVisitor implements Visitor
{
	private final static Logger logger = Logger.getLogger(GeneradorGrammar.class.getName());

	final Gramatica gramatica;
	
	public TransVisitor(Gramatica gramatica)
	{
		this.gramatica = gramatica;
	}
	
	@Override
	public Nodo visita(Nodo nodo)
	{
		//El patrón Visitor permite que se llame a las clases hijas en linkado en tiempo de ejecución
		//La clase abstracta no debe ser llamada nunca
		throw new AssertionError("Código no alcanzable");
	}
	
	@Override
	public Nodo visita(NoTerminal noTerminal)
	{
		int numHijos = noTerminal.getNumeroHijos();
		assert(numHijos > 0);
		
		//Visita los hijos y actualiza los mismos en caso de que cambien a noTerminal
		for(int i = 0; i < numHijos; i++)
		{
			Nodo hijo = (Nodo) noTerminal.getHijo(i).acepta(this);
			
			if(noTerminal.getHijo(i) != hijo)
				noTerminal.setHijo(i, hijo);
		}
		
		return noTerminal;
	}

	@Override
	public Nodo visita(Terminal terminal)
	{
		Nodo nodo;
		//Busca una regla de no terminal en la nueva gramática para este terminal en la gramática anterior
		Regla regla = gramatica.getRegla(terminal.getTerminal());

		if(regla != null)
		{
			//logger.info("TransVisitor CAMBIA:" + terminal.getTerminal());
			
			nodo = new NoTerminal(terminal.getPadre(), terminal.getTerminal());
		}
		else
		{
			//logger.info("TransVisitor NO cambia:" + terminal.getTerminal());
			
			nodo = terminal;
		}

		return nodo;
	}
}
