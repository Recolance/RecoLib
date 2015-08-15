package net.recolib.util.calculator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculator{

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
	
	public static Number add(Number number1, Number number2){
		
		return 1;
	}
	
	public static Number addDoubles(double d1, double d2){
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
