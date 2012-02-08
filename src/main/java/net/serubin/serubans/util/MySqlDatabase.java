package net.serubin.serubans.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.bukkit.entity.Player;

import net.serubin.serubans.SeruBans;

public class MySqlDatabase {

	Connection conn;
	private String host;
	private String username;
	private String password;
	private Map<Integer, String> playerList;
	private Map<Integer, String> bannedPlayers;

	public MySqlDatabase(String host, String username, String password,
			Map<Integer, String> playerList,
			Map<Integer, String> bannedPlayers, SeruBans plugin) {
		// TODO Auto-generated constructor stub
		this.host = host;
		this.username = username;
		this.password = password;
		this.playerList = playerList;
		this.bannedPlayers = bannedPlayers;
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
			rs = conn.getMetaData().getTables(null, null, "users", null);
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

	public void getPlayer() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * type FROM users");
			rs = ps.executeQuery();
			while (rs.next()) {
				Integer pId = rs.getInt("id");
				String pName = rs.getString("name");
				playerList.put(pId, pName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getBans() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn
					.prepareStatement("SELECT player_id, type FROM bans WHERE (type = 1)");
			rs = ps.executeQuery();
			while (rs.next()) {
				Integer bId = rs.getInt("id");
				Integer pId = rs.getInt("player_id");
				String pName = playerList.get(pId);
				bannedPlayers.put(bId, pName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addBan(Player victim, int type, Player mod, String reason){
		PreparedStatement ps = null;
		ResultSet rs = null;
		//add player
		try {
			ps = conn.prepareStatement("INSERT INTO bans (player_id, type, date, mod ,reason) VALUES(?,?,?,?,?)");
			ps.setString(1, bannedPlayers.get(victim.getName()));
			ps.setInt(2, type);
			ps.setDate(3, ArgProcessing.getDateTime());
			ps.setString(4, mod.getName());
			ps.setString(5, reason);
			ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean addPlayer(Player victim) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		//add player
		try {
			ps = conn.prepareStatement("INSERT INTO users (name) VALUES(?)");
			ps.setString(1, victim.getName());
			ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		//get id, add to list
		try {
			ps = conn.prepareStatement("SELECT * FROM users WHERE (id = ?)");
			ps.setString(1, victim.getName());
			rs = ps.executeQuery();
			int pId = rs.getInt("id");
			playerList.put(pId, victim.getName());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
