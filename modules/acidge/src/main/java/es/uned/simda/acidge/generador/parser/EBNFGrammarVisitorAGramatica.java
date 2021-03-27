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
 * EBNFGrammarVisitorAGramatica
 * 
 * Convierte un AST en un objeto Gramática
 */
package es.uned.simda.acidge.generador.parser;

import es.uned.simda.acidge.generador.gramatica.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class EBNFGrammarVisitorAGramatica implements EBNFGrammarVisitor 
{
	private final static Logger logger = Logger.getLogger(EBNFGrammarVisitorAGramatica.class.getName());
	
	private Gramatica gramatica;
	
	public EBNFGrammarVisitorAGramatica()
	{
		gramatica = null;
	}

	@Override
	public Object visit(SimpleNode node, Object data) 
	{
		throw new AssertionError("SimpleNode no es visitable");
	}

	@Override
	public Object visit(ASTSyntax node, Object data)
	{
	    logger.finest("Syntax:" + node.jjtGetValue());
	    
	    if(gramatica != null)
	    {
	    	logger.severe("Nodo Syntax duplicado");
	    	throw new AssertionError("Nodo Syntax duplicado");
	    }
	     
	    gramatica = new Gramatica();
	    
		int numChildren = node.jjtGetNumChildren();
		logger.finest("Syntax hijos:" + numChildren);
	    
	    for (int i = 0; i < numChildren; ++i) 
	    {
	    	Regla regla = (Regla) node.jjtGetChild(i).jjtAccept(this, null);
	    	
	    	gramatica.add(regla);
	    }
	    
	    return gramatica;
	}

	@Override
	public Regla visit(ASTSyntaxRule node, Object data) 	
	{
		int numChildren = node.jjtGetNumChildren();
		logger.finest("SyntaxRule hijos:" + numChildren);
	    assert(gramatica != null);   
		assert(numChildren == 2);
		assert(node.jjtGetChild(0) instanceof ASTNoTerminal);
			
		String noTerminal = (String) node.jjtGetChild(0).jjtAccept(this, null);
		assert((noTerminal != null) && (noTerminal.isEmpty() == false));

		assert(node.jjtGetChild(1) instanceof ASTRegla);
		Regla regla = (Regla) node.jjtGetChild(1).jjtAccept(this, noTerminal);
		
	    return regla;
	}

	@Override
	public Object visit(ASTRegla node, Object data) 
	{
		logger.finest("Regla:" + node.jjtGetValue());
		assert(gramatica != null);

	    
	    String noTerminal = (String) data;
	    Regla regla = new Regla(noTerminal);
	    
		int numChildren = node.jjtGetNumChildren();
		logger.finest("ASTRegla hijos:" + numChildren);
	    
	    for (int i = 0; i < numChildren; ++i) 
	    {
	    	Subregla subregla = (Subregla) node.jjtGetChild(i).jjtAccept(this, noTerminal);
	    	
	    	regla.add(subregla);
	    }
	    
	    return regla;
	}

	@Override
	public Object visit(ASTSubregla node, Object data) 
	{
		logger.finest("Subregla:" + node.jjtGetValue());
		assert(gramatica != null);

	    
	    String noTerminal = (String) data;
	    Subregla subregla = new Subregla(noTerminal);
	    
		int numChildren = node.jjtGetNumChildren();
		logger.finest("ASTSubregla hijos:" + numChildren);
	    
	    for (int i = 0; i < numChildren; ++i) 
	    {
	    	//En este caso se pasa la subregla para que se añada cada nodo
	    	node.jjtGetChild(i).jjtAccept(this, subregla);
	    }
	    
	    return subregla;
	}

	//Puede ser llamada como parte izquierda o como subregla, se determina por parametro data
	@Override
	public Object visit(ASTNoTerminal node, Object data) 
	{
		logger.finest("NoTerminal:" + node.jjtGetValue());
		assert(gramatica != null);
		assert(node.jjtGetNumChildren() == 0);

	    Subregla subregla = (Subregla) data;
	    
	    String noTerminal = (String) node.jjtGetValue(); 
	    
	    if(subregla != null)
	    {
	    	ElementoRegla er;
	    	//En caso de comenzar por % es una variable
	    	if(noTerminal.startsWith(ElementoRegla.VAR))
	    		er = new ElementoRegla(ElementoRegla.VARIABLE, noTerminal);
	    	else
	    		er = new ElementoRegla(ElementoRegla.NOTERMINAL, noTerminal);
	    
	    	subregla.add(er);
	    }
	   
	    return noTerminal;
	}

	@Override
	public Object visit(ASTTerminal node, Object data) 
	{
		logger.finest("Terminal:" + node.jjtGetValue());
		assert(gramatica != null);
		assert(node.jjtGetNumChildren() == 0);

	    Subregla subregla = (Subregla) data;
	    
	    String terminal = (String) node.jjtGetValue(); 
	    
	    ElementoRegla er = new ElementoRegla(ElementoRegla.TERMINAL, terminal);
	    
	    subregla.add(er);
	   
	    return null;
	}
	
	@Override
	public Object visit(ASTSpecial node, Object data) 
	{
		logger.finest("Special:" + node.jjtGetValue());
		assert(gramatica != null);
		assert(node.jjtGetNumChildren() == 0);

	    Subregla subregla = (Subregla) data;
	    
	    String special = (String) node.jjtGetValue();
	    
	    String terminal = Special.expande(special);
	    
	    ElementoRegla er = new ElementoRegla(ElementoRegla.TERMINAL, terminal);
	    
	    subregla.add(er);
	   
	    return null;
	}

	@Override
	public Object visit(ASTExpansion node, Object data) 
	{
		logger.finest("Expansion:" + node.jjtGetValue());
		assert(gramatica != null);
		assert(data != null);
		
		int numChildren = node.jjtGetNumChildren();
		assert((numChildren == 0) || (numChildren == 2));
		
		logger.finest("ASTExpansion hijos:" + numChildren);

	    Subregla subregla = (Subregla) data;
	    
	    String expansion = (String) node.jjtGetValue(); 
	    
	    Expansion exp = new Expansion(expansion);
	    
	    for (int i = 0; i < numChildren; ++i) 
	    {
		    List<Expression> list = new ArrayList<Expression>();
		    
	    	node.jjtGetChild(i).jjtAccept(this, list);
	    	
		    exp.addExpression(list);
	    }
	    
	    subregla.add(exp);

	    return null;
	}

	//Expresiones limitadas a dos sumandos
	@Override
	public Object visit(ASTExpression node, Object data) 
	{
		logger.finest("Expression:" + node.jjtGetValue());
		assert(gramatica != null);
		//assert(data != null);
		
		int numChildren = node.jjtGetNumChildren();
		assert(numChildren % 2 == 0);
		
		logger.finest("ASTExpression hijos:" + numChildren);
		
		List<Expression> list = (List<Expression>) data;
		if(list != null)
		{
			Expression exp = new Expression((String) null, (String) node.jjtGetValue());
			list.add(exp);
		}
		else
		{
			assert(numChildren == 0);
		}

		//Sólo se soportan dos hijos
    	//El objeto Expression se crea en el padre y se indica llamando al hijo con data=null
	    for (int i = 0; i < numChildren; i+=2) 
	    {
	    	String operator = (String) node.jjtGetChild(i).jjtAccept(this, null);
	    	String val = (String) node.jjtGetChild(i+1).jjtAccept(this, null);
	    	
	    	Expression exp = new Expression(operator, val);
	    	list.add(exp);
	    } 

		return node.jjtGetValue(); 
	}

	@Override
	public Object visit(ASTOperator node, Object data) 
	{
		logger.finest("Operator:" + node.jjtGetValue());
		assert(gramatica != null);
		assert(node.jjtGetNumChildren() == 0);

		return node.jjtGetValue(); 
	}
}
