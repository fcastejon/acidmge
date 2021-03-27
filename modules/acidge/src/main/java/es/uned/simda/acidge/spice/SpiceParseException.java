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
 * SpiceParseException 
 */
package es.uned.simda.acidge.spice;

import java.util.logging.Logger;

public class SpiceParseException extends Exception 
{
	private final static Logger logger = Logger.getLogger(SpiceParseException.class.getName());

	//Sin otro uso que evitar un warning de eclipse 
	private static final long serialVersionUID = 1551961905346849834L;

	public SpiceParseException() 
	{
		super();
	}

	public SpiceParseException(String cadena) 
	{
		super(cadena);
	}

	public SpiceParseException(Throwable obj) 
	{
		super(obj);
	}

	public SpiceParseException(String cadena, Throwable obj) 
	{
		super(cadena, obj);
	}
}
