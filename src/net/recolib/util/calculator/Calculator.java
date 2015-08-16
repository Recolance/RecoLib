package net.recolib.util.calculator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Calculator class to provide some more functionality when performing
 * different math operations.
 * 
 * @author Cody Filatov
 */
public class Calculator{

	/**
	 * Solve an equasion from a string. Exmaple: "2+2" would return 4;
	 * 
	 * @param equasion The equasion to solve.
	 * @return Solution to the problem.
	 */
	public static Number solveFromString(String equasion){
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		Number num = 0.0;
		try{
			num = (Number)engine.eval(equasion);
		}catch(ScriptException exception){
			exception.printStackTrace();
		}
		return num;
	}
	
	
	/**
	 * Addition security to floats to provide the correct answer and
	 * preventing results such as "1.1999999999 ...".
	 * 
	 * @param f1 First float to add.
	 * @param f2 Second float to add.
	 * @return Sum of the addition.
	 */
	public static float addFloats(float f1, float f2){
		return (float)addDoubles((double)f1, (double)f2);
	}
	
	
	/**
	 * Addition security to doubles to provide the correct answer and
	 * preventing results such as "1.199999999 ...".
	 * 
	 * @param d1 First double to add.
	 * @param d2 Second double to add.
	 * @return Sum of the addition.
	 */
	public static double addDoubles(double d1, double d2){
		double d1E = getDecimalExponent(d1);
		double d2E = getDecimalExponent(d2);
		double exponent = (double)d2E;
		if(d1E > d2E) exponent = d1E;
		int toMultiply = (int)Math.pow(10D, exponent);
		
		return ((d1 * toMultiply) + (d2 * toMultiply)) / toMultiply;
	}
	
	private static double getDecimalExponent(Number number){
		String toString = String.valueOf(number);
		if(!toString.contains(".")) return 0;
		int exponent = 0;
		boolean counting = false;
		for(char c : toString.toCharArray()){
			if(c == '.') counting = true;
			else if(counting == false) continue;
			else exponent++;
		}
		return exponent;
	}
}
