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
 * ProblemFactory
 * 
 * Utiliza el patrón Simple Factory para la creación del problema concreto a resolver
 */
package es.uned.simda.acidge.problema.dev.eval;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;

public class DevEvalFactory 
{
	private final static Logger logger = Logger.getLogger(DevEvalFactory.class.getName());
	
	static DevEvalFactory factory = null;
	
	//Patrón singleton (no multithread safe)
	public static DevEvalFactory getFactory()
	{
		if(factory == null)
			factory = new DevEvalFactory();
		
		return factory;
	}
	
	private DevEvalFactory() {}
	
	public DevEval createDevEval(String type, GEProperties geproperties) 
	{	
	    Class<?> devEval;
	    DevEval instancia; 
	    
		try 
		{
			devEval = Class.forName(type);
			instancia = (DevEval) devEval.getConstructor(GEProperties.class).newInstance(geproperties);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
}
