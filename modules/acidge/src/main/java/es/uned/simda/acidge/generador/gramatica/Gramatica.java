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
 * Clase Gramática
 * 
 * Contiene una gramática válida para la generación de expresiones con GE
 */

package es.uned.simda.acidge.generador.gramatica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class Gramatica 
{
	private final static Logger logger = Logger.getLogger(Gramatica.class.getName());
	public final static int BUILDERLENGTH = 500;
	//public final static String VAR = "%";

	private String simboloComienzo;
	final static String newLine = String.format("%n");
	private List<String> vars;

	//Set<String> terminales;		//Puede no ser necesario
	//Set<String> noTerminales;
	
	private HashMap<String, Regla> reglas;
	
	public Gramatica() 
	{
		reglas = new HashMap<String, Regla>();
		vars = new LinkedList<String>(); 
	}

	public String getSimboloComienzo() { return simboloComienzo; }
	public boolean hasVars() { return !vars.isEmpty(); }
	public Iterator<String> varIterator() { return vars.iterator(); }

	//Busca una regla existente y la crea en caso contrario, utilidad para funciones add()
	private Regla getCreateRegla(String noTerminal)
	{
		//El símbolo de comienzo es el primer no terminal definido, se ignoran las reglas anteriores que sean una variable
		/*
		if(reglas.isEmpty())
			simboloComienzo = noTerminal;
		*/
		if((simboloComienzo == null) && (noTerminal.startsWith(ElementoRegla.VAR) == false))
			{
			simboloComienzo = noTerminal;
			}
		
		//Si la regla es de una variable, se anota como tal
		if(noTerminal.startsWith(ElementoRegla.VAR))
			{
			vars.add(noTerminal);
			}
		
		Regla regla;
		
		//Comprueba si existen reglas para el noTerminal indicado y la crea en caso contrario
		if((regla = reglas.get(noTerminal)) == null)
		{
			//logger.fine("getCreateRegla: crea Regla para simbolo: " + noTerminal);
			
			regla = new Regla(noTerminal);
			reglas.put(noTerminal, regla);
		}

		return regla;
	}
	
	public void add(Regla reg)
	{
		assert(reg != null);

		//logger.fine("add_regla:" + reg);
		
		String noTerminal = reg.getNoTerminal();
		
		//El símbolo de comienzo es el primer no terminal definido, se ignoran las reglas anteriores que sean una variable
		/* Se hace en getCreateRegla
		if((simboloComienzo == null) && (noTerminal.startsWith(VAR) == false))
			{
			simboloComienzo = noTerminal;
			}
		*/
		
		Regla regla = getCreateRegla(noTerminal);
		
		Iterator<Subregla> iterator = reg.iterator();
		
		while(iterator.hasNext())
		{
			regla.add(iterator.next());
		}
	}
	
	public void add(Subregla subregla)
	{
		assert(subregla != null);

		//logger.fine("add_subregla:" + subregla);
		
		String noTerminal = subregla.getNoTerminal();
		
		Regla regla = getCreateRegla(noTerminal);

		regla.add(subregla);
	}
	
	public Regla getRegla(String noTerminal)
	{
		return reglas.get(noTerminal);
	}
	
	public String toString()
	{
		String res = "";
		
		Set<String> keySet = reglas.keySet();
		List<String> list = new ArrayList<String>(keySet);     
		Collections.sort(list);
			
		for(String noTerminal : list)
		{
			logger.fine(noTerminal);
			Regla regla = reglas.get(noTerminal); 
			res += regla.toString() + newLine;
		}
		
		return res;
	}
	
}
