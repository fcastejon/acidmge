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
 * ExpCache
 * 
 * Implementa una cache de expresiones con sus evaluaciones anteriores
 * 
 * También permite obtener una lista de las expresiones evaluadas, junto con el número de veces 
 * que han aparecido y su evaluación
 */

package es.uned.simda.acidge.ge.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.Problema;


public class ExpCacheBasico implements ExpCache
{
	private final static Logger logger = Logger.getLogger(ExpCacheBasico.class.getName());

	HashMap<String, EvalItem> hashMap;
	
	public ExpCacheBasico()
	{
		hashMap = new HashMap<String, EvalItem>();
	}
	
	@Override
	public EvalItem getEvalItem(String exp) throws ExpCacheNotFoundException, ExpCacheSyncYaSolicitadoException
	{
		EvalItem item;
		//double fitness;
		
		//Consulta y si existe devuelve el valor e incrementa en uno el hit
		if((item = hashMap.get(exp)) == null)
			throw new ExpCacheNotFoundException("ExpCache entrada no encontrada");
		
		//if((fitness = item.getFitness()) == -1.0)
		if(item.getFitness() == -1.0)
		{
			throw new ExpCacheSyncYaSolicitadoException();
		}
		else
		{
			item.incHits();
			return item;
		}
	}
	
	//Esta función NO incrementa los hits!!! sólo es una consulta diferenciada de la de fitness
	//Se llamará siempre a posteriori de getFitness
	/*
	@Override
	public double getCanonicalFitness(String exp) throws ExpCacheNotFoundException
	{
		EvalItem item;
		
		if((item = hashMap.get(exp)) == null)
			throw new ExpCacheNotFoundException("ExpCache entrada no encontrada");
		
		return item.getCanonicalFitness();		
	}
	*/
	
	@Override
	public boolean add(String exp, EvalRes evalRes)
	{
		EvalItem item;

		//Comprueba si se añadió anteriormente en caso asíncrono
		if((item = hashMap.get(exp)) == null)
		{
			item = new EvalItem(evalRes);
			
			hashMap.put(exp, item);
			
			//Anotamos la longitud de la cadena para calcular el tamaño de la caché
			//item.setLongClave(exp.length());
			
			return true;
		}
		else
		{
			assert(item.evalRes == null);
			//Indica que se sobreescribió un valor -1.0 de una petición asíncrona
			//Modificado a evalRes = null 
			//logger.info("Item antes:" + item);
			
			item.setEvalRes(evalRes);
			
			//logger.info("Item:" + item);
			return false;
		}
	}
	
	@Override
	public void esperaAsinc(Problema problema) 
	{
		//En esta implementación no hace nada, al no ser concurrente
	}
	
	@Override
	public void dump()
	{
		Set<String> keySet = hashMap.keySet();
		List<String> list = new ArrayList<String>(keySet);     
		Collections.sort(list);
		
		int total = 0;
			
		for(String exp : list)
		{
			total += hashMap.get(exp).getHits();
			logger.info(exp + ";" + hashMap.get(exp));
		}
		
		logger.info("ExpCache: entradas:" + hashMap.size() + " total_hits:" + total + " eficiencia:" 
			+ (1.0 - (double) hashMap.size() / total));
	}

	@Override
	public int getTotalHits() 
	{
		Set<String> keySet = hashMap.keySet();
		List<String> list = new ArrayList<String>(keySet); 
		
		int total = 0;
		
		for(String exp : list)
		{
			total += hashMap.get(exp).getHits();
		}
		
		return total;
	}

	@Override
	public int getEntradas() 
	{
		return hashMap.size();
	}

	@Override
	public double getEficiencia() 
	{
		return 1.0 - (double) hashMap.size() / getTotalHits();
	}
	
	@Override
	public int getOcupacion() 
	{
		Set<String> keySet = hashMap.keySet();
		List<String> list = new ArrayList<String>(keySet); 
		
		int total = 0;
		
		for(String exp : list)
		{
			total += exp.length();
		}
		
		return total;
	}
}
