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
 * Clase ProcesaSalidaSpice
 * 
 * Procesa la salida de Spice
 * 
 * Obtiene todas las señales en frecuencia del análisis de pequeña señal
 *  
 */
package es.uned.simda.acidge.spice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import es.uned.simda.acidge.problema.InviableException;

import java.util.HashMap;
import java.util.logging.Logger;



public class ProcesaSalidaSpice 
{
	private final static Logger logger = Logger.getLogger(ProcesaSalidaSpice.class.getName());

	private static final String SPICE_SEP = "--------------------------------------------------------------------------------";
	private static final String KEYINDEX = "Index";
	private static final String KEYFREC = "frequency";
	private static final String KEYTIME = "time";
	private static final String KEYNODE = "	Node";
	private static final String KEYTEMP = "temp-sweep";
	private static final String KEYVSWEEP = "v-sweep";
	private static final String KEYBJT = " BJT: Bipolar Junction Transistor";
	private static final String KEYIC = "         ic";			//Corriente de colector
	private static final String KEYFOUR = "Fourier analysis";
	private static final String KEYHARM = "  No. Harmonics:";
	private static final String KEYTHD = "THD";
	private static final int FORMFEED = 12;
	private static final String ERROR1 = "Warning: singular matrix";
	private static final String ERROR2 = "doAnalyses: matrix is singular";
	private static final String ERROR3 = "Error: wavelength longer than time span";
	private static final String ERROR4 = "run simulation(s) aborted";
	
	//Ya no se utiliza
	//private static final String KEYNODESAL = "	V(4)";		//Continua a la salida
	
	
	private static final int MODO_FREC_CARTESIANO = 0;
	private static final int MODO_FREC_POLAR = 1;
	private static final int MODO_TIEMPO = 2;
	private static final int MODO_INDEX = 3;		//Utiliza index para KEYTEMP
	private static final int ESTADO_BUSCANDO = 0;
	private static final int ESTADO_BUSCANDO_1 = 1;
	private static final int ESTADO_LEYENDO = 3;
	private static final int ESTADO_LEYENDO_OP = 4;		//Lee v(4) del analisis op e ic del análisis del BJT
														//El estado es el mismo, pero no vienen en el mismo bloque
	
	private static final double PRECISION = 1e-12;		//Valores inferiores se redondean a cero
	
	private final File outFile;
	private int estado;
	private int modo;
	private String nombre;
	private boolean TempSweepIndex;
	
	HashMap<String, Signal> signals;
	Signal signal;
	
	public ProcesaSalidaSpice(String file, boolean tempSweepIndex)
	{
		this(new File(file), tempSweepIndex);
	}
		
	public ProcesaSalidaSpice(File outFile, boolean tempSweepIndex)
	{
		this.outFile = outFile;
		estado = ESTADO_BUSCANDO;
		modo = MODO_FREC_CARTESIANO;
		TempSweepIndex = tempSweepIndex;
		signals = new HashMap<String, Signal>();
	}
	
	public HashMap<String, Signal> getSignals() { return signals; }
	
	public void lee() throws InviableException
	{		
		BufferedReader br = null;
		String linea;
		 	 
		if (outFile.exists())
		{			
			try 
			{		
				br = new BufferedReader(new FileReader(outFile));
			}
			catch(FileNotFoundException e) 
			{
				logger.severe("Error archivo no encontrado");
			}
		}
		
		if(br != null)
		{
			try
			{
				try
				{
					while(((linea = br.readLine()) != null))
					{				
						procesaLinea(linea);
					}
				}
				finally
				{
					//logger.info("Cerrando fichero");
					//quita las señales vacías
					Signal.purgaSignals(signals);
					br.close();
				}
			}
			catch (IOException e) 
			{
				logger.severe("Error E/S: " + e.getMessage());
			}
		} 
	} 
	
