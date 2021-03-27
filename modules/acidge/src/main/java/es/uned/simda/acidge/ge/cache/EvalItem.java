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
 * Clase EvalItem
 * 
 * Conveniencia para la caché de expresiones
 * 
 * agrupa un número de evaluaciones y el valor de evaluación
 */
package es.uned.simda.acidge.ge.cache;

import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;



//
//El valor de canonicalFitness se añade posteriormente y se ignora en el calculo de hash
//La caché se indexa por la expresión, por lo que no son problema las funciones hashCode y equals
public class EvalItem implements Comparable<EvalItem>
{
	private final static Logger logger = Logger.getLogger(EvalItem.class.getName());

	private int hits;
	private double fitness;
	EvalRes evalRes;
	//private double canonicalFitness;
	//private int longClave;		//Contiene la longitud de la cadena de clave, se añade posteriormente
	
	//Se debe inicializar hits a 0
	//En ejecución asíncrona se lee de nuevo en getFitnessFromCache
	//En ejecució síncrona hay una llamada ad hoc a getEvalItem
	EvalItem(EvalRes evalRes)
	{
		this.evalRes = evalRes;
		hits = 0;
		//longClave = -1;
		if(evalRes == null)
			fitness = -1.0;
		else
			{
			this.fitness = evalRes.getFitness();
			}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(fitness);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + hits;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EvalItem other = (EvalItem) obj;
		if (Double.doubleToLongBits(fitness) != Double
				.doubleToLongBits(other.fitness))
			return false;
		if (hits != other.hits)
			return false;
		return true;
	}

	public double getFitness() { return fitness; }
	void setFitness(double fitness) { assert(this.fitness == -1.0); this.fitness = fitness; }
	public double getHits() { return hits; }
	void incHits() { hits++; }
	public EvalRes getEvalRes() { return evalRes; }
	public void setEvalRes(EvalRes evalRes) 
		{ 
		assert(evalRes != null);
		this.evalRes = evalRes;
		fitness = evalRes.getFitness();
		}
	//public int getLongClave() { return longClave; }
	//public void setLongClave(int longClave) { this.longClave = longClave; }
	
	@Override
	public String toString() 
	{
		assert(evalRes != null);
		
		return "hits:" + hits + ";fitness:" + fitness + ";evalRes;" + evalRes.toString();
	}

	@Override
	public int compareTo(EvalItem obj) 
	{
		if(this.getHits() > obj.getHits())
			return 1;
		else if(this.getHits() == obj.getHits())
			return 0;
		else if(this.getHits() < obj.getHits())
			return -1;
		else 
			{
			logger.info("this.hits:" + this.getHits() + " obj.hits:" + obj.getHits());
			throw new AssertionError("Código no alcanzable");
			}
	}
}
