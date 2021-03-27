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
 * Clase Signal
 * 
 * Clase de conveniencia para gestión de una señal compleja en frecuencia
 * 
 * Recubre un HashMap
 * 
 * Resulta leída de la salida de Spice
 */
package es.uned.simda.acidge.spice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;



public class Signal 
{
	private final static Logger logger = Logger.getLogger(Signal.class.getName());

	final static String newLine = String.format("%n");

	private final String nombre;
	private HashMap<Double,SigItem> valores;
	private ArrayList<SigItem> lista;
	
	private double media;
	private double maxPicoPico;
	private double mediaPos;
	private double mediaNeg;
	
	public Signal(String nombre)
	{
		valores = new HashMap<Double,SigItem>();
		lista = new ArrayList<SigItem>();
		this.nombre = nombre;
		
		media = 0.0;
		maxPicoPico = 0.0;
		mediaPos = 0.0;
		mediaNeg = 0.0;
	}
	
	public void putMP(double frecuencia, double magnitud, double fase)
	{
		SigItem si = new SigItem(frecuencia, magnitud, fase);
		
		valores.put(new Double(frecuencia), si);
		lista.add(si);
	}
	
	public void putRI(double frecuencia, double real, double imag)
	{
		double magnitud = convertMagnitud(real, imag);
		double fase = convertFase(real,imag);
		
		SigItem si = new SigItem(frecuencia, magnitud, fase);
		
		valores.put(new Double(frecuencia), si);
		lista.add(si);
	}
	
	public void put(SigItem si)
	{	
		valores.put(new Double(si.getFrecuencia()), si);
		lista.add(si);
	}
	
	public String getNombre() { return nombre; }
	public SigItem getSigItem(double frecuencia) { return valores.get(new Double(frecuencia)); }
	public SigItem getSigItemIndex(int i) { return lista.get(i); }
	
	public double getMagnitud(double frecuencia) { return valores.get(new Double(frecuencia)).getMagnitud(); }
	public double getFase(double frecuencia) { return valores.get(new Double(frecuencia)).getFase(); }
	public int getSize() { return valores.size(); }
	
	public double getMaxPicoPico() { return maxPicoPico; }
	public double getMedia() { return media; }
	public double getMediaPos() { return mediaPos; }
	public double getMediaNeg() { return mediaNeg; }
	public boolean isEmpty() { return valores.isEmpty(); }
	
	public Iterator<SigItem> iterator() { return valores.values().iterator(); }
	
	public String toString()
	{
		return "nombre:" + nombre + newLine + valores.toString();
	}
	
	//Funciones de conveniencia
	public static double convertMagnitud(double real, double imag)
	{
		return Math.sqrt(real * real + imag * imag);
	}
	
	public static double convertFase(double real, double imag)
	{
		return Math.atan2(imag, real);
	}
	
	public static double AToDB(double A)
	{
		return 20 * Math.log10(A);
	}
	
	/*
	//Calcula la media de un vector y la excursión pico pico
	public void calculaStats()
	{
		Set<Double> keySet = valores.keySet();
		List<Double> list = new ArrayList<Double>(keySet);     
		//Collections.sort(list);
		
		int total = 0;
		double suma = 0.0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		double aux;
			
		for(Double time : list)
		{
			aux =  valores.get(time).getMagnitud();
			suma += aux;

			if(aux > max)
				max = aux;
			
			if(aux < min)
				min = aux;
			
			total++;
		}
		
		logger.info("suma:" + suma + " max:" + max + " min:" + min + " total:" + total);
		
		media = suma/total;
		maxPicoPico = max - min;
	}
	*/
	
	
	//Calcula la máxima excursión pico pico, requiere un valor de continua inicial
	public void calculaStats(double continua)
	{
		Set<Double> keySet = valores.keySet();
		List<Double> list = new ArrayList<Double>(keySet);     
		//Collections.sort(list);
		
		int total = 0;
		double suma = 0.0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		double aux;
			
		for(Double time : list)
		{
			aux =  valores.get(time).getMagnitud();
			suma += aux;

			if(aux > max)
				max = aux;
			
			if(aux < min)
				min = aux;
			
			total++;
		}
		
		logger.info("continua:" + continua + "suma:" + suma + " max:" + max + " min:" + min + " total:" + total);
		
		media = suma/total;
		maxPicoPico = 2 * Math.min(max - continua, continua - min);
		
		logger.info("maxPicoPico:" + maxPicoPico + " media:" + media);
	}
	
