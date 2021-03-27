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
 * Clase ExpCacheSync
 * 
 * Implementa la interfaz ExpCache mediante un ExpCacheBasico y añade sincronización
 */
package es.uned.simda.acidge.ge.cache;

import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.Problema;



public class ExpCacheSync extends ExpCacheDecorator
{
	private final static Logger logger = Logger.getLogger(ExpCacheSync.class.getName());
	
	//private ExpCacheBasico expCache;
	private int pendientes;
	
	public ExpCacheSync(ExpCache expCache) 
	{
		super(expCache);
		
		//expCache = new ExpCacheBasico();
		pendientes = 0;
	}
	
	public int getPendientes() { return pendientes; }

	@Override
	public synchronized EvalItem getEvalItem(String exp) throws ExpCacheNotFoundException, 
		ExpCacheSyncYaSolicitadoException
	{
		return expCache.getEvalItem(exp);
	}
	
	/*
	@Override
	public synchronized double getCanonicalFitness(String exp) throws ExpCacheNotFoundException
	{
		return expCache.getCanonicalFitness(exp);
	}*/

	@Override
	public synchronized boolean add(String exp, EvalRes evalRes) 
	{
		//logger.info("add1:" + exp + ":" + fitness + ":" + canonicalFitness);
		
		boolean ret = expCache.add(exp, evalRes);
		
		//logger.info("add2");
		
		//Este es el caso de una petición asíncrona, valor -1 y entrada nueva
		//if((fitness == -1.0) && (ret == true))
		if((evalRes == null) && (ret == true))
		{
			pendientes++;
			logger.info("add pendiente++:" + pendientes);
		}
		//Esta es una resolución de petición asíncrona, pués la entrada ya existía
		else if(ret == false)
		{
			//Filtramos algunas notificaciones, sólo se hace cuando se pone una fitness buena
			pendientes--;
			
			assert(pendientes >= 0);
			
			this.notify();
		}
		
		//logger.info("add3");
		
		return ret;
	}
	
	@Override
	public synchronized void esperaAsinc(Problema problema)
	{
		//logger.info("esperaAsinc entrada:" + pendientes);
		
		while(pendientes != 0)
		{
			try 
			{
				//logger.info("servers:" + problema.toString());
				logger.info("esperaAsinc wait:" + pendientes);
				this.wait();
			} 
			catch (InterruptedException e) 
			{
				logger.info("InterruptedException:" + e.getMessage());
			}
		}
		
		//logger.info("esperaAsinc salida:" + pendientes);
	}

	@Override
	public synchronized void dump() 
	{
		expCache.dump();
	}

	@Override
	public int getTotalHits() 
	{
		return expCache.getTotalHits();
	}

	@Override
	public int getEntradas() 
	{
		return expCache.getEntradas();
	}

	@Override
	public double getEficiencia() 
	{
		return expCache.getEficiencia();
	}

	@Override
	public int getOcupacion() 
	{
		return expCache.getOcupacion();
	}
}
