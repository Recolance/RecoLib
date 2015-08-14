package net.recolib.util.scheduler;

import java.util.HashMap;

/**
 * This class holds all of the active scheduled tasks.
 * Used for future return of scheduled tasks for future 
 * modification or cancellation.
 * 
 * @author Cody Filatov
 * @version 1.0
 */

public class TaskHolder{
	
	/**
	 * Maps all of the currently active scheduled tasks.
	 */
	private static HashMap<Integer, ScheduledTask> activeTasks = new HashMap<Integer, ScheduledTask>();
	
	
	/**
	 * Check to see if the task is actively running or
	 * waiting to be ran.
	 * 
	 * @param taskId Task ID for the requested operation.
	 * @return True if the task exists.
	 */
	protected static boolean isActiveTask(int taskId){
		return activeTasks.containsKey(taskId);
	}
	
	
	/**
	 * Returs the requested ScheduledTask object.
	 * 
	 * @param taskId Task ID for the requested operation.
	 * @return ScheduledTask object requested from the task ID.
	 * If no ScheduledTask exists with the task ID this method will return 
	 * null.
	 */
	protected static ScheduledTask getTask(int taskId){
		return activeTasks.get(Integer.valueOf(taskId));
	}
	
	
	/**
	 * Store the ScheduledTask within the active task map.
	 * 
	 * @param task ScheduledTask to be stored.
	 */
	protected static void storeTask(ScheduledTask task){
		activeTasks.put(task.getTaskId(), task);
	}
	
	
	/**
	 * Remove the ScheduledTask from the active task map.
	 * 
	 * @param taskId Task ID of the task to be removed.
	 */
	protected static void removeTask(int taskId){
		activeTasks.remove(taskId);
	}
}
