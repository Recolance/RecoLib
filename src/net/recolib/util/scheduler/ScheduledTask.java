package net.recolib.util.scheduler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles scheduled tasks for later or periodical execution.
 * Tasks are given ID numbers and stored until the task is
 * successfully executed or cancelled.
 * 
 * Tasks are retrievable within the TaskHolder class by the
 * task ID number.
 * 
 * @author Cody Filatov
 * @version 1.0
 */

public abstract class ScheduledTask implements Runnable{

	private static int taskIdCounter;
	
	private Timer timer;
	private int taskId;
	private boolean periodical;
	
	public ScheduledTask(){
		taskIdCounter++;
		
		this.timer = new Timer();
		this.taskId = taskIdCounter;
		
		mapTask();
	}
	
	private void runTask(){
		this.run();
		
		if(!this.periodical) unmapTask();
	}
	
	
	/**
	 * Runs a task at a later time once.
	 * 
	 * @param delay Delay in milliseconds to execute task.
	 * @return Instance of ScheduledTask.
	 */
	public ScheduledTask runTaskLater(int delay){
		this.periodical = false;
		
		timer.schedule(getTimerTask(), delay);
		return this;
	}
	
	
	/**
	 * Runs a task at a later time periodically until cancelled
	 * or the application is terminated. To cancel the task call
	 * the cancel method on the ScheduledTask instance.
	 * 
	 * @param delay Delay until the task is first ran.
	 * @param period Period in between each execution.
	 * @return Instance of ScheduledTask.
	 */
	public ScheduledTask runTaskTimer(int delay, int period){
		this.periodical = true;
		
		timer.scheduleAtFixedRate(getTimerTask(), delay, period);
		return this;
	}
	
	private void mapTask(){
		TaskHolder.storeTask(this);
	}
	
	private void unmapTask(){
		TaskHolder.removeTask(getTaskId());
	}
	
	private TimerTask getTimerTask(){
		return new TimerTask(){
			@Override
			public void run(){
				runTask();
			}
		};
	}
	
	
	/** 
	 * @return Returns true if the task is periodically ran.
	 */
	public boolean isPeriodical(){
		return this.periodical;
	}
	
	
	/**
	 * Returned task ID can be used to reference to the task
	 * throughout the task's lifetime from the TaskHolder class.
	 * 
	 * @return ID of the ScheduledTask.
	 */
	public int getTaskId(){
		return this.taskId;
	}
	
	
	/**
	 * Cancels the task. If the task has never ran it will never
	 * run. If the task is periodical it will never run again.
	 * 
	 * Once a task is cancelled it can no longer be referenced by
	 * the task ID and is removed from the TaskHolder.
	 */
	public void cancel(){
		unmapTask();
		this.timer.cancel();
	}
	
	
	/**
	 * Get the ScheduledTask referenced by the provided task ID.
	 * 
	 * If no ScheduledTask exists null is returned.
	 * 
	 * @param taskId Task ID of the requesting ScheduledTask.
	 * @return The ScheduledTask requested by the provided task ID.
	 */
	public static ScheduledTask getById(int taskId){
		return TaskHolder.getTask(taskId);
	}
}
