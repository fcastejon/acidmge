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
 * Clase GeneradorGrammar
 * 
 * Decodifica un Genotipo y genera una expresión según 
 * una gramática definida en datos
 * 
 */

package es.uned.simda.acidge.generador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import es.uned.simda.acidge.generador.arbol.*;
import es.uned.simda.acidge.generador.gramatica.*;
import es.uned.simda.acidge.generador.parser.ASTSyntax;
import es.uned.simda.acidge.generador.parser.EBNFGrammar;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitor;
import es.uned.simda.acidge.generador.parser.EBNFGrammarVisitorAGramatica;
import es.uned.simda.acidge.generador.parser.ParseException;
import es.uned.simda.acidge.ge.GEProperties;

public class GeneradorGrammar extends Generador
{	
	private final static Logger logger = Logger.getLogger(GeneradorGrammar.class.getName());
	
	public final static String GERESULT = "GEResult";
	public final static String GECODONVALUE = "GECodonValue";
	public final static String GEXOMARKER = "GEXOMarker";

	//protected NoTerminal raiz;
	static Gramatica gramatica;
	
	HashMap<String, String> vars;
	
	//Necesaria para tests
	static void setGramatica(Gramatica grama) { gramatica = grama; }
	
	public GeneradorGrammar(byte[] genes, GEProperties geproperties)
	{
		super(genes, geproperties);
		
		//assert(gramatica != null);
		
		vars = new HashMap<String, String>();
	}
	
	//Dummy constructor for loading grammar file
	public GeneradorGrammar(byte [] genes, GEProperties geproperties, String dirgrammar, String grammarFileName)
	{
		super(genes, geproperties);
		
		cargaGrama(new File(dirgrammar + File.separator + grammarFileName));
	}
	
	
	public static void cargaGrama(File gramaFile)
	{
		gramatica = cargaGramatica(gramaFile);
	}
	
	public static void cargaGrama(InputStream is)
	{
		gramatica = cargaGramatica(is);
	}
	
	public String genera() throws GenException
	{
		NoTerminal raiz;
		
		//Carga las variables definidas, en caso de que existan
		procesaVariables();
		
		raiz = new NoTerminal(gramatica.getSimboloComienzo());
		
		expande(raiz);
		
		assert(recursionLevel == 0);
		
		Visitor visitor = new ExpVisitor();
		
		return (String) visitor.visita(raiz);
	}
	
	//El uso de una lista de variables garantiza que se procesarán en el orden
	//en el que aparecen en el fichero de gramática
	void procesaVariables() throws GenException
	{
		if(gramatica.hasVars() == false)
				return;
		
		Iterator<String> iterator = gramatica.varIterator();
		
		while(iterator.hasNext())
		{
			String var = iterator.next();
			
			assert(var.startsWith(ElementoRegla.VAR) == true);
			
			Regla regla = gramatica.getRegla(var);

			assert(regla.getNumSubreglas() == 1);
			
			Subregla subregla = regla.getSubregla(0);

			for(int i = 0; i < subregla.getSize(); i++)
			{
				ElementoRegla el = subregla.getElemento(i);
				assert((el != null) && (el.getTipo() == Expansion.EXPANSION));
				Expansion expansion = (Expansion) el;
			
				if(expansion.getElemento().equals(GECODONVALUE) == true)
				{
					String valor = expandeGECodonValue(expansion);
					vars.put(var, valor);
					logger.fine("Var:" + var + " = " + valor);
				}
				else 
					expandeExpansion((Expansion) el);
			}
		}
	}
	
	
	public void expande(NoTerminal nodo) throws GenException
	{
		incRecursionLevel();
		
		//logger.finest("expande: " + nodo.getNoTerminal());
		
		Regla regla = gramatica.getRegla(nodo.getNoTerminal());
		 
		if(regla == null)
		{
			//logger.severe(nodo.getNoTerminal());
			throw new AssertionError("No hay regla para:" + nodo.getNoTerminal());
		}
		
		//En GE sólo se lee un codón cuando es necesario tomar una decisión
		//Se comprueba si hay más de una regla de producción y se toma un codón en ese caso
		//en caso contrario se selecciona la única regla existente
		int val;
		
		if(regla.getNumSubreglas() > 1)
		{
			int codon;
	
			//Caso general, se toma un codon y se obtiene la regla adecuada
			codon = getCodon();		//Gestión como int para evitar falta de unsigned byte
			val = codon % regla.getNumSubreglas();
			//logger.finer("expande: codon:" + codon + ":opciones:" + regla.getNumSubreglas() + ":resultado:" + val);
		}
		else
		{
			val = 0;
			//logger.finest("expande: no se lee codón");
		}
		
		Subregla subregla = regla.getSubregla(val);
		assert(subregla != null);
		
		//logger.finest("expande: subregla:" + subregla);
		
		for(int i = 0; i < subregla.getSize(); i++)
		{
			ElementoRegla el = subregla.getElemento(i);
			assert(el != null);
			
			switch(el.getTipo())
			{
				case ElementoRegla.TERMINAL:
					//logger.finest("expande: TERMINAL: " + el.getElemento());
					Terminal terminal = new Terminal(nodo, el.getElemento());
					nodo.addHijo(terminal);
					break;
				case ElementoRegla.NOTERMINAL:
					//logger.finest("expande: NOTERMINAL: " + el.getElemento());
					NoTerminal noTerminal = new NoTerminal(nodo, el.getElemento());
					nodo.addHijo(noTerminal);
					expande(noTerminal);
					break;
				case Expansion.EXPANSION:
					Expansion expansion = (Expansion) el;
					if(expansion.getElemento().equals(GECODONVALUE) == true)
					{
						terminal = new Terminal(nodo,expandeGECodonValue(expansion));						
						nodo.addHijo(terminal);
					}
					else 
						expandeExpansion((Expansion) el);
					break;
				case ElementoRegla.VARIABLE:
					//logger.finest("expande: VARIABLE: " + el.getElemento());
					terminal = new Terminal(nodo, vars.get(el.getElemento()));
					nodo.addHijo(terminal);
					break;
				default:
					throw new AssertionError("Valor tipo no valido:" + el.getTipo());
			}
		}
		
		decRecursionLevel();
	}
	
