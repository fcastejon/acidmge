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
 * TestCase para probar TestLinearize
 * 
 * 
 */
package es.uned.simda.acidge.spice;

//import static org.junit.Assert.*;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.logging.Logger;

//import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import es.uned.simda.acidge.problema.InviableException;




public class TestLinearize
{	
	private final static Logger logger = Logger.getLogger(TestLinearize.class.getName());
	private final static String NOMBRE = "V(3)";
	private final static String NOMBRESAL = "V(3)lin";
	static Signal signal;
	static Signal signal3;
	static Signal signal4;
	static Signal signal5;
	static Linearize linearize;
	private final static int puntos = 21;
	//private final static double Vinicial = -250e-3;
	//private final static double Vfinal = 250e-3;
	private final static double TOL = 1e-6;
	private final static double TOLINTERPOL = 1e-2;
	private final static double Tinicial = 0;
	private final static double Tfinal = 0.2;
	private final static int Xpuntos = 59;
	
	private static double vi[];
	private static double expected[];
	private static double Tequi[];

	@BeforeClass
	public static void before()
	{
		es.uned.simda.acidge.util.Util.checkEA();
		es.uned.simda.acidge.util.LoggerStart.simpleSetup();
		
		init();
		linearize = new Linearize(puntos, vi);
	}
	
	static void init()
	{
		SigItem si;
		signal = new Signal(NOMBRE);
		signal3 = new Signal(NOMBRE);
		signal4 = new Signal(NOMBRE);
		signal5 = new Signal(NOMBRE);
		vi = new double[puntos];
		expected = new double[puntos];
		Tequi = new double[puntos];
		
		for(int i = 0; i < values.length;i++)
		{
			si  = new SigItem(values[i][1], values[i][2], 0);
			signal.put(si);
		}
		
		//segunda salida
		for(int i = 0; i < values3.length;i++)
		{
			si  = new SigItem(values3[i][1], values3[i][2], 0);
			signal3.put(si);
		}

		//tercera salida
		for(int i = 0; i < values4.length;i++)
		{
			si  = new SigItem(values4[i][1], values4[i][2], 0);
			signal4.put(si);
		}
		
		//salida errónea
		for(int i = 0; i < values5.length;i++)
		{
			si  = new SigItem(values5[i][1], values5[i][2], 0);
			signal5.put(si);
		}		
		assert(values2.length == puntos);
		
		for(int i = 0; i < values2.length;i++)
		{
			vi[i] = values2[i][1];
			expected[i] = values2[i][2];
		}
		
		double step = (Tfinal - Tinicial)/(puntos - 1);
		
		for(int i = 0; i < puntos; i++)
		{
			Tequi[i] = Tinicial + step * i;
			logger.finer("Tequi[" + i + "]=" + Tequi[i]);
		}
	}
	
	@Test
	public void testLinearize()
	{
		motor(signal);
		
		//Debe ir aquí pues Tequi tiene el tamaño de signal
		assertArrayEquals(Tequi, linearize.getXequi(), TOL);
	}
	
	@Test
	public void testLinearize2()
	{
		motor(signal3);
	}

	@Test
	public void testLinearize3()
	{
		motor(signal4);
	}
	
	@Test
	public void testLinearize4()
	{
		motorFail(signal5);
	}
	
	void motor(Signal sig)
	{
		try 
		{
			Signal so = linearize.linearize(sig);
			
			assertEquals(NOMBRESAL, so.getNombre());
			assertEquals(expected.length, so.getSize());
			
			//Iterator<SigItem> it = so.iterator();
			SigItem si;
			double dif, error = 0;
			for(int i = 0; i < puntos; i++)
			{
				si = so.getSigItemIndex(i);;
				
				assertEquals(vi[i], si.getFrecuencia(), TOL);
				assertEquals(expected[i], si.getMagnitud(), TOLINTERPOL);
				
				dif = Math.abs(expected[i] - si.getMagnitud());
				error += dif;
				logger.info("expected: " + expected[i] + ":actual:" + si.getMagnitud() + ":error:" + dif);
			}
			error /= puntos;
			
			logger.info("Error medio: " + error);
		}
		catch (InviableException e)
		{
			fail();
		}
	}

	void motorFail(Signal sig)
	{
		try 
		{
			Signal so = linearize.linearize(sig);
			fail();			
		}
		catch (InviableException e)
		{
			logger.info(e.getClass().toString() + e.getMessage());
			assertTrue(true);
		}
	}
	
	@Test
	public void testInterpola()
	{
		double res;
		
		res = linearize.interpola(1.0, 2.0, 2.0, 3.0, 1.5);
		assertEquals(2.5, res, TOL);

		res = linearize.interpola(1.0, 2.0, 2.0, 3.0, 1.75);
		assertEquals(2.75, res, TOL);
	}
	
