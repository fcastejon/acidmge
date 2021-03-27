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
 * Clase Special
 * 
 * Clase con métodos estáticos para la expansión de cadenas especiales de las gramáticas
 */

package es.uned.simda.acidge.generador.parser;

public class Special 
{
	private final static String SPCR = "Carriage return";
	private final static String SPEOL = "EOL";

	//El tratamiento del retorno de carro se hará de forma dependiente de la plataforma
	private final static String newLine = String.format("%n");

	//Se define como contenedora de métodos estáticos, no se permite su instanciación
	private Special() {}
	
	public static String expande(String cadena)
	{
		if(SPCR.compareTo(cadena) == 0)
			return newLine;
		else if(SPEOL.compareTo(cadena) == 0)
			return newLine;
		else 
			throw new AssertionError("Cadena especial no soportada");
	}
}
