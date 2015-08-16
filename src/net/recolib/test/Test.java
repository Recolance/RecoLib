package net.recolib.test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import net.recolib.util.calculator.Calculator;

public class Test{

	public static void main(String[] args) throws Exception{
		System.out.println(1.1 + 5.1);
		System.out.println(Calculator.addDoubles(1.1, 5.1));
	}
}
