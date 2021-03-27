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
 * Clase Linearize
 * 
 * Realiza una transformación de un vector con tiempos no equiespaciados
 * en tiempos equiespaciados 
 * 
 */
package es.uned.simda.acidge.spice;

//import java.util.NoSuchElementException;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.InviableException;

public class Linearize 
{
	private final static Logger logger = Logger.getLogger(Signal.class.getName());

	private final String SUFFIX = "lin";
	private final static double TOL = 1e-9;
	private final int puntos;
	private double vi[];
	
	private double Xequi[];
	private int Xpuntos;
	//private boolean Xinicializado;
	
	public Linearize(int puntos, double [] vi)
	{
		logger.info("Linearize:" + puntos);
		assert(puntos > 0);
		assert(puntos == vi.length);
		
		this.puntos = puntos;
		this.vi = new double[vi.length];
		//Xinicializado = false;
		
		for(int i = 0; i < vi.length; i++)
		{
			this.vi[i] = vi[i];
		}
	}
	
	private void calculaVector(Signal entrada) throws InviableException
	{
		/*
		if(Xinicializado == true)
		{
			assert(entrada.getSize() == Xpuntos);
			
			return;
		}
		*/
		
		double Tinicial, Tfinal;

		Tinicial = entrada.getSigItemIndex(0).getFrecuencia();
		Tfinal = entrada.getSigItemIndex(entrada.getSize() - 1).getFrecuencia();
		
		calculaVector(Tinicial, Tfinal, entrada.getSize());
	}
	
	private void calculaVector(double Tinicial, double Tfinal, int size) throws InviableException
	{
		/*
		if(Xinicializado == true)
		{
			assert(size == Xpuntos);
			
			return;
		}
		*/
				
		Xpuntos = size;
		Xequi = new double[puntos];
		
		double step = (Tfinal - Tinicial)/(puntos - 1);
		
		for(int i = 0; i < puntos; i++)
		{
			Xequi[i] = Tinicial + step * i;
			logger.finer("Xequi[" + i + "]=" + Xequi[i]);
		}
		
		if(Math.abs(Xequi[puntos - 1] - Tfinal) >= TOL)
		{
			logger.warning("CalculaVector Xequi:" + Xequi[puntos - 1] + 
					"Tinicial=" + Tinicial + ":Tfinal=" + Tfinal + ":size=" + size);

			throw new InviableException("CalculaVector");
		}
		
		//Xinicializado = true;
	}
	
	double [] getXequi() { return Xequi; } //Para TestLinearize
	
	//Espera que el primer y el último valor sean Tinicial y Tfinal
	//Atención las abscisa se llaman frecuencia por razones históricas
	//Sólo vale para señales reales
	//Además cambia los valores de abscisas al vector vi
	public Signal linearize(Signal entrada) throws InviableException //, NoSuchElementException
	{
		calculaVector(entrada);
		
		Signal salida = new Signal(entrada.getNombre()+SUFFIX);
		SigItem si, so;
		SigItem siold = null;
		double mag;
		int i = 0;
		int j = 0;

		si = entrada.getSigItemIndex(j++);
		so = new SigItem(vi[i++], si.getMagnitud(), 0);
		
		salida.put(so);

		do
		{
			do
			{
				siold = si;
				si = entrada.getSigItemIndex(j++);
			
				if(si.getFrecuencia() <= siold.getFrecuencia())
				{
					logger.warning("linearize x[i-1]:" + siold.getFrecuencia() + 
						"x[i]=" + si.getFrecuencia());

					throw new InviableException("linearize");
				}
				logger.finer("t=" + si.getFrecuencia() + ":x=" + Xequi[i] + ":i=" + i + ":j=" + j);
			} while(si.getFrecuencia() < Xequi[i]);
			
			do
			{
				mag = interpola(siold.getFrecuencia(), siold.getMagnitud(),
					si.getFrecuencia(), si.getMagnitud(), Xequi[i]);
				so = new SigItem(vi[i], mag, 0);
				salida.put(so);
				i++;
			}
			while((i < puntos) && (si.getFrecuencia() > Xequi[i]));
		}		
		while((i < puntos) || (j < Xpuntos));
		
		logger.finer("i=" + i + ":j=" + j);
		
		assert((i == puntos) && (j == Xpuntos));
		
		return salida;
	}
	
	//Interpolación lineal
	public double interpola(double x1, double y1, double x2, double y2, double x)
	{
		double y = ((y2 - y1) / (x2 - x1)) * (x - x1) + y1;
		
		logger.fine("x1:" + x1 + ":y1:" + y1 + ":x2:" + x2 + ":y2:" + y2 + ":x:" + x + ":y:" + y);
		return y;
	}
}
