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
package es.uned.simda.acidge.problema;

import java.util.logging.Logger;

import es.uned.simda.acidge.ge.GEProperties;


public class ProblemFactory 
{
	private final static Logger logger = Logger.getLogger(ProblemFactory.class.getName());
	static ProblemFactory factory = null;
	
	//Patrón singleton (no multithread safe)
	public static ProblemFactory getProblemFactory()
	{
		if(factory == null)
			factory = new ProblemFactory();
		
		return factory;
	}
	
	private ProblemFactory() {}
	
	public ProblemaSC createProblema(String type, GEProperties geproperties) 
	{	
	    Class<?> problema;
	    ProblemaSC instancia; 
	    
		try 
		{
			problema = Class.forName(type);
			instancia = (ProblemaSC) problema.getConstructor(GEProperties.class).newInstance(geproperties);
		} 
	    catch (Exception e) 
	    {
			logger.severe(e.toString());
			throw new AssertionError(e.getMessage());
		}
	    return instancia;
	}
	
	/*
	public ProblemaSC creaProblema(int FitnessType, int TipoCirConstructor,
			String DevEvalClassName, double UmbralError, int tipoFunAdaptacion)
	{
		ProblemaSC problema = null;
			
		switch(FitnessType)
		{
			case 1:
				problema = new ProblemaPolCuad();
				break;
			case 2:
				problema = new ProblemaExponencial();
				break;
			case 3:
				problema = new ProblemaSeno();
				break;
			case 4:
				problema = new ProblemaPuntos();
				break;
			case 5:
				problema = new ProblemaPolinomioGE(); ;
				break;
			case 6:
				problema = new ProblemaDev(TipoCirConstructor, DevEvalClassName, UmbralError, tipoFunAdaptacion);
				break;
			default:
				//código no alcanzable
				throw new AssertionError("Código no alcanzable" + 
						FitnessType);
		}
		
		return problema;
	}
	*/
}
