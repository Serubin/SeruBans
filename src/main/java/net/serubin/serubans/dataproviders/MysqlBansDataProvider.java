package net.serubin.serubans.dataproviders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.BanInfo;
import net.serubin.serubans.util.DataCache;

public class MysqlBansDataProvider implements Runnable, IBansDataProvider {

    private SeruBans plugin;
    private Connection conn;
    private String host;
    private String username;
    private String password;
    private String database;

    private DataCache dc = null;
    private int lastBanId;

    /**
     * Initiates Mysql object
     * 
     * @param host
     *            of the mysql server
     * @param username
     *            of the mysql account
     * @param password
     *            of the mysql account
     * @param database
     *            to be used
     * @param plugin
     */
    public MysqlBansDataProvider(String host, String username, String password,
            String database, SeruBans plugin) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
        this.plugin = plugin;
        startSQL();
    }

    /**
     * Start sql connections
     */
    public void startSQL() {
        createConnection();
        createTable();
    }

    public void run() {
        maintainConnection();
    }

    /**
     * Create MySQL connection
     */
    protected void createConnection() {
        String sqlUrl = String.format("jdbc:mysql://%s/%s", host, database);

        Properties sqlStr = new Properties();
        sqlStr.put("user", username);
        sqlStr.put("password", password);
        try {
            conn = DriverManager.getConnection(sqlUrl, sqlStr);
        } catch (SQLException e) {
            plugin.printError("A MySQL connection could not be made");
            e.printStackTrace();
        }
    }

    /**
     * Checks if tables are existing, then
     */
    protected void createTable() {
        try {
            plugin.printInfo("Searching for storage table");
            ResultSet rs = conn.getMetaData().getTables(null, null, "bans",
                    null);
            if (!rs.next()) {
                plugin.printWarning("No 'bans' data table found, Attempting to create one...");
                PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `bans` ( "
                        + "`id` mediumint unsigned not null auto_increment, "
                        + "`player_id` mediumint unsigned not null, "
                        + "`type` tinyint(2) not null, "
                        + "`length` bigint(20) not null, "
                        + "`mod` mediumint(8) unsigned not null, "
                        + "`date` DATETIME not null, "
                        + "`reason` varchar(255) not null, "
                        + "`display` tinyint(1) not null, "
                        + "primary key (`id`));");
                ps.executeUpdate();
                ps.close();
                plugin.printWarning("'bans' data table created!");
            } else {
                plugin.printInfo("Table found");
            }
            rs.close();

            plugin.printInfo("Searching for log table");
            rs = conn.getMetaData().getTables(null, null, "log", null);
            if (!rs.next()) {
                plugin.printWarning("No 'log' data table found, Attempting to create one...");
                PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `log` ( "
                        + "`id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT, "
                        + "`action` enum('delete','unban','update') NOT NULL, "
                        + "`banid` mediumint(8) unsigned NOT NULL, "
                        + "`ip` text NOT NULL, "
                        + "`data` text NOT NULL, "
                        + "primary key (`id`), key `id` (`id`));");
                ps.executeUpdate();
                ps.close();
                plugin.printWarning("'log' data table created!");
            } else {
                plugin.printInfo("Table found");
            }
            rs.close();

            plugin.printInfo("Searching for warns table");
            rs = conn.getMetaData().getTables(null, null, "warns", null);
            if (!rs.next()) {
                plugin.printWarning("No 'warns' data table found, Attempting to create one...");
                PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `warns` ( "
                        + "`id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT, "
                        + "`player_id` mediumint(8) unsigned NOT NULL, "
                        + "`ban_id` mediumint(8) unsigned NOT NULL, "
                        + "primary key (`id`), key `id` (`id`));");
                ps.executeUpdate();
                ps.close();
                plugin.printWarning("'warns' data table created!");
            } else {
                plugin.printInfo("Table found");
            }
            rs.close();

            plugin.printInfo("Searching for users table");
            rs = conn.getMetaData().getTables(null, null, "users", null);
            if (!rs.next()) {
                plugin.printWarning("No 'users' data table found, Attempting to create one...");
                PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS `users` ( "
                        + "`id` mediumint unsigned not null auto_increment, "
                        + "`name` varchar(16) not null, "
                        + "primary key (`id`), UNIQUE key `player` (`name`));");
                ps.executeUpdate();
                ps.close();
                plugin.printWarning("'users' data table created!");
            } else {
                plugin.printInfo("Table found");
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void maintainConnection() {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT count(*) FROM bans limit 1;");
            ps.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        plugin.self.log.info("plugin has checked in with database");
    }

    public BanInfo getPlayerBannedInfo(String name) {
        return getPlayerBanInfo(name, SeruBans.BAN);
    }

    public BanInfo getPlayerTempBannedInfo(String name) {
        return getPlayerBanInfo(name, SeruBans.TEMPBAN);
    }

    private BanInfo getPlayerBanInfo(String name, int status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT type, bans.id, mod, users.id, name, length, reason"
                    + " FROM bans"
                    + " INNER JOIN users"
                    + "  ON bans.player_id=users.id"
                    + " WHERE type = ?"
                    + " AND name = ?");
            ps.setInt(1, status);
            ps.setString(2, name);
            rs = ps.executeQuery();
            while (rs.next()) {
                BanInfo banInfo = new BanInfo(rs.getInt("type"),
                        rs.getInt("bans.id"), rs.getInt("mod"),
                        rs.getInt("users.id"), rs.getLong("length"),
                        rs.getString("reason"));
                return banInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean unbanPlayer(String player) {
        BanInfo banTest = getPlayerBannedInfo(player);
        if (banTest != null) {
            updateBanType(SeruBans.UNBAN, banTest.getBanId());
            return true;
        }
        banTest = getPlayerTempBannedInfo(player);
        if (banTest != null) {
            updateBanType(SeruBans.UNTEMPBAN, banTest.getBanId());
            return true;
        }
        return false;
    }

    public int getBanId(String name, int status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT id" + " FROM bans"
                    + " WHERE name = \"" + name + "\" AND type =?");
            ps.setInt(1, status);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public List<BanInfo> getPlayerWarnsInfo(String name) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT ban_id" + " FROM warns"
                    + " INNER JOIN users" + "  ON warns.player_id=id"
                    + " WHERE name = \"" + name + "\"");
            rs = ps.executeQuery();
            List<BanInfo> warnInfo = new ArrayList<BanInfo>();
            while (rs.next()) {
                BanInfo banInfo = new BanInfo(rs.getInt("bans.id"),
                        rs.getInt("users.id"), SeruBans.WARN, rs.getInt("mod"),
                        rs.getString("reason"));
                warnInfo.add(banInfo);
            }
            return warnInfo;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<BanInfo> getPermBans() {
        return getBans(SeruBans.BAN);
    }

    public List<BanInfo> getTempBans() {
        return getBans(SeruBans.TEMPBAN);
    }

    public List<BanInfo> getBans(int status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT bans.id, users.id, mod, name, reason, length"
                    + " FROM bans"
                    + " INNER JOIN users"
                    + "  ON bans.player_id=users.id" + " WHERE type =?");
            ps.setInt(1, status);
            rs = ps.executeQuery();
            List<BanInfo> listInfo = new ArrayList<BanInfo>();
            while (rs.next()) {
                BanInfo banInfo = new BanInfo(rs.getInt("bans.id"),
                        rs.getInt("users.id"), status, rs.getInt("mod"),
                        rs.getLong("length"), rs.getString("reason"));
                listInfo.add(banInfo);
            }
            return listInfo;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // to remove - START
    public void getPlayers() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM users;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer pId = rs.getInt("id");
                String pName = rs.getString("name");
                dc.addPlayer(pId, pName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // to remove - END

    public boolean addBan(String victim, int type, long length, String mod,
            String reason, int display) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        // add player
        try {
            ps = conn.prepareStatement(
                    "INSERT INTO bans (`player_id`, `type`, `length`, `mod`, `date`, `reason`, `display`) VALUES(?,?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            // Sets objects
            ps.setInt(1, dc.getPlayerId(victim.toLowerCase()));
            ps.setInt(2, type);
            ps.setLong(3, length);
            ps.setInt(4, dc.getPlayerId(mod.toLowerCase()));
            ps.setObject(5, ArgProcessing.getDateTime());
            ps.setString(6, reason);
            ps.setInt(7, display);
            ps.executeUpdate();
            // Gets generated keys
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Integer bId = rs.getInt(1);
                lastBanId = bId;

                plugin.printInfo(ArgProcessing.getBanTypeString(type) + ": "
                        + victim + " Ban Id: " + bId);
            } else {
                plugin.printInfo("Error adding ban!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean untempBan(int banId) {
        return updateBanType(SeruBans.UNTEMPBAN, banId);
    }

    private boolean updateBanType(int type, int bId) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE bans SET type=? WHERE id=?;");
            ps.setInt(1, type);
            ps.setInt(2, bId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void updateReason(int bId, String reason) {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        try {
            ps = conn.prepareStatement("UPDATE bans SET reason=? WHERE id=?;");
            ps2 = conn.prepareStatement("INSERT INTO `log`(`action`, `banid`, `ip`, `data`) VALUES ('update',?,'In-Game',?)");
            String data = "UPDATE:Id=" + bId + "Reason=" + reason;
            ps2.setInt(1, bId);
            ps2.setString(2, data);
            ps2.executeUpdate();
            ps.setString(1, reason);
            ps.setInt(2, bId);
            ps.executeUpdate();
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public boolean addPlayer(String player) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        plugin.printInfo("Attempting to add player " + player + " to database;");
        // add player
        try {
            ps = conn.prepareStatement("INSERT INTO users (name) VALUES(?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, player.toLowerCase());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Integer pId = rs.getInt(1);
                dc.addPlayer(pId, player);
                plugin.printInfo("Player Added: " + player + " Id: " + pId);
            } else {
                plugin.printInfo("Error adding user!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean addWarn(int pId, int bId) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(
                    "INSERT INTO warns (player_id, ban_id) VALUES(?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pId);
            ps.setInt(2, bId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeWarn(int playerId, int banId) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM warns WHERE player_id=? AND ban_id=?;");
            ps.setInt(1, playerId);
            ps.setInt(2, banId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Gets last ban id.
     * 
     * @return lastBanId
     */
    public int getLastBanId() {
        return lastBanId;
    }
}
