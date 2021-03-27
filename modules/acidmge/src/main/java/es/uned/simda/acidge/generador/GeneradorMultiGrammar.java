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
 * Clase GeneradorMultiGrammar
 * 
 * Decodifica un individuo con una lista de gramáticas
 * 
 */

package es.uned.simda.acidge.generador;

import java.io.File;
import java.util.logging.Logger;

import es.uned.simda.acidge.generador.arbol.*;
import es.uned.simda.acidge.generador.gramatica.*;
import es.uned.simda.acidge.ge.GEProperties;

public class GeneradorMultiGrammar extends GeneradorGrammar
{	
	private final static Logger logger = Logger.getLogger(GeneradorMultiGrammar.class.getName());
	final static String newLine = String.format("%n");

	static Gramatica gramaticas[];
	
	//Necesaria para tests
	public static void setGramaticas(Gramatica [] gramas) 
	{ 
		gramaticas = gramas;
		//Evita la aserción de la clase padre
		gramatica = gramaticas[0];
	}
	
	public GeneradorMultiGrammar(byte[] genes, GEProperties geproperties)
	{
		super(genes, geproperties);
	}
	
	//Dummy constructor for loading grammar file
	public GeneradorMultiGrammar(byte [] genes, GEProperties geproperties, String dirgrammar, String grammarFileName)
	{
		super(genes, geproperties);
		
		cargaMultiGrama(dirgrammar, grammarFileName);
	}
	
	public static void cargaMultiGrama(String dir, String GrammarFileName)
	{
		String names[] = GrammarFileName.split(",");
		
		gramaticas = new Gramatica[names.length];
		
		for(int i = 0; i < names.length; i++)
			gramaticas[i] = cargaGramatica(new File(dir + File.separator + names[i]));
	
		//Evita la aserción de la clase padre
		gramatica = gramaticas[0];
	}

	@Override 
	public String genera() throws GenException
	{
		NoTerminal raiz;
		String expresion = "";
		ExpVisitor visitor;
				
		gramatica = gramaticas[0];

		//Carga las variables definidas, en caso de que existan
		//Sólo para la primera gramática
		procesaVariables();
		
		//Se utiliza el símbolo de comienzo de la primera gramática
		raiz = new NoTerminal(gramatica.getSimboloComienzo());

		expande(raiz);
		
		//Depuracion, quitar despues
		//visitor = new ExpVisitor();
		//expresion = (String) visitor.visita(raiz);
		//logger.info("Expresion= " + expresion);
		//Fin depuracion
		
		assert(recursionLevel == 0);
		
		for(int i = 1; i < gramaticas.length; i++)
		{			
			//Utilizamos la siguiente gramática para expandir la expresión recién decodificada
			gramatica = gramaticas[i];
			
			TransVisitor tvisitor = new TransVisitor(gramatica);
		
			raiz = (NoTerminal) tvisitor.visita(raiz);
			
			//logger.fine("expresion[" + i + "]=" + expresion);
			
			//Añade un xopoint si no lo ha añadido la expansión GEXOPoint antes
			if(xopoints.contains(indice) == false)
			{
				logger.info("XOPoint cambio gramática:" + indice);
				xopoints.add(indice);
			}
		
			logger.fine("xopoint:" + indice);
			
			//Expande de nuevo el árbol, sabiendo que parte ya está expandido
			recorre(raiz);
			
			//Depuracion, quitar despues
			//visitor = new ExpVisitor();
			//expresion = (String) visitor.visita(raiz);
			//logger.info("Expresion= " + expresion);
			//Fin depuracion
			
			//logger.info("recursionLevel:" + recursionLevel);
			//Necesario desconectar en esta clase, al no cumplirse 
			//assert(recursionLevel == 0);
		}
		
		visitor = new ExpVisitor();
		
		expresion = (String) visitor.visita(raiz);
		
		//Mejor dejar esta decisión para el operador
		/*
		if(wrapping > 0)
			xopoints = null;
		*/
		
		logger.fine("indice final:" + indice);
		
		return expresion; 
	}
	
	//Permite la expansión de un árbol parcialmente expandido
	public void recorre(Nodo nodo) throws GenException
	{
		//logger.info("recursionLevel:" + recursionLevel);

		//incRecursionLevel();
		
		if(nodo instanceof Terminal )
			return;
		else if(nodo.getNumeroHijos() != 0)
		{
			for(int i = 0; i < nodo.getNumeroHijos(); i++)
				recorre(nodo.getHijo(i));
		}
		else 
		{
			expande((NoTerminal) nodo);
		}
		
		//decRecursionLevel();
	}
	
}