	void procesaLinea(String linea) throws InviableException
	{
		try
		{
			maquinaEstados(linea);
		}
		catch(SpiceParseException ex)
		{
			estado = ESTADO_BUSCANDO;
			logger.severe(ex.getMessage());
		}
		catch(NumberFormatException ex)
		{
			estado = ESTADO_BUSCANDO;
			logger.severe(ex.getClass().toString() + ex.getMessage());
			logger.severe("Linea:" + linea);
		}
	}

	void maquinaEstados(String linea) throws SpiceParseException, InviableException
	{
		if((linea.startsWith(ERROR1)) || (linea.startsWith(ERROR2)))
		{
			throw new InviableException("Matriz singular");
		}
		else if(linea.contains(ERROR3))
		{
			throw new InviableException(ERROR3);
		}
		else if(linea.startsWith(ERROR4))
		{
			throw new InviableException(ERROR4);
		}
		//Encontramos una línea que comienza con "Index"
		else if((estado == ESTADO_BUSCANDO) && (linea.startsWith(KEYINDEX)))
		{
			String token;
			String tokens[] = linea.split("\\s+");
			int modoaux = MODO_FREC_CARTESIANO;
			String nombreaux = "";
			
			//logger.info(linea);	
			
			for(int ntoken = 0;ntoken < tokens.length;ntoken++)
			{
				token = tokens[ntoken];
				
				switch(ntoken)
				{
				case 0:
					if(KEYINDEX.equals(token) == false)
					{
						logger.info("Fuera de secuencia, no aparece:" + KEYINDEX);
						estado = ESTADO_BUSCANDO;
						throw new SpiceParseException("Fuera de secuencia, no aparece:" + KEYINDEX);
					}
					break;
				case 1:
					if(KEYFREC.equals(token) == true)
					{
						modoaux = MODO_FREC_CARTESIANO;
					}
					else if(KEYTIME.equals(token) == true)
					{
						modoaux = MODO_TIEMPO;
					}
					//La temperatura tiene dos casos según parámetro de duplicidad
					else if(KEYTEMP.equals(token) == true)
					{
						if(TempSweepIndex)
							modoaux = MODO_INDEX;
						else
							modoaux = MODO_TIEMPO;
					}
					//barrido de voltaje
					else if(KEYVSWEEP.equals(token) == true)
					{
						modoaux = MODO_TIEMPO;
					}
					else
					{
						logger.info("Fuera de secuencia, no aparece:" + KEYFREC + " :" + KEYTIME + " :" + KEYTEMP + " :" + KEYVSWEEP);
						estado = ESTADO_BUSCANDO;
						throw new SpiceParseException("Fuera de secuencia");
					}
					break;
				case 2:
					//Tomamos el nombre y se mantiene el modo detectado
					nombreaux = token;
					break;
				//Aparece con el volcado en polares, en el caso de tiempo es otro vector que se rechaza
				case 3:
					if(modoaux == MODO_FREC_CARTESIANO)
						modoaux = MODO_FREC_POLAR;
					else
					{
						logger.info("Modo tiempo, hay más de un vector!! Se rechaza el segundo");
					}
					break;
				default:
					logger.info("Demasiados tokens1");
					estado = ESTADO_BUSCANDO;
					throw new SpiceParseException("Demasiados tokens1");
				}
			}

			estado = ESTADO_BUSCANDO_1;
			nombreaux += calificador(modoaux);	//Añade f o t al nombre según dominio (o análisis ac o tran)

			if(nombreaux.equals(nombre))
			{
				assert(signal != null);
				//logger.info("Vector continúa:" + nombre + " modo:" + modo2String(modo));
			}
			else
			{
				modo = modoaux;
				nombre = nombreaux;

				//logger.info("Encontrado vector:" + nombre + " modo:" + modo2String(modo));
				signal = new Signal(nombre);
				signals.put(nombre, signal);
			}
		}
		else if((estado == ESTADO_BUSCANDO) && linea.startsWith(KEYNODE))
		{
			//Se ha detectado el análisis OP, se busca el voltaje de salida
			estado = ESTADO_LEYENDO_OP;
		}
		else if((estado == ESTADO_BUSCANDO) && linea.startsWith(KEYBJT))
		{		
			//Se ha detectado el análisis del transistor, se busca la Ic
			estado = ESTADO_LEYENDO_OP;
		}
		else if((estado == ESTADO_BUSCANDO) && linea.startsWith(KEYFOUR))
		{		
			//Se ha detectado el análisis de Fourier, se busca el valor de THD
			estado = ESTADO_LEYENDO_OP;
		}
		else if(estado == ESTADO_BUSCANDO_1)
		{
			if(SPICE_SEP.equals(linea))
			{
				//logger.info("Encuentra segunda raya");
				estado = ESTADO_LEYENDO;
			}
			else
			{
				logger.info("Demasiados tokens2");
				estado = ESTADO_BUSCANDO;
				throw new SpiceParseException("Demasiados tokens2");
			}
		}
		else if(((estado == ESTADO_LEYENDO) || (estado == ESTADO_LEYENDO_OP)) && linea.isEmpty())
		{
			//Línea vacía siempre es un final de vector
			estado = ESTADO_BUSCANDO;
		}
		else if((estado == ESTADO_LEYENDO) && (linea.toCharArray()[0] == FORMFEED))
		{	
			//Desgraciadamente, un formfeed puede continuar el vector... 
			//logger.info("FormFeed");
			estado = ESTADO_BUSCANDO;
		}
		else if(estado == ESTADO_LEYENDO)
		{
			//Leemos valores
			SigItem si = readSigItem(linea);
			signal.put(si);
		}
		/* Ya no se utiliza la continua a la salida en amplificadores
		else if((estado == ESTADO_LEYENDO_OP) && linea.startsWith(KEYNODESAL))
		{
			Signal signal = readSignalNode(linea);
			signals.put(signal.getNombre(), signal);
		}
		*/
		else if((estado == ESTADO_LEYENDO_OP) && linea.startsWith(KEYIC))
		{
			Signal signal = readSignalNode(linea);
			signals.put(signal.getNombre(), signal);
		}
		else if((estado == ESTADO_LEYENDO_OP) && linea.startsWith(KEYHARM))
		{
			Signal signal = readTHD(linea);
			signals.put(signal.getNombre(), signal);
		}
		else
		{
			//Ignoramos cualquier otra línea
		}
	}
	
