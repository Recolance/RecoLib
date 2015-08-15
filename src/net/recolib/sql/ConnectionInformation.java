package net.recolib.sql;

/**
 * ConnectionInformation is an object containing the nessicary information
 * to make a connection to the DatabaseConnection class.
 * 
 * @author Cody Filatov
 */

public final class ConnectionInformation{

	private final String hostName;
	private final int port;
	private final String databaseName;
	private final String username;
	private final String password;
	
	/**
	 * Create a full ConnectionInformation object that can be used to
	 * make DatabaseConnection connections.
	 * 
	 * @param hostName Hostname of the database.
	 * @param port Port of the database.
	 * @param databaseName Name of the database.
	 * @param username Username to connect with.
	 * @param password Password to connect with.
	 */
	public ConnectionInformation(String hostName, int port, String databaseName, String username, String password){
		this.hostName = hostName;
		this.port = port;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
	}
	
	protected ConnectionInformation(String hostName, int port, String databaseName, String username){
		this.hostName = hostName;
		this.port = port;
		this.databaseName = databaseName;
		this.username = username;
		this.password = null;
	}

	/**
	 * Getter for the ConnectionInformation host name.
	 * 
	 * @return The host name.
	 */
	public String getHostName(){
		return this.hostName;
	}

	
	/**
	 * Getter for the ConnectionInformation port number.
	 * 
	 * @return The port number.
	 */
	public int getPort(){
		return this.port;
	}

	
	/**
	 * Getter for the ConnectionInformation database name.
	 * 
	 * @return The database name.
	 */
	public String getDatabaseName(){
		return this.databaseName;
	}

	
	/**
	 * Getter for the ConnectionInformation username.
	 * 
	 * @return The username.
	 */
	public String getUsername(){
		return this.username;
	}
	
	protected String getPassword() throws ConnectionInformationMissingPasswordException{
		if(this.password == null) throw new ConnectionInformationMissingPasswordException(this);
		else return this.password;
	}
	
	protected String serialize(){
		return this.hostName + this.port + this.databaseName + this.username;
	}
	
	protected static String serializeConnectionInformation(String hostName, int port, String databaseName, String username){
		return new ConnectionInformation(hostName, port, databaseName, username).serialize();
	}
}
