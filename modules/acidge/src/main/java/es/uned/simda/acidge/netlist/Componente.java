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
 * Componente de circuito
 */
package es.uned.simda.acidge.netlist;

import java.text.DecimalFormat;
import java.util.logging.Logger;



public class Componente 
{
	private final static Logger logger = Logger.getLogger(Componente.class.getName());

	public final static int Z = 0;	//hilo modificable
	public final static int R = 1;
	public final static int C = 2;
	public final static int V = 3;
	public final static int VAC = 4;
	public final static int Q = 5;
	public final static int JFET = 6;
	public final static int MOSFET = 7;
	
	final static String nombreZ = "Z";
	final static String nombreR = "R";
	final static String nombreC = "C";
	final static String nombreV = "V";
	final static String nombreVAC = "VAC";
	final static String nombreQ = "Q";
	final static String nombreJFET = "J";
	final static String nombreMOSFET = "M";
	
	final int tipo;
	double valor;
	final String nombre;
	final int sec;
	final String modelo;

	public Componente(int tipo)
	{
		this(tipo, -1, "");
	}
	
	public Componente(int tipo, double valor, String modelo)
	{
		assert(valida(tipo));
		this.tipo = tipo;
		this.valor = valor;
		
		sec = Secuencia.getSecuencia(getStringTipo());
		nombre = getStringTipo() + sec;
		this.modelo = modelo;
		
		//logger.info("Constructor Componente:" + this);
	}
	
	public Componente(int tipo, double valor)
	{
		this(tipo, valor, "");
	}
	
	public Componente(int tipo, String modelo)
	{
		this(tipo, -1, modelo);
	}
	
	public Componente(Componente componente)
	{
		assert(valida(componente.tipo));
		
		this.tipo = componente.tipo;
		this.valor = componente.valor;
		
		sec = Secuencia.getSecuencia(getStringTipo());
		nombre = getStringTipo() + sec;
		this.modelo = getModelo();
	}
	
	public int getTipo() {	return tipo; }
	//public void setTipo(int tipo) {	assert(valida(tipo)); this.tipo = tipo;	}
	public String getStringTipo() { return tipoToString(tipo); }
	public double getValor() {	/*logger.info("Componente valor:" + valor);*/ return valor;	}
	public void setValor(double valor) { this.valor = valor; }
	public String getNombre() { return nombre; }
	public String getModelo() { return modelo; }
	//public boolean isModificable() { return modificable; }
	//public void fija() { modificable = false; }
	//public void setModificable(boolean modificable) { this.modificable = modificable; } 

	static boolean valida(int tipo)
	{
		switch(tipo)
		{
			case Z:
			case R:
			case C:
			case V:
			case VAC:
			case Q:
			case JFET:
			case MOSFET:
				return true;
			default:
				return false;
		}
	}
	
	static String tipoToString(int tipo)
	{
		switch(tipo)
		{
			case Z:
				return nombreZ;
			case R:
				return nombreR;
			case C:
				return nombreC;
			case V:
				return nombreV;
			case VAC:
				return nombreVAC;
			case Q:
				return nombreQ;
			case JFET:
				return nombreJFET;
			case MOSFET:
				return nombreMOSFET;
			default:
				throw new Error("Valor tipo no valido:" + tipo);
		}
	}

	public String toString()
	{
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
		df.applyPattern("0.0");
		
		return nombre + " valor:" + df.format(valor);
	}
	
	public static double StringToValor(String cadena)
	{
		double valor = 0.0;
		int exp = 0;
		
		logger.finer("StringToValor:" + cadena);

		if(cadena.endsWith("p"))
			exp = -12;
		else if(cadena.endsWith("n"))
			exp = -9;
		else if(cadena.endsWith("u"))
			exp = -6;
		else if(cadena.endsWith("m"))
			exp = -3;
		else if(cadena.endsWith("K"))
			exp = 3;
		else if(cadena.endsWith("Meg"))
			exp = 6;
		else if(cadena.endsWith("G"))
			exp = 9;
		
		if(exp != 0)
			cadena = cadena.replaceFirst("p|n|u|m|K|Meg|G", "");
		
		logger.finer("StringToValor sin sufijo:" + cadena);
		
		valor = Double.parseDouble(cadena);
		
		valor *= Math.pow(10, exp);
		
		logger.fine("StringToValor valor:" + valor);
		
		return valor;
	}
	
	public static String valorToString(double valor)
	{
		DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
		df.applyPattern("0.0");
		//obtenemos el exponente cuantificado a múltiplos de 3
		int exp;
		
		//Umbral para considerar 0
		if(valor < 1e-15)
		{
			exp = 0;
		}
		else
		{
			exp = ((int) Math.floor(Math.log10(valor) / 3)) * 3;
		}
		
		//logger.info("Valor:" + valor + " exp:" + exp);

		assert((exp <= 9) && (exp >= -12));
			
		//double mant = (int) Math.round(valor / Math.pow(10, exp));
		//double par = valor / Math.pow(10, exp - 1);
		//double par2 = Math.round(par);
		//double par3 = par2 / 10;
		
		//logger.info("par:" + par + " par2:" + par2 + " par3:" + par3);
		
		double mant = 0.1 * Math.round(valor / Math.pow(10, exp - 1));
		
		//logger.info("mant:" + mant);
		
		return df.format(mant) + sufijo(exp);
	}
	
	public static String sufijo(int exp)
	{
		assert(exp % 3 == 0);
		
		switch(exp)
		{
		case -12:
			return "p";
		case -9:
			return "n";
		case -6:
			return "u";
		case -3:
			return "m";
		case 0:
			return "";
		case 3:
			//Manual de NGSpice dice que debe ser mayúscula
			return "K";
		case 6:
			return "Meg";
		case 9:
			return "G";
		default:
			logger.info("Exponente no múltiplo de 3:" + exp);
			throw new AssertionError("Código no alcanzable");
		}
	}
}
