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
 * Clase RandomMock 
 * 
 * Se utiliza para las pruebas de código que utiliza números aleatorios
 * permite devolver una secuencia conocida que permita realizar pruebas deterministas
 */
package es.uned.simda.acidge.ge.random;

import java.util.logging.Logger;




public class RandomMock implements RandomGenerator 
{
	private final static Logger logger = Logger.getLogger(RandomMock.class.getName());

	double [] rd;
	int [] ri;
	int id;
	int ii;

	public RandomMock() 
	{
		super();
		
		rd = null;
		ri = null;
		id = -1;
		ii = -1;
	}
	
	public void setDouble(double [] d)
	{
		rd = d;
		id = 0;
	}
	
	public void setInteger(int [] i)
	{
		ri = i;
		ii = 0;
	}
	
	@Override
	public double random()
	{
		assert(rd != null);
		assert((id >= 0) && (id < rd.length));
		
		double val = rd[id++];		
		//Check por consistencia del array pasado
		assert((val > 0) && (val < 1));
		
		return val;
	}
	
	@Override
	public int randomInt(int maxValue)
	{
		assert(ri != null);
		assert((ii >= 0) && (ii < ri.length));
		
		int val = ri[ii++];
		logger.info("randomInt:" + val + " / " + maxValue);
		
		//Check por consistencia del array pasado	
		assert(val >= 0);
		
		if(val >= maxValue)
			logger.info("randomInt val:" + val + " mayor o igual que maxValue:" + maxValue);
		
		assert(val < maxValue);
		
		return val;
	}
}
