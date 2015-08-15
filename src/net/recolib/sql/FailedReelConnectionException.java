package net.recolib.sql;

/**
 * This exception is thrown when a DatabaseConnection is attempted
 * to be purchased from the Pool when it does not exist within the
 * pool.
 * 
 * @author Cody Filatov
 */

public class FailedReelConnectionException extends Exception{

	private DatabaseConnection databaseConnection;
	
	public FailedReelConnectionException(DatabaseConnection databaseConnection){
		this.databaseConnection = databaseConnection;
	}
	
	@Override
	public String toString(){
		String hostName = this.databaseConnection.getConnectionInformation().getHostName();
		int port = this.databaseConnection.getConnectionInformation().getPort();
		return "DatabaseConnection object failed to reel or does not exist in pool. Connection: " + hostName + ":" + port;
	}
}
