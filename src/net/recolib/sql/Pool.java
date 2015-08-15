package net.recolib.sql;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import net.recolib.util.scheduler.ScheduledTask;

public class Pool{

	private static boolean idleDestroySchedulerActive = false;
	
	private static ConcurrentHashMap<String, List<DatabaseConnection>> openConnections = new ConcurrentHashMap<>();
	
	protected static void castConnection(DatabaseConnection databaseConnection) throws PoolContainsConnectionException{
		List<DatabaseConnection> connectionsList = null;
		
		if(openConnections.containsKey(databaseConnection.getConnectionInformation().serialize())){
			connectionsList = openConnections.get(databaseConnection.getConnectionInformation().serialize());
		}else connectionsList = new Vector<DatabaseConnection>();
		
		if(connectionsList.contains(databaseConnection)) throw new PoolContainsConnectionException(databaseConnection);
	
		connectionsList.add(databaseConnection);
		
		runIdleConnectionDestroyScheduler();
	}
	
	protected synchronized static void reelConnection(DatabaseConnection databaseConnection) throws FailedReelConnectionException{
		List<DatabaseConnection> connectionsList = null;
		
		if(openConnections.containsKey(databaseConnection.getConnectionInformation().serialize())){
			connectionsList = openConnections.get(databaseConnection.getConnectionInformation().serialize());
		}else throw new FailedReelConnectionException(databaseConnection);

		connectionsList.remove(databaseConnection);
		
		if(connectionsList.isEmpty()) openConnections.remove(databaseConnection.getConnectionInformation().serialize());
	}
	
	protected synchronized static DatabaseConnection fishConnection(String databaseInformation){
		if(openConnections.containsKey(databaseInformation)){
			for(DatabaseConnection databaseConnection : openConnections.get(databaseInformation)){
				if(databaseConnection.isAvailable()) return databaseConnection;
			}
		}
		return null;
	}
	
	private static void runIdleConnectionDestroyScheduler(){
		if(idleDestroySchedulerActive) return;
		idleDestroySchedulerActive = true;
		new ScheduledTask(){
			@Override
			public void run(){
				destroyIdleConnections();
				if(openConnections.isEmpty()){
					idleDestroySchedulerActive = false;
					this.cancel();
				}
			}
		}.runTaskTimer(0, 10000);
	}
	
	private static void destroyIdleConnections(){
		long currentTime = System.currentTimeMillis();
		openConnections.keySet().stream().parallel().forEach(key -> openConnections.get(key).removeIf(dbC -> dbC.getIdleDestroyTime() < currentTime && dbC.isAvailable()));
	}
}
