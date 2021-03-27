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
 * Class: OperatorFactory
 * 
 * Instantiates crossover, mutation, population initialization and survivor selection operators
 */
package es.uned.simda.acidge.ge.operadores;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.ge.random.RandomGenerator;

public class OperatorFactory 
{
	private final static Logger logger = Logger.getLogger(OperatorFactory.class.getName());

	static OperatorFactory factory = null;
	
	//Patrón singleton (no multithread safe)
	public static OperatorFactory getOperatorFactory()
	{
		if(factory == null)
			factory = new OperatorFactory();
		
		return factory;
	}
	
	private OperatorFactory() {}
	
	public OperadorRecombinacion createOperadorRecombinacion(String type, 
			GEProperties geproperties, RandomGenerator randomGenerator) 
	{	
	    Class<?> operadorRecombinacion;
	    OperadorRecombinacion instancia; 
	    
		try 
		{
			operadorRecombinacion = Class.forName(type);
			instancia = (OperadorRecombinacion) operadorRecombinacion.getConstructor(GEProperties.class,
					RandomGenerator.class).newInstance(geproperties, randomGenerator);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
		
	public OperadorMutacion createOperadorMutacion(String type, 
			GEProperties geproperties, RandomGenerator randomGenerator) 
	{	
	    Class<?> operadorMutacion;
	    OperadorMutacion instancia; 
	    
		try 
		{
			operadorMutacion = Class.forName(type);
			instancia = (OperadorMutacion) operadorMutacion.getConstructor(GEProperties.class,
					RandomGenerator.class).newInstance(geproperties, randomGenerator);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
	
	public OperadorDuplicacion createOperadorDuplicacion(String type, 
			GEProperties geproperties, RandomGenerator randomGenerator) 
	{	
	    Class<?> operadorDuplicacion;
	    OperadorDuplicacion instancia; 
	    
		try 
		{
			operadorDuplicacion = Class.forName(type);
			instancia = (OperadorDuplicacion) operadorDuplicacion.getConstructor(GEProperties.class,
					RandomGenerator.class).newInstance(geproperties, randomGenerator);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
	
	public Selector createSelector(String type, GEProperties geproperties, RandomGenerator randomGenerator) 
	{	
	    Class<?> selector;
	    Selector instancia; 
	    
		try 
		{
			selector = Class.forName(type);
			instancia = (Selector) selector.getConstructor(GEProperties.class,
					RandomGenerator.class).newInstance(geproperties, randomGenerator);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
	
	public OperadorInicializacion createOperadorInicializacion(String type, 
			GEProperties geproperties, RandomGenerator randomGenerator) 
	{	
	    Class<?> operadorInicializacion;
	    OperadorInicializacion instancia; 
	    
		try 
		{
			operadorInicializacion = Class.forName(type);
			instancia = (OperadorInicializacion) operadorInicializacion.getConstructor(GEProperties.class,
					RandomGenerator.class).newInstance(geproperties, randomGenerator);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
	
	public OperadorReemplazo createOperadorReemplazo(String type, 
			GEProperties geproperties) 
	{	
	    Class<?> operadorReemplazo;
	    OperadorReemplazo instancia; 
	    
		try 
		{
			operadorReemplazo = Class.forName(type);
			instancia = (OperadorReemplazo) operadorReemplazo.getConstructor(GEProperties.class).newInstance(geproperties);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
}
