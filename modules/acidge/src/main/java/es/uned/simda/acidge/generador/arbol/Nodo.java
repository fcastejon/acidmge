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
 * Clase Nodo 
 * 
 * Para el generador basado en gramática
 * 
 * clase base de un árbol de parseado generado 
 */
package es.uned.simda.acidge.generador.arbol;

import java.util.ArrayList;


public abstract class Nodo 
{
	private final Nodo padre;
	private ArrayList<Nodo> hijos;
	//private final GeneradorExp generador; 
	private final int nivel; 
	
	public Nodo getPadre() { return padre; }
	//public void setPadre(Nodo padre) { this.padre = padre; }
	
	public void addHijo(Nodo nodo) { hijos.add(nodo); }
	public Nodo getHijo(int i) { return hijos.get(i); }
	public void setHijo(int i, Nodo nodo) { hijos.set(i, nodo); }
	public int getNumeroHijos() { return hijos.size(); }
	
	//GeneradorExp getGenerador() { return generador; }
	//void setGenerador(GeneradorExp generador) { this.generador = generador; }
	public int getNivel() { return nivel; }

	public Nodo() //GeneradorExp generador)
	{
		padre = null;
		hijos = new ArrayList<Nodo>();
		//this.generador = generador;
		nivel = 1;
	}
			
	public Nodo(Nodo padre)
	{
		assert(padre != null);
		this.padre = padre;
		hijos = new ArrayList<Nodo>();
		//generador = padre.getGenerador();
		nivel = padre.getNivel() + 1;
	}

	public Object acepta(Visitor visitor)
	{
		return visitor.visita(this);
	}
}
