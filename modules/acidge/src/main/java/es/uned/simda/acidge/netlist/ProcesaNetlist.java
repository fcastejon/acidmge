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
 * ProcesaNetlist
 * 
 * Esta clase se utiliza en la generación directa de netlists. Se añade a este paquete por tratar
 * de netlists. Utiliza Secuencia y las constantes de Componente
 * 
 * Lee una netlist y genera los nombres de los componentes únicos, que la gramática libre de contexto 
 * no puede hacer
 * 
 * También realiza validación de los nodos para comprobar si están sin conexión
 */
package es.uned.simda.acidge.netlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.InviableException;

public class ProcesaNetlist
{
	private final static Logger logger = Logger.getLogger(ProcesaNetlist.class.getName());
	final static String newLine = String.format("%n");
	
	final static String opNetlist = ".";		//inicio de palabras de operación en Spice
	final static String Separador = " "; 
	final static String Nulo = "nulo";
	final static String NuloRegEX = "[ ]?nulo[12]";
	final static String CadenaVacia = "";
	//final static String valorAltaResistencia = "1G";
	final static int nodoMasa = 0;

	//Nodos protegidos, que deben tener grado 2 o superior
	private Set<Integer> NodosProtegidos;
	
	int EvitarComponentesColgando;
	String ResistenciaElevada;
	
	HashMap<Integer, Integer> gradoNodo;
	
	public ProcesaNetlist(Set<Integer> NodosProtegidos, int EvitarComponentesColgando, 
			String ResistenciaElevada) 
	{
		if((EvitarComponentesColgando != 0) && (EvitarComponentesColgando != 1))
			throw new AssertionError("Valor EvitarComponentesColgando no valido:" + 
					EvitarComponentesColgando);
		
		this.NodosProtegidos = NodosProtegidos;
		this.EvitarComponentesColgando = EvitarComponentesColgando;
		this.ResistenciaElevada = ResistenciaElevada;
	}
	
	public void inicia(String exp) throws InviableException
	{
		//Inicia los contadores de componentes
		Secuencia.resetSecuencia();
		
		gradoNodo = new HashMap<Integer, Integer>();
		
		lee(exp, true, true);
	}
	
	//public int getNumNodosProtegidos() { return numNodosProtegidos; }
	
	public String procesa(String exp) throws InviableException
	{
		String nuevaExp = lee(exp, false, false);
		
		return nuevaExp;
	}
	
	private String lee(String exp, boolean discardFirst, boolean esInicia) throws InviableException
	{		
		StringReader sr = new StringReader(exp);
		BufferedReader br = new BufferedReader(sr);
		StringBuilder builder = new StringBuilder();
		String linea;
		
		try
		{
			try
			{ 
				if(discardFirst)
				{
					linea = br.readLine();
					assert(linea != null);
				}
				
				while(((linea = br.readLine()) != null))
				{	
					linea = procesaLinea(linea);
					if(linea != null)
					{
						logger.fine(linea);
						builder.append(linea);
						builder.append(newLine);
					}
					else
						logger.fine("Linea nula");
				}
				
				//Se insertan resistencias elevadas en los componentes colgando
				//Sólo se hace si no se llama desde inicia (sólo se insertan al final del proceso de la netlist completa)
				if((EvitarComponentesColgando == 1) && !esInicia)
					insertaResistencias(builder);
				
			}
			finally
			{
				sr.close();
			}
		}
		catch (NumberFormatException e) 
		{
			logger.severe(e.getClass().toString());
			throw new InviableException(e.getClass().toString());
		}
		catch (IOException e) 
		{
			logger.severe("Error E/S: " + e.getMessage());
		} 
		
		return builder.toString();
	} 
	
