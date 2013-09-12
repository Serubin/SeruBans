package net.serubin.serubans.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.serubin.serubans.SeruBans;

public class MySqlDatabase implements Runnable {

    private static Connection conn;
    private static String host;
    private static String username;
    private static String password;
    private static String database;
    private static String reason;
    private static String mod;
    private static int lastBanId;

    /**
     * Initiates Mysql object
     * 
     * @param host of the mysql server
     * @param username of the mysql account
     * @param password of the mysql account
     * @param database to be used
     * @param plugin
     */
    public MySqlDatabase(String host, String username, String password,
            String database, SeruBans plugin) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    /**
     * Start sql connections
     */
    public static void startSQL() {
        createConnection();
        createTable();
        getPlayer();
        getBans();
        getTempBans();
        getBanIds();
        getWarns();

    }

    public void run() {
        maintainConnection();
    }

    /**
     * Create MySQL connection
     */
    protected static void createConnection() {
        String sqlUrl = String.format("jdbc:mysql://%s/%s", host, database);

        Properties sqlStr = new Properties();
        sqlStr.put("user", username);
        sqlStr.put("password", password);
        try {
            conn = DriverManager.getConnection(sqlUrl, sqlStr);
        } catch (SQLException e) {
            SeruBans.printError("A MySQL connection could not be made");
            e.printStackTrace();
        }
    }

