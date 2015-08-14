package net.recolib.test;

import net.recolib.sql.ConnectionInformation;
import net.recolib.sql.DatabaseConnection;

public class Test{

	public static void main(String[] args){
		ConnectionInformation connectionInformation = new ConnectionInformation("127.0.0.1", 3306, "test", "root", "codster1");
		DatabaseConnection databaseConnection = DatabaseConnection.getDatabaseConnection(connectionInformation);
		DatabaseConnection databaseConnection2 = DatabaseConnection.getDatabaseConnection(connectionInformation);
		
		if(databaseConnection == databaseConnection2) System.out.println(true);
	}
}
