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
 * Clase Spice
 * 
 * Lanza una ejecuciï¿½n del programa NGSPICE para simular un circuito
 */
package es.uned.simda.acidge.spice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class Spice extends Thread
{
	private final static Logger logger = Logger.getLogger(Spice.class.getName());
	final static String newLine = String.format("%n");
	final static String NAME = "Spice";
	
	final static int IDLE = 0;
	final static int WAIT_TIMEOUT = 1;
	
	final static long TIMEOUT = 5000;
	final static long SLICE = 250;

	private static final String SPICE_COMMAND = "bin/spice2.sh";
	private static final String SPICE_FLAGS = "-n -b";
 	final static String cadError1 = "Warning: singular matrix:";
 	final static String cadError2 = "Reference value";
 	final static String cadError3 = "doAnalyses: Too many iterations without convergence";
 	final static byte eol1 = '\n';
 	final static byte eol2 = '\r';
	private boolean die;
	//BufferedInputStream bis;
	Process p;
	int estado;
	long timeout;
	
	String command;
	
	int numProcesos;
	int numTimeout;
	int numError;
 	
 	ProcessBuilder builder;

	public Spice()
	{
	 	//builder = new ProcessBuilder(SPICE_COMMAND);
		builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		
		die = false;
		p = null;
		estado = IDLE;
		timeout = 0;
		
		command = SPICE_COMMAND;
		
		numProcesos = 0;
		numTimeout = 0;
		numError = 0;
		
		this.setName(NAME);
		
		start();
	}
	
	public void setCommand(String command) { this.command = command; }
	
	public synchronized void setTimeout(long tout)
	{
		if(estado != IDLE)
			logger.severe("Estado MAL!!!");
		
		logger.finer("setTimeout:" + tout);
		timeout = tout;
		estado = WAIT_TIMEOUT;
		notify();
	}
	
	public synchronized void paraTimeout()
	{
		logger.fine("paraTimeout");
		p = null;	//Se hace aquí para estar en función sincronizada
		timeout = 0;
		estado = IDLE;
		notify();
	}

	public synchronized void espera(long slice)
	{
		try 
		{
			wait(slice);
		}
		catch (InterruptedException e)
		{
			//Pinta una traza y vuelve al bucle
 			logger.severe(e.getClass() + " " + e.getMessage());
			System.out.print("espera: InterruptedException. " + e.getMessage());
		}
	}
	
	public synchronized void paraProceso()
	{
		
		//try 
		{
			estado = IDLE;
			
			if(p != null)
			{
				logger.fine("paraProceso: matamos proceso");
				p.destroy();
				waitFor();
				p = null;
		        numTimeout++;
			}
			else
			{
				logger.fine("paraProceso: Process null");
			}
		}
		/*
		catch(IOException e)
		{
 			logger.info(e.getClass() + " " + e.getMessage());
 			throw new AssertionError("IOException");
		}
		*/
	}
	
	/*
	 * Cuerpo de una thread para realizar un timeout
	 * Debe ser synchronized, pues entre la lectura del estado y la acción debe haber exclusión 
	 * mutua con el thread principal 
	 */
	@Override
	public synchronized void run()
	{
		logger.info("Spice.run en marcha");
		while(!die)
		{
			if(estado == IDLE)
			{
				logger.fine("Spice.run esperamos setTimeout");
				espera(0);
			}
			else if((estado == WAIT_TIMEOUT) && (timeout <= 0))
			{
				logger.fine("Spice.run paramos proceso");
				paraProceso();
			}
			else if((estado == WAIT_TIMEOUT) && (timeout > 0))
			{
				espera(SLICE);
				timeout -= SLICE;
				logger.fine("Spice timeout:" + timeout);
			} 
		}
	}
		
	public void cerrar()
 	{
 		//Indicamos a la thread de timeout que termine
 		die = true;
 		interrupt();
 	}
	
	//Realiza el arranque del proceso en método atómico
	//Devuelve un stream con la salida del comando
	private synchronized BufferedInputStream arranca(String netListName) throws IOException
	{
		builder.command(command, SPICE_FLAGS, netListName);
		logger.fine("builder.start() command:" + command);
        p = builder.start();
        numProcesos++;

	 	InputStream is = p.getInputStream();
	 	BufferedInputStream bis = new BufferedInputStream(is);
		InputStream es = p.getErrorStream();
		es.close();
		OutputStream os = p.getOutputStream();
		os.close();
		
		return bis;
	}
	
	/*
	 * Lee en modo binario para poder admitir diferentes tipos de end of line
	 */
	public boolean lanza(String netListName, String outFile) 
	{
	 	byte b[] = new byte[1];
	 	int n;
	 	String cad = "";
	 	//StringBuilder cb = new StringBuilder(""); 
	 	
	 	if(builder == null)
			throw new AssertionError("Spice.lanza builder null");

	 	if(estado != IDLE)
	 		throw new AssertionError("Spice.lanza estado:" + estado);
	 	
		try
		{ 				
			/*
			builder.command(command, SPICE_FLAGS, netListName);
			logger.fine("builder.start() command:" + command);
            p = builder.start();
            numProcesos++;
    
    	 	InputStream is = p.getInputStream();
    	 	BufferedInputStream bis = new BufferedInputStream(is);
    	 	
    		File file = new File(outFile);
    		PrintWriter pw = new PrintWriter(file);
    		
    		InputStream es = p.getErrorStream();
    		es.close();
    		OutputStream os = p.getOutputStream();
    		os.close();
    		*/
			
			this.setTimeout(TIMEOUT);
			BufferedInputStream bis = arranca(netListName);
			File file = new File(outFile);
			PrintWriter pw = new PrintWriter(file);
			
		 	try 
		 	{
		 		while((n = bis.read(b, 0, 1)) != -1)
		 		{	 			
		 			if(b[0] == eol2)
		 			{
		 				logger.fine("Encontrado eol2");
		 				logger.fine("cadena:" + cad);
		 			}
		 			
		 			if("Soy un bucle infinito".equals(cad))
		 				logger.fine("cadena:" + cad);
		 			
		 			if((b[0] == eol1) || (b[0] == eol2))
		 			{
		 				if(cad.contains(cadError1))
		 					{
		 					logger.info("Encontrado " + cadError1);
		 					logger.fine("Matando proceso fichero:" + netListName);
		 					p.destroy();
		 		            numError++;
		 					return false;
		 					}
		 				else if(cad.contains(cadError2))
	 					{
		 					logger.info("Encontrado " + cadError2);
	 						logger.fine("Matando proceso fichero:" + netListName);
	 						p.destroy();
		 		            numError++;
	 						return false;
	 					}
		 				else if(cad.contains(cadError3))
	 					{
		 					//Este caso no cuelga Spice
		 					logger.info("Encontrado " + cadError3);
	 						//logger.info("Matando proceso fichero:" + netListName);
	 						//p.destroy();
		 		            numError++;
	 						return false;
	 					}
		 				/* Para prueba de error de invalid parameter
		 				else if(cad.contains("parametros"))
		 				{
		 					logger.info(cad + newLine);
		 					pw.println(cad);
		 					cad = "";
		 				}
		 				else if(cad.contains("invalid"))
		 				{
		 					logger.info(cad + newLine);
		 					pw.println(cad);
		 					cad = "";
		 				}
		 				*/
		 				else
		 				{
		 					pw.println(cad);
		 					cad = "";
		 				}
		 			}
		 			else
		 			{
		 				cad += (char) b[0];
		 			}
		 		}
		 		
		 		if(cad.isEmpty() == false)
		 		{
		 			pw.println(cad);
		 		}
		 	}
			finally
			{
				logger.fine("Cerrando ficheros!!");
				bis.close();
				pw.close();
				bis = null;
				waitFor();
				//La puesta a null se hace en función sincronizada
				//p = null;
				paraTimeout();
			}
		}
		catch(FileNotFoundException e) 
		{
			logger.severe("lanza: FileNotFoundException:" + outFile);
		 	return false;
		}
		catch (IOException ex) 
		{
			logger.severe("lanza: IOException. " + ex.getMessage());
			ex.printStackTrace(System.out);
		 	return false;
		}
		
		return true;
	}
	
	void waitFor()
	{
		if(p==null)
		{
			//logger.info("waitFor p nulo, salimos");
			return;
		}
		
		try
		{
			//logger.info("waitFor");
			int retval = p.waitFor();
			logger.fine("waitFor devuelve:" + retval);
		}
		catch (InterruptedException ex) 
		{
			logger.severe("waitFor: InterruptedException. " + ex.getMessage());
			//TODO quitar
			System.out.print("waitFor: InterruptedException. " + ex.getMessage());
		}
	}
	
	public void logStats()
	{
		logger.info("SpiceStats total:" + numProcesos + ":timeout:" + numTimeout + ":error:" + numError);
	}



}