	static double values[][] = {
	{0, 0.000000e+00, -6.28465e-01},
	{1, 2.000000e-05, -6.28428e-01},
	{2, 4.000000e-05, -6.28390e-01},
	{3, 8.000000e-05, -6.28316e-01},
	{4, 1.600000e-04, -6.28167e-01},
	{5, 3.200000e-04, -6.27868e-01},
	{6, 6.400000e-04, -6.27269e-01},
	{7, 1.280000e-03, -6.26065e-01},
	{8, 2.560000e-03, -6.23632e-01},
	{9, 5.120000e-03, -6.18664e-01},
	{10, 9.120000e-03, -6.10624e-01},
	{11, 1.312000e-02, -6.02227e-01},
	{12, 1.712000e-02, -5.93448e-01},
	{13, 2.112000e-02, -5.84258e-01},
	{14, 2.512000e-02, -5.74618e-01},
	{15, 2.912000e-02, -5.64474e-01},
	{16, 3.312000e-02, -5.53760e-01},
	{17, 3.712000e-02, -5.42391e-01},
	{18, 4.112000e-02, -5.30264e-01},
	{19, 4.512000e-02, -5.17269e-01},
	{20, 4.912000e-02, -5.03301e-01},
	{21, 5.312000e-02, -4.88298e-01},
	{22, 5.712000e-02, -4.72284e-01},
	{23, 6.112000e-02, -4.55398e-01},
	{24, 6.512000e-02, -4.37883e-01},
	{25, 6.912000e-02, -4.20021e-01},
	{26, 7.312000e-02, -4.02061e-01},
	{27, 7.712000e-02, -3.84153e-01},
	{28, 8.112000e-02, -3.66216e-01},
	{29, 8.512000e-02, -3.47409e-01},
	{30, 8.912000e-02, -3.14831e-01},
	{31, 9.312000e-02, -1.99013e-01},
	{32, 9.712000e-02, -8.16687e-02},
	{33, 1.011200e-01, 3.149929e-02},
	{34, 1.051200e-01, 1.377969e-01},
	{35, 1.091200e-01, 2.300799e-01},
	{36, 1.131200e-01, 2.981726e-01},
	{37, 1.171200e-01, 3.432736e-01},
	{38, 1.211200e-01, 3.753586e-01},
	{39, 1.251200e-01, 4.004324e-01},
	{40, 1.291200e-01, 4.215077e-01},
	{41, 1.331200e-01, 4.399511e-01},
	{42, 1.371200e-01, 4.566206e-01},
	{43, 1.411200e-01, 4.719147e-01},
	{44, 1.451200e-01, 4.861830e-01},
	{45, 1.491200e-01, 4.995357e-01},
	{46, 1.526220e-01, 5.099690e-01},
	{47, 1.560246e-01, 5.194086e-01},
	{48, 1.600246e-01, 5.302874e-01},
	{49, 1.640246e-01, 5.409648e-01},
	{50, 1.680246e-01, 5.515229e-01},
	{51, 1.720246e-01, 5.619173e-01},
	{52, 1.760246e-01, 5.722040e-01},
	{53, 1.800246e-01, 5.823231e-01},
	{54, 1.840246e-01, 5.923254e-01},
	{55, 1.880246e-01, 6.021448e-01},
	{56, 1.920246e-01, 6.118254e-01},
	{57, 1.960246e-01, 6.212917e-01},
	{58, 2.000000e-01, 6.305273e-01}};		

	static double values2[][] = {
		{0, -2.50000e-01, -6.28465e-01},
		{1, -2.25000e-01, -6.08816e-01},
		{2, -2.00000e-01, -5.86917e-01},
		{3, -1.75000e-01, -5.62165e-01},
		{4, -1.50000e-01, -5.33864e-01},
		{5, -1.25000e-01, -5.00081e-01},
		{6, -1.00000e-01, -4.60198e-01},
		{7, -7.50000e-02, -4.16069e-01},
		{8, -5.00000e-02, -3.71260e-01},
		{9, -2.50000e-02, -2.91201e-01},
		{10, -3.46945e-17, 4.869920e-04},
		{11, 2.500000e-02, 2.476226e-01},
		{12, 5.000000e-02, 3.673397e-01},
		{13, 7.500000e-02, 4.257585e-01},
		{14, 1.000000e-01, 4.677566e-01},
		{15, 1.250000e-01, 5.023174e-01},
		{16, 1.500000e-01, 5.302155e-01},
		{17, 1.750000e-01, 5.566740e-01},
		{18, 2.000000e-01, 5.822691e-01},
		{19, 2.250000e-01, 6.069618e-01},
		{20, 2.500000e-01, 6.305375e-01}};

