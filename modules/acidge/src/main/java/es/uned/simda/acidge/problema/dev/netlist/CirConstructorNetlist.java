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
 * Clase ProblemaDevExp
 * 
 * Crea un circuito mediante expresiones de desarrollo
 */

package es.uned.simda.acidge.problema.dev.netlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.Set;
import java.util.logging.Logger;

import es.uned.simda.acidge.netlist.ProcesaNetlist;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.dev.CirConstructor;
import es.uned.simda.acidge.ge.GEProperties;


public class CirConstructorNetlist implements CirConstructor 
{
	private final static Logger logger = Logger.getLogger(CirConstructorNetlist.class.getName());

	private final static String DIRPROPERTIES = "properties";

	private final static String PropertiesFile = DIRPROPERTIES + File.separator + "netlist.properties";
	final static String newLine = String.format("%n");
	private final static String VTSUS = "%VTRAN";
	
	private int Simulaciones;
	private String [] NetlistHeader;
	private String NetlistModelo;
	private String [] NetlistAnalisis;
	
	private String exp;			//Es la propia netList generada
	ProcesaNetlist procNetlist;
	
	private double Vtran;
	
	private int nodosMalos[];
	
	public CirConstructorNetlist()
	{
		this(PropertiesFile);
	}
	
	public CirConstructorNetlist(String file)
	{	
		NetlistProperties properties = new NetlistProperties(file);	
	
		properties.printProperties();

		inicia(properties.getSimulaciones(), properties.getNodosProtegidos(),
				properties.getNetlistHeader(),
				properties.getNetlistModelo(), 
				properties.getNetlistAnalisis(),
				properties.getEvitarComponentesColgando(),
				properties.getResistenciaElevada());
	}
	
	public CirConstructorNetlist(int Simulaciones, Set<Integer> NodosProtegidos, 
			String NetlistHeader[], 
			String NetlistModelo, String NetlistAnalisis[],
			int EvitarComponentesColgando, String ResistenciaElevada)
	{	
		inicia(Simulaciones, NodosProtegidos, NetlistHeader, NetlistModelo, NetlistAnalisis,
				EvitarComponentesColgando, ResistenciaElevada);
	}
	
	void inicia(int Simulaciones, Set<Integer> NodosProtegidos, String NetlistHeader[],  
			String NetlistModelo, String NetlistAnalisis[],
			int EvitarComponentesColgando, String ResistenciaElevada)
	{
		exp = "";
		
		this.Simulaciones = Simulaciones;
		this.NetlistHeader = NetlistHeader;
		this.NetlistModelo = NetlistModelo;
		this.NetlistAnalisis = NetlistAnalisis;
		
		procNetlist = new ProcesaNetlist(NodosProtegidos, EvitarComponentesColgando, ResistenciaElevada);
	}
	
	public void setVtran(double vtran) { Vtran = vtran; }
	public int getSimulaciones() { return Simulaciones; }

	@Override
	public boolean creaCircuitoExp(String exp) throws InviableException
	{
		//Se crea el circuito para la primera simulación
		procNetlist.inicia(NetlistHeader[0]);
		
		//El circuito queda con las etiquetas generadas contando los componentes de
		//la cabecera AC, para el experimento zout no hay cambios en las etiquetas
		this.exp = procNetlist.procesa(exp);
	
		//Comprueba el número de nodos que no cumplen grado > 1
		nodosMalos = procNetlist.checkNodos();
		
		//Devuelve false si existen nodos protegidos con grado < 2  
		return (nodosMalos[0] > 0) ? false : true;
	}
	
	@Override
	public int getNivelInviable()
	{
		return nodosMalos[0];
	}

	@Override
	public void adaptaCircuitoAnalisis(int analisis, String netFile) throws InviableException
	{
		String contenido = adaptaCircuitoAnalisis(analisis);
		
		graba(netFile, contenido);
	}
	
	//Only used to print the best circuit in the log file
	@Override
	public String getCircuit(int analisis) throws InviableException
	{
		String contenido = adaptaCircuitoAnalisis(analisis);
		
		return contenido;
	}
	
	//Permite realizar pruebas unitarias, hace overload por parámetros
	public String adaptaCircuitoAnalisis(int analisis) throws InviableException
	{
		String contenido = "";
		
		//analisisAC
		if(analisis == 0)
		{
			contenido = NetlistHeader[analisis].replace(VTSUS, Double.toString(Vtran)); 
			
			contenido += exp + NetlistModelo + NetlistAnalisis[analisis];
		}
		//analisisACTemp
		else if(analisis == 1)
		{
			contenido = NetlistHeader[analisis].replace(VTSUS, Double.toString(Vtran)); 
			contenido += exp + NetlistModelo + NetlistAnalisis[analisis];
		}
		//analisisZout
		else if(analisis == 2)
		{
			contenido = NetlistHeader[analisis].replace(VTSUS, Double.toString(Vtran)); 
			contenido += exp + NetlistModelo + NetlistAnalisis[analisis];		
		}
		else
			throw new AssertionError("Análisis inválido:" + analisis);
		
		return contenido;
	}
	
	//Graba la Netlist a fichero
	public void graba(String netFile, String contenido)
	{
		File file = new File(netFile);
		
		PrintWriter pw = null;
		
		file.getParentFile().mkdirs();
		
		/*
		if(file.getParentFile().mkdirs() == true)
			logger.info("Creando directorios para:" + netFile);
		 */	 
		try
		{
			//No es necesario el autoflush, pues se escribe en esta función y se cierra
			pw = new PrintWriter(file);
		}
		catch(FileNotFoundException e) 
		{
			logger.info("graba: error apertura de fichero");
		}
		
		if(pw != null)
		{
			pw.print(contenido);

			pw.close();
		} 
	}

}
