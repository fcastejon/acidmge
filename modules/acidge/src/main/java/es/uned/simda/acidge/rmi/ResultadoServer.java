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
 * Implementa un ResultadoServer
 * 
 * Se llama por RMI desde el programa maestro
 */
package es.uned.simda.acidge.rmi;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import es.uned.simda.acidge.ge.Fenotipo;
import es.uned.simda.acidge.ge.cache.ExpCache;
import es.uned.simda.acidge.ge.cache.ExpCacheSync;
import es.uned.simda.acidge.problema.EvalRes;


public class ResultadoServer implements Resultado
{
	private final static Logger logger = Logger.getLogger(ResultadoServer.class.getName());

	public static final String ResultadoServerName = "ResultadoServer";
	
	ExpCache expCache;
	ProblemaGestorParalelo gestor;
	String serverName;

 	public ResultadoServer(String host, ExpCache expCache)
	{
 		this.expCache = expCache;		

		if(expCache instanceof ExpCacheSync == false)
			throw new AssertionError("expCache no es instancia de ExpCacheSync");
 		
 		gestor = null;
 		
 		serverName = host + "/" + ResultadoServerName;
 		
	   	Registry registry = null;
	    	
	   	try 
	   	{
	 		Resultado stub = (Resultado) UnicastRemoteObject.exportObject(this, 0);
	   		
	   		//Inscribe en el Registro
    		registry = LocateRegistry.getRegistry();
    		registry.bind(serverName, stub);
	    	logger.info(serverName + " levantado");
	   	} 
    	catch (IOException e) 	
    	{
    		logger.severe("IOException: " + e.getMessage());
    	} 
    	catch (AlreadyBoundException e) 
    	{
    		logger.severe("AlreadyBoundException: " + e.getMessage());
		}	
    }
 	
 	public void setGestor(ProblemaGestorParalelo gestor) { assert(gestor != null); this.gestor = gestor; }
 	
	public void cerrar()
	{
		try 
		{
    		Registry registry = LocateRegistry.getRegistry();
			registry.unbind(serverName);
		} 
		catch (IOException e) 
		{
			logger.severe(e.getMessage());
		} 
		catch (NotBoundException e) 
		{
			logger.severe(e.getMessage());
		}
	}

	@Override
	public void enviaResultado(String nombreServer, String expresion, EvalRes evalRes) 
	{
		logger.info("enviaResultado1:" + nombreServer);
		Server server = gestor.getServer(nombreServer);

		//logger.info("enviaResultado2:" + nombreServer);

		if(server == null)
			throw new AssertionError("server null");

		//logger.info("enviaResultado3:" + nombreServer);

		server.decCarga();
		
		//logger.info("enviaResultado4:" + nombreServer);

		//fitness = Fenotipo.corrige(fitness);
		//canonicalFitness = Fenotipo.corrige(canonicalFitness);
		
		expCache.add(expresion, evalRes);
		
		//logger.info("enviaResultado5:" + nombreServer);
	}

	@Override
	public void enviaInviableException(String nombreServer, String expresion) throws RemoteException 
	{
		logger.info("enviaInviableException1:" + nombreServer);
		Server server = gestor.getServer(nombreServer);
		
		//logger.info("enviaInviableException2:" + nombreServer);

		if(server == null)
			throw new AssertionError("server null");
		
		//logger.info("enviaInviableException3:" + nombreServer);

		server.decCarga();

		//logger.info("enviaInviableException4:" + nombreServer);
		
		EvalRes evalRes = new EvalRes(EvalRes.PenalizacionNaN);
		expCache.add(expresion, evalRes);
		
		//logger.info("enviaInviableException5:" + nombreServer);
	}
}
