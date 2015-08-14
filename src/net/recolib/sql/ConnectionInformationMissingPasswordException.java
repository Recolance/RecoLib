package net.recolib.sql;

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
