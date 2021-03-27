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
 * InviableException: clase de conveniencia para devolver errores de evaluación de circuitos
 */
package es.uned.simda.acidge.problema;

public class InviableException extends Exception 
{
	/*
	 * Evita un warning de Eclipse
	 */
	private static final long serialVersionUID = -4761068387660290116L;

	public InviableException()	
	{
		super();
	}

	public InviableException(String cadena) 
	{
		super(cadena);
	}

	public InviableException(Throwable excepcion) {
		super(excepcion);
	}

	public InviableException(String cadena, Throwable excepcion) 
	{
		super(cadena, excepcion);
	}

}
