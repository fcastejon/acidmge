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
 * Clase DevExpProperties
 * 
 * Obtiene los datos del problema de regresión
 */
package es.uned.simda.acidge.problema.dev.netlist;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class NetlistProperties 
{	
	private final static Logger logger = Logger.getLogger(NetlistProperties.class.getName());

	//Parametros para desarrollo de circuitos
	//int NumRPRList;
	//private int TipoEmbrion;
	private char SEP = '.';
	
	private int Simulaciones;
	private String NetlistHeader[];
	private String NetlistModelo;
	private String NetlistAnalisis[];
	private String ResistenciaElevada;
	private int EvitarComponentesColgando;
	
	public static Set<Integer> NodosProtegidos;

	//Nombres de las propiedades
	final static private String SimulacionesName = "Simulaciones";
	final static private String NetlistHeaderName = "netlist.header";
	final static private String NetlistModeloName = "netlist.modelo";
	final static private String NetlistAnalisisName = "netlist.analisis";
	final static private String NodosProtegidosName = "NodosProtegidos";
	final static private String EvitarComponentesColgandoName = "EvitarComponentesColgando";
	final static private String ResistenciaElevadaName = "ResistenciaElevada";
	
    Properties properties;  
    
    //Constructor que carga el fichero de propiedades
    public NetlistProperties(String fileName)
    {
    	try
    	{
    	InputStream is = new FileInputStream(new File(fileName));
        
        lee(is);
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		
    		throw new AssertionError();
    	}    	
    }
    
    //Constructor que carga el fichero de propiedades desde Stream
    public NetlistProperties(InputStream is)
    {
    	lee(is);
    }
    
    //Carga el fichero
    public void lee(InputStream is)
    {
    	try
    	{       
        properties = new Properties();
        
        properties.load(is);
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		
    		throw new AssertionError();
    	}
       
    	Simulaciones = Integer.parseInt(properties.getProperty(SimulacionesName));

    	NetlistHeader = new String[Simulaciones];
    	for(int i = 0; i < Simulaciones; i++)
    		NetlistHeader[i] = properties.getProperty(NetlistHeaderName + SEP + i);
    	
    	NetlistModelo = properties.getProperty(NetlistModeloName);
    	
    	NetlistAnalisis = new String[Simulaciones];
    	for(int i = 0; i < Simulaciones; i++)
    		NetlistAnalisis[i] = properties.getProperty(NetlistAnalisisName + SEP + i);

    	//Lee la lista de nodos protegidos
    	NodosProtegidos = new HashSet<Integer>();
    	String nodosRaw = properties.getProperty(NodosProtegidosName);
    	String nodos[] = nodosRaw.split(", ");
    	for(String nodo : nodos)
    	{
    		NodosProtegidos.add(Integer.parseInt(nodo));
    	}
    	
    	EvitarComponentesColgando = Integer.parseInt(properties.getProperty(EvitarComponentesColgandoName));
    	ResistenciaElevada = properties.getProperty(ResistenciaElevadaName);
    }

    public int getSimulaciones() { return Simulaciones; }
    public Set<Integer> getNodosProtegidos() { return NodosProtegidos; }
	public String [] getNetlistHeader() { return NetlistHeader; }
	public String getNetlistHeader(int i) { return NetlistHeader[i]; }
	public void setNetlistHeader(String netlistHeader, int i) { NetlistHeader[i] = netlistHeader; }
	public String getNetlistModelo() { return NetlistModelo; }
	public void setNetlistModelo(String netlistModelo) { NetlistModelo = netlistModelo;	}
	public String [] getNetlistAnalisis() { return NetlistAnalisis; }
	public String getNetlistAnalisis(int i) { return NetlistAnalisis[i]; }
	public void setNetlistAnalisis(String netlistAnalisis, int i) { NetlistAnalisis[i] = netlistAnalisis;	}
	public Properties getProperties() {	return properties; }
	public void setProperties(Properties properties) { this.properties = properties; }
	public String getResistenciaElevada() { return ResistenciaElevada; }
	public void setResistenciaElevada(String ResistenciaElevada) { this.ResistenciaElevada = ResistenciaElevada; }
	public int getEvitarComponentesColgando() { return EvitarComponentesColgando; }
	public void setEvitarComponentesColgando(int EvitarComponentesColgando) { this.EvitarComponentesColgando = EvitarComponentesColgando; }

    void printProperties()
    {
    	logger.info("netlist.properties");
    	logger.info(SimulacionesName + " = " + Simulaciones);
    	for(int i = 0; i < Simulaciones; i++)
    		logger.info(NetlistHeaderName + SEP + i + " = " + NetlistHeader[i]);
    	logger.info(NetlistModeloName + " = " + NetlistModelo);
    	for(int i = 0; i < Simulaciones; i++)
    		logger.info(NetlistAnalisisName + SEP + i + " = " + NetlistAnalisis[i]);
    	
    	String nodos = NodosProtegidosName +" = ";
    	for(int nodo : NodosProtegidos)
    	{
    		nodos += nodo + ", "; 
    	}
    	logger.info(nodos);
    	logger.info(EvitarComponentesColgandoName + " = " + EvitarComponentesColgando);
    	logger.info(ResistenciaElevadaName + " = " + ResistenciaElevada);
    	logger.info("------------------------------------------------");
    }
}
