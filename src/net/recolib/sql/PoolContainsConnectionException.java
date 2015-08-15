package net.recolib.sql;

/**
 * This exception is thrown when a DatabaseConnection has attempted
 * to be placed into the Pool when it already exists within it.
 * 
 * @author Cody Filatov
 */

public class PoolContainsConnectionException extends Exception{

	private DatabaseConnection databaseConnection;

	public PoolContainsConnectionException(DatabaseConnection databaseConnection){
		this.databaseConnection = databaseConnection;
	}
	
	@Override
	public String toString(){
		String hostName = this.databaseConnection.getConnectionInformation().getHostName();
		int port = this.databaseConnection.getConnectionInformation().getPort();
		return "Connection object already exists within the pool. Connection: " + hostName + ":" + port;
	}
}
