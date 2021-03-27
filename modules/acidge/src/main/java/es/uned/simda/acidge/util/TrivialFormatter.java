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
 * Clase TrivialFormatter
 * 
 * Formatea las trazas a medida omitiendo fecha y nivel
 * 
 * ATENCIÓN: La clase debe ser pública para que pueda funcionar desde la referencia del fichero 
 * de propiedades
 */
package es.uned.simda.acidge.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class TrivialFormatter extends Formatter 
{
	final static String newLine = String.format("%n");
	final static String SEPARADOR = " "; 
	final static String logDateFormat = "yyyyMMdd_HHmmss_SSS";
	
	@Override
	public String format(LogRecord rec) 
	{
		//La documentación recomienda usar formatMessage(rec) para localizar la cadena
		return formatMessage(rec) + newLine;
	}
} 