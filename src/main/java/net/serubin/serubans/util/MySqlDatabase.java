package net.serubin.serubans.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private static SeruBans plugin;
    private static String reason;
    private static String mod;

    public MySqlDatabase(String host, String username, String password,
            String database, SeruBans plugin) {
        this.plugin = plugin;
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
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
                        .prepareStatement("CREATE TABLE IF NOT EXISTS `bans` ( `id` mediumint unsigned not null auto_increment, `player_id` mediumint unsigned not null, `type` tinyint(2) not null, `mod` varchar(16) not null, `reason` varchar(255) not null,  primary key (`id`));");
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
            ps = conn.prepareStatement("SELECT * FROM users;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer pId = rs.getInt("id");
                String pName = rs.getString("name");
                HashMaps.PlayerList.put(pName, pId);
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
                    .prepareStatement("SELECT bans.player_id, users.name, users.id"
                            + " FROM bans"
                            + " INNER JOIN users"
                            + "  ON bans.player_id=users.id"
                            + " WHERE (type = 1) ");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer bId = rs.getInt("id");
                String pName = rs.getString("name");
                HashMaps.BannedPlayers.put(pName, bId);
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
                    .prepareStatement(
                            "INSERT INTO bans (`player_id`, `type`, `mod`, `reason`) VALUES(?,?,?,?);",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, HashMaps.PlayerList.get(victim.getName().toLowerCase()));
            ps.setInt(2, type);
            ps.setString(3, mod);
            ps.setString(4, reason);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Integer bId = rs.getInt(1);
                HashMaps.BannedPlayers.put(victim.getName().toLowerCase(), bId);
            } else {
                plugin.log.severe("Error adding ban!");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void updateBan(int type, int bId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        // add player
        try {
            ps = conn.prepareStatement("UPDATE bans SET type=? WHERE id=?;");
            ps.setInt(1, type);
            ps.setInt(2, bId);
            ps.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static boolean addPlayer(String victim) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SeruBans.printInfo("Attempting to add player " + victim
                + " to database;");
        // add player
        try {
            ps = conn.prepareStatement("INSERT INTO users (name) VALUES(?);");
            ps.setString(1, victim);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean addPlayerHash(String victim) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM users WHERE (name = ?);");
            ps.setString(1, victim);
            rs = ps.executeQuery();
            int pId = rs.getInt("id");
            HashMaps.PlayerList.put(victim, pId);
            plugin.log
                    .info("Player Added: " + victim + " Id: " + pId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getReason(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        reason = "";
        try {
            ps = conn.prepareStatement("SELECT id, reason FROM bans WHERE (id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
            reason = rs.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return reason;
    }

    public static String getMod(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        mod = "";
        try {
            ps = conn.prepareStatement("SELECT id, `mod` FROM bans WHERE (id = ?);");
            SeruBans.printInfo(ps.toString());
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
           mod = rs.getString("mod");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return mod;
    }
}
