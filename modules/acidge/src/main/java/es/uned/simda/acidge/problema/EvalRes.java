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
 * Superclase EvalRes
 * 
 * Implementa la interfaz EvalRes de forma simple
 */
package es.uned.simda.acidge.problema;

import java.io.Serializable;

public class EvalRes implements Serializable
{
	/*
	 * Hacemos feliz al compilador 
	 */
	private static final long serialVersionUID = -1005958412900468139L;
	//Se penaliza de diferente manera el caso de no generar expresión (MaxWrapping) 
	//Que el caso de obtener expresión pero ofrecer un valor NaN o Infinito
	//En ambos casos se escoge un valor muy alto, pero menor que MAX_VALUE 
	//Para evitar problemas con la selección por torneo
	//Se considera peor el caso de no ofrecer expresión
	//Finalmente se utilizan valores elevados pero no el MAX_VALUE para permitir que se pueda
	//calcular un valor medio sin dar INFINITO.	
	final public static double MaxFitness = 1e8;   //Double.MAX_VALUE
	final public static double PenalizacionMaxWrapping = MaxFitness / 2;	//No expresables se penalizan más que los inviables
	final public static double PenalizacionNaN = MaxFitness / 4;
	final public static double PenalizacionGradual = MaxFitness / 100;		//Para inviables graduales
	//Valor de penalización de fitness por un valor infinito o NaN en los sumandos (FunAdaptacion)
	public final static double PenalizacionInfNan = EvalRes.MaxFitness / 1e3;
	final public static double PenalizacionGradualSoft = MaxFitness / 1e6;		//Para penalizar componentes colgando de forma gradual
	
	protected double fitness;
	protected double canonicalFitness;
	protected boolean finished;
	
	public EvalRes () {}
	
	public EvalRes (double fitval) 
	{ 
		fitness = corrige(fitval);
		canonicalFitness = fitness;
	}
	public EvalRes (double fitval, double canonicalFitval) 
	{ 
		fitness = corrige(fitval); 
		canonicalFitness = corrige(canonicalFitval);
	}
	
	public double getFitness() { return fitness; }

	public double getCanonicalFitness() { return canonicalFitness; }

	public boolean isFinished() { return finished; }

	@Override 
	public String toString() 
	{	
		return "fitness;" + fitness + ";canonicalFitness;" + canonicalFitness;
	}
	
	public static double corrige(double val)
	{
		double res = val;
		
		if(Double.isNaN(val) || Double.isInfinite(val))
		{
			res = PenalizacionNaN;
		} 
				
		return res;	
	}
}
