package net.recolib.sql;

import java.io.UnsupportedEncodingException;

public final class ConnectionInformation{

	private final String hostName;
	private final int port;
	private final String databaseName;
	private final String username;
	private final String password;
	
	public ConnectionInformation(String hostName, int port, String databaseName, String username, String password){
		this.hostName = hostName;
		this.port = port;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
	}
	
	public ConnectionInformation(String hostName, int port, String databaseName, String username){
		this.hostName = hostName;
		this.port = port;
		this.databaseName = databaseName;
		this.username = username;
		this.password = null;
	}

	public String getHostName(){
		return this.hostName;
	}

	public int getPort(){
		return this.port;
	}

	public String getDatabaseName(){
		return this.databaseName;
	}

	public String getUsername(){
		return this.username;
	}
	
	public String getPassword() throws ConnectionInformationMissingPasswordException{
		if(this.password == null) throw new ConnectionInformationMissingPasswordException(this);
		else return this.password;
	}
	
	public String serialize(){
		return this.hostName + this.port + this.databaseName + this.username;
	}
	
	protected static String serializeConnectionInformation(String hostName, int port, String databaseName, String username){
		return new ConnectionInformation(hostName, port, databaseName, username).serialize();
	}
}