	//Calcula la máxima excursión pico pico, requiere un valor de continua inicial
	/* Función que estaba siendo desarrollada para la mejora de este cálculo
	 * Se abandonó en favor de THD
	 * Se mantiene la función anterior por compatibilidad con la versión del paper
	public void calculaStats(double continua)
	{
		Set<Double> keySet = valores.keySet();
		List<Double> list = new ArrayList<Double>(keySet);
		final int numPuntos = list.size();

		//Si es par son consecutivos y si no, se evita el punto central
		final int semiNeg = numPuntos/2 - 1;
		final int semiPos = numPuntos - semiNeg;

		//Para obtener una mediana de los valores
		final boolean par = (numPuntos % 2 == 0) ? true : false;
		final int central = numPuntos/2;		//es el punto central si es impar, si no son central y central+1
		double mediana = 0.0;
		
		//La ordenación es necesaria para el semiciclo positivo y negativo!!
		Collections.sort(list);
		
		int total = 0;
		double suma = 0.0;
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		double sumaSemiNeg = 0.0;
		double sumaSemiPos = 0.0;
		double aux;
		double timeant = 0.0;
		
		for(Double time : list)
		{
			aux =  valores.get(time).getMagnitud();
			
			logger.info("time:" + time + " val:" + aux);
			assert(time > timeant);
				
			suma += aux;

			if(aux > max)
				max = aux;
			
			if(aux < min)
				min = aux;
			
			if(total <= semiNeg)
				sumaSemiNeg += aux;
			
			if(total >= semiPos)
				sumaSemiPos += aux;
			
			if(par && (total == central))
			{
				logger.info("PAR:" + total + " aux:" + aux);
				mediana = aux;
			}
			
			if(!par && ((total == central) || (total == central + 1)))
			{
				logger.info("IMPAR:" + total + " aux:" + aux);
				mediana += aux/2;
			}
			
			total++;
		}
		
		logger.info("cont:" + continua + " suma:" + suma + " max:" + max + " min:" + min + " total:" + total);
		logger.info("numPuntos:" + numPuntos + " semiNeg:" + semiNeg + " semiPos:" + semiPos + " sumaSemiNeg:" + sumaSemiNeg + " sumaSemiPos:" + sumaSemiPos);

		media = suma/total;
		mediaNeg = sumaSemiNeg / semiNeg;
		mediaPos = sumaSemiPos / semiNeg;	//El denominador es semiNeg también
		
		maxPicoPico = 2 * Math.min(max - continua, continua - min);
		double maxPicoPico2 = 2 * Math.min(mediaPos - continua, continua - mediaNeg);
		double maxPicoPico3 = 2 * Math.min(mediaPos - mediana, mediana - mediaNeg);

		logger.info("max:" + max + " medPos:" + mediaPos + " media:" + media + " mediana:" + mediana + " mediaNeg:" + mediaNeg + " min:" + min);
		logger.info("maxPicoPico:" + maxPicoPico + " maxPicoPico2:" + maxPicoPico2 + " maxPicoPico3:" + maxPicoPico3);
	}
	*/
	//Función de utilidad
	//Se hace aquí, al no existir una clase agrupación de señales
	public static String signalsToString(HashMap<String, Signal> signals)
	{
		Set<String> keySet = signals.keySet();
		List<String> list = new ArrayList<String>(keySet);
		String ret = "";
			
		for(String nombre : list)
		{
			ret += nombre + " ";
		}	
		
		return ret;
	}

	//Igualmente se hace aquí al no existir una clase de agrupación de señales
	//Borra las señales vacías debidas a errores de lectura del fichero
	public static void purgaSignals(HashMap<String, Signal> signals)
	{
		Set<String> keySet = signals.keySet();
		List<String> list = new ArrayList<String>(keySet);
		String ret = "";
			
		for(String nombre : list)
		{
			if(signals.get(nombre).isEmpty())
			{
				logger.info("purgaSignals, signal vacia " + nombre);
				signals.remove(nombre);
			}
		}	
	}

	
}
