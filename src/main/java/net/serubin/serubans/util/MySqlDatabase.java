package net.serubin.serubans.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.serubin.serubans.SeruBans;

public class MySqlDatabase {

	Connection conn;
	private String host;
	private String username;
	private String password;
	
     public MySqlDatabase(String host, String username, String password,
			SeruBans plugin) {
		// TODO Auto-generated constructor stub
    	 this.host = host;
    	 this.username = username;
    	 this.password = password;
	}

	public void createConnection(){
		try {

			DriverManager.getConnection(host + "?autoReconnect=true&user=" + username + "&password=" + password);
		} catch (SQLException ex) {
			SeruBans.printError("Unable to connect to database!");
		}
	
	}
	
	public void createTable(){
		
		try {
			SeruBans.printWarning("No 'bans' data table found, Attempting to create one...");
			PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `bans` ( `player_id` INTEGER NOT NULL,  `type` INTEGER(2) NOT NULL,  `time` DATETIME");
			ps.executeUpdate();
			ps.close();
			SeruBans.printWarning("'bans' data table created!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			SeruBans.printError("Error while creating data table 'bans'!");
		}
	}
}
