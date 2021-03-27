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
 * Secuencia
 * 
 * Clase Singleton para nombrado de componentes mediante secuencias propias de cada tipo
 * 
 * La interfaz es realmente estÃ¡tica 
 * 
 */
package es.uned.simda.acidge.netlist;

import java.util.HashMap;
import java.util.logging.Logger;



public class Secuencia 
{
	private final static Logger logger = Logger.getLogger(Secuencia.class.getName());

	static Secuencia instancia = null;
	
	HashMap<String, Integer> hash;
	
	private static Secuencia getInstancia()
	{
		if(instancia == null)
			instancia = new Secuencia();
		
		return instancia;
	}
	
	private Secuencia()
	{
		hash = new HashMap<String, Integer>();
	}
	
	//inicializa todas las secuencias, para llamar entre embriones
	public static void resetSecuencia()
	{
		instancia = null;
	}
	
	
	public static int getSecuencia(String tipo)
	{
		return getInstancia().getSecuenciaPrivada(tipo);
	}
	
	private int getSecuenciaPrivada(String tipo)
	{
		Integer sec = null;
		
		if(hash.containsKey(tipo))
		{
			sec = hash.get(tipo);
			int val = sec.intValue();
			
			sec = new Integer(val + 1);
			
			//logger.info("Resultado de sec++:" + sec.intValue());
		}
		else
		{
			sec = new Integer(1);	
		}
		
		hash.put(tipo, sec);
		
		//logger.info("getSecuencia:" + tipo + " sec:" + sec.intValue());
		return sec.intValue();
	}
	
	public static void setSecuencia(String tipo, int valor)
	{
		getInstancia().setSecuenciaPrivada(tipo, valor);
	}
	
	/* Modificado posteriormente para uso de Netlist directa
	//Consideramos que no se debe haber llamado nunca para evitar duplicidades
	private int setSecuenciaPrivada(String tipo, int valor)
	{
		assert(hash.containsKey(tipo) == false);

		Integer sec = new Integer(valor);
		
		hash.put(tipo, sec);
		
		//logger.info("setSecuencia:" + tipo + " sec:" + sec.intValue());
		return sec.intValue();
	}
	*/
	
	private void setSecuenciaPrivada(String tipo, int valor)
	{
		int sec;
		
		if(hash.containsKey(tipo))
		{
			sec = hash.get(tipo);	
		}
		else
		{
			sec = 1;	
		}
		
		if(valor > sec)
			sec = valor;
			
		logger.fine("setSecuencia:" + tipo + " sec:" + sec);
		hash.put(tipo, sec);			
	}	
}

