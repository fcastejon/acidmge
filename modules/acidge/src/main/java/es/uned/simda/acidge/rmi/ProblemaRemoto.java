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
 * Interfaz ProblemaRemoto
 * 
 * 	No extiende Problema, sino que declara los métodos de nuevo
 *	Esto es así pues las interfaces que van por RMI deben lanzar RemoteException
 *	y no resulta limpio añadir dicha excepción a la interfaz Problema
 **
 */

package es.uned.simda.acidge.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProblemaRemoto extends Remote 
{
	void evalua(String exp) throws RemoteException;
	
	int getEvaluationCount() throws RemoteException;
	void resetEvaluationCount() throws RemoteException;
	void setGeneracion(int generacion) throws RemoteException;
	void setNombreFecha(String nombreFecha) throws RemoteException;
	String build(String exp) throws RemoteException;
	
	//Métodos añadidos a esta interfaz además de los de Problema
	public boolean sanityCheck(String ProblemClassName, String CircuitConstructorClassName,
			String DevEvalClassName, String FunAdaptacionClassName) throws RemoteException;
	public void bind(String masterHostName) throws RemoteException;
}
