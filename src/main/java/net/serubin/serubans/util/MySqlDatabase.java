package net.serubin.serubans.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.bukkit.entity.Player;

import net.serubin.serubans.SeruBans;

public class MySqlDatabase {

    static Connection conn;
    public static String host;
    public static String username;
    public static String password;
    public static String database;
    private static Map<String, Integer> playerList;
    private static Map<String, Integer> bannedPlayers;
    private static SeruBans plugin;

    public MySqlDatabase(String host, String username, String password,
            String database, Map<String, Integer> playerList,
            Map<String, Integer> bannedPlayers, SeruBans plugin) {
        this.plugin = plugin;
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.playerList = playerList;
        this.bannedPlayers = bannedPlayers;
    }

    public static void startSQL() {
        createConnection();
        createTable();
        getPlayer();
        getBans();
    }

    protected static void createConnection() {
        String sqlUrl = String.format("jdbc:mysql://%s/%s", host, database);

        Properties sqlStr = new Properties();
        sqlStr.put("user", username);
        sqlStr.put("password", password);
        try {
            conn = DriverManager.getConnection(sqlUrl, sqlStr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void createTable() {
        try {
            SeruBans.printInfo("Searching for storage table");
            ResultSet rs = conn.getMetaData().getTables(null, null, "bans",
                    null);
            if (!rs.next()) {
                SeruBans.printWarning("No 'bans' data table found, Attempting to create one...");
                PreparedStatement ps = conn
                        .prepareStatement("CREATE TABLE IF NOT EXISTS `bans` ( `id` mediumint unsigned not null auto_increment, `player_id` mediumint unsigned not null, `type` tinyint(2) not null, `date` DATETIME not null, `mod` mediumint unsigned not null, `reason` varchar(255) not null,  primary key (`id`));");
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
                SeruBans.printWarning("No 'users' data table found, Attempting to create one...");
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
            e.printStackTrace();
        }
    }

    public static void getPlayer() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * type FROM users;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer pId = rs.getInt("id");
                String pName = rs.getString("name");
                playerList.put(pName, pId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getBans() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn
                    .prepareStatement("SELECT player_id, type FROM bans WHERE (type = 1);");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer bId = rs.getInt("id");
                Integer pId = rs.getInt("player_id");
                String pName = playerList.get(pId);
                bannedPlayers.put(pName, bId);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void addBan(Player victim, int type, String mod, String reason) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        // add player
        try {
            ps = conn
                    .prepareStatement("INSERT INTO bans (player_id, type, date, mod ,reason) VALUES(?,?,?,?,?);");
            ps.setInt(1, playerList.get(victim.getName()));
            ps.setInt(2, type);
            ps.setDate(3, ArgProcessing.getDateTime());
            ps.setString(4, mod);
            ps.setString(5, reason);
            ps.executeQuery();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void addPlayer(Player victim) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SeruBans.printInfo("Attempting to add player " + victim.getName()
                + " to database;");
        // add player
        try {
            ps = conn.prepareStatement("INSERT INTO users (name) VALUES(?);");
            ps.setString(1, victim.getName());
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // get id, add to list
        try {
            ps = conn.prepareStatement("SELECT * FROM users WHERE (id = ?);");
            ps.setString(1, victim.getName());
            rs = ps.executeQuery();
            int pId = rs.getInt("id");
            playerList.put(pId, victim.getName());
            plugin.log
                    .info("Player Added: " + victim.getName() + " Id: " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getReason(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String reason = "";
        String mod = "";
        try {
            ps = conn.prepareStatement("SELECT id FROM bans WHERE (id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            reason = rs.getString("reason");
            mod = rs.getString("mod");
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return reason;
    }

    public static String getMod(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String mod = "";
        try {
            ps = conn.prepareStatement("SELECT id FROM bans WHERE (id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            mod = rs.getString("mod");
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return mod;
    }
}
