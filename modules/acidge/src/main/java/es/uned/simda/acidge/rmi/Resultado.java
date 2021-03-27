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
 * Interfaz Resultado
 * 
 * Permite comunicar resultados de evaluación de circuitos al algoritmo principal
 */
package es.uned.simda.acidge.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import es.uned.simda.acidge.problema.EvalRes;

public interface Resultado extends Remote 
{
	void enviaResultado(String nombreServer, String expresion, EvalRes evalRes) throws RemoteException;
	void enviaInviableException(String nombreServer, String expresion) throws RemoteException;
}
