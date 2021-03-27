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
 * Implementa un problema remoto
 * 
 * Se llama por RMI desde el programa maestro
 * 
 * Recubre una clase problema con los métodos de llamadas RMI
 */
package es.uned.simda.acidge.rmi;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectIOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import es.uned.simda.acidge.ge.cache.ExpCacheSync;
import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.ProblemFactory;
import es.uned.simda.acidge.problema.Problema;
//import es.uned.simda.acidge.problema.exp.ProblemaPuntos;
import es.uned.simda.acidge.ge.GEProperties;


public class ProblemaServer extends Thread implements ProblemaRemoto 
{
	private final static Logger logger = Logger.getLogger(ProblemaServer.class.getName());

	public static final String ProblemaServerName = "ProblemaRemoto";
	public static final int PORT = 1099;
	private final String nombreServer;
	private final String hostname;
	private final Problema problema;
	
	//Parámetros básicos de comprobación de integridad
	private String ProblemClassName;
	private String CircuitConstructorClassName;
	private String DevEvalClassName;
	private String FunAdaptacionClassName;
	private boolean die;
	private Resultado resultado;
	//private String serverName;
	
	private BlockingQueue<String> cola;
	
 	public ProblemaServer(String host, int serverNumber, GEProperties geproperties) 
	{
 		this.ProblemClassName = geproperties.getProblemClassName();
 		this.CircuitConstructorClassName = geproperties.getCircuitConstructorClassName();
 		this.DevEvalClassName = geproperties.getDevEvalClassName();
 		this.FunAdaptacionClassName = geproperties.getFunAdaptacionClassName();
 		
 		die = false;
 		nombreServer = host + "/" + serverNumber;
 		hostname = host;
 		
 		logger.info("Constructor de ProblemaServer:" + nombreServer);
 		
 		this.setName(nombreServer);
 		
 		ProblemFactory probFactory = ProblemFactory.getProblemFactory();
 		problema = probFactory.createProblema(geproperties.getProblemClassName(), geproperties);
		
	   	Registry registry = null;
	   	
	   	cola = new LinkedBlockingQueue<String>();
	   	
	   	try 
	   	{
	 		ProblemaRemoto stub = (ProblemaRemoto) UnicastRemoteObject.exportObject(this, 0);
	   		
	 		//Sólo crea el registro si es el server número 0
	 		
	 		if(serverNumber == 0)
	 		{
	 			logger.info("Creando registro");
	 			registry = LocateRegistry.createRegistry(PORT);
	 			logger.info("create:" + registry);
	 		}
    	   	
	   		//Inscribe en el Registro
    		registry = LocateRegistry.getRegistry();
    		logger.info("get:" + registry);
	 		
    		registry.bind(nombreServer + "/" + ProblemaServerName, stub);
	    	logger.info(nombreServer + "/" + ProblemaServerName + " levantado");
	    	
	    	this.start();
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
 	
 	public void cerrar()
 	{
 		//Indicamos a la thread de atención que termine
 		die = true;
 		this.interrupt();
 		
   		try 
   		{
   			Registry registry = LocateRegistry.getRegistry();
   			
   			logger.info("unbind");
   			
			registry.unbind(nombreServer);
						
   			logger.info("unexportObject ProblemaServer");
			
			UnicastRemoteObject.unexportObject(this, true);
			
			/*
   			logger.info("unexportObject Registry");
			
			//Desconectamos el propio registro 
			UnicastRemoteObject.unexportObject(registry, true);
			*/
		} 
   		catch (IOException e) 
		{
			logger.severe(e.getClass() + " " + e.getMessage());
		} 
		catch (NotBoundException e) 
		{
			logger.severe(e.getClass() + " " + e.getMessage());
		}
 	}

 	
	@Override
	public void run()
	{
		String exp = "";
		double fitness, canonicalFitness;
		
		while(!die)
		{
			try 
			{
			logger.info("Thread: antes cola.take:" + cola.size());	
			exp = cola.take();
			//logger.info("Thread: despues cola.take:" + cola.size());
			
			if(exp == null)
				throw new AssertionError("exp null");
			
			logger.info("Llega una expresión");

			EvalRes evalRes = problema.evalua(exp);
			//canonicalFitness = problema.getCanonicalFitness();
			
			//logger.info("Enviamos resultado:" + evalRes.getFitness());
			
			logger.info("Antes de enviar a " + nombreServer);
			
			//Se introducen reintentos en caso de ConnectIOException
			boolean res = false;
			do
			{
				try
				{
				resultado.enviaResultado(nombreServer, exp, evalRes);
				res = true;
				//logger.info("Despues de envia");
				}
				catch(ConnectIOException e)
				{
				logger.severe(e.getClass() + " " + e.getMessage() + " causa:" + e.getCause());
				}
			}
			while(!res);
			}
			catch (InterruptedException e)
			{
				//Pinta una traza y vuelve al bucle
	 			logger.severe(e.getClass() + " " + e.getMessage());
			}
			catch (InviableException e) 
			{
				//Es una salida legal, se envía un resultado de InviableException
	 			logger.warning("InviableException:" + e.getMessage());
	 			
 				logger.info("InviableException: antes de enviar a " + nombreServer);
 				//Se introducen reintentos en caso de ConnectIOException
 				boolean res = false;
 				do
 				{
 					try
 					{
 		 			resultado.enviaInviableException(nombreServer, exp);
 		 			//logger.info("Despues de envia");
 					res = true;
 					}
 					catch(ConnectIOException e2)
 					{
 					logger.severe(e2.getClass() + " " + e2.getMessage() + " causa:" + e2.getCause());
 					}
 		 			catch(RemoteException e1)
 		 			{
 			 			logger.severe(e1.toString() + Arrays.toString(e1.getStackTrace()));
 						logger.severe(e1.getCause().toString() + Arrays.toString(e1.getCause().getStackTrace()));
 			 			throw new AssertionError("RemoteException");
 		 			}
 				}
 				while(!res);			
			}
			catch(RemoteException e)
			{
	 			logger.severe(e.toString() + Arrays.toString(e.getStackTrace()));
				logger.severe(e.getCause().toString() + Arrays.toString(e.getCause().getStackTrace()));
	 			throw new AssertionError("RemoteException");
	 		}
			//Las aserciones no funcionan bien con RMI. Esto es un intento de capturar esta situación
			catch(AssertionError e)
			{
				logger.severe(e.toString() + Arrays.toString(e.getStackTrace()));
				logger.severe(e.getCause().toString() + Arrays.toString(e.getCause().getStackTrace()));
	 			throw new AssertionError("AssertionError!!!!!!!");
	 		}
		}
		
		logger.info("Me muero");
	}

 	//Métodos de Problema, se pasan a la clase Problema recubierta
 	
	@Override
	public void evalua(String exp) 
	{
		cola.offer(exp);
		logger.info("ProblemaServer.evalua cola:" + cola.size());
	}

	@Override
	public int getEvaluationCount() 
	{
		return problema.getEvaluationCount();
	}

	@Override
	public void resetEvaluationCount() 
	{
		problema.resetEvaluationCount();
	}
	
	@Override
	public void setGeneracion(int generacion) 
	{
		//Traza de pantalla
		logger.info("ProblemaServer Generacion:" + generacion);
		problema.setGeneracion(generacion);
	}

	@Override
	public void setNombreFecha(String nombreFecha) 
	{
		problema.setNombreFecha(nombreFecha);
	}
	
	@Override
	public boolean sanityCheck(String ProblemClassName, String CircuitConstructorClassName,
			String DevEvalClassName, String FunAdaptacionClassName)
	{
		boolean res; 
		
		if(this.ProblemClassName.equals(ProblemClassName) == false)
		{
			logger.severe("this.ProblemClassName:" + this.ProblemClassName + " ProblemClassName:" + ProblemClassName);
			res = false;
		}
		else if(this.CircuitConstructorClassName.equals(CircuitConstructorClassName) == false)
		{
			logger.severe("this.CircuitConstructorClassName:" + this.CircuitConstructorClassName + " CircuitConstructorClassName:" +
					CircuitConstructorClassName);
			res = false;
		}
		else if(this.DevEvalClassName.equals(DevEvalClassName) == false)
		{
			logger.severe("this.DevEvalClassName:" + this.DevEvalClassName + " DevEvalClassName:" + DevEvalClassName);
			res = false;
		}
		else if(this.FunAdaptacionClassName.equals(FunAdaptacionClassName) == false)
		{
			logger.severe("this.FunAdaptacionClassName:" + this.FunAdaptacionClassName + " FunAdaptacionClassName:" + FunAdaptacionClassName);
			res = false;
		}
		else 
			res = true;
		
		logger.severe("SanityCheck:" + res);
		
		return res;
	}
	
	@Override
	public void bind(String masterHostName)
	{
		logger.info("bind:" + masterHostName);
		ClientFactory clientFactory = ClientFactory.getFactory();
		
		resultado = clientFactory.createClienteResultado(masterHostName); 
		
		if(resultado== null)
		{
			logger.severe("resultado null");
			throw new AssertionError("resultado null");
		}
	}

	//Must not be called
	@Override
	public String build(String exp) 
	{
		throw new AssertionError();
	}
}