	/* de sal_cuberoot_esperado.txt
	static double values2[][] = {
		{ 1, -0.250, -0.62996052495},
		{ 2, -0.225, -0.60822019956},
		{ 3, -0.200, -0.58480354764},
		{ 4, -0.175, -0.55934447104},
		{ 5, -0.150, -0.53132928459},
		{ 6, -0.125, -0.50000000000},
		{ 7, -0.100, -0.46415888336},
		{ 8, -0.075, -0.42171633265},
		{ 9, -0.050, -0.36840314986},
		{ 10, -0.025, -0.29240177382},
		{ 11, 0.000, 0.00000000000},
		{ 12, 0.025, 0.29240177382},
		{ 13, 0.050, 0.36840314986},
		{ 14, 0.075, 0.42171633265},
		{ 15, 0.100, 0.46415888336},
		{ 16, 0.125, 0.50000000000},
		{ 17, 0.150, 0.53132928459},
		{ 18, 0.175, 0.55934447104},
		{ 19, 0.200, 0.58480354764},
		{ 20, 0.225, 0.60822019956},
		{ 21, 0.250, 0.62996052495}};*/
	
	static double values3[][] = {
		{ 0, 0.000000e+00, -6.28465e-01 },
		{ 1, 2.000000e-05, -6.28428e-01 },
		{ 2, 4.000000e-05, -6.28391e-01 },
		{ 3, 8.000000e-05, -6.28316e-01 },
		{ 4, 1.600000e-04, -6.28167e-01 },
		{ 5, 3.200000e-04, -6.27868e-01 },
		{ 6, 6.400000e-04, -6.27269e-01 },
		{ 7, 1.280000e-03, -6.26065e-01 },
		{ 8, 2.560000e-03, -6.23632e-01 },
		{ 9, 4.560000e-03, -6.19763e-01 },
		{ 10, 6.560000e-03, -6.15810e-01 },
		{ 11, 8.560000e-03, -6.11770e-01 },
		{ 12, 1.056000e-02, -6.07643e-01 },
		{ 13, 1.256000e-02, -6.03424e-01 },
		{ 14, 1.456000e-02, -5.99111e-01 },
		{ 15, 1.656000e-02, -5.94700e-01 },
		{ 16, 1.856000e-02, -5.90188e-01 },
		{ 17, 2.056000e-02, -5.85570e-01 },
		{ 18, 2.256000e-02, -5.80841e-01 },
		{ 19, 2.456000e-02, -5.75995e-01 },
		{ 20, 2.656000e-02, -5.71025e-01 },
		{ 21, 2.856000e-02, -5.65925e-01 },
		{ 22, 3.056000e-02, -5.60684e-01 },
		{ 23, 3.256000e-02, -5.55294e-01 },
		{ 24, 3.456000e-02, -5.49744e-01 },
		{ 25, 3.656000e-02, -5.44022e-01 },
		{ 26, 3.856000e-02, -5.38114e-01 },
		{ 27, 4.056000e-02, -5.32007e-01 },
		{ 28, 4.256000e-02, -5.25686e-01 },
		{ 29, 4.456000e-02, -5.19138e-01 },
		{ 30, 4.656000e-02, -5.12350e-01 },
		{ 31, 4.856000e-02, -5.05311e-01 },
		{ 32, 5.056000e-02, -4.98013e-01 },
		{ 33, 5.256000e-02, -4.90454e-01 },
		{ 34, 5.456000e-02, -4.82639e-01 },
		{ 35, 5.656000e-02, -4.74578e-01 },
		{ 36, 5.856000e-02, -4.66292e-01 },
		{ 37, 6.056000e-02, -4.57805e-01 },
		{ 38, 6.256000e-02, -4.49150e-01 },
		{ 39, 6.456000e-02, -4.40362e-01 },
		{ 40, 6.656000e-02, -4.31478e-01 },
		{ 41, 6.856000e-02, -4.22532e-01 },
		{ 42, 7.056000e-02, -4.13556e-01 },
		{ 43, 7.256000e-02, -4.04575e-01 },
		{ 44, 7.456000e-02, -3.95606e-01 },
		{ 45, 7.656000e-02, -3.86657e-01 },
		{ 46, 7.856000e-02, -3.77715e-01 },
		{ 47, 8.056000e-02, -3.68744e-01 },
		{ 48, 8.256000e-02, -3.59648e-01 },
		{ 49, 8.456000e-02, -3.50192e-01 },
		{ 50, 8.656000e-02, -3.39666e-01 },
		{ 51, 8.856000e-02, -3.23973e-01 },
		{ 52, 9.056000e-02, -2.74838e-01 },
		{ 53, 9.256000e-02, -2.15506e-01 },
		{ 54, 9.456000e-02, -1.56493e-01 },
		{ 55, 9.656000e-02, -9.79201e-02 },
		{ 56, 9.856000e-02, -4.03856e-02 },
		{ 57, 1.005600e-01, 1.606835e-02 },
		{ 58, 1.025600e-01, 7.078787e-02 },
		{ 59, 1.045600e-01, 1.235185e-01 },
		{ 60, 1.065600e-01, 1.731317e-01 },
		{ 61, 1.085600e-01, 2.184845e-01 },
		{ 62, 1.105600e-01, 2.577209e-01 },
		{ 63, 1.125600e-01, 2.902361e-01 },
		{ 64, 1.145600e-01, 3.164819e-01 },
		{ 65, 1.165600e-01, 3.379708e-01 },
		{ 66, 1.185600e-01, 3.559124e-01 },
		{ 67, 1.205600e-01, 3.713816e-01 },
		{ 68, 1.225600e-01, 3.849757e-01 },
		{ 69, 1.245600e-01, 3.972255e-01 },
		{ 70, 1.265600e-01, 4.083867e-01 },
		{ 71, 1.285600e-01, 4.187370e-01 },
		{ 72, 1.305600e-01, 4.283863e-01 },
		{ 73, 1.325600e-01, 4.375025e-01 },
		{ 74, 1.345600e-01, 4.461282e-01 },
		{ 75, 1.365600e-01, 4.543753e-01 },
		{ 76, 1.385600e-01, 4.622561e-01 },
		{ 77, 1.405600e-01, 4.698517e-01 },
		{ 78, 1.425600e-01, 4.771596e-01 },
		{ 79, 1.445600e-01, 4.842410e-01 },
		{ 80, 1.465600e-01, 4.910829e-01 },
		{ 81, 1.485600e-01, 4.977309e-01 },
		{ 82, 1.505600e-01, 5.040012e-01 },
		{ 83, 1.520417e-01, 5.083180e-01 },
		{ 84, 1.534045e-01, 5.121576e-01 },
		{ 85, 1.550421e-01, 5.167158e-01 },
		{ 86, 1.570421e-01, 5.221949e-01 },
		{ 87, 1.590421e-01, 5.276340e-01 },
		{ 88, 1.610421e-01, 5.330109e-01 },
		{ 89, 1.630421e-01, 5.383635e-01 },
		{ 90, 1.650421e-01, 5.436633e-01 },
		{ 91, 1.670421e-01, 5.489430e-01 },
		{ 92, 1.690421e-01, 5.541724e-01 },
		{ 93, 1.710421e-01, 5.593839e-01 },
		{ 94, 1.730421e-01, 5.645459e-01 },
		{ 95, 1.750421e-01, 5.696899e-01 },
		{ 96, 1.770421e-01, 5.747832e-01 },
		{ 97, 1.790421e-01, 5.798590e-01 },
		{ 98, 1.810421e-01, 5.848807e-01 },
		{ 99, 1.830421e-01, 5.898833e-01 },
		{ 100, 1.850421e-01, 5.948308e-01 },
		{ 101, 1.870421e-01, 5.997564e-01 },
		{ 102, 1.890421e-01, 6.046232e-01 },
		{ 103, 1.910421e-01, 6.094649e-01 },
		{ 104, 1.930421e-01, 6.142432e-01 },
		{ 105, 1.950421e-01, 6.189926e-01 },
		{ 106, 1.970421e-01, 6.236738e-01 },
		{ 107, 1.990421e-01, 6.283220e-01 },
		{ 108, 2.000000e-01, 6.305138e-01 }
		};
	
