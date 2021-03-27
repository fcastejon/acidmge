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
 * Clase ExpCacheDecorator
 * 
 * El uso del patrón decorador permite ampliar la funcionalidad de una caché básica
 */
package es.uned.simda.acidge.ge.cache;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.Problema;

public abstract class ExpCacheDecorator implements ExpCache 
{
	protected ExpCache expCache;

	ExpCacheDecorator(ExpCache expCache)
	{
		assert(expCache != null);
		
		this.expCache = expCache;
	}
	
	@Override
	public abstract EvalItem getEvalItem(String exp) throws ExpCacheNotFoundException,
			ExpCacheSyncYaSolicitadoException;
	
	@Override
	public abstract boolean add(String exp, EvalRes evalRes);

	@Override
	public abstract void esperaAsinc(Problema problema);
	
	@Override
	public abstract void dump();
	
	@Override
	public abstract int getTotalHits();

	@Override
	public abstract int getEntradas();
	
	@Override
	public abstract double getEficiencia();
	
	@Override
	public abstract int getOcupacion();
}
