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
 * Expansion
 * 
 * Subclase de ElementoRegla
 */
package es.uned.simda.acidge.generador.gramatica;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Expansion extends ElementoRegla
{
	private final static Logger logger = Logger.getLogger(Expansion.class.getName());

	final static char exp1 = '<';
	final static char exp2 = '>';
	final static char SEP1 = ':';
	final static char SEP2 = ',';
	final static String GECodonValue = "GECodonValue";
	
	final static int MAXEXPRESSION = 2;
	
	//Sólo para el caso de GECodonValue
	private List<List<Expression>> expression;
	
	public Expansion(String elemento)
	{
		super(EXPANSION, elemento);
		
		expression = new ArrayList<List<Expression>>(MAXEXPRESSION);
	}
	
	//public int getTipo() { return tipo; }
	//public String getElemento() { return elemento; }
	public void setExpression(List<Expression> exp, int pos) 
	{
		assert((pos >= 0) && (pos < MAXEXPRESSION));
		expression.set(pos, exp);
	}
	
	public void addExpression(List<Expression> exp) 
	{
		assert(expression.size() <= MAXEXPRESSION);
		expression.add(exp);
	}
	
	public List<Expression> getExpression(int i) { return expression.get(i); } 
	
	@Override
	public String toString() 
	{
		assert(tipo == EXPANSION);
		StringBuilder cad = new StringBuilder();
		
		if(elemento.equals(GECodonValue))
		{
			cad.append(exp1).append(elemento).append(SEP1);
			cad.append(printList(expression.get(0))).append(SEP2);
			cad.append(printList(expression.get(1))).append(exp2);
		}
		else
			cad.append(exp1).append(elemento).append(exp2);

		return cad.toString();
	}
	
	public String printList(List<Expression> list)
	{
		StringBuilder cad = new StringBuilder();

		Iterator<Expression> it = list.iterator();
		
		while(it.hasNext())
			cad.append(it.next().toString());
	
		return cad.toString();
	}
}