	String procesaLinea(String linea) throws InviableException
	{
		String nombre = "";
		int numNodos = 0;
		int numero = 0;
		boolean actualizarValor = false;
		
		//logger.info(linea);
		
		if(linea.isEmpty())
			return linea;
		else if(linea.startsWith(opNetlist))
			return linea;
		else if(linea.startsWith(Componente.nombreR))
		{
			nombre = Componente.nombreR;
			numNodos = 2;
			actualizarValor = true;
		}
		else if(linea.startsWith(Componente.nombreC))
		{
			nombre = Componente.nombreC;
			numNodos = 2;
			actualizarValor = true;
		}
		else if(linea.startsWith(Componente.nombreQ))
		{
			nombre = Componente.nombreQ;
			numNodos = 3;
		}
		else if(linea.startsWith(Componente.nombreJFET))
		{
			nombre = Componente.nombreJFET;
			numNodos = 3;
		}
		else if(linea.startsWith(Componente.nombreMOSFET))
		{
			nombre = Componente.nombreMOSFET;
			numNodos = 4;
		}
		//Debe ir primero por prefijo común con V! 
		else if(linea.startsWith(Componente.nombreVAC))
		{
			nombre = Componente.nombreVAC;
			numNodos = 2;
		}
		else if(linea.startsWith(Componente.nombreV))
		{
			nombre = Componente.nombreV;
			numNodos = 2;
		}
		//Elimina las líneas de componentes dummy, nulos
		else if(linea.startsWith(Nulo))
			return null;
		//Devuelve el resto de líneas sin modificar
		else
			return linea;
		
		//Elimina valores nulos
		linea = quitaNulos(linea);
		
		//Proceso del componente detectado
		String tokens[] = linea.split(Separador, numNodos + 2);
		
		if(tokens.length != numNodos + 2)
		{
			logger.severe("linea incorrecta:" + linea);
			throw new AssertionError("linea incorrecta:");
		}
		
		int nodos[] = new int[numNodos];
		
		//Obtenemos lista de nodos
		for(int i = 0; i < numNodos; i++)
		{
			logger.fine("parsea:" + tokens[i + 1]);
			
			nodos[i] = Integer.parseInt(tokens[i + 1]);
		}
		
		//Se comprueba si los terminales están cortocircuitados y se elimina el componente en su caso
		if(esCorto(nodos))
		{
			return "*" + linea;
		}

		//Se calcula el grado de los nodos una vez eliminados los componentes en corto
		for(int i = 0; i < numNodos; i++)
		{		
			incrementaNodo(nodos[i]);
		}
		
		//Comprobamos si tiene número asignado
		if(tokens[0].length() > nombre.length())
		{
			//logger.info("parsea:" + tokens[0]);
			
			numero = Integer.parseInt(tokens[0].substring(nombre.length()));
			
			Secuencia.setSecuencia(nombre, numero);
		}
		else
		{
			//Asignamos nuevo número
			numero = Secuencia.getSecuencia(nombre);
			
			linea = linea.replaceFirst(nombre, nombre + numero);
		}

		//Volvemos si no hay que actualizar el valor
		if(!actualizarValor)
			return linea;
		
		//Finalmente se modifica el valor
		//double valor = Double.parseDouble(tokens[numNodos + 1]);
		double valor = Componente.StringToValor(tokens[numNodos + 1]);
		
		String valorProcesado = Componente.valorToString(valor);

		linea = linea.replaceFirst(tokens[numNodos + 1], valorProcesado);
		
		return linea;
	}
	
	//Es un corto en caso de que todos los terminales estén cortocircuitados
	public boolean esCorto(int nodos[])
	{
		assert(nodos.length >= 2);
		
		int nodo = nodos[0];
		
		for(int i = 1; i < nodos.length; i++)
			if(nodos[i] != nodo)
				return false;
		
		return true;
	}
	
	//Devuelve el número de nodos protegidos que no cumplen la regla de grado > 1
	//Debe llamarse despues de procesa
	public int [] checkNodos()
	{
		//logGradoNodos();
		int [] res = new int[2];
		
		Set<Integer> nodos = gradoNodo.keySet();
		
		int nodosMalosProtegidos = 0;
		int nodosMalos = 0;
		
		for(int nodo : nodos)
		{
			int grado = gradoNodo.get(nodo);

			assert(grado >= 1);
			
			if(NodosProtegidos.contains(nodo) && (grado < 2))
			{
				nodosMalosProtegidos++;
				logger.fine("checkNodos nodo con grado menor de 2:" + nodo);
			}
			else if(grado < 2)
				nodosMalos++;
		}
		
		res[0] = nodosMalosProtegidos;
		res[1] = nodosMalos;
		
		return res;
	}
	
	//Crea resistencias de valor elevado entre el componente colgando y masa
	//Aumenta la cuenta, de forma que luego checkNodos debe dar un valor de 0 nodos no protegidos con grado < 2
	public void insertaResistencias(StringBuilder builder)
	{	
		int numero;
		String linea;
		
		Set<Integer> nodos = gradoNodo.keySet();
		
		for(int nodo : nodos)
		{
			int grado = gradoNodo.get(nodo);

			assert(grado >= 1);
			
			//Sólo tratamos los nodos no protegidos con grado 1
			if((NodosProtegidos.contains(nodo) == false) && (grado == 1))
			{
				logger.fine("Insertamos resistencia en nodo:" + nodo);
				
				numero = Secuencia.getSecuencia(Componente.nombreR);
				linea = Componente.nombreR + numero + Separador + nodo + Separador + nodoMasa + Separador + ResistenciaElevada; 
				
				//FIXME posible problema de modificación concurrente
				gradoNodo.put(nodo, 2);
				
				logger.fine(linea);
				builder.append(linea);
				builder.append(newLine);
			}
		}
	}
	
	private void incrementaNodo(int nodo)
	{
		int sec;
		
		if(gradoNodo.containsKey(nodo))
		{
			sec = gradoNodo.get(nodo) + 1;
		}
		else
		{
			sec = 1;
		}
		
		gradoNodo.put(nodo, sec);
		
		logger.fine("incrementaNodo nodo:" + nodo + " sec:" + sec);
	}
	
	//Sólo se llama desde una clase de test
	public void logGradoNodos()
	{
		logger.info("logGradoNodos");
		
		Set<Integer> keySet = gradoNodo.keySet();
		List<Integer> nodos = new ArrayList<Integer>(keySet);     
		Collections.sort(nodos);
			
		for(int nodo : nodos)
		{
			logger.info(nodo + ";" + gradoNodo.get(nodo));
		}
	}
	
	//Quita los valores de parámetros nulos
	public String quitaNulos(String linea)
	{
		return linea.replaceAll(NuloRegEX, CadenaVacia);
	}
}
