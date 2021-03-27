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
 * Clase Stats
 * 
 *  Implementa tratamiento estadístico de un conjunto de números reales
 */
package es.uned.simda.acidge.stats;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.logging.Logger;




public class Stats 
{
	private final static Logger logger = Logger.getLogger(Stats.class.getName());

	ArrayList<Double> valores;
	double max, min;
	double avg, mediana, primerCuartil, tercerCuartil;
	double des;
	int length;
	String name;
	
	public Stats(String name)
	{
		valores = new ArrayList<Double>();
		
		length = 0;
		max = -Double.MAX_VALUE;
		min = Double.MAX_VALUE;
		avg = 0;
		mediana = 0;
		primerCuartil = 0;
		tercerCuartil = 0;
		des = 0;
		this.name = name;
	}
	
	public Stats()
	{
		this("");
	}
	
	public double getAvg() { return avg; }
	public double getDes() { return des; }
	public double getMax() { return max; }
	public double getMin() { return min; }
	public double getMediana() { return mediana; }
	public double getPrimerCuartil() { return primerCuartil; }
	public double getTercerCuartil() { return tercerCuartil; }
	public int getLength() { return length; }
	
	public void add(double valor) 
	{ 
		assert(!(Double.isInfinite(valor)) && !(Double.isNaN(valor)));
		valores.add(new Double(valor)); 
	}
	
	/*
	public void calcula()
	{
		double valor;
		
		length = valores.size();
		
		logger.info("calcula:" + length);
		
		max = -Double.MAX_VALUE;
		min = Double.MAX_VALUE;
		avg = 0;
		mediana = 0;
		primerCuartil = 0;
		tercerCuartil = 0;
		des = 0;
		
		Collections.sort(valores);
				
		Iterator<Double> iterator = valores.iterator();
				
		while(iterator.hasNext())
		{
			valor = iterator.next().doubleValue();
			
			assert(!(Double.isInfinite(valor)) && !(Double.isNaN(valor)));
						
			if(valor > max) max = valor;
			if(valor < min) min = valor;
			
			avg += valor;
			des += valor * valor;
		}
		//suma = avg;
		avg /= length;
		des /= length;
		des = Math.sqrt(des - avg * avg);
		
		int iMid = length / 2;
		if(length % 2 == 0)
			mediana = (valores.get(iMid - 1) + valores.get(iMid)) / 2;
		else
			mediana = valores.get(iMid);
		
		int iQuart = iMid / 2;
		if(iMid % 2 == 0)
			primerCuartil = (valores.get(iQuart - 1) + valores.get(iQuart)) / 2;
		else
			primerCuartil = valores.get(iQuart);		
		
		if(iMid % 2 == 0)
			tercerCuartil = (valores.get(iMid + iQuart - 1) + valores.get(iMid + iQuart)) / 2;
		else
			tercerCuartil = valores.get(iMid + iQuart + ((length % 4 == 3) ? 1: 0));
		
		//logger.info("length:" + length + " iMid:" + iMid + " iQuart:" + iQuart);
	}
	*/
	public void calcula()
	{
		double valor;
		
		length = valores.size();
		
		Collections.sort(valores);
				
		Iterator<Double> iterator = valores.iterator();
				
		while(iterator.hasNext())
		{
			valor = iterator.next().doubleValue();			
			assert(!(Double.isInfinite(valor)) && !(Double.isNaN(valor)));

			avg += valor;
			des += valor * valor;
		}

		avg /= length;
		des /= length;
		des = Math.sqrt(des - avg * avg);

		//Por eficiencia se calculan de otra manera los valores max y min
		//min = getCuartil(0);
		primerCuartil = getCuartil(1);
		mediana = getCuartil(2);
		tercerCuartil = getCuartil(3);
		//max = getCuartil(4);
		
		max = valores.get(length - 1);
		min = valores.get(0);		
		
		
		//logger.info("length:" + length + " iMid:" + iMid + " iQuart:" + iQuart);
	}
	
	double getCuartil(int k)
	{
		//Caso extremos
		if(length == 1)
			return valores.get(0);
		
		//Método del excel de cálculo de cuartiles y mediana
		//length debe venir cargado
		double h = (((double) length - 1) * k) / 4; 
		
		int indice = (int) h;

		double p = h - indice;
		
		//logger.info("k:" + k + " h:" + h + " indice:" + indice + " p:" + p);

		//Casos extremos
		if(indice == length - 1)
			return valores.get(indice);
				
		assert((p >= 0.0) && (p < 1.0));
		
		double v1 = valores.get(indice);
		double v2 = valores.get(indice + 1);
		
		return v1 + (v2 - v1) * p;
	}
	
	public String valores() { return valores.toString(); }
	
	public String cabecera()
	{
		String linea = name;
		
		linea += ";size;max;tCuar;med;pCuar;min;avg;des";
	
		return linea;
	}
	
	public String toString()
	{
		String linea = name;
		
		//linea += "size:" + length + " max:" + max + " tCuar:" + tercerCuartil + " med:" + mediana + 
		//	" pCuar:" + primerCuartil + " min:" + min + " avg:" + avg + " des:" + des;
		
		linea += ";" + length + ";" + max + ";" + tercerCuartil + ";" + mediana + 
		";" + primerCuartil + ";" + min + ";" + avg + ";" + des;
	
		return linea;
	}	
}