	//Salida con pwl de 11 puntos
	static double values4[][] = {
		{0, 0.000000e+00, -6.28465e-01},
		{1, 2.000000e-05, -6.28428e-01},
		{2, 4.000000e-05, -6.28391e-01},
		{3, 8.000000e-05, -6.28316e-01},
		{4, 1.600000e-04, -6.28167e-01},
		{5, 3.200000e-04, -6.27868e-01},
		{6, 6.400000e-04, -6.27269e-01},
		{7, 1.280000e-03, -6.26065e-01},
		{8, 2.560000e-03, -6.23632e-01},
		{9, 5.120000e-03, -6.18664e-01},
		{10, 9.120000e-03, -6.10624e-01},
		{11, 1.312000e-02, -6.02227e-01},
		{12, 1.712000e-02, -5.93448e-01},
		{13, 2.000000e-02, -5.86874e-01},
		{14, 2.040000e-02, -5.85943e-01},
		{15, 2.120000e-02, -5.84069e-01},
		{16, 2.280000e-02, -5.80265e-01},
		{17, 2.600000e-02, -5.72430e-01},
		{18, 3.000000e-02, -5.62168e-01},
		{19, 3.400000e-02, -5.51319e-01},
		{20, 3.800000e-02, -5.39792e-01},
		{21, 4.000000e-02, -5.33738e-01},
		{22, 4.040000e-02, -5.32502e-01},
		{23, 4.120000e-02, -5.30007e-01},
		{24, 4.280000e-02, -5.24912e-01},
		{25, 4.600000e-02, -5.14277e-01},
		{26, 5.000000e-02, -5.00088e-01},
		{27, 5.400000e-02, -4.84858e-01},
		{28, 5.800000e-02, -4.68637e-01},
		{29, 6.000000e-02, -4.60200e-01},
		{30, 6.040000e-02, -4.58490e-01},
		{31, 6.120000e-02, -4.55052e-01},
		{32, 6.280000e-02, -4.48102e-01},
		{33, 6.600000e-02, -4.33973e-01},
		{34, 7.000000e-02, -4.16071e-01},
		{35, 7.400000e-02, -3.98115e-01},
		{36, 7.800000e-02, -3.80219e-01},
		{37, 8.000000e-02, -3.71263e-01},
		{38, 8.040000e-02, -3.69464e-01},
		{39, 8.120000e-02, -3.65854e-01},
		{40, 8.280000e-02, -3.58539e-01},
		{41, 8.600000e-02, -3.42813e-01},
		{42, 9.000000e-02, -2.91283e-01},
		{43, 9.400000e-02, -1.72982e-01},
		{44, 9.800000e-02, -5.63492e-02},
		{45, 1.000000e-01, 3.648174e-04},
		{46, 1.004000e-01, 1.157525e-02},
		{47, 1.012000e-01, 3.375981e-02},
		{48, 1.028000e-01, 7.726625e-02},
		{49, 1.060000e-01, 1.596231e-01},
		{50, 1.100000e-01, 2.474201e-01},
		{51, 1.140000e-01, 3.096878e-01},
		{52, 1.180000e-01, 3.511814e-01},
		{53, 1.200000e-01, 3.672556e-01},
		{54, 1.204000e-01, 3.702109e-01},
		{55, 1.212000e-01, 3.759018e-01},
		{56, 1.228000e-01, 3.865166e-01},
		{57, 1.260000e-01, 4.053574e-01},
		{58, 1.300000e-01, 4.257479e-01},
		{59, 1.340000e-01, 4.437580e-01},
		{60, 1.380000e-01, 4.600838e-01},
		{61, 1.400000e-01, 4.677518e-01},
		{62, 1.404000e-01, 4.692508e-01},
		{63, 1.412000e-01, 4.722167e-01},
		{64, 1.428000e-01, 4.780251e-01},
		{65, 1.460000e-01, 4.891908e-01},
		{66, 1.500000e-01, 5.023087e-01},
		{67, 1.530901e-01, 5.112837e-01},
		{68, 1.560378e-01, 5.194481e-01},
		{69, 1.600000e-01, 5.302172e-01},
		{70, 1.604000e-01, 5.312908e-01},
		{71, 1.612000e-01, 5.334384e-01},
		{72, 1.628000e-01, 5.377145e-01},
		{73, 1.660000e-01, 5.461962e-01},
		{74, 1.700000e-01, 5.566727e-01},
		{75, 1.740000e-01, 5.670143e-01},
		{76, 1.780000e-01, 5.772188e-01},
		{77, 1.800000e-01, 5.822683e-01},
		{78, 1.804000e-01, 5.832739e-01},
		{79, 1.812000e-01, 5.852804e-01},
		{80, 1.828000e-01, 5.892771e-01},
		{81, 1.860000e-01, 5.971951e-01},
		{82, 1.900000e-01, 6.069479e-01},
		{83, 1.940000e-01, 6.165242e-01},
		{84, 1.980000e-01, 6.259073e-01},
		{85, 2.000000e-01, 6.305185e-01}};
	