	//Expande expansiones distintas de GECodonValue
	private void expandeExpansion(Expansion expansion) throws GenException
	{
		if(expansion.getElemento().equals(GERESULT) == true)
		{
			//Toma entrada de la lista anterior
			logger.fine("GEResult");
		}
		else if(expansion.getElemento().equals(GEXOMARKER) == true)
		{
			//Marca un XOPoint
			logger.fine("GEXOMarker:" + indice);
			xopoints.add(indice);
		}
		else
			throw new AssertionError("Expansion no válida");
	}
	
	private String expandeGECodonValue(Expansion expansion) throws GenException
	{
		int minval, maxval;
		
		minval = Expression.evalua(expansion.getExpression(0), vars);
		maxval = Expression.evalua(expansion.getExpression(1), vars);
		
		//logger.info("expandeGECodonValue min=" + minval + " max=" + maxval);
		
		assert(minval < maxval);
		
		//Se toma un codón del cromosoma
		int codon = getCodon();		//Gestión como int para evitar falta de unsigned byte
		int valor = codon % (maxval - minval + 1) + minval;
		
		//logger.info("expandeGECodonValue = " + valor + " min=" + minval + " max=" + maxval);
		
		return Integer.toString(valor); 
	}
	
	//Carga desde String
	public static Gramatica cargaGramatica(String gramaticaString)
	{
		logger.info("cargaGramaticaString");
		logger.fine(gramaticaString);

    	StringReader sr = new StringReader(gramaticaString);
	    	
	    assert(sr != null);
		    
	    EBNFGrammar parser = new EBNFGrammar(sr);
	        
		return cargaGrammarConvenience(parser);
	}
	
	//Carga desde Stream
	public static Gramatica cargaGramatica(InputStream gramaticaStream)
	{
		logger.info("cargaGramaticaStream");
		
	    assert(gramaticaStream != null);
		    
	    EBNFGrammar parser = new EBNFGrammar(gramaticaStream);
	        
		return cargaGrammarConvenience(parser);
	}
	
	//Carga desde fichero 
	public static Gramatica cargaGramatica(File grammarFile)
	{
		logger.info("cargaGramaticaFichero: " + grammarFile.getName());
		try
		{
	    	InputStream is = new FileInputStream(grammarFile);

		    assert(is != null);
		    
		    EBNFGrammar parser = new EBNFGrammar(is);
	        		
			return cargaGrammarConvenience(parser);
		}
		catch(FileNotFoundException e)
		{
			//e.printStackTrace();
			throw new AssertionError("cargaGramaticaFichero:" + e.toString());
		}
	}
	
	private static Gramatica cargaGrammarConvenience(EBNFGrammar parser)
	{
		Gramatica grammar;
		try
		{   
			ASTSyntax ast = parser.Syntax();

			EBNFGrammarVisitor v = new EBNFGrammarVisitorAGramatica();
			
			grammar = (Gramatica) ast.jjtAccept(v, null);
			
			String actual = grammar.toString();
								
			logger.finest("gramatica:" + actual);
			
			return grammar;
		}
		catch(ParseException e)
		{
			//e.printStackTrace();
			throw new AssertionError("cargaGramaticaFichero:" + e.toString());
		}
	}
	
}
