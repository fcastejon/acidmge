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
 * Clase ServerParameters
 * 
 * Lee el fichero servers.properties y obtiene la configuración de servers del cluster
 */
package es.uned.simda.acidge.rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;



public class ServerProperties 
{	
	private final static Logger logger = Logger.getLogger(ServerProperties.class.getName());

    Properties serverProperties;
    String name;
    
    //Constructor que carga el fichero de propiedades
    public ServerProperties(String fileName)
    {
    	name = fileName;
    	
    	try
    	{
    	   	InputStream is = new FileInputStream(new File(fileName));
    	   	
        	commonServerProperties(is);
    	}
    	catch(FileNotFoundException e)
    	{
    		e.printStackTrace();
    		
    		throw new AssertionError();
    	}
    }
    
    public ServerProperties(InputStream is) 
    {
    	commonServerProperties(is);		
	}

	//Common method 
    public void commonServerProperties(InputStream is)
    {   	
    	try
    	{        
    	serverProperties = new Properties();
        
    	serverProperties.load(is);
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		
    		throw new AssertionError();
    	}
    }
    
    ArrayList<String> getNames()
    {
    	ArrayList<String> keys = new ArrayList<String>(serverProperties.stringPropertyNames());
    	ArrayList<String> names = new ArrayList<String>();
    	
    	Collections.sort(keys);
    	
    	for(String key : keys)
    	{
    		//if(Boolean.parseBoolean(serverProperties.getProperty(key)))
    		int n = Integer.parseInt(serverProperties.getProperty(key));

    		for(int i = 0; i < n; i++)
    		{
    			names.add(key + "/" + i);
    		}    		
    	}
    	
    	return names;
    }
    
    public void printProperties()
    {
    	logger.info("server.properties:" + name);
    	
    	ArrayList<String> keys = new ArrayList<String>(serverProperties.stringPropertyNames());
    	
    	Collections.sort(keys);
    	
    	for(String key : keys)
    	{
    		logger.info("Key:" + key + ", " + serverProperties.getProperty(key));
    	}

    	logger.info("------------------------------------------------");
    }
}
