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
 * Clase GEProperties
 * 
 * Obtiene los datos del algoritmo genético a partir de un fichero de propiedades
 */
package es.uned.simda.acidge.ge;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class GEProperties 
{	
	private final static Logger logger = Logger.getLogger(GEProperties.class.getName());
	private final static String nombreClase = "GEProperties:";     //Para el volcado de valores

	private String ProblemClassName;
	private int IndividualsNumber;

	private int GenerationsNumber;
	private int TerminationConditionType;
	private String PopulationInitializationClassName;
	private String ParentSelectionClassName;
	//private int ExpectedOffspringNumberForFittestIndividual;
	private int TournamentSize;
	private String CrossoverClassName;
	private double CrossoverRate;
	private int CrossoverPointsNumber;
	//private double ExchangeProbability;
	private double CrossoverRateGE2;
	private double CrossoverRateGE3;
	private String MutationClassName;
	private double MutationRate;
	private double MutationRateGE2;
	private String SurvivorSelectionClassName;
	private int Elitism;
	private int GenerationalGap;
	private int MinGenesNumber;
	private int MaxGenesNumber;
	private String DuplicationClassName;
	private double DuplicationRate;
	private double GoalFitness;
	private boolean HasGoal;
	private int MaxEvaluationCount;
	//private int Parsimony;
	//private double ParsimonyValue;
	private int MaxWrappingNumber;
	private String RandomGeneratorClassName;
	private int MaxRecursionLevel;
	private boolean Paralelismo;
	private int LimitMaxGenesNumber;
	private String FunAdaptacionClassName;
	private String GrammarFileName;
	private String CircuitConstructorClassName;
	private String DevEvalClassName;
	private boolean HashCache;
	private int CrossoverBlockSize;
	private String GeneratorClassName;
	private double UmbralError;
		
	//Nombres de las propiedades
	final static private String ProblemClassNameName = "ProblemClassName";
	final static private String IndividualsNumberName = "IndividualsNumber";
	final static private String MinGenesNumberName = "MinGenesNumber";
	final static private String MaxGenesNumberName = "MaxGenesNumber";
	final static private String GenerationsNumberName = "GenerationsNumber";
	final static private String TerminationConditionTypeName = "TerminationConditionType";
	final static private String PopulationInitializationClassNameName = "PopulationInitializationClassName";
	final static private String ParentSelectionClassNameName = "ParentSelectionClassName";
	//final static private String ExpectedOffspringNumberForFittestIndividualName = "ExpectedOffspringNumberForFittestIndividual";
	final static private String TournamentSizeName = "TournamentSize";
	final static private String CrossoverClassNameName = "CrossoverClassName";
	final static private String CrossoverRateName = "CrossoverRate";
	final static private String CrossoverPointsNumberName = "CrossoverPointsNumber";
	//final static private String ExchangeProbabilityName = "ExchangeProbability";
	final static private String MutationClassNameName = "MutationClassName";
	final static private String MutationRateName = "MutationRate";
	final static private String DuplicationClassNameName = "DuplicationClassName";
	final static private String DuplicationRateName = "DuplicationRate";
	final static private String SurvivorSelectionClassNameName = "SurvivorSelectionClassName";
	final static private String ElitismName = "Elitism";
	final static private String GenerationalGapName = "GenerationalGap";
	final static private String GoalFitnessName = "GoalFitness";
	final static private String HasGoalName = "HasGoal";
	final static private String MaxEvaluationCountName = "MaxEvaluationCount";
	//final static private String ParsimonyName = "Parsimony";
	//final static private String ParsimonyValueName = "ParsimonyValue";
	final static private String MaxWrappingNumberName = "MaxWrappingNumber";
	final static private String RandomGeneratorClassNameName = "RandomGeneratorClassName";
	final static private String MaxRecursionLevelName = "MaxRecursionLevel";
	final static private String ParalelismoName = "Paralelismo";
	final static private String LimitMaxGenesNumberName = "LimitMaxGenesNumber";
	final static private String FunAdaptacionClassNameName = "FunAdaptacionClassName";
	final static private String GrammarFileNameName = "GrammarFileName";
	final static private String CircuitConstructorClassNameName = "CircuitConstructorClassName";
	final static private String DevEvalClassNameName = "DevEvalClassName";
	final static private String HashCacheName = "HashCache";
	final static private String CrossoverBlockSizeName = "CrossoverBlockSize";
	final static private String GeneratorClassNameName = "GeneratorClassName";
	final static private String MutationRateGE2Name = "MutationRateGE2";
	final static private String CrossoverRateGE2Name = "CrossoverRateGE2";
	final static private String CrossoverRateGE3Name = "CrossoverRateGE3";
	final static private String UmbralErrorName = "UmbralError";
	
    Properties properties;  

	public String getProblemClassName() { return ProblemClassName; }
	public void setProblemClassName(String problemClassName) { ProblemClassName = problemClassName; }
	public int getIndividualsNumber() {	return IndividualsNumber; }
	public void setIndividualsNumber(int individualsNumber) { IndividualsNumber = individualsNumber; }
	public int getGenerationsNumber() {	return GenerationsNumber; }
	public void setGenerationsNumber(int generationsNumber) { GenerationsNumber = generationsNumber; }
	public int getTerminationConditionType() { return TerminationConditionType;	}
	public void setTerminationConditionType(int terminationConditionType) 
		{ TerminationConditionType = terminationConditionType; }
	//public int getPopulationInitializationType() { return PopulationInitializationType;	}
	//public void setPopulationInitializationType(int populationInitializationType) 
	//	{ PopulationInitializationType = populationInitializationType;	}
	public String getPopulationInitializationClassName() { return PopulationInitializationClassName;	}
	public void setPopulationInitializationClassName(String populationInitializationClassName) 
		{ PopulationInitializationClassName = populationInitializationClassName;	}
	//public int getParentSelectionType() { return ParentSelectionType; }
	//public void setParentSelectionType(int parentSelectionType) { ParentSelectionType = parentSelectionType; }
	public String getParentSelectionClassName() { return ParentSelectionClassName; }
	public void setParentSelectionClassName(String parentSelectionClassName) { ParentSelectionClassName = parentSelectionClassName; }
	//public int getExpectedOffspringNumberForFittestIndividual() { return ExpectedOffspringNumberForFittestIndividual; }
	//public void setExpectedOffspringNumberForFittestIndividual(int expectedOffspringNumberForFittestIndividual) { ExpectedOffspringNumberForFittestIndividual = expectedOffspringNumberForFittestIndividual; }
	public int getTournamentSize() { return TournamentSize;	}
	public void setTournamentSize(int tournamentSize) { TournamentSize = tournamentSize; }
	//public int getCrossoverType() {	return CrossoverType; }
	//public void setCrossoverType(int crossoverType) { CrossoverType = crossoverType; }
	public String getCrossoverClassName() {	return CrossoverClassName; }
	public void setCrossoverClassName(String crossoverClassName) { CrossoverClassName = crossoverClassName; }
	public double getCrossoverRate() { return CrossoverRate; }
	public void setCrossoverRate(double crossoverRate) { CrossoverRate = crossoverRate; }
	public int getCrossoverPointsNumber() { return CrossoverPointsNumber; }
	public void setCrossoverPointsNumber(int crossoverPointsNumber) 
		{ CrossoverPointsNumber = crossoverPointsNumber; }
	//public double getExchangeProbability() { return ExchangeProbability; }
	//public void setExchangeProbability(double exchangeProbability) { ExchangeProbability = exchangeProbability; }
	public double getCrossoverRateGE2() { return CrossoverRateGE2; }
	public void setCrossoverRateGE2(double crossoverRateGE2) { CrossoverRateGE2 = crossoverRateGE2; }
	public double getCrossoverRateGE3() { return CrossoverRateGE3; }
	public void setCrossoverRateGE3(double crossoverRateGE3) { CrossoverRateGE3 = crossoverRateGE3; }
	//public int getMutationType() { return MutationType; }
	//public void setMutationType(int mutationType) {	MutationType = mutationType; }
	public String getMutationClassName() { return MutationClassName; }
	public void setMutationClassName(String mutationClassName) { MutationClassName = mutationClassName; }
	public double getMutationRate() { return MutationRate; }
	public void setMutationRate(double mutationRate) { MutationRate = mutationRate; }
	public double getMutationRateGE2() { return MutationRateGE2; }
	public void setMutationRateGE2(double mutationRateGE2) { MutationRateGE2 = mutationRateGE2; }
	//public int getSurvivorSelectionType() {	return SurvivorSelectionType; }
	//public void setSurvivorSelectionType(int survivorSelectionType) { SurvivorSelectionType = survivorSelectionType; }
	public String getSurvivorSelectionClassName() { return SurvivorSelectionClassName; }
	public void setSurvivorSelectionClassName(String survivorSelectionClassName) 
		{ SurvivorSelectionClassName = survivorSelectionClassName; }
	public int getElitism() { return Elitism; }
	public void setElitism(int elitism) { Elitism = elitism; }
	public int getGenerationalGap() { return GenerationalGap; }
	public void setGenerationalGap(int generationalGap) { GenerationalGap = generationalGap; }
	public int getMinGenesNumber() { return MinGenesNumber; }
	public void setMinGenesNumber(int minGenesNumber) { MinGenesNumber = minGenesNumber; }
	public int getMaxGenesNumber() { return MaxGenesNumber; }
	public void setMaxGenesNumber(int maxGenesNumber) {	MaxGenesNumber = maxGenesNumber; }
	//public int getDuplicationType() { return DuplicationType; }
	//public void setDuplicationType(int duplicationType) { DuplicationType = duplicationType; }
	public String getDuplicationClassName() { return DuplicationClassName; }
	public void setDuplicationClassName(String duplicationClassName) { DuplicationClassName = duplicationClassName; }
	public double getDuplicationRate() { return DuplicationRate; }
	public void setDuplicationRate(double duplicationRate) { DuplicationRate = duplicationRate;	}
	public double getGoalFitness() { return GoalFitness; }
	public void setGoalFitness(double goalFitness) { GoalFitness = goalFitness; }
	public boolean isHasGoal() { return HasGoal; }
	public void setHasGoal(boolean hasGoal) { HasGoal = hasGoal; }
	public int getMaxEvaluationCount() { return MaxEvaluationCount; }
	public void setMaxEvaluationCount(int maxEvaluationCount) {	MaxEvaluationCount = maxEvaluationCount; }
	/*
	public int getParsimony() {	return Parsimony; }
	public void setParsimony(int parsimony) { Parsimony = parsimony; }
	public double getParsimonyValue() {	return ParsimonyValue; }
	public void setParsimonyValue(double parsimonyValue) { ParsimonyValue = parsimonyValue;	}
	*/
	public int getMaxWrappingNumber() {	return MaxWrappingNumber; }
	public void setMaxWrappingNumber(int maxWrappingNumber) { MaxWrappingNumber = maxWrappingNumber; }
	public String getRandomGeneratorClassName() { return RandomGeneratorClassName; }
	public void setRandomGeneratorClassName(String randomGeneratorClassName) { RandomGeneratorClassName = randomGeneratorClassName; }
	public boolean isParalelismo() { return Paralelismo; }
	public void setParalelismo(boolean paralelismo) { Paralelismo = paralelismo; }
	public int getLimitMaxGenesNumber() { return LimitMaxGenesNumber; }
	public void setLimitMaxGenesNumber(int limitMaxGenesNumber) { LimitMaxGenesNumber = limitMaxGenesNumber; }
	//public int getTipoFunAdaptacion() { return TipoFunAdaptacion; }
	//public void setTipoFunAdaptacion(int tipoFunAdaptacion) { TipoFunAdaptacion = tipoFunAdaptacion; }
	public String getFunAdaptacionClassName() { return FunAdaptacionClassName; }
	public void setFunAdaptacionClassName(String funAdaptacionClassName) { FunAdaptacionClassName = funAdaptacionClassName; }
	public String getGrammarFileName() { return GrammarFileName; }
	public void setGrammarFileName(String grammarFileName) { GrammarFileName = grammarFileName;	}
	public int getMaxRecursionLevel() { return MaxRecursionLevel; }
	public void setMaxRecursionLevel(int maxRecursionLevel) { MaxRecursionLevel = maxRecursionLevel; }

	public String getCircuitConstructorClassName() { return CircuitConstructorClassName; }
	public void setCircuitConstructorClassName(String circuitConstructorClassName) { CircuitConstructorClassName = circuitConstructorClassName; }
	//public int getTipoDevEval() { return TipoDevEval; }
	public String getDevEvalClassName() { return DevEvalClassName; }
	//public void setTipoDevEval(int tipoDevEval) { TipoDevEval = tipoDevEval; }
	public void setDevEvalClassName(String devEvalClassName) { DevEvalClassName = devEvalClassName; }
	public void setHashCache(boolean hashCache) { HashCache = hashCache; }
	public boolean isHashCache() { return HashCache; }
	public int getCrossoverBlockSize() { return CrossoverBlockSize; }
	public void setCrossoverBlockSize(int crossoverBlockSize) { CrossoverBlockSize = crossoverBlockSize; }
	public String getGeneratorClassName() { return GeneratorClassName; }
	public void setGeneratorClassName(String generatorClassName) { this.GeneratorClassName = generatorClassName; }
	public double getUmbralError() { return UmbralError; }
	public void setUmbralError(double umbralError) { UmbralError = umbralError; }
	
	private double readPropertyDouble(Properties properties, String name)
	{
		double ret;

		String property;
		
		if((property = properties.getProperty(name)) == null)
			ret = -1;
		else
		{
			try
			{
				ret = Double.parseDouble(property);
			}
			catch(NumberFormatException e)
			{
				logger.info(e.toString());
				ret = -1;
			}
		}
		
		return ret;
	}
	
	private int readPropertyInteger(Properties properties, String name)
	{
		int ret;

		String property;
		
		if((property = properties.getProperty(name)) == null)
			ret = -1;
		else
		{
			try
			{
				ret = Integer.parseInt(property);
			}
			catch(NumberFormatException e)
			{
				logger.info(e.toString());
				ret = -1;
			}
		}
		
		return ret;
	}
	
	//Constructor
	public GEProperties() {}
	
    //Constructor que carga el fichero de propiedades
    public GEProperties(String fileName)
    {
    	try
    	{
    	InputStream is = new FileInputStream(new File(fileName));
        
        properties = new Properties();
        
        properties.load(is);
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    		
    		throw new AssertionError();
    	}
       
    	ProblemClassName = properties.getProperty(ProblemClassNameName);
    	IndividualsNumber = Integer.parseInt(properties.getProperty(IndividualsNumberName));
    	MinGenesNumber = Integer.parseInt(properties.getProperty(MinGenesNumberName));
    	MaxGenesNumber = Integer.parseInt(properties.getProperty(MaxGenesNumberName));
    	GenerationsNumber = Integer.parseInt(properties.getProperty(GenerationsNumberName));
    	TerminationConditionType = Integer.parseInt(properties.getProperty(TerminationConditionTypeName));
    	PopulationInitializationClassName = properties.getProperty(PopulationInitializationClassNameName);
    	ParentSelectionClassName = properties.getProperty(ParentSelectionClassNameName);
    	//ExpectedOffspringNumberForFittestIndividual = Integer.parseInt(properties.getProperty(ExpectedOffspringNumberForFittestIndividualName));
    	TournamentSize = Integer.parseInt(properties.getProperty(TournamentSizeName));
    	CrossoverClassName = properties.getProperty(CrossoverClassNameName);
    	CrossoverRate = Double.parseDouble(properties.getProperty(CrossoverRateName));
    	CrossoverPointsNumber = Integer.parseInt(properties.getProperty(CrossoverPointsNumberName));
    	CrossoverBlockSize = Integer.parseInt(properties.getProperty(CrossoverBlockSizeName));
    	//ExchangeProbability = Double.parseDouble(properties.getProperty(ExchangeProbabilityName));
    	CrossoverRateGE2 = readPropertyDouble(properties, CrossoverRateGE2Name);
    	CrossoverRateGE3 = readPropertyDouble(properties, CrossoverRateGE3Name);
    	MutationClassName = properties.getProperty(MutationClassNameName);
    	MutationRate = Double.parseDouble(properties.getProperty(MutationRateName));
    	MutationRateGE2 = readPropertyDouble(properties, MutationRateGE2Name);
    	DuplicationClassName = properties.getProperty(DuplicationClassNameName);
    	DuplicationRate = Double.parseDouble(properties.getProperty(DuplicationRateName));
    	SurvivorSelectionClassName = properties.getProperty(SurvivorSelectionClassNameName);
    	Elitism = Integer.parseInt(properties.getProperty(ElitismName));
    	GenerationalGap = Integer.parseInt(properties.getProperty(GenerationalGapName));
    	MaxWrappingNumber = Integer.parseInt(properties.getProperty(MaxWrappingNumberName));
    	GoalFitness = Double.parseDouble(properties.getProperty(GoalFitnessName));
    	HasGoal = new Boolean(properties.getProperty(HasGoalName)).booleanValue();
    	MaxEvaluationCount = Integer.parseInt(properties.getProperty(MaxEvaluationCountName));
    	//Parsimony = readPropertyInteger(properties, ParsimonyName);
    	//ParsimonyValue = readPropertyDouble(properties, ParsimonyValueName);
    	RandomGeneratorClassName = properties.getProperty(RandomGeneratorClassNameName);
    	MaxRecursionLevel = Integer.parseInt(properties.getProperty(MaxRecursionLevelName));
    	Paralelismo = Boolean.parseBoolean(properties.getProperty(ParalelismoName));
    	LimitMaxGenesNumber = Integer.parseInt(properties.getProperty(LimitMaxGenesNumberName));
    	FunAdaptacionClassName = properties.getProperty(FunAdaptacionClassNameName);
    	GrammarFileName = properties.getProperty(GrammarFileNameName);
    	CircuitConstructorClassName = properties.getProperty(CircuitConstructorClassNameName);
    	DevEvalClassName = properties.getProperty(DevEvalClassNameName);
    	HashCache = Boolean.parseBoolean(properties.getProperty(HashCacheName));
    	GeneratorClassName = properties.getProperty(GeneratorClassNameName);
    	UmbralError = Double.parseDouble(properties.getProperty(UmbralErrorName));
    }
    
    void printProperties()
    {
    	logger.info(nombreClase + ParalelismoName + " = " + Paralelismo);
    	logger.info(nombreClase + GeneratorClassNameName + " = " + GeneratorClassName);
    	logger.info(nombreClase + ProblemClassNameName + " = " + ProblemClassName);
    	logger.info(nombreClase + CircuitConstructorClassNameName + " = " + CircuitConstructorClassName);
    	logger.info(nombreClase + DevEvalClassNameName + " = " + DevEvalClassName);
    	logger.info(nombreClase + IndividualsNumberName + " = " + IndividualsNumber);
    	logger.info(nombreClase + MinGenesNumberName + " = " + MinGenesNumber);
    	logger.info(nombreClase + MaxGenesNumberName + " = " + MaxGenesNumber);
    	logger.info(nombreClase + LimitMaxGenesNumberName + " = " + LimitMaxGenesNumber);
    	logger.info(nombreClase + GenerationsNumberName + " = " + GenerationsNumber);
    	logger.info(nombreClase + TerminationConditionTypeName + " = " + TerminationConditionType);
    	logger.info(nombreClase + PopulationInitializationClassNameName + " = " + PopulationInitializationClassName);
    	logger.info(nombreClase + ParentSelectionClassNameName + " = " + ParentSelectionClassName);
    	//logger.info(nombreClase + ExpectedOffspringNumberForFittestIndividualName + " = " + ExpectedOffspringNumberForFittestIndividual);
    	logger.info(nombreClase + TournamentSizeName + " = " + TournamentSize);
    	logger.info(nombreClase + CrossoverClassNameName + " = " + CrossoverClassName);
    	logger.info(nombreClase + CrossoverRateName + " = " + CrossoverRate);
    	logger.info(nombreClase + CrossoverPointsNumberName + " = " + CrossoverPointsNumber);
    	logger.info(nombreClase + CrossoverBlockSizeName + " = " + CrossoverBlockSize);
    	//logger.info(nombreClase + ExchangeProbabilityName + " = " + ExchangeProbability);
    	logger.info(nombreClase + CrossoverRateGE2Name + " = " + CrossoverRateGE2);
    	logger.info(nombreClase + CrossoverRateGE3Name + " = " + CrossoverRateGE3);
    	logger.info(nombreClase + MutationClassNameName + " = " + MutationClassName);
    	logger.info(nombreClase + MutationRateName + " = " + MutationRate);    	
    	logger.info(nombreClase + DuplicationClassNameName + " = " + DuplicationClassName);
    	logger.info(nombreClase + MutationRateGE2Name + " = " + MutationRateGE2);
    	logger.info(nombreClase + DuplicationRateName + " = " + DuplicationRate);
    	logger.info(nombreClase + SurvivorSelectionClassNameName + " = " + SurvivorSelectionClassName);
    	logger.info(nombreClase + ElitismName + " = " + Elitism);
    	logger.info(nombreClase + GenerationalGapName + " = " + GenerationalGap);
    	logger.info(nombreClase + MaxWrappingNumberName + " = " + MaxWrappingNumber);
    	logger.info(nombreClase + GrammarFileNameName + " = " + GrammarFileName);
    	logger.info(nombreClase + UmbralErrorName + " = " + UmbralError);
    	logger.info(nombreClase + FunAdaptacionClassNameName + " = " + FunAdaptacionClassName);
    	logger.info(nombreClase + GoalFitnessName + " = " + GoalFitness);
    	logger.info(nombreClase + HasGoalName + " = " + HasGoal);
    	logger.info(nombreClase + MaxEvaluationCountName + " = " + MaxEvaluationCount);
    	//logger.info(nombreClase + ParsimonyName + " = " + Parsimony);
    	//logger.info(nombreClase + ParsimonyValueName + " = " + ParsimonyValue);
    	logger.info(nombreClase + RandomGeneratorClassNameName + " = " + RandomGeneratorClassName);
    	logger.info(nombreClase + MaxRecursionLevelName + " = " + MaxRecursionLevel);
    	logger.info(nombreClase + HashCacheName + " = " + HashCache);
    }
}
