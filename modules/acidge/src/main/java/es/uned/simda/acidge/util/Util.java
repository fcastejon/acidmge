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
 * Clase Util
 * 
 * Contiene funciones de utilidad comunes
 */
package es.uned.simda.acidge.util;

public class Util 
{
	public static void checkEA()
	{
		//Comprobación de ejecución con assert activada
		 boolean ea = false;
	     assert(ea = true); 
	     if (!ea)
	     {
	    	 System.err.println("checkEA: es necesario activar la opción enableassertions");
	    	 throw new RuntimeException(
	    			 "Es necesario activar la opción enableassertions");
	     }
	}
}
