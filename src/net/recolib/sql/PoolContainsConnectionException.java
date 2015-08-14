package net.recolib.sql;

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
