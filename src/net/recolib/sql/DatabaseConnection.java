package net.recolib.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection{

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
	
	public boolean isAvailable(){
		return this.isAvailable;
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
	public ConnectionInformation getConnectionInformation(){
		return this.connectionInformation;
	}
	
	public void setMaxIdleTime(long maxIdleTimeMillis){
		this.maxIdleTime = maxIdleTimeMillis;
	}
	
	protected long getIdleDestroyTime(){
		return this.idleDestroyTime;
	}
	
	public boolean open(){
		if(this.isAvailable == true){
			this.isAvailable = false;
			return true;
		}
		return false;
	}
	
	public boolean close(){
		if(this.isAvailable == false){
			this.idleDestroyTime = System.currentTimeMillis() + this.maxIdleTime;
			this.isAvailable = true;
			return true;
		}
		return false;
	}
	
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
	
	public static DatabaseConnection getDatabaseConnectionAndOpen(String hostName, int port, String databaseName, String username, String password){
		DatabaseConnection databaseConnection = getDatabaseConnection(new ConnectionInformation(hostName, port, databaseName, username, password));
		databaseConnection.open();
		return databaseConnection;
	}
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
