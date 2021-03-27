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
 * GeneradorFactory
 * 
 * Utiliza el patrón Simple Factory para la creación del generador concreto a resolver
 */
package es.uned.simda.acidge.generador;

import java.util.Arrays;
import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;


public class GeneradorFactory 
{
	private final static Logger logger = Logger.getLogger(GeneradorFactory.class.getName());
	final static String DIRGRAMMAR = "grammar";
	
	static GeneradorFactory factory = null;

	private String GeneratorClassName;
	private GEProperties geproperties;
	
	//Patrón singleton (no multithread safe)
	public static GeneradorFactory getFactory()
	{
		if(factory == null)
			factory = new GeneradorFactory();
		
		return factory;
	}
	
	public void initClass(String type, GEProperties geproperties, String GrammarFileName)
	{
		logger.severe("initClass: " + GrammarFileName);
		
	    Class<?> generador; 
	    byte [] genes = new byte[0];
	    
	    GeneratorClassName = type;
	    this.geproperties = geproperties;
	    
		try 
		{
			generador = Class.forName(GeneratorClassName);
			generador.getConstructor(byte[].class, GEProperties.class, String.class, String.class).newInstance(genes, geproperties, 
					DIRGRAMMAR, GrammarFileName);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString() + Arrays.toString(e.getStackTrace()));
			logger.severe(e.getCause().toString() + Arrays.toString(e.getCause().getStackTrace()));
			throw new AssertionError(e.getMessage());
		}
	}
	
	private GeneradorFactory() {}
	
	public Generador createGenerador(byte[] genes) 
	{	
	    Class<?> generador;
	    Generador instancia; 
	    
		try 
		{
			generador = Class.forName(GeneratorClassName);
			instancia = (Generador) generador.getConstructor(byte[].class, GEProperties.class).newInstance(genes, geproperties);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
}
