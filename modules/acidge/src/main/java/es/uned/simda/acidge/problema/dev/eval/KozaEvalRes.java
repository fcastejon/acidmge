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
 * KozaEvalRes
 * 
 * Implementa la interfaz EvalRes incluyendo el concepto de hits que introduce Koza 
 * en sus problemas utilizados como benchmark
 */
package es.uned.simda.acidge.problema.dev.eval;

import es.uned.simda.acidge.problema.EvalRes;

public class KozaEvalRes extends EvalRes 
{
	/*
	 * Elimina un warning que aparece si falta 
	 */
	private static final long serialVersionUID = 4324874668334728516L;
	int hits;
	int goalHits;
	
	public KozaEvalRes (double fitness, double canonicalFitness, int hits, int goalHits) 
	{ 
		super(fitness, canonicalFitness);
		this.hits = hits; 
		this.goalHits = goalHits;
		
		if(hits == goalHits)
			finished = true;
	}
	
	public int getHits() { return hits; }
	public int getGoalHits() { return goalHits; }

	@Override 
	public String toString() 
	{	
		return "fitness;" + fitness + ";canonicalFitness;" + canonicalFitness +
			";hits;" + hits + ";" + goalHits;
	}
}
