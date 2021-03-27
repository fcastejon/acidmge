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
 * ClientFactory
 * 
 * Factoría de clientes para objetos RMI
 * 
 * No es necesario separarlos en clases diferentes
 */
package es.uned.simda.acidge.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;
import es.uned.simda.acidge.ge.GEProperties;



public class ClientFactory 
{
	private final static Logger logger = Logger.getLogger(ClientFactory.class.getName());

	static ClientFactory factory = null;
	
	//Patrón singleton (no multithread safe)
	public static ClientFactory getFactory()
	{
		if(factory == null)
			factory = new ClientFactory();
		
		return factory;
	}
	
	private ClientFactory() {}
	
	public ProblemaRemoto createClienteProblema(String hostServer, int numero, String masterHostName, 
			GEProperties geproperties, boolean bind)
	{
		try 
		{
		    Registry registry = LocateRegistry.getRegistry(hostServer);
		    
		    ProblemaRemoto problema = (ProblemaRemoto) registry.lookup(hostServer + "/" + numero + "/" + ProblemaServer.ProblemaServerName);

		    if(problema.sanityCheck(geproperties.getProblemClassName(), geproperties.getCircuitConstructorClassName(),
		    		geproperties.getDevEvalClassName(), geproperties.getFunAdaptacionClassName()) == false)
		    {
		    	logger.info("Problema inconsistente:" + geproperties.getProblemClassName() + " " + 
		    			geproperties.getCircuitConstructorClassName() +	" " + 
		    			geproperties.getDevEvalClassName());
		    	throw new AssertionError("Problema inconsistente");
		    }
		    
		    if(bind)
		    {
		    	logger.info("Llamando a problema.bind");
		    	problema.bind(masterHostName);
		    }
		    
		    return problema;
		} 
		catch (NotBoundException e)
		 
		{
		    logger.info("NotBoundException: " + e.getMessage());
		    throw new AssertionError("Imposible crear cliente ProblemaRemoto");
		} 
		catch (RemoteException e) 
		{
		    logger.info("RemoteException: " + e.getMessage());
		    throw new AssertionError("Imposible crear cliente ProblemaRemoto");
		}
	}
	
	public Resultado createClienteResultado(String host)
	{
		try 
		{
		    Registry registry = LocateRegistry.getRegistry(host);
		    
		    Resultado resultado = (Resultado) registry.lookup(host + "/" + ResultadoServer.ResultadoServerName);
		    
		    return resultado;
		} 
		catch (NotBoundException e)
		 
		{
		    logger.info("NotBoundException: " + e.getMessage());
		    throw new AssertionError("Imposible crear cliente ResultadoServer");
		} 
		catch (RemoteException e) 
		{
		    logger.info("RemoteException: " + e.getMessage());
		    throw new AssertionError("Imposible crear cliente ResultadoServer");
		}
	}
}
