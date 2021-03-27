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
 * Clase SigItem
 * 
 * Conveniencia para gestión de señales en frecuencia (V o I)
 * 
 * Almacena un valor imaginario de una señal para un valor de frecuencia
 */
package es.uned.simda.acidge.spice;

import java.util.logging.Logger;



public class SigItem implements Comparable<SigItem>
{
	private final static Logger logger = Logger.getLogger(SigItem.class.getName());

	private final double frecuencia;
	private final double magnitud;
	private final double fase;
	
	public SigItem(double frecuencia, double magnitud, double fase)
	{
		this.frecuencia = frecuencia;
		this.magnitud = magnitud;
		this.fase = fase;
	}
	
	public SigItem(SigItem sig)
	{
		this.frecuencia = sig.getFrecuencia();
		this.magnitud = getMagnitud();
		this.fase = getFase();
	}
	
	//Generado por eclipse
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(fase);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(frecuencia);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(magnitud);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	//Generado por eclipse
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SigItem other = (SigItem) obj;
		if (Double.doubleToLongBits(fase) != Double
				.doubleToLongBits(other.fase))
			return false;
		if (Double.doubleToLongBits(frecuencia) != Double
				.doubleToLongBits(other.frecuencia))
			return false;
		if (Double.doubleToLongBits(magnitud) != Double
				.doubleToLongBits(other.magnitud))
			return false;
		return true;
	}

	public double getFrecuencia() { return frecuencia; }
	public double getMagnitud() { return magnitud; }
	public double getFase() { return fase; }
	
	@Override
	public String toString() 
	{
		return "f:" + frecuencia + " (" + magnitud + ", " + fase + ")";
	}

	//La ordenación se hace por frecuencias
	@Override
	public int compareTo(SigItem obj) 
	{
		if(this.getFrecuencia() > obj.getFrecuencia())
			return 1;
		else if(this.getFrecuencia() == obj.getFrecuencia())
			return 0;
		else if(this.getFrecuencia() < obj.getFrecuencia())
			return -1;
		else 
			{
			logger.info("this.frecuencia:" + this.getFrecuencia() + " obj.frecuencia:" + obj.getFrecuencia());
			throw new AssertionError("Código no alcanzable");
			}
	}
}
