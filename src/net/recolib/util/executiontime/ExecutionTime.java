package net.recolib.util.executiontime;

/**
 * This class tests the execution time of code.
 * 
 * @author Cody Filatov
 */

public abstract class ExecutionTime implements Runnable{

	private long time;
	private long nanoTime;
	
	private long startTime;
	private long endTime;
	
	private long startNanoTime;
	private long endNanoTime;
	
	
	/**
	 * Runs the code provided and tracks execution time.
	 * 
	 * @return Instance of ExecutionTime.
	 */
	public ExecutionTime start(){
		this.startTime = System.currentTimeMillis();
		this.startNanoTime = System.nanoTime();
		
		this.run();
		
		this.endTime = System.currentTimeMillis();
		this.endNanoTime = System.nanoTime();
		this.time = (this.endTime - this.startTime);
		this.nanoTime = (this.endNanoTime - this.startNanoTime);
		return this;
	}
	
	
	/**
	 * Get the time in milliseconds taken to execute.
	 * 
	 * @return Milliseconds taken to execute.
	 */
	public long getTimeMillis(){
		return this.time;
	}
	
	
	/**
	 * Get the time in nanoseconds taken to execute.
	 * 
	 * The time in nanoseconds is approximate and can not be
	 * guarenteed to be completely accurate.
	 * 
	 * @return Nanoseconds taken to execute.
	 */
	public long getTimeNano(){
		return this.nanoTime;
	}
	
	
	/**
	 * Simple print of the execution time in both milliseconds
	 * and nanoseconds.
	 */
	public void print(){
		System.out.println("-----------------------------");
		System.out.println("Time in milliseconds: " + this.time);
		System.out.println("Time in nanoseconds (Approx): " + this.nanoTime);
		System.out.println("-----------------------------");
	}
}
