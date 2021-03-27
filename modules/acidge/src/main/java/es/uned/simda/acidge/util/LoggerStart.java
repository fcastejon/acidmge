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
 * Clase LoggerStart
 *
 * Esta clase estática inicializa el sistema de trazas 
 * 
 * Para usar el class loader, la clase se instancia a sí misma
 * 
 */
package es.uned.simda.acidge.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import es.uned.simda.acidge.util.CustomFormatter;


public class LoggerStart 
{
	private final static String PROPERTIESFILE = "logging.properties";
	private final static String TESTPROPERTIESFILE = "testLogging.properties";
	private final static String LOGGER = "";		//Anonymous logger
	private final static String nombreFechaFormat = "yyyyMMdd_HHmmss_SSS";
	private final static String FileHandlerPatternProperty = "java.util.logging.FileHandler.pattern";
	private final static String DefaultFileTemp = "logs/salida_%.log";
	
	private static String nombreFecha = "";

	//Arranque completo
	public static void setup() { setup(Level.INFO); }
	
	public static void setup(Level level)
	{
		//new LoggerStart().setupFromFile();
		customSetup(level);
	}

	public static void setupFromFile()
	{
		new LoggerStart().setupFromFileInterna(PROPERTIESFILE);
	}
	
	public static void setupFromFile(String file)
	{
		new LoggerStart().setupFromFileInterna(file);
	}
	
	
	//Lectura del fichero de propiedades y configuración del sistema de logging
	//Usa el classLoader que no se puede llamar desde método estático
	private void setupFromFileInterna(String file)
	{    
	    InputStream is = getClass().getResourceAsStream(file);
	    
	    assert(is != null);
	    
		try
		{
			LogManager.getLogManager().readConfiguration(is);
		}
		catch(IOException e)
		{
			throw new AssertionError(e.getMessage());
		}
	}
	
	//Arranque desde programa para poder poner un nombre ad hoc al fichero de log
	public static void customSetup(Level level)
	{
		//Get the global logger to configure it
		Logger logger = Logger.getLogger(LOGGER);
		
		logger.setLevel(level);
			
		Handler[] handlers = logger.getHandlers();
		
		System.out.println("numHandlers:" + handlers.length);
		
		assert(handlers[0] instanceof ConsoleHandler);
		
		Formatter formatterTxt = new CustomFormatter();
		handlers[0].setFormatter(formatterTxt);	
		handlers[0].setLevel(Level.SEVERE);
		
		try 
		{
			nombreFecha = creaNombreFecha();
			String fichero = DefaultFileTemp.replaceFirst("%", nombreFecha);
			Handler handler = new FileHandler(fichero);
			handler.setFormatter(formatterTxt);
			logger.addHandler(handler);
		} 
		catch (SecurityException e) 
		{
			throw new AssertionError(e.getClass());
		} 
		catch (IOException e) 
		{
			throw new AssertionError(e.getClass());
		}		
	}

	
	//Inicialización básica para pruebas
	static public void simpleSetup(Level level) 
	{
		simpleSetup();
	}
	
	static public void simpleSetup() 
	{ 
		new LoggerStart().setupFromFileInterna(TESTPROPERTIESFILE);
	}
	
	
/*
	static public void simpleSetup() { simpleSetup(Level.ALL); }
	
	static public void simpleSetup(Level level)
	{
		//Get the global logger to configure it
		Logger logger = Logger.getLogger(LOGGER);
		
		logger.setLevel(level);
			
		Handler[] handlers = logger.getHandlers();
		
		assert(handlers[0] instanceof ConsoleHandler);
		
		Formatter formatterTxt = new CustomFormatter();
		handlers[0].setFormatter(formatterTxt);	
		handlers[0].setLevel(level);
	}
*/	
	//Inicialización básica para pruebas
	static public void trivialSetup() { trivialSetup(Level.ALL); }
	
	static public void trivialSetup(Level level)
	{
		//Get the global logger to configure it
		Logger logger = Logger.getLogger(LOGGER);
		
		logger.setLevel(level);
			
		//Handler[] handlers = logger.getHandlers();
		
		//assert(handlers[0] instanceof ConsoleHandler);
		
		//Formatter formatterTxt = new TrivialFormatter();
		//handlers[0].setFormatter(formatterTxt);	
		//handlers[0].setLevel(level);
	}	
	

	public static String getNombreFecha() 
	{
		return nombreFecha;
	}
	
	/*
	 * getNombreFecha devuelve el nombre del log file que 
	 * dará nomrbe al directorio con la fecha, hora, minutos, segundos y milisegundos
	 * 
	 * Se usa en expresiones de desarrollo para almacenar las netlists
	 */	
	private static String creaNombreFecha()
	{
        Date now = new Date();
       
        SimpleDateFormat sdf = new SimpleDateFormat(nombreFechaFormat);
        
        nombreFecha = sdf.format(now);
        
        return nombreFecha;
	}
}
 