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
 * Expression
 * 
 * Codifica expresiones dentro de una expansion GECodonValue
 * 
 */
package es.uned.simda.acidge.generador.gramatica;

import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Logger;

public class Expression 
{
	private final static Logger logger = Logger.getLogger(Expression.class.getName());

	public final static int VAR = 0;
	public final static int VAL = 1;
	//public final static String STVAR = "%";
	
	public final static int NONE = 0;
	public final static int PLUS = 1;
	public final static int MINUS = 2;
	public final static String STPLUS = "+";
	public final static String STMINUS = "-";
	
	final private int type;
	final private int operator;
	final private String val;
	final private String var;
	
	public Expression(String operator, String operand)
	{
		if(operand.startsWith(ElementoRegla.VAR))
			{
			type = VAR;
			var = operand;
			val = "";
			}
		else 
			{
			type = VAL; 
			val = operand;
			var = null;
			}
		
		this.operator = operatorRead(operator);
	}
	
	public Expression(int operator, String var)
	{
		this.type = VAR;
		this.operator = operator;
		this.var = var;
		this.val = "";
	}
	
	public int getType() { return type; }
	public int getOperator() { return operator; }
	public String getVal() { return val; }
	public String getVar() { return var; }
	
	private int operatorRead(String op)
	{ 
		if((op == null) || (op.equals("")))
			return NONE;
		else if(op.equals(STPLUS))
			return PLUS;
		else if(op.equals(STMINUS))
			return MINUS;
		else 
			throw new AssertionError("Expression: cadena de operador inválido");
	}
	
	public String toString() 
	{
		StringBuilder cad = new StringBuilder();
		
		switch(type)
		{
		case VAL:
			cad.append(opToString()).append(val);
			break;
		case VAR:
			cad.append(opToString()).append(var);
			break;
		default:
			throw new AssertionError("Expression: tipo de operador inválido");
		}
		
		return cad.toString();
	}
	
	public String opToString()
	{ 
		switch(operator)
		{
		case NONE:
			return "";
		case PLUS:
			return STPLUS;
		case MINUS:
			return STMINUS;
		default:
			throw new AssertionError("Expression: tipo de operador inválido");
		}
	}
	
	//Función de utilidad para evaluar una expresión
	//Recibe la expresión y la lista de variables
	public static int evalua(List<Expression> expression, HashMap<String, String> vars)
	{
		int valor = 0;
		Iterator<Expression> iterator = expression.iterator();
		Expression exp;

		assert(iterator.hasNext());
		exp = iterator.next();
		assert(exp.getOperator() == NONE);
		valor = evalua(exp, vars);
		
		while(iterator.hasNext())
		{
			exp = iterator.next();
			
			switch(exp.getOperator())
			{
			case PLUS:
				valor += evalua(exp, vars);
				break;
			case MINUS:
				valor -= evalua(exp, vars);
				break;
			case NONE:
			default:
				throw new AssertionError("Expression.evalua:valor no valido");
			}
		}
		
		return valor;
	}
	
	//Función de utilidad para evaluar una expresión parcial
	//Recibe la expresión y la lista de variables
	private static int evalua(Expression exp, HashMap<String, String> vars)
	{
		int valor;
		
		switch(exp.getType())
		{
		case VAL:
			valor = Integer.parseInt(exp.getVal());
			break;
		case VAR:
			valor = Integer.parseInt(vars.get(exp.getVar()));
			break;
		default:
			throw new AssertionError("Expression.evalua:valor no valido");
		}
	
		return valor;
	}
	
}
