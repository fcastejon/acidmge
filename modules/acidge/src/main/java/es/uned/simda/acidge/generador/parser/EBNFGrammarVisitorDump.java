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
 * Esta clase visita el AST para volcar el árbol en depuración
 */
package es.uned.simda.acidge.generador.parser;

import java.util.logging.Logger;


public class EBNFGrammarVisitorDump implements EBNFGrammarVisitor 
{
	private final static Logger logger = Logger.getLogger(EBNFGrammarVisitorDump.class.getName());

	@Override
	public Object visit(SimpleNode node, Object data) 
	{
		throw new AssertionError("SimpleNode no es visitable");
	    //return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSyntax node, Object data) 
	{
	    logger.info("Syntax:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSyntaxRule node, Object data) 	
	{
	    logger.info("SyntaxRule:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRegla node, Object data) 
	{
	    logger.info("Regla:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSubregla node, Object data) 
	{
	    logger.info("Subregla:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNoTerminal node, Object data) 
	{
	    logger.info("NoTerminal:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTerminal node, Object data) 
	{
	    logger.info("Terminal:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}
	
	@Override
	public Object visit(ASTSpecial node, Object data) 
	{
	    logger.info("Special:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTExpression node, Object data) 
	{
	    logger.info("Expression:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTExpansion node, Object data) 
	{
	    logger.info("Expansion:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOperator node, Object data) 
	{
	    logger.info("Expansion:" + node.jjtGetValue());
	    return node.childrenAccept(this, data);
	}
}
