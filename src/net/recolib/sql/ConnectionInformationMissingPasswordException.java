package net.recolib.sql;

/**
 * This exception is thrown when a ConnectionInformation object
 * is appempted to be used to create a DatabaseConnection. Or is
 * thrown when getPassword(); is called within ConnectionInformation
 * and no password exists.
 * 
 * @author Cody Filatov
 */

public class ConnectionInformationMissingPasswordException extends Exception{

	private ConnectionInformation connectionInformation;
	
	public ConnectionInformationMissingPasswordException(ConnectionInformation connectionInformation){
		this.connectionInformation = connectionInformation;
	}
	
	@Override
	public String toString(){
		String hostName = this.connectionInformation.getHostName();
		int port = this.connectionInformation.getPort();
		return "Attempted to retrieve password from ConnectionInformation where none exists. ConnectionInformation: " + hostName + ":" + port;
	}
}
