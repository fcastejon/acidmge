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
 * ProblemaGestorParalelo
 * 
 * Gestiona los servidores paralelos y ofrece una interfaz común Problema
 * 
 * Fenotipo llama a esta clase, que realiza el reparto de tareas en los diferentes servers
 */
package es.uned.simda.acidge.rmi;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import es.uned.simda.acidge.ge.GEProperties;
import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.Problema;


public class ProblemaGestorParalelo implements Problema 
{
	private final static Logger logger = Logger.getLogger(ProblemaGestorParalelo.class.getName());

	ArrayList<Server> servers;
	HashMap<String, Server> serverMap;
	ServerProperties serverProperties;
	
	//El constructor crea los clientes a los servidores remotos y enlaza con los mismos
	public ProblemaGestorParalelo(String hostname, String fileName, GEProperties geproperties)
	{
		//Obtiene los servidores configurados de un fichero de propiedades
		serverProperties = new ServerProperties(fileName);
		ArrayList<String> serverNames = serverProperties.getNames();
		
		servers = new ArrayList<Server>();
		serverMap = new HashMap<String, Server>();
		ClientFactory factory = ClientFactory.getFactory();
		
		for(int i = 0; i < serverNames.size(); i++)
		{
			String serverName = serverNames.get(i);

			Server server = new Server(serverName);
			servers.add(server);
			serverMap.put(serverName, server);
			
			logger.info("Vinculando server:" + serverName);
			ProblemaRemoto problema = factory.createClienteProblema(server.getHost(), server.getNumero(),
					hostname, geproperties, true);
			
			server.setProblema(problema);
		}
		
		logger.info(servers.toString());
	}
	
	public Server getServer(String nombreServer)
	{
		return serverMap.get(nombreServer);
	}
	
	@Override
	public EvalRes evalua(String exp) throws InviableException 
	{
		Iterator<Server> iterator = servers.iterator();
		Server smin = iterator.next();
		assert(smin != null);
		int min = smin.getCarga();
		
		Server s;
		int aux; 
		while(iterator.hasNext())
		{
			s = iterator.next();
			if((aux = s.getCarga()) < min)
			{
				smin = s;
				min = aux;
			}
		}
		
		try
		{
			smin.incCarga();
			smin.getProblema().evalua(exp);
		}
		catch(RemoteException re)
		{
			logger.severe("RemoteException:" + re.getMessage());
		}
		
		return null;
	}

	@Override
	public int getEvaluationCount() 
	{
		int count = 0;
		
		try
		{
			for(Server s: servers)
			{
				count += s.getProblema().getEvaluationCount();
			}
		}
		catch(RemoteException re)
		{
			logger.severe("RemoteException:" + re.getMessage());
		}
		
		return count;
	}
	
	@Override
	public void resetEvaluationCount() 
	{
		try
		{
			for(Server s: servers)
			{
				s.getProblema().resetEvaluationCount();
			}
		}
		catch(RemoteException re)
		{
			logger.severe("RemoteException:" + re.getMessage());
		}
	}
	

	@Override
	public void setGeneracion(int generacion) 
	{
		try
		{
			for(Server s: servers)
			{
				s.getProblema().setGeneracion(generacion);
			}
		}
		catch(RemoteException re)
		{
			logger.severe("RemoteException:" + re.getMessage());
		}
	}

	@Override
	public void setNombreFecha(String nombreFecha) 
	{
		try
		{
			for(Server s: servers)
			{
				s.getProblema().setNombreFecha(nombreFecha + "_" + s.getNumero());
			}
		}
		catch(RemoteException re)
		{
			logger.severe("RemoteException:" + re.getMessage());
		}
	}
	
	@Override
	public String toString()
	{
		String cad = "";
		
		for(Server s: servers)
		{
			cad += s.toString() + " ";
		}
		
		return cad;
	}

	//Must not be called
	@Override
	public String build(String exp) 
	{
		throw new AssertionError();
	}

	//En este caso siempre devuelve -1.0, pues sólo se llama desde Fenotipo en caso asíncrono
	//El valor final vuelve por la interfaz Resultado
	/*
	@Override
	public double getCanonicalFitness() 
	{
		return -1.0;
	}
	*/

}