    /**
     * Checks if tables are existing, then
     */
    protected static void createTable() {
        try {
            SeruBans.printInfo("Searching for storage table");
            ResultSet rs = conn.getMetaData().getTables(null, null, "bans",
                    null);
            if (!rs.next()) {
                SeruBans.printWarning("No 'bans' data table found, Attempting to create one...");
                PreparedStatement ps = conn
                        .prepareStatement("CREATE TABLE IF NOT EXISTS `bans` ( "
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
                SeruBans.printWarning("'bans' data table created!");
            } else {
                SeruBans.printInfo("Table found");
            }
            rs.close();

            SeruBans.printInfo("Searching for log table");
            rs = conn.getMetaData().getTables(null, null, "log", null);
            if (!rs.next()) {
                SeruBans.printWarning("No 'log' data table found, Attempting to create one...");
                PreparedStatement ps = conn
                        .prepareStatement("CREATE TABLE IF NOT EXISTS `log` ( "
                                + "`id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT, "
                                + "`action` enum('delete','unban','update') NOT NULL, "
                                + "`banid` mediumint(8) unsigned NOT NULL, "
                                + "`ip` text NOT NULL, "
                                + "`data` text NOT NULL, "
                                + "primary key (`id`), key `id` (`id`));");
                ps.executeUpdate();
                ps.close();
                SeruBans.printWarning("'log' data table created!");
            } else {
                SeruBans.printInfo("Table found");
            }
            rs.close();

            SeruBans.printInfo("Searching for warns table");
            rs = conn.getMetaData().getTables(null, null, "warns", null);
            if (!rs.next()) {
                SeruBans.printWarning("No 'warns' data table found, Attempting to create one...");
                PreparedStatement ps = conn
                        .prepareStatement("CREATE TABLE IF NOT EXISTS `warns` ( "
                                + "`id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT, "
                                + "`player_id` mediumint(8) unsigned NOT NULL, "
                                + "`ban_id` mediumint(8) unsigned NOT NULL, "
                                + "primary key (`id`), key `id` (`id`));");
                ps.executeUpdate();
                ps.close();
                SeruBans.printWarning("'warns' data table created!");
            } else {
                SeruBans.printInfo("Table found");
            }
            rs.close();

            SeruBans.printInfo("Searching for users table");
            rs = conn.getMetaData().getTables(null, null, "users", null);
            if (!rs.next()) {
                SeruBans.printWarning("No 'users' data table found, Attempting to create one...");
                PreparedStatement ps = conn
                        .prepareStatement("CREATE TABLE IF NOT EXISTS `users` ( "
                                + "`id` mediumint unsigned not null auto_increment, "
                                + "`name` varchar(16) not null, "
                                + "primary key (`id`), UNIQUE key `player` (`name`));");
                ps.executeUpdate();
                ps.close();
                SeruBans.printWarning("'users' data table created!");
            } else {
                SeruBans.printInfo("Table found");
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void maintainConnection() {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT count(*) FROM bans limit 1;");
            ps.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SeruBans.self.log.info("SeruBans has checked in with database");
    }

    public static BanInfo getPlayerBannedInfo(String name) {
        return getBanInfo(name, SeruBans.BAN);
    }

    public static BanInfo getPlayerTempBannedInfo(String name) {
        return getBanInfo(name, SeruBans.TEMPBAN);
    }

    private static BanInfo getBanInfo(String name, int status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn
                    .prepareStatement("SELECT type, bans.id, mod, users.id, name, length, reason"
                            + " FROM bans"
                            + " INNER JOIN users"
                            + "  ON bans.player_id=users.id"
                            + " WHERE type = "
                            + status
                            + " AND name = \""
                            + name
                            + "\"");
            rs = ps.executeQuery();
            while (rs.next()) {
                BanInfo banInfo = new BanInfo();
                banInfo.setType(rs.getInt("type"));
                banInfo.setBanId(rs.getInt("bans.id"));
                int modId = rs.getInt("mod");
                banInfo.setModId(modId);
                banInfo.setModName(getUserName(modId));
                banInfo.setPlayerId(rs.getInt("users.id"));
                banInfo.setPlayerName(rs.getString("name"));
                banInfo.setLength(rs.getLong("length"));
                banInfo.setReason(rs.getString("reason"));
                return banInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getUserName(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT name"
                            + " FROM users"
                            + " WHERE id = "
                            + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getBanId(String name, int status) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT id"
                            + " FROM bans"
                            + " WHERE name = \""
                            + name
                            + "\" AND type ="
                            + status);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static ArrayList<BanInfo> getPlayerWarnsInfo(String name) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT ban_id"
                            + " FROM warns"
                            + " INNER JOIN users"
                            + "  ON warns.player_id=id"
                            + " WHERE name = \""
                            + name
                            + "\"");
            rs = ps.executeQuery();
            ArrayList<BanInfo> warnInfo = new ArrayList<BanInfo>();
            while (rs.next()) {
                BanInfo banInfo = new BanInfo();
                banInfo.setBanId(rs.getInt("bans.id"));
                int modId = rs.getInt("mod");
                banInfo.setModId(modId);
                banInfo.setModName(getUserName(modId));
                banInfo.setPlayerId(rs.getInt("users.id"));
                banInfo.setReason(rs.getString("reason"));
                warnInfo.add(banInfo);
            }
            return warnInfo;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

// to remove - START
    public static void getPlayer() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM users;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer pId = rs.getInt("id");
                String pName = rs.getString("name");
                HashMaps.setPlayerList(pName, pId);
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
                    .prepareStatement("SELECT bans.player_id, bans.id, users.name, users.id"
                            + " FROM bans"
                            + " INNER JOIN users"
                            + "  ON bans.player_id=users.id"
                            + " WHERE (type = 1 OR type = 2) ");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer bId = rs.getInt("bans.id");
                String pName = rs.getString("name");
                HashMaps.setBannedPlayers(pName, bId);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void getBanIds() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT bans.id FROM bans;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer id = rs.getInt("bans.id");
                HashMaps.setIds(id);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void getTempBans() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT id, length" + " FROM bans"
                    + " WHERE (type = 2) ");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer bId = rs.getInt("id");
                Long length = rs.getLong("length");
                HashMaps.setTempBannedTime(bId, length);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public static void getWarns() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT player_id, ban_id FROM warns;");
            rs = ps.executeQuery();
            while (rs.next()) {
                Integer pId = rs.getInt("player_id");
                Integer bId = rs.getInt("ban_id");
                List<Integer> warns;
                if (HashMaps.isWarn(pId)) {
                    warns = HashMaps.getWarn(pId);
                    warns.add(bId);
                } else {
                    warns = new ArrayList<Integer>();
                    warns.add(bId);
                }
                HashMaps.setWarn(pId, warns);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
// to remove - END

    public static void addBan(String victim, int type, long length, String mod,
            String reason, int display) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        // add player
        try {
            ps = conn
                    .prepareStatement(
                            "INSERT INTO bans (`player_id`, `type`, `length`, `mod`, `date`, `reason`, `display`) VALUES(?,?,?,?,?,?,?);",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, HashMaps.getPlayerList(victim.toLowerCase()));
            ps.setInt(2, type);
            ps.setLong(3, length);
            ps.setInt(4, HashMaps.getPlayerList(mod.toLowerCase()));
            ps.setObject(5, ArgProcessing.getDateTime());
            ps.setString(6, reason);
            ps.setInt(7, display);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Integer bId = rs.getInt(1);
                HashMaps.setIds(bId);
                if (type == 1 || type == 2) {
                    HashMaps.setBannedPlayers(victim.toLowerCase(), bId);
                }
                if (type == 2) {
                    HashMaps.setTempBannedTime(bId, length);
                }
                lastBanId = bId;
                SeruBans.printInfo(ArgProcessing.getBanTypeString(type) + ": "
                        + victim + " Ban Id: " + bId);
            } else {
                SeruBans.printInfo("Error adding ban!");
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void updateBan(int type, int bId) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("UPDATE bans SET type=? WHERE id=?;");
            ps.setInt(1, type);
            ps.setInt(2, bId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateReason(int bId, String reason) {
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        try {
            ps = conn.prepareStatement("UPDATE bans SET reason=? WHERE id=?;");
            ps2 = conn
                    .prepareStatement("INSERT INTO `log`(`action`, `banid`, `ip`, `data`) VALUES ('update',?,'In-Game',?)");
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

    public static void addPlayer(String victim) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SeruBans.printInfo("Attempting to add player " + victim
                + " to database;");
        // add player
        try {
            ps = conn.prepareStatement("INSERT INTO users (name) VALUES(?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, victim);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                Integer pId = rs.getInt(1);
                HashMaps.setPlayerList(victim, pId);
                SeruBans.printInfo("Player Added: " + victim + " Id: " + pId);
            } else {
                SeruBans.printInfo("Error adding user!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getReason(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        reason = "";
        try {
            ps = conn
                    .prepareStatement("SELECT id, reason FROM bans WHERE (id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
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
            ps = conn
                    .prepareStatement("SELECT bans.id, bans.mod, users.id, users.name FROM bans INNER JOIN users ON bans.mod = users.id WHERE (bans.id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                mod = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return mod;
    }

    public static Long getLength(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Long length = null;
        try {
            ps = conn
                    .prepareStatement("SELECT id, length FROM bans WHERE (id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                length = rs.getLong("length");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return length;
    }

    public static void addWarn(int pId, int bId) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(
                    "INSERT INTO warns (player_id, ban_id) VALUES(?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pId);
            ps.setInt(2, bId);
            ps.executeUpdate();
            ps.getGeneratedKeys();
            List<Integer> warns;
            if (HashMaps.isWarn(pId)) {
                warns = HashMaps.getWarn(pId);
                warns.add(bId);
            } else {
                warns = new ArrayList<Integer>();
                warns.add(bId);
            }
            HashMaps.setWarn(pId, warns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeWarn(int pId, int bId) {
        PreparedStatement ps = null;
        try {
            ps = conn
                    .prepareStatement("DELETE FROM warns WHERE player_id=? AND ban_id=?;");
            ps.setInt(1, pId);
            ps.setInt(2, bId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> searchPlayer(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> type = new ArrayList<Integer>();
        try {
            ps = conn
                    .prepareStatement("SELECT `player_id`, `type` FROM bans WHERE (player_id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                type.add(rs.getInt("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        if (type.isEmpty()) {
            type = null;
        }
        return type;
    }

    public static List<String> searchType(int id, int type) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> typeList = new ArrayList<Integer>();
        List<String> PlayerInfo = new ArrayList<String>();
        try {
            if ((type == 1) || (type == 2)) {
                int type2 = 0;
                if (type == 1) {
                    type2 = 11;
                } else if (type == 2) {
                    type2 = 12;
                }
                ps = conn
                        .prepareStatement("SELECT `player_id`, `type`, `id` FROM bans WHERE ((player_id = ?) AND (type = ?)) OR ((player_id = ?) AND (type = ?));");

                ps.setInt(1, id);
                ps.setInt(2, type);
                ps.setInt(3, id);
                ps.setInt(4, type2);
            } else {
                ps = conn
                        .prepareStatement("SELECT `player_id`, `type`, `id` FROM bans WHERE (player_id = ?) AND (type = ?);");

                ps.setInt(1, id);
                ps.setInt(2, type);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                typeList.add(rs.getInt("id"));
            }

            if (typeList.isEmpty()) {
                return PlayerInfo = null;
            }

            Iterator<Integer> typeListItor = typeList.iterator();
            while (typeListItor.hasNext()) {
                PlayerInfo.add(getPlayerInfo(typeListItor.next()));
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
        return PlayerInfo;
    }

    public static String getPlayerInfo(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String line = null;
        new ArrayList<String>();
        try {
            ps = conn
                    .prepareStatement("SELECT bans.id, bans.mod, users.id, users.name, bans.type, bans.reason, bans.length, bans.date FROM bans INNER JOIN users ON bans.mod = users.id WHERE (bans.id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                int bId = rs.getInt("bans.id");
                int tId = rs.getInt("bans.type");
                String mName = rs.getString("users.name");
                String date = rs.getObject("bans.date").toString();
                String reason = rs.getString("bans.reason");
                if (tId == 2) {
                    rs.getLong("bans.length");
                }
                line = bId + " - " + ArgProcessing.getBanTypeString(tId)
                        + " - " + mName + " - " + date + " - " + reason;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return line;
    }

    /**
     * Get ban id info
     * 
     * @param id ban id
     * @return
     */
    public static Map<String, String> getBanIdInfo(int id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> BanId = new HashMap<String, String>();
        int pId = 0;
        try {
            ps = conn
                    .prepareStatement("SELECT bans.player_id, bans.id, bans.mod, users.id, users.name, bans.type, bans.reason, bans.length, bans.date FROM bans INNER JOIN users ON bans.mod = users.id WHERE (bans.id = ?);");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                pId = rs.getInt("bans.player_id");
                int tId = rs.getInt("bans.type");
                String mName = rs.getString("users.name");
                String date = rs.getObject("bans.date").toString();
                String reason = rs.getString("bans.reason");
                Long length = null;
                if (tId == 2) {
                    length = rs.getLong("bans.length");
                    BanId.put("length", Long.toString(length));
                }
                BanId.put("type", ArgProcessing.getBanTypeString(tId));
                BanId.put("mod", mName);
                BanId.put("date", date);
                BanId.put("reason", reason);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ps = conn
                    .prepareStatement("SELECT `name` FROM users WHERE (`id` = ?);");
            ps.setInt(1, pId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                BanId.put("name", name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BanId;
    }

    /**
     * Gets last ban id.
     * 
     * @return lastBanId
     */
    public static int getLastBanId() {
        return lastBanId;
    }
}
