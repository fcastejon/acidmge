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
 * Server
 * 
 * Esta clase permite al maestro gestionar servidores remotos
 * 
 * Contiene una interfaz Problema stub de un ProblemaRemoto
 * También contien otros indicadores de carga
 * 
 * Implementa sincronización para ser multithread safe
 */

package es.uned.simda.acidge.rmi;

import java.util.logging.Logger;

public class Server 
{
	private final static Logger logger = Logger.getLogger(Server.class.getName());

	//host y numero identifican un servidor
	private final String host;
	private final int numero;
	private ProblemaRemoto problema;		//Stub de un ProblemaRemoto 
	private int carga;
	
	//El nombre facilitado se compone como hostname/numero
	Server(String nombreServer)
	{	
		String [] aux = nombreServer.split("/");
		
		this.host = aux[0];
		this.numero = Integer.parseInt(aux[1]);
		carga = 0;
	}
	
	String getHost() { return host; }
	int getNumero() { return numero; }
	String getServerName() { return host + "/" + numero; }
	void setProblema(ProblemaRemoto problema) { assert(problema != null); this.problema = problema; }
	ProblemaRemoto getProblema() { return problema; }
	
	public String toString()
	{
		return host + "/" + numero + ":" + carga;
	}
	
	synchronized void incCarga()
	{
		carga++;
	}
	
	synchronized void decCarga()
	{
		carga--;
		if(carga < 0)
			throw new AssertionError("server null");
	}

	synchronized int getCarga()
	{
		return carga;
	}
}
