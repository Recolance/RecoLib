package net.recolib.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection objects hold all information for the pool to
 * handle connections properly and distribute them properly when
 * requested.
 * 
 * Methods for all control over DatabaseConnections are also within
 * this class, opening, closing, managing, etc.
 * 
 * @author Cody Filatov
 */

public final class DatabaseConnection{

	private Connection connection;
	private final ConnectionInformation connectionInformation;
	private boolean isAvailable;
	private long maxIdleTime;
	private long idleDestroyTime;
	
	protected DatabaseConnection(String hostName, int port, String databaseName, String username, String password){
		this.connectionInformation = new ConnectionInformation(hostName, port, databaseName, username);
		this.maxIdleTime = 3600000L;
		this.idleDestroyTime = System.currentTimeMillis() + 3600000L;
		try{
			this.connection = DriverManager.getConnection("jdbc:mysql://" + hostName + ":" + port + "/" + databaseName, username, password);
			Pool.castConnection(this);
		}catch(SQLException exception){
			exception.printStackTrace();
		}catch(PoolContainsConnectionException exception){
			exception.toString();
			exception.printStackTrace();
		}
		
		this.isAvailable = true;
	}
	
	
	/**
	 * @return True if the DatabaseConnection is closed and not in use.
	 */
	public boolean isAvailable(){
		return this.isAvailable;
	}
	
	
	/**
	 * @return Connection object for this DatabaseConnection.
	 */
	public Connection getConnection(){
		return this.connection;
	}
	
	
	/**
	 * ConnectionInformation is the information contained within
	 * this DatabaseConnection to contact the database. Included
	 * is the connection's host name, port, database name, etc.
	 * 
	 * @return ConnectionInformation object for this DatabaseConnection.
	 */
	public ConnectionInformation getConnectionInformation(){
		return this.connectionInformation;
	}
	
	
	/**
	 * The max idle time is the duration this DatabaseConnection can
	 * be closed for before it is purged from the pool and permanently
	 * closed.
	 * 
	 * @param maxIdleTimeMillis Time in milliseconds the connection will
	 * remain in the connection pool for reuse.
	 */
	public void setMaxIdleTime(long maxIdleTimeMillis){
		this.maxIdleTime = maxIdleTimeMillis;
	}
	
	
	/**
	 * The max idle time is the duration this DatabaseConnection can
	 * be closed for before it is purged from the pool and permanently
	 * closed.
	 * 
	 * @return Time in milliseconds the connection will remain in the
	 * connection pool for reuse.
	 */
	protected long getIdleDestroyTime(){
		return this.idleDestroyTime;
	}
	
	
	/**
	 * Open the connection stating that the connection is in use for
	 * an operation. While a DatabaseConnection is open, it can not be
	 * used or fished out of the connection pool by another operation.
	 * 
	 * After use, DatabaseConnections must be closed by running the close();
	 * method for the DatabaseConnection.
	 * 
	 * This method will return false if the DatabaseConnection was already
	 * opened.
	 * 
	 * @return True if the connection was successfully opened.
	 */
	public boolean open(){
		if(this.isAvailable == true){
			this.isAvailable = false;
			return true;
		}
		return false;
	}
	
	
	/**
	 * Closing the DatabaseConnection states that you are finished using this
	 * DatabaseConnection and should be returned to the pool to be reused by
	 * another operation.
	 * 
	 * If a DatabaseConnection is not closed after use, this can cause a memory 
	 * leak and passively decrease performance within the connection pool.
	 * 
	 * This method will return false if the connection has already been closed.
	 * 
	 * @return True if the connection was successfully closed.
	 */
	public boolean close(){
		if(this.isAvailable == false){
			this.idleDestroyTime = System.currentTimeMillis() + this.maxIdleTime;
			this.isAvailable = true;
			return true;
		}
		return false;
	}
	
	
	/**
	 * Destroying a DatabaseConnection will completely force remove the
	 * DatabaseConnection from the connection pool. This connection will
	 * never be used again and will release all resources this connection
	 * uses.
	 */
	public void destroy(){
		try{
			this.connection.close();
			Pool.reelConnection(this);
		}catch(SQLException exception){
			exception.printStackTrace();
		}catch(FailedReelConnectionException exception){
			exception.toString();
			exception.printStackTrace();
		}
	}
	
	
	/**
	 * Retrieve a DatabaseConnection for the requested database you want
	 * to create or find an available connection for. If no connection is
	 * available or exists, this method will automatically create one for
	 * you. The returned DatabaseConnection will still be closed.
	 * 
	 * @param hostName Host name of your database.
	 * @param port Port of your database.
	 * @param databaseName Name of the database.
	 * @param username Username of the database.
	 * @param password Password of the database.
	 * @return The DatabaseConnection requested for use with your database.
	 */
	public static DatabaseConnection getDatabaseConnection(String hostName, int port, String databaseName, String username, String password){
		DatabaseConnection databaseConnection = Pool.fishConnection(ConnectionInformation.serializeConnectionInformation(hostName, port, databaseName, username));
		
		try{
			if(databaseConnection == null || databaseConnection.getConnection().isClosed()) return new DatabaseConnection(hostName, port, databaseName, username, password);
			else return databaseConnection;
		}catch(SQLException exception){
			exception.printStackTrace();
		}
		
		return new DatabaseConnection(hostName, port, databaseName, username, password);
	}
	
	
	/**
	 * Retrieve a DatabaseConnection for the requested database you want
	 * to create or find an available connection for. If no connection is
	 * available or exists, this method will automatically create one for
	 * you. The returned DatabaseConnection will still be closed.
	 * 
	 * @param connectionInformation ConnectionInformation object containing request database information.
	 * @return TheDatabaseConnection requested for use with your database.
	 */
	public static DatabaseConnection getDatabaseConnection(ConnectionInformation connectionInformation){
		String hostName = connectionInformation.getHostName();
		int port = connectionInformation.getPort();
		String databaseName = connectionInformation.getDatabaseName();
		String username = connectionInformation.getUsername();
		String password = null;
		try{
			password = connectionInformation.getPassword();
		}catch(ConnectionInformationMissingPasswordException exception){
			exception.toString();
			exception.printStackTrace();
		}
		
		return getDatabaseConnection(hostName, port, databaseName, username, password);
	}
	
	
	/**
	 * Retrieve a DatabaseConnection for the requested database you want
	 * to create or find an available connection for. If no connection is
	 * available or exists, this method will automatically create one for
	 * you. The returned DatabaseConnection will be opened for you once this
	 * method is called.
	 * 
	 * @param hostName Host name of your database.
	 * @param port Port of your database.
	 * @param databaseName Name of the database.
	 * @param username Username of the database.
	 * @param password Password of the database.
	 * @return The DatabaseConnection requested for use with your database, opened.
	 */
	public static DatabaseConnection getDatabaseConnectionAndOpen(String hostName, int port, String databaseName, String username, String password){
		DatabaseConnection databaseConnection = getDatabaseConnection(new ConnectionInformation(hostName, port, databaseName, username, password));
		databaseConnection.open();
		return databaseConnection;
	}
	
	
	/**
	 * Retrieve a DatabaseConnection for the requested database you want
	 * to create or find an available connection for. If no connection is
	 * available or exists, this method will automatically create one for
	 * you. The returned DatabaseConnection will be opened for you once this
	 * method is called.
	 * 
	 * @param connectionInformation ConnectionInformation object containing request database information.
	 * @return TheDatabaseConnection requested for use with your database, opened.
	 */
	public static DatabaseConnection getDatabaseConnectionAndOpen(ConnectionInformation connectionInformation){
		String hostName = connectionInformation.getHostName();
		int port = connectionInformation.getPort();
		String databaseName = connectionInformation.getDatabaseName();
		String username = connectionInformation.getUsername();
		String password = null;
		try{
			password = connectionInformation.getPassword();
		}catch(ConnectionInformationMissingPasswordException exception){
			exception.toString();
			exception.printStackTrace();
		}
		return getDatabaseConnectionAndOpen(hostName, port, databaseName, username, password);
	}
}
