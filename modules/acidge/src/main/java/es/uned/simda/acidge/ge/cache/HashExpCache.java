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
 * Clase HashExpCache
 * 
 * Esta clase utiliza un hash de las expresiones para ahorrar memoria
 */
package es.uned.simda.acidge.ge.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.Problema;

public class HashExpCache extends ExpCacheDecorator 
{
	private final static Logger logger = Logger.getLogger(HashExpCache.class.getName());
	private final String SEPARADOR = "-";

    MessageDigest md;
	
	public HashExpCache(ExpCache expCache)
	{		
		super(expCache);
		try 
		{
			md = MessageDigest.getInstance("SHA1");
		} 
		catch (NoSuchAlgorithmException e) 
		{
			logger.severe("HashExpCache: No es posible obtener instancia de SHA1");
			throw new AssertionError("NoSuchAlgorithm");
		}	
	}
	
	@Override
	public synchronized EvalItem getEvalItem(String exp) throws ExpCacheNotFoundException, 
		ExpCacheSyncYaSolicitadoException
	{
		String hash = stringHash(exp);
		
		return expCache.getEvalItem(hash);
	}

	@Override
	public boolean add(String exp, EvalRes evalRes) 
	{
		String hash = stringHash(exp);
		
		return expCache.add(hash, evalRes);
	}
	
	private String stringHash(String exp)
	{
		String res;
		
		//FIXME: no se tiene en cuenta la codificación de caracteres!!
		byte[] bexp = exp.getBytes();
	    
		md.update(bexp);
	     
		byte[] mdbytes = md.digest();
	 
		//convert the byte to hex format
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mdbytes.length; i++) 
		{
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		sb.append(SEPARADOR);
		sb.append(new Integer(exp.length()).toString());
		
		res = sb.toString();
			
		logger.fine(res);
		
		return res;
	}
	
	@Override
	public void esperaAsinc(Problema problema) 
	{
		expCache.esperaAsinc(problema);
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
