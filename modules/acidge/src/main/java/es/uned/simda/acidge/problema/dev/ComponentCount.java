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
 * ComponentCount
 * 
 * Counts components from an expression
 * 
 */
package es.uned.simda.acidge.problema.dev;

import java.util.HashMap;
import java.util.logging.Logger;

import es.uned.simda.acidge.spice.SigItem;
import es.uned.simda.acidge.spice.Signal;

public class ComponentCount 
{    
	private final static Logger logger = Logger.getLogger(ComponentCount.class.getName());

	public static final String NAMECOMPONENTCOUNT = "ComponentCount";
	private static final String KEYCOMMENT = "*";
	
	private String exp;
	
	ComponentCount(String exp)
	{
		this.exp = exp;
	}
	
	/*
	 * Counts components from an expression
	 */
	public Signal count()
	{
		int num_componentes = 0;

		String lines[] = exp.split("\\r?\\n");
		
		for(int i = 0; i < lines.length; i++)
		{
			if(lines[i].startsWith(KEYCOMMENT) == false)
				num_componentes++;
		}

        //Se registra como único item el valor de num_componentes
        SigItem si = new SigItem(0, num_componentes, 0);
        Signal signal = new Signal(NAMECOMPONENTCOUNT);
        signal.put(si);

        return signal;
	}

}
