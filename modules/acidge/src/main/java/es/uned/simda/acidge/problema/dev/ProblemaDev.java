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
 * ProblemaDev
 *
 * Utiliza el patrón estrategy para implementar la construcción de circuitos y su evaluación
 * de forma desacoplada
 */
package es.uned.simda.acidge.problema.dev;

import es.uned.simda.acidge.problema.EvalRes;
import es.uned.simda.acidge.problema.InviableException;
import es.uned.simda.acidge.problema.ProblemaSC;
import es.uned.simda.acidge.problema.dev.eval.DevEval;
import es.uned.simda.acidge.problema.dev.eval.DevEvalFactory;
import es.uned.simda.acidge.spice.ProcesaSalidaSpice;
import es.uned.simda.acidge.spice.Signal;
import es.uned.simda.acidge.spice.Spice;
import es.uned.simda.acidge.ge.GEProperties;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class ProblemaDev extends ProblemaSC
{
	private final static Logger logger = Logger.getLogger(ProblemaDev.class.getName());
	
	private final static String nombreNetFile = "netlist";
	private final static String sepNetFile = "_";
	private final static String extNetFile = ".cir";
	private final static String nombreOutFile = "sal";
	private final static String extOutFile = ".txt";
	
	protected static String UserDir;

	//Strategy pattern para añadir un constructor de circuitos 
	// y un evaluador ad hoc del mismo
	protected DevEval devEval;
	protected CirConstructor cirConstructor;
	protected boolean TempSweepIndex;  			//para el circuito de referencia de voltaje
	
	protected Spice spice;
	
	public ProblemaDev()
	{
	}

	public ProblemaDev(GEProperties geproperties)
	{
		CirConstructorFactory cirConstructorFactory = CirConstructorFactory.getFactory();
		cirConstructor = cirConstructorFactory.createCirConstructor(geproperties.getCircuitConstructorClassName());
		
		DevEvalFactory devEvalFactory = DevEvalFactory.getFactory();
		
		devEval = devEvalFactory.createDevEval(geproperties.getDevEvalClassName(), geproperties);
		
		TempSweepIndex = devEval.getTempSweepIndex();
		
		double vtran = devEval.getVtran();
		cirConstructor.setVtran(vtran);

		spice = new Spice();
		
		UserDir = System.getProperty("user.dir");
	}	
	
	public ProblemaDev(String CircuitConstructorClassName, String DevEvalClassName, 
			double UmbralError, String FunAdaptacionClassName)
	{
		CirConstructorFactory cirConstructorFactory = CirConstructorFactory.getFactory();
		cirConstructor = cirConstructorFactory.createCirConstructor(CircuitConstructorClassName);
		
		GEProperties geproperties = new GEProperties();
		geproperties.setUmbralError(UmbralError);
		geproperties.setFunAdaptacionClassName(FunAdaptacionClassName);
		
		DevEvalFactory devEvalFactory = DevEvalFactory.getFactory();
		devEval = devEvalFactory.createDevEval(DevEvalClassName, geproperties);
		
		TempSweepIndex = devEval.getTempSweepIndex();
		
		double vtran = devEval.getVtran();
		cirConstructor.setVtran(vtran);

		spice = new Spice();
		
		UserDir = System.getProperty("user.dir");
	}	


	
	//protected abstract double getVtran();
	
	/*
	 * Evaluates the circuit
	 */
	@Override
	public EvalRes evalua(String exp) throws InviableException
	{	
		logger.fine(exp);
		
		//Se incrementa una nueva evaluación de circuito
		//Se contabilizan todos sean viables o no
		incrementEvaluationCount();
		
		boolean flag = cirConstructor.creaCircuitoExp(exp);
		
		//Esto es para circuitos inviables con nivel variable de penalización
		if(flag == false)
		{
			double fit = cirConstructor.getNivelInviable() * EvalRes.PenalizacionGradual;
			
			return new EvalRes(fit);
		}
		
		List<HashMap<String, Signal>> listaSignals = new ArrayList<HashMap<String, Signal>>();
			
		HashMap<String, Signal> signals;
		
		//Ejecuta las simulaciones necesarias
		for(int i = 0; i < cirConstructor.getSimulaciones(); i++)
		{
			signals = simula(i);
			listaSignals.add(signals);
		}
		
		//Component count is added to the first analysis
		ComponentCount cc = new ComponentCount(exp);
		Signal signalCC = cc.count();
		
		listaSignals.get(0).put(signalCC.getNombre(), signalCC);

		/*
		logger.info(Signal.signalsToString(listaSignals.get(0)));
		for(int i = 0; i < listaSignals.size(); i++)
			logger.info("listaSignals.get (i" + i + "):"+ listaSignals.get(i).size());
		*/
		
		EvalRes evalRes = devEval.evalua(listaSignals);
		//canonicalFitness = devEval.getCanonicalFitness();
		
		return evalRes;
	}
	


	/*
	 * Simula un circuito realizando el análisis indicado
	 */
	public HashMap<String, Signal> simula(int analisis) throws InviableException	
	{
		//String dir = UserDir + File.separator + nombreFecha + File.separator + generacion;
		String dir = UserDir + File.separator + nombreFecha;
		/*
		String netFile = dir + File.separator + nombreNetFile + getEvaluationCount() + 
			sepNetFile + analisis + extNetFile;
		*/
		String netFile = dir + File.separator + nombreNetFile + extNetFile;
		String outFile = dir + File.separator + nombreOutFile + extOutFile;
		/*
		String outFile = dir + File.separator + nombreOutFile  + getEvaluationCount() +
			sepNetFile + analisis + extOutFile;
		*/
		
		logger.info(netFile);
	
		cirConstructor.adaptaCircuitoAnalisis(analisis, netFile);
	
		logger.info("Lanza Spice");
		boolean ret = spice.lanza(netFile, outFile);
		
		if(ret == false)
		{
			logger.warning("Error en lanzamiento y proceso de Spice");
			throw new InviableException("Error en lanzamiento y proceso de Spice");
		}
		
		logger.fine("Llamando a ProcesaSalidaSpice");
		ProcesaSalidaSpice pss = new ProcesaSalidaSpice(outFile, TempSweepIndex);
		
		pss.lee();
		
		HashMap<String, Signal> signals = pss.getSignals();
		
		logger.fine("Fin simula");
		return signals;
	}
	
	//Artificio para llamar a logstats!!  
	@Override
	public void setNombreFecha(String nombreFecha) 
	{ 
		super.setNombreFecha(nombreFecha);
		//assert(nombreFecha != null); info(
		//this.nombreFecha = nombreFecha;
		
		spice.logStats();
	}
	
	/*
	 * Construct and return a circuit. Used to print the best circuit in the log file
	 */
	@Override
	public String build(String exp)
	{	
		String res = "";
		
		try
		{
			logger.fine(exp);
			
			boolean flag = cirConstructor.creaCircuitoExp(exp);
		
			logger.info("Flag:" + flag);
		
			res = cirConstructor.getCircuit(0);
		}
		catch(InviableException e)
		{
			res = "Unfeasible circuit";
		}
		
		return res;
	}
	
	
}
