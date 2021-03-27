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
 ** ProblemaSC
 **
 ** Superclase para agrupar todos los problemas posibles
 **
 ** Implementa la interfaz Problema
 **
 */
package es.uned.simda.acidge.problema;

import java.util.logging.Logger;

//import es.uned.simda.acidge.generador.Generador;


public abstract class ProblemaSC implements Problema 
{
	private final static Logger logger = Logger.getLogger(ProblemaSC.class.getName());

	//Conveniencia para generación de nombres de ficheros, no necesario para el algoritmo
	protected int generacion;
	protected String nombreFecha;
	
	//Contador de evaluaciones de fitness para el cálculo del AES
	protected int evaluationCount;
	
	//Solución rápida para canonicalFitness;
	//protected double canonicalFitness;
	
	protected ProblemaSC()
	{	
		evaluationCount = 0;
		//canonicalFitness = -1.0;
	}
		
	abstract public EvalRes evalua(String exp) throws InviableException;
	
	//public double getCanonicalFitness() { return canonicalFitness; }
	
	public int getEvaluationCount() { return evaluationCount; }
	protected void incrementEvaluationCount() { evaluationCount++;}
	public void resetEvaluationCount() { evaluationCount = 0; }
	
	public void setGeneracion(int generacion) { assert(generacion > 0); this.generacion = generacion; }
	public void setNombreFecha(String nombreFecha) { assert(nombreFecha != null); this.nombreFecha = nombreFecha; }
	
	public String build(String exp) { return null; }

}
