package net.serubin.serubans.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

	public void createConnection() {
		try {

			DriverManager.getConnection(host + "?autoReconnect=true&user="
					+ username + "&password=" + password);
		} catch (SQLException ex) {
			SeruBans.printError("Unable to connect to database!");
		}

	}

	public void createTable() {
		try {
			SeruBans.printInfo("Searching for storage table");
			ResultSet rs = conn.getMetaData().getTables(null, null, "bans",
					null);
			if (!rs.next()) {
				SeruBans.printWarning("No 'bans' data table found, Attempting to create one...");
				PreparedStatement ps = conn
						.prepareStatement("CREATE TABLE IF NOT EXISTS `bans` ( `id` mediumint unsigned not null auto_increment,  `player_id` mediumint unsigned not null,  `type` tinyint(2) not null,  `date` DATETIME not null, 'mod' mediumint unsigned not null, 'reason' varchar(255) not null");
				ps.executeUpdate();
				ps.close();
				SeruBans.printWarning("'bans' data table created!");
			} else {
				SeruBans.printInfo("Table found");
			}
			rs.close();

			SeruBans.printInfo("Searching for users table");
			rs = conn.getMetaData().getTables(null, null, "bans", null);
			if (!rs.next()) {
				SeruBans.printWarning("No 'user' data table found, Attempting to create one...");
				PreparedStatement ps = conn
						.prepareStatement("CREATE TABLE IF NOT EXISTS `users` ( `id` mediumint unsigned not null auto_increment, `name` varchar(16) not null, primary key (`id`), UNIQUE key `player` (`name`));");
				ps.executeUpdate();
				ps.close();
				SeruBans.printWarning("'user' data table created!");
			} else {
				SeruBans.printInfo("Table found");
			}
			rs.close();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getBans(){
		try{
		PreparedStatement ps = null;
			ps = conn.prepareStatement("SELECT player_id, type FROM bans WHERE (type = 1)");
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
