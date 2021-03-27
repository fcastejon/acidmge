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
 * Clase Main
 * 
 * Lee la línea de comandos e instancia un algoritmo de grammatical evolution
 */

package es.uned.simda.acidge.ge;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.uned.simda.acidge.rmi.ProblemaGestorParalelo;
import es.uned.simda.acidge.rmi.ProblemaServer;
import es.uned.simda.acidge.rmi.ResultadoServer;
import es.uned.simda.acidge.util.LoggerStart;


public class Main 
{
	private final static Logger logger = Logger.getLogger(Main.class.getName());

	//private final static String nombreFechaFormat = "yyyyMMdd_HHmmss_SSS";
	private final static String DIRPROPERTIES = "properties";

	private final static String PropertiesFile = DIRPROPERTIES + File.separator + "GE.properties"; 
	private final static String SeverPropertiesFile = DIRPROPERTIES + File.separator + "server.properties"; 
	private final static String SeverLocalPropertiesFile = DIRPROPERTIES + File.separator + "serverLocal.properties"; 
	//private final static String TokenPropertiesFile = DIRPROPERTIES + File.separator + "tokens.properties"; 
	
	//public static Salida salida;
	//public static Salida FitnessFile;
	
	//Si se arranca con la opción -server se marca como true
	static boolean server = false;
	static boolean local = false;
	static String serverName = "";
	static int serverNumber = -1;
	static ProblemaServer problemaServer = null;
	static ResultadoServer resultadoServer = null;
	static ProblemaGestorParalelo gestor = null;
	static double CrossoverRate;
	static double MutationRate;
	static double CrossoverRateGE2;
	static double CrossoverRateGE3;
	static int MedGenesNumber;
	static boolean CrossRateFlag = false;
	static boolean MutRateFlag = false;	
	static boolean CrossRateGE2Flag = false;
	static boolean CrossRateGE3Flag = false;
	static boolean MedGenesFlag = false;

	/*
	 * Función para devolver error de uso
	 */
	static void usage(int error)
	{
		System.err.println("Uso: GE -server -local -s numServer -m MutationRate -c CrossoverRate -g CrossoverRateGE2 -i MedGenesNumber");
		System.exit(error);
	}
	
	/*
	 * Función para parsear los argumentos de línea de comandos
	 * 
	 * Por ahora sobreescribe los valores de parámetros leídos previamente del fichero de properties
	 */
	static void parseArgs(String[] args) //, GEProperties geproperties)
	{
		int i = 0;
		
		while(i < args.length)
		{
			if("-local".compareTo(args[i]) == 0)
			{
				i += 1;
				local = true;
			}
			else if("-server".compareTo(args[i]) == 0)
			{
				i++;
				server = true;
			}
			else if("-s".compareTo(args[i]) == 0)
			{
				serverNumber = Integer.parseInt(args[i + 1]);
				i += 2;
			}
			else if("-c".compareTo(args[i]) == 0)
			{
				CrossoverRate = Double.parseDouble(args[i + 1]);
				CrossRateFlag = true;
				i += 2;
			}
			else if("-g2".compareTo(args[i]) == 0)
			{
				CrossoverRateGE2 = Double.parseDouble(args[i + 1]);
				CrossRateGE2Flag = true;
				i += 2;
			}
			else if("-g3".compareTo(args[i]) == 0)
			{
				CrossoverRateGE3 = Double.parseDouble(args[i + 1]);
				CrossRateGE3Flag = true;
				i += 2;
			}
			else if("-m".compareTo(args[i]) == 0)
			{
				MutationRate = Double.parseDouble(args[i + 1]);
				MutRateFlag = true;
				i += 2;
			}
			else if("-i".compareTo(args[i]) == 0)
			{
				MedGenesNumber = Integer.parseInt(args[i + 1]);
				MedGenesFlag = true;
				i += 2;
			}
			else 
				usage(-1);
		}
	}