	SigItem readSigItem(String linea) throws SpiceParseException, NumberFormatException
	{
		String token;
		double ft = 0;
		double magnitud = 0;
		double fase = 0;
		double val1 = 0;
		double val2 = 0;
		
		String tokens[] = linea.split(",?\\s+");
		
		//logger.info(linea);	
		
		for(int ntoken = 0;ntoken < tokens.length;ntoken++)
		{
			token = tokens[ntoken];
			
			switch(ntoken)
			{
			case 0:
				//Es el índice, sólo se toma en el caso de MODO_INDEX
				if(modo == MODO_INDEX)
					ft = Double.parseDouble(token);
				break;
			case 1:
				//Frecuencia, tiempo, voltajes, ... para resto de modos
				if(modo != MODO_INDEX)
					ft = Double.parseDouble(token);
				break;
			case 2:
				val1 = Double.parseDouble(token);
				break;
			case 3:
				val2 = Double.parseDouble(token);
				break;
			default:
				logger.info("Mas campos de los esperados");
				throw new SpiceParseException("Mas campos de los esperados"); 		
			}
		}
		
		if(modo == MODO_FREC_CARTESIANO)
		{
			magnitud = Signal.convertMagnitud(val1, val2);
			fase = Signal.convertFase(val1, val2);
		}
		else if(modo == MODO_FREC_POLAR)
		{
			magnitud = val1;
			fase = val2;
		}
		else if((modo == MODO_TIEMPO) || (modo == MODO_INDEX))
		{
			magnitud = val1;
			fase = 0;
		}
		else throw new AssertionError("Código no alcanzable");
		
		//Se redondea el indice ft a cero si es menor que PRECISION
		if(Math.abs(ft) < PRECISION)
			ft = 0.0;
		
		return new SigItem(new Double(ft), magnitud, fase);
	}
	
