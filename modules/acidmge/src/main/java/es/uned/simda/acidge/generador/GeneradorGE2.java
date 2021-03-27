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
 * Clase GeneradorGE2
 * 
 * Decodifica una gramática de la solución y luego la usa para decodificar el individuo
 * 
 */

package es.uned.simda.acidge.generador;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import es.uned.simda.acidge.generador.arbol.*;
import es.uned.simda.acidge.generador.gramatica.*;
import es.uned.simda.acidge.ge.GEProperties;

public class GeneradorGE2 extends GeneradorGrammar
{	
	private final static Logger logger = Logger.getLogger(GeneradorGE2.class.getName());
	final static String newLine = String.format("%n");
	
	//Gramática universal
	static Gramatica gramUni;
	
	public GeneradorGE2(byte[] genes, GEProperties geproperties)
	{
		super(genes, geproperties);
	}
	
	//Dummy constructor for loading grammar file
	public GeneradorGE2(byte [] genes, GEProperties geproperties, String dirgrammar, String grammarFileName)
	{
		super(genes, geproperties);
		
		cargaGramaGE2(new File(dirgrammar + File.separator + grammarFileName));
	}
	
	public static void cargaGramaGE2(File gramaFile)
	{
		gramUni = cargaGramatica(gramaFile);
	
		//Evita la aserción de la clase padre
		gramatica = gramUni;
	}
	
	public static void cargaGramaGE2(InputStream is)
	{
		gramUni = cargaGramatica(is);
	
		//Evita la aserción de la clase padre
		gramatica = gramUni;
	}
	
	@Override 
	public String genera() throws GenException
	{
		String gramaticaSolucion;
		NoTerminal raiz;
		
		gramatica = gramUni;

		//Carga las variables definidas, en caso de que existan
		//Sólo para la gramática universal
		procesaVariables();
		
		//La gramática en esta clase es la gramática universal y debe decodificarse primero
		raiz = new NoTerminal(gramatica.getSimboloComienzo());
		
		expande(raiz);
		
		assert(recursionLevel == 0);
		
		Visitor visitor = new ExpVisitor();
		
		gramaticaSolucion = (String) visitor.visita(raiz);
		
		logger.fine("Gramática decodificada" + newLine + gramaticaSolucion);
		
		//indGE2 = indice;
		xopoints.add(indice);

		logger.fine("indGE2:" + indice);
		
		gramatica = cargaGramatica(gramaticaSolucion);
		
		//Expandimos la netlist con la gramática de la solución decodificada
		raiz = new NoTerminal(gramatica.getSimboloComienzo());
		
		expande(raiz);
		
		assert(recursionLevel == 0);
		
		//Mejor dejar esta decisión para el operador
		/*
		if(wrapping > 0)
			xopoints = null;
		*/
		
		visitor = new ExpVisitor();
		
		return (String) visitor.visita(raiz);
	}
	
}