	/*
	 * Función que aplica los valores de línea de comandos
	 * que sobreescribe los valores de parámetros leídos previamente del fichero de properties
	 */
	static void aplicaArgs(GEProperties geproperties)
	{
		if(CrossRateFlag)
			geproperties.setCrossoverRate(CrossoverRate);
		
		if(CrossRateGE2Flag)
			geproperties.setCrossoverRateGE2(CrossoverRateGE2);
		
		if(CrossRateGE3Flag)
			geproperties.setCrossoverRateGE3(CrossoverRateGE3);
		
		if(MutRateFlag)
			geproperties.setMutationRate(MutationRate);
		
		if(MedGenesFlag)
		{
			geproperties.setMinGenesNumber(MedGenesNumber/2);
			geproperties.setMaxGenesNumber(MedGenesNumber*3/2);
		}
	}
	
	static String getHostName()
	{
		//Obtenemos el nombre del host
		String hostname;
		try 
		{
			hostname = InetAddress.getLocalHost().getHostName();
			logger.info("IP:" + InetAddress.getLocalHost());
		} 
		catch (UnknownHostException e) 
		{
 			logger.info(e.getClass() + " " + e.getMessage());
 			throw new AssertionError("UnkownHostException");
		}
		
		logger.info("Hostname:" + hostname);
		
		return hostname;
	}
	
	/*
	 * getNombreFecha  crea un nombre de directorio con la fecha, hora, minutos, segundos y milisegundos
	 * 
	 * Se usa en expresiones de desarrollo para almacenar las netlists
	 */
	/*
	static String getNombreFecha() 
	{
        Date now = new Date();
       
        SimpleDateFormat sdf = new SimpleDateFormat(nombreFechaFormat);
        
        String nombreFecha = sdf.format(now);
        
        logger.severe(nombreFecha);
        
        return nombreFecha;
	}
	*/
	/*
	 * Realiza la inicialización de servicios comunes, instancia y ejecuta el algoritmo GE
	 */
	public static void main(String args[])
	{
		GEProperties geproperties;
		GE ge;
		
		//Comprobación de ejecución con assert activada
		es.uned.simda.acidge.util.Util.checkEA();
		
		//
		String classpath = java.lang.System.getProperty( "java.class.path" );
		System.out.println ("Classpath:" + classpath);
		for (String path : classpath.split(System.getProperty("path.separator"))){
		   File f = new File (path);
		   String resource = (f.isDirectory()?Arrays.asList( f.list()).toString():f.toString());
		   System.out.println (resource);
		}
		//

		parseArgs(args);
		
		//En el caso de server se inicializa con el fichero de propiedades logger.properties
		if(server)
			LoggerStart.setupFromFile();
		else
		{
			//Debe hacerse lo antes posible para asignar el nombre adecuado al fichero
			//LoggerStart.setup(Level.FINER);
			LoggerStart.setup(Level.INFO);
		}
		
		geproperties = new GEProperties(PropertiesFile);

		aplicaArgs(geproperties);
		
		geproperties.printProperties();
		
		String hostname = getHostName();
		
		if(!server)
		{			
			ge = new GE(geproperties);
			
			if(geproperties.isParalelismo())
			{
				String file;
				
				if(local)
					file = SeverLocalPropertiesFile;
				else
					file = SeverPropertiesFile;
				
				resultadoServer = new ResultadoServer(hostname, Fenotipo.getExpCache());

				ProblemaGestorParalelo gestor = new ProblemaGestorParalelo(hostname, file, geproperties);
				
				ge.setProblema(gestor);
				resultadoServer.setGestor(gestor);
								
				//Conveniencia para creación de directorios sólo en caso de desarrollo de circuitos				
				gestor.setNombreFecha(LoggerStart.getNombreFecha());
			}
			
			//long comienzo = System.currentTimeMillis();
		
			//Ejecución del algoritmo
			ge.ejecuta();
			
			//long tiempo = System.currentTimeMillis() - comienzo;
			//logger.info("Tiempo total:" + tiempo);
			
			if(geproperties.isParalelismo())
			{
				resultadoServer.cerrar();
			}
		}
		else
		{ 
			assert(geproperties.isParalelismo());
			
			problemaServer = new ProblemaServer(hostname, serverNumber,	geproperties);
		}
		
		logger.info("Terminando main");
		
		if(!server)
		{
			//Por uso de servidores RMI, no es posible salir sin hacer exit
			System.exit(0);
		}
	}
}