	//Lee valores de corriente y voltaje del punto de polarización
	//Sólo se usa en el caso de amplificador de una etapa
	//En caso de encontrar más de una lectura, ignora el resto de datos
	Signal readSignalNode(String linea) throws SpiceParseException, NumberFormatException
	{
		String token;
		double v = 0;
		String nombre = "";
		
		//logger.info(linea);
		
		//linea = linea.replace("\\t", "");
		//logger.info(linea);
		
		//Por alguna razón no funciona con "\\t|[ ]+", se quita el tabulador posteriormente
		//String tokens[] = linea.split("[ ]+|\n");
		//La cadena que funciona es la siguiente:
		String tokens[] = linea.split("\\s+");
		
		
		for(int ntoken = 0;ntoken < tokens.length;ntoken++)
		{
			token = tokens[ntoken];
			
			//logger.info("ntoken:" + ntoken + ":" + token + " l:" + token.length());
			
			switch(ntoken)
			{
			case 0:
				//Por alguna razón el primer token es de longitud 0
				assert(token.length() == 0);
				break;
			case 1:
				//nombre = token.replaceFirst("\\t", "");
				nombre = token;
				break;
			case 2:
				//Es el voltaje o la ic 
				v = Double.parseDouble(token);
				break;
			default:
				logger.finest("Se ignoran los campos adicionales");
				//throw new SpiceParseException("Mas campos de los esperados");
				break;
			}
		}
		
		//Se registra como único item el valor de voltaje o de ic
		SigItem si = new SigItem(0, v, 0);
		Signal signal = new Signal(nombre);
		signal.put(si);
		
		//logger.info(si.toString());
		
		return signal;
	}
	
	//Lectura ad hoc de la línea de análisis de Fourier
	Signal readTHD(String linea) throws NumberFormatException
	{
		String token;
		double thd = 0;
		String nombre = null;
		
		//logger.info(linea);
	
		//Separación en tokens
		String tokens[] = linea.split(":\\s*|,\\s*|\\s+");
		
		for(int ntoken = 0;ntoken < tokens.length;ntoken++)
		{
			token = tokens[ntoken];
			
			//logger.info("ntoken:" + ntoken + ":" + token + " l:" + token.length());
			
			if(token.equals(KEYTHD) == true)
			{
				//logger.info("Encontrado KEYTHD:" + tokens[ntoken + 1]);
				nombre = token;
				
				token = tokens[ntoken + 1];
				if("nan".equals(token) || "-nan".equals(token))
					thd = Double.NaN;
				else
					thd = Double.parseDouble(token);
				
				//logger.info("THD:" + thd);
				break;
			}
		}
		
		assert(nombre != null);
		
		//Se registra como único item el valor de voltaje o de ic
		SigItem si = new SigItem(0, thd, 0);
		Signal signal = new Signal(nombre);
		signal.put(si);
		
		//logger.info(si.toString());
		
		return signal;
	}
	
	
	private String modo2String(int modo)
	{
		String ret = "";
		
		switch(modo)
		{
		case MODO_FREC_CARTESIANO:
			ret = "MODO_FREC_CARTESIANO";
			break;
		case MODO_FREC_POLAR:
			ret = "MODO_FREC_POLAR";
			break;
		case MODO_TIEMPO:
			ret = "MODO_TIEMPO";
			break;
		default:
			logger.info("Código no alcanzable");
			throw new AssertionError("Código no alcanzable");
		}
		
		return ret;
	}
	
	private String calificador(int modo)
	{
		String ret = "";
		
		switch(modo)
		{
		case MODO_FREC_CARTESIANO:
			ret = "f";
			break;
		case MODO_FREC_POLAR:
			ret = "f";
			break;
		case MODO_TIEMPO:
			ret = "t";
			break;
		case MODO_INDEX:
			ret = "i";
			break;
		default:
			logger.info("Código no alcanzable");
			throw new AssertionError("Código no alcanzable");
		}
		
		return ret;
	}
}