	//Ejecución larga que causaba error
	static double values5[][] = {
		{0, 0.000000e+00, -2.69106e-07},
		{1, 2.500000e-06, -2.49690e-07},
		{2, 2.504883e-06, -2.43297e-07},
		{3, 2.514648e-06, -2.44340e-07},
		{4, 2.534180e-06, -2.57290e-07},
		{5, 2.573242e-06, -2.45430e-07},
		{6, 2.583008e-06, -2.44036e-07},
		{7, 2.583313e-06, -2.44567e-07},
		{8, 2.583923e-06, -2.45768e-07},
		{9, 2.584076e-06, -2.46289e-07},
		{10, 2.584381e-06, -2.46843e-07},
		{11, 2.584991e-06, -2.47752e-07},
		{12, 2.585144e-06, -2.47543e-07},
		{13, 2.585449e-06, -2.47103e-07},
		{14, 2.586060e-06, -2.46699e-07},
		{15, 2.587280e-06, -2.50447e-07},
		{16, 2.587318e-06, -2.50522e-07},
		{17, 2.587395e-06, -2.50689e-07},
		{18, 2.587547e-06, -2.51236e-07},
		{19, 2.587585e-06, -2.50959e-07},
		{20, 2.587662e-06, -2.50771e-07},
		{21, 2.587814e-06, -2.51109e-07},
		{22, 2.588120e-06, -2.51244e-07},
		{23, 2.588730e-06, -2.49892e-07},
		{24, 2.588882e-06, -2.49293e-07},
		{25, 2.589188e-06, -2.48474e-07},
		{26, 2.589798e-06, -2.46959e-07},
		{27, 2.591019e-06, -2.46882e-07},
		{28, 2.593460e-06, -2.51685e-07},
		{29, 2.598343e-06, -2.51482e-07},
		{30, 2.608109e-06, -2.47191e-07},
		{31, 2.627640e-06, -2.44500e-07},
		{32, 2.666702e-06, -2.48648e-07},
		{33, 2.744827e-06, -2.54512e-07},
		{34, 2.901077e-06, -2.47758e-07},
		{35, 3.213577e-06, -2.49587e-07},
		{36, 3.838577e-06, -2.49672e-07},
		{37, 5.088577e-06, -2.49445e-07},
		{38, 7.588577e-06, -2.49163e-07},
		{39, 1.258858e-05, -2.49253e-07},
		{40, 2.258858e-05, -2.48928e-07},
		{41, 4.258858e-05, -2.48532e-07},
		{42, 8.258858e-05, -2.47471e-07},
		{43, 8.383858e-05, -2.47535e-07},
		{44, 8.633858e-05, -2.47377e-07},
		{45, 9.133858e-05, -2.47474e-07},
		{46, 1.013386e-04, -2.46918e-07},
		{47, 1.213386e-04, -2.46768e-07},
		{48, 1.613386e-04, -2.45486e-07},
		{49, 1.713386e-04, -2.45426e-07},
		{50, 1.913386e-04, -2.44958e-07},
		{51, 2.313386e-04, -2.43989e-07},
		{52, 3.113386e-04, -2.42139e-07},
		{53, 3.313386e-04, -2.41659e-07},
		{54, 3.713386e-04, -2.40726e-07},
		{55, 4.513386e-04, -2.38872e-07},
		{56, 4.713386e-04, -2.38410e-07},
		{57, 5.113386e-04, -2.37492e-07},
		{58, 5.913386e-04, -2.35660e-07},
		{59, 7.513386e-04, -2.32032e-07},
		{60, 7.563386e-04, -2.31915e-07},
		{61, 7.663386e-04, -2.31706e-07},
		{62, 7.863386e-04, -2.31222e-07},
		{63, 8.263386e-04, -2.30399e-07},
		{64, 8.264948e-04, -2.28967e-07},
		{65, 8.268073e-04, -2.30138e-07},
		{66, 8.274323e-04, -2.32267e-07},
		{67, 8.286823e-04, -2.27984e-07},
		{68, 8.289948e-04, -2.29512e-07},
		{69, 8.296198e-04, -2.30851e-07},
		{70, 8.308698e-04, -2.30001e-07},
		{71, 8.333698e-04, -2.30446e-07},
		{72, 8.383698e-04, -2.29915e-07},
		{73, 8.396198e-04, -2.29912e-07},
		{74, 8.421198e-04, -2.30138e-07},
		{75, 8.421980e-04, -2.29736e-07},
		{76, 8.423542e-04, -2.29974e-07},
		{77, 8.426667e-04, -2.30627e-07},
		{78, 8.427448e-04, -2.30807e-07},
		{79, 8.429011e-04, -2.29814e-07},
		{80, 8.432136e-04, -2.28537e-07},
		{81, 8.432917e-04, -2.30165e-07},
		{82, 8.434480e-04, -2.29678e-07},
		{83, 8.437605e-04, -2.30852e-07},
		{84, 8.443855e-04, -2.28495e-07},
		{85, 8.456355e-04, -2.31410e-07},
		{86, 8.481355e-04, -2.28459e-07},
		{87, 8.487605e-04, -2.30265e-07},
		{88, 8.489167e-04, -2.29984e-07},
		{89, 8.492292e-04, -2.28859e-07},
		{90, 8.498542e-04, -2.31232e-07},
		{91, 8.511042e-04, -2.28236e-07},
		{92, 8.536042e-04, -2.31162e-07},
		{93, 8.586042e-04, -2.28150e-07},
		{94, 8.686042e-04, -2.30974e-07},
		{95, 8.886042e-04, -2.27403e-07},
		{96, 9.286042e-04, -2.29630e-07},
		{97, 1.008604e-03, -2.24755e-07},
		{98, 1.028604e-03, -2.25883e-07},
		{99, 1.068604e-03, -2.24978e-07},
		{100, 1.078604e-03, -2.24784e-07},
		{101, 1.098604e-03, -2.24318e-07},
		{102, 1.103604e-03, -2.24239e-07},
		{103, 1.104854e-03, -2.24132e-07},
		{104, 1.105167e-03, -2.25207e-07},
		{105, 1.105792e-03, -2.22250e-07},
		{106, 1.107042e-03, -2.25898e-07},
		{107, 1.109542e-03, -2.22461e-07},
		{108, 1.114542e-03, -2.25521e-07},
		{109, 1.124542e-03, -2.22312e-07},
		{110, 1.127042e-03, -2.23567e-07},
		{111, 1.132042e-03, -2.23725e-07},
		{112, 1.142042e-03, -2.23339e-07},
		{113, 1.162042e-03, -2.23012e-07},
		{114, 1.202042e-03, -2.22049e-07},
		{115, 1.282042e-03, -2.20402e-07},
		{116, 1.442042e-03, -2.16911e-07},
		{117, 1.482042e-03, -2.16111e-07},
		{118, 1.562042e-03, -2.14413e-07},
		{119, 1.582042e-03, -2.14003e-07},
		{120, 1.622042e-03, -2.13170e-07},
		{121, 1.702042e-03, -2.11501e-07},
		{122, 1.862042e-03, -2.08217e-07},
		{123, 2.182042e-03, -2.01764e-07},
		{124, 2.822042e-03, -1.89405e-07},
		{125, 4.102042e-03, -1.66626e-07},
		{126, 6.102042e-03, -1.35722e-07},
		{127, 8.102042e-03, -1.09690e-07},
		{128, 1.010204e-02, -8.77885e-08},
		{129, 1.210204e-02, -6.93215e-08},
		{130, 1.410204e-02, -5.37741e-08},
		{131, 1.610204e-02, -4.06394e-08},
		{132, 1.810204e-02, -2.95686e-08},
		{133, 2.010204e-02, -2.02091e-08},
		{134, 2.210204e-02, -1.23246e-08},
		{135, 2.410204e-02, -5.64553e-09},
		{136, 2.610204e-02, -1.90687e-11},
		{137, 2.810204e-02, 4.752253e-09},
		{138, 3.010204e-02, 8.769611e-09},
		{139, 3.210204e-02, 1.218453e-08},
		{140, 3.410204e-02, 1.505540e-08},
		{141, 3.610204e-02, 1.750115e-08},
		{142, 3.810204e-02, 1.955291e-08},
		{143, 4.010204e-02, 2.130630e-08},
		{144, 4.210204e-02, 2.277117e-08},
		{145, 4.410204e-02, 2.402854e-08},
		{146, 4.610204e-02, 2.507267e-08},
		{147, 4.810204e-02, 2.597394e-08},
		{148, 5.010204e-02, 2.671481e-08},
		{149, 5.210204e-02, 2.735975e-08},
		{150, 5.410204e-02, 2.788233e-08},
		{151, 5.435204e-02, 2.795333e-08},
		{152, 5.485204e-02, 2.806133e-08},
		{153, 5.585204e-02, 2.828875e-08},
		{154, 5.785204e-02, 2.866596e-08},
		{155, 5.985204e-02, 2.899502e-08},
		{156, 6.185204e-02, 2.925677e-08},
		{157, 6.385204e-02, 2.948643e-08},
		{158, 6.585204e-02, 2.966388e-08},
		{159, 6.785204e-02, 2.982204e-08},
		{160, 6.985204e-02, 2.993773e-08},
		{161, 7.185204e-02, 3.004432e-08},
		{162, 7.385204e-02, 3.011530e-08},
		{163, 7.585204e-02, 3.018374e-08},
		{164, 7.785204e-02, 3.022263e-08},
		{165, 7.985204e-02, 3.026336e-08},
		{166, 8.185204e-02, 3.027931e-08},
		{167, 8.385204e-02, 3.029917e-08},
		{168, 8.585204e-02, 3.029861e-08},
		{169, 8.785204e-02, 3.030370e-08},
		{170, 8.985204e-02, 3.029091e-08},
		{171, 9.185204e-02, 3.028568e-08},
		{172, 9.385204e-02, 3.026392e-08},
		{173, 9.585204e-02, 3.025073e-08},
		{174, 9.785204e-02, 3.022349e-08},
		{175, 9.985204e-02, 3.020461e-08},
		{176, 1.018520e-01, 3.017283e-08},
		{177, 1.038520e-01, 3.015039e-08},
		{178, 1.058520e-01, 3.011543e-08},
		{179, 1.078520e-01, 3.009046e-08},
		{180, 1.098520e-01, 3.005378e-08},
		{181, 1.118520e-01, 3.002670e-08},
		{182, 1.138520e-01, 2.998946e-08},
		{183, 1.158520e-01, 2.996080e-08},
		{184, 1.178520e-01, 2.992314e-08},
		{185, 1.198520e-01, 2.989399e-08},
		{186, 1.218520e-01, 2.985557e-08},
		{187, 1.238520e-01, 2.982702e-08},
		{188, 1.258520e-01, 2.978816e-08},
		{189, 1.278520e-01, 2.975978e-08},
		{190, 1.298520e-01, 2.972082e-08},
		{191, 1.318520e-01, 2.969354e-08},
		{192, 1.338520e-01, 2.965444e-08},
		{193, 1.358520e-01, 2.962761e-08},
		{194, 1.378520e-01, 2.958894e-08},
		{195, 1.398520e-01, 2.956278e-08},
		{196, 1.418520e-01, 2.952448e-08},
		{197, 1.438520e-01, 2.949922e-08},
		{198, 1.458520e-01, 2.946117e-08},
		{199, 1.478520e-01, 2.943703e-08},
		{200, 1.498520e-01, 2.939934e-08},
		{201, 1.518520e-01, 2.937581e-08},
		{202, 1.538520e-01, 2.933893e-08},
		{203, 1.558520e-01, 2.931589e-08},
		{204, 1.578520e-01, 2.927964e-08},
		{205, 1.598520e-01, 2.925748e-08},
		{206, 1.618520e-01, 2.922175e-08},
		{207, 1.638520e-01, 2.920037e-08},
		{208, 1.658520e-01, 2.916537e-08},
		{209, 1.678520e-01, 2.914452e-08},
		{210, 1.698520e-01, 2.911024e-08},
		{211, 1.718520e-01, 2.909001e-08},
		{212, 1.721020e-01, 2.908564e-08},
		{213, 1.726020e-01, 2.907447e-08},
		{214, 1.736020e-01, 2.906498e-08},
		{215, 1.756020e-01, 2.903509e-08},
		{216, 1.776020e-01, 2.901229e-08},
		{217, 1.796020e-01, 2.898324e-08},
		{218, 1.816020e-01, 2.896078e-08},
		{219, 1.836020e-01, 2.893282e-08},
		{220, 1.856020e-01, 2.891066e-08},
		{221, 1.876020e-01, 2.888342e-08},
		{222, 1.896020e-01, 2.886167e-08},
		{223, 1.916020e-01, 2.883524e-08},
		{224, 1.936020e-01, 2.881393e-08},
		{225, 1.956020e-01, 2.878811e-08},
		{226, 1.976020e-01, 2.876740e-08},
		{227, 1.996020e-01, 2.874185e-08},
		{228, 1.996028e-01, 2.876606e-08},
		{229, 1.996044e-01, 2.891078e-08},
		{230, 1.996075e-01, 2.845838e-08},
		{231, 1.996083e-01, 2.869857e-08},
		{232, 1.996098e-01, 2.877051e-08},
		{233, 1.996099e-01, 3.058171e-08},
		{234, 1.996100e-01, 2.831358e-08},
		{235, 1.996102e-01, 2.919125e-08},
		{236, 1.996102e-01, 3.148221e-08},
		{237, 1.996102e-01, 3.151186e-08},
		{238, 1.996102e-01, 3.155643e-08},
		{239, 1.996102e-01, 3.156779e-08},
		{240, 1.996102e-01, 3.155084e-08},
		{241, 1.996102e-01, 3.168633e-08},
		{242, 1.996102e-01, 3.194258e-08},
		{243, 1.996102e-01, 3.190654e-08},
		{244, 1.996102e-01, 3.186424e-08},
		{245, 1.996102e-01, 3.185260e-08},
		{246, 1.996102e-01, 3.179841e-08},
		{247, 1.996102e-01, 3.154364e-08},
		{248, 1.996102e-01, 3.106829e-08},
		{249, 1.996102e-01, 3.087115e-08},
		{250, 1.996102e-01, 3.064064e-08},
		{251, 1.996102e-01, 2.754263e-08},
		{252, 1.996102e-01, 2.487279e-08},
		{253, 1.996102e-01, 2.536060e-08},
		{254, 1.996102e-01, 2.538545e-08},
		{255, 1.996102e-01, 2.562342e-08},
		{256, 1.996102e-01, 2.586891e-08},
		{257, 1.996102e-01, 2.349556e-08},
		{258, 1.996102e-01, 2.825880e-08},
		{259, 1.996102e-01, 2.880935e-08},
		{260, 1.996102e-01, 3.023462e-08},
		{261, 1.996102e-01, 3.045141e-08},
		{262, 1.996102e-01, 3.100389e-08},
		{263, 1.996102e-01, 3.207242e-08},
		{264, 1.996102e-01, 3.098552e-08},
		{265, 1.996102e-01, 2.519094e-08},
		{266, 1.996102e-01, 2.824950e-08},
		{267, 1.996102e-01, 3.303007e-08},
		{268, 1.996102e-01, 2.884480e-08},
		{269, 1.996102e-01, 2.711174e-08},
		{270, 1.996102e-01, 2.605122e-08},
		{271, 1.996102e-01, 2.288468e-08},
		{272, 1.996103e-01, 3.746865e-08},
		{273, 1.996104e-01, 2.050043e-08},
		{274, 1.996106e-01, 3.778521e-08},
		{275, 1.996110e-01, 2.008407e-08},
		{276, 1.996117e-01, 3.675364e-08},
		{277, 1.996133e-01, 2.096941e-08},
		{278, 1.996137e-01, 2.877180e-08},
		{279, 1.996145e-01, 2.881864e-08},
		{280, 1.996160e-01, 2.831131e-08},
		{281, 1.996191e-01, 2.942814e-08},
		{282, 1.996253e-01, 2.795404e-08},
		{283, 1.996378e-01, 2.956015e-08},
		{284, 1.996626e-01, 2.793311e-08},
		{285, 1.997124e-01, 2.955008e-08},
		{286, 1.998119e-01, 2.793339e-08},
		{287, 1.998148e-01, 2.877305e-08},
		{288, 1.998207e-01, 2.864109e-08},
		{289, 1.998325e-01, 2.882998e-08},
		{290, 1.998354e-01, 2.873730e-08},
		{291, 1.998413e-01, 2.874887e-08},
		{292, 1.998530e-01, 2.873723e-08},
		{293, 1.998765e-01, 2.875997e-08},
		{294, 1.999236e-01, 2.871288e-08},
		{295, 1.999331e-01, 2.873310e-08},
		{296, 1.999522e-01, 2.874648e-08},
		{297, 1.999904e-01, 2.874038e-08},
		{298, 2.000000e-01, 2.868481e-08}};	
	
}