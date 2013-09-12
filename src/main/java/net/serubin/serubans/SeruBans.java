package net.serubin.serubans;

import java.util.logging.Logger;

import net.serubin.serubans.commands.BanCommand;
import net.serubin.serubans.commands.CheckBanCommand;
import net.serubin.serubans.commands.SeruBansCommand;
import net.serubin.serubans.commands.KickCommand;
import net.serubin.serubans.commands.SearchCommand;
import net.serubin.serubans.commands.TempBanCommand;
import net.serubin.serubans.commands.UnbanCommand;
import net.serubin.serubans.commands.UpdateCommand;
import net.serubin.serubans.commands.WarnCommand;
import net.serubin.serubans.search.DisplayManager;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class SeruBans extends JavaPlugin {
    /*
     * Base class of SeruBans
     * 
     * By Serubin323, Solomon Rubin
     */

    public SeruBans plugin;
    public Logger log = Logger.getLogger("Minecraft");
    private static boolean debug;

    private static String name;
    private static String version;
    public static SeruBans self = null;

    /*
     * Class Short Cuts
     */

    MySqlDatabase db;
    DisplayManager dm = null;

    /*
     * defines config variables
     */

    public static String BanMessage;
    public static String GlobalBanMessage;
    public static String TempBanMessage;
    public static String GlobalTempBanMessage;
    public static String KickMessage;
    public static String GlobalKickMessage;
    public static String WarnMessage;
    public static String WarnPlayerMessage;
    public static String UnBanMessage;

    /*
     * sql variables
     */

    public static String username;
    public static String password;
    public static String database;
    public static String host;

    /*
     * Ban types
     */

    public static final int BAN = 1;
    public static final int TEMPBAN = 2;
    public static final int KICK = 3;
    public static final int WARN = 4;
    public static final int UNBAN = 11;
    public static final int UNTEMPBAN = 12;

    /*
     * perms
     */

    public static final String HELPPERM = "serubans.help";
    public static final String BANPERM = "serubans.ban";
    public static final String TEMPBANPERM = "serubans.tempban";
    public static final String KICKPERM = "serubans.kick";
    public static final String WARNPERM = "serubans.warn";
    public static final String UNBANPERM = "serubans.unban";
    public static final String CHECKBANPERM = "serubans.checkban";
    public static final String UPDATEPERM = "serubans.update";
    public static final String SEARCHPERM = "serubans.search";
    public static final String DEBUGPERM = "serubans.debug";
    public static final String BROADCASTPERM = "serubans.broadcast.normal";
    public static final String BROADCASTPERMSILENT = "serubans.broadcast";

    /*
     * other, final
     */

    public static final int SHOW = 0;
    public static final int HIDE = 1;

    BukkitTask checkTempBansTask;
    BukkitTask dbKeepAliveTask;

    public void onDisable() {
        reloadConfig();
        saveConfig();
        getServer().getScheduler().cancelTask(checkTempBansTask.getTaskId());
        log.info(name + " has been disabled");

    }

    public void onEnable() {

        version = this.getDescription().getVersion();
        name = this.getDescription().getName();
        self = this;
        log.info(name + " version " + version + " has started...");
        PluginManager pm = getServer().getPluginManager();
        getConfig().options().copyDefaults(true);
        saveConfig();

        /*
         * Ban messages
         */

        BanMessage = getConfig().getString("SeruBans.messages.ban.BanMessage");
        GlobalBanMessage = getConfig().getString(
                "SeruBans.messages.ban.GlobalBanMessage");
        TempBanMessage = getConfig().getString(
                "SeruBans.messages.tempban.TempBanMessage");
        GlobalTempBanMessage = getConfig().getString(
                "SeruBans.messages.tempban.GlobalTempBanMessage");
        /*
         * kick messages
         */

        KickMessage = getConfig().getString(
                "SeruBans.messages.kick.KickMessage");
        GlobalKickMessage = getConfig().getString(
                "SeruBans.messages.kick.GlobalKickMessage");
        /*
         * warn message
         */

        WarnMessage = getConfig().getString(
                "SeruBans.messages.warn.WarnMessage");
        WarnPlayerMessage = getConfig().getString(
                "SeruBans.messages.warn.WarnPlayerMessage");
        UnBanMessage = getConfig().getString("SeruBans.messages.UnBanMessage");

        /*
         * MySql
         */

        host = getConfig().getString("SeruBans.database.host");
        username = getConfig().getString("SeruBans.database.username");
        password = getConfig().getString("SeruBans.database.password");
        database = getConfig().getString("SeruBans.database.database");

        debug = getConfig().getBoolean("SeruBans.debug");

        /*
         * Add Classes
         */

        BanCommand Ban = new BanCommand(BanMessage, GlobalBanMessage, name,
                this);
        TempBanCommand TempBan = new TempBanCommand(TempBanMessage,
                GlobalTempBanMessage, name, this);
        KickCommand Kick = new KickCommand(KickMessage, GlobalKickMessage,
                name, this);
        WarnCommand Warn = new WarnCommand(WarnMessage, WarnPlayerMessage,
                name, this);
        UnbanCommand Unban = new UnbanCommand(this);
        MySqlDatabase sqldb = new MySqlDatabase(host, username, password,
                database, this);
        SeruBansCommand DebugC = new SeruBansCommand(this);
        CheckBanCommand CheckBan = new CheckBanCommand(this);
        UnTempbanThread UnTempanThread = new UnTempbanThread(this);
        SearchCommand Search = new SearchCommand(this);
        UpdateCommand Update = new UpdateCommand(this);

        /*
         * init commands
         */

        getCommand("ban").setExecutor(Ban);
        getCommand("tempban").setExecutor(TempBan);
        getCommand("kick").setExecutor(Kick);
        getCommand("warn").setExecutor(Warn);
        getCommand("unban").setExecutor(Unban);
        getCommand("checkban").setExecutor(CheckBan);
        getCommand("bsearch").setExecutor(Search);
        getCommand("bupdate").setExecutor(Update);
        getCommand("serubans").setExecutor(DebugC);

        MySqlDatabase.startSQL();

        /*
         * Create listener
         */

        pm.registerEvents(new SeruBansPlayerListener(this, BanMessage,
                TempBanMessage), this);
        /*
         * Create Thread
         */

        checkTempBansTask = getServer().getScheduler().runTaskTimerAsynchronously(this,
                UnTempanThread, 1200, 1200);
        dbKeepAliveTask = getServer().getScheduler()
                .runTaskTimerAsynchronously(this, sqldb, 5800, 5800);

    }

    public static void printInfo(String line) {
        self.log.info("[SeruBans] " + line);
    }

    public void printDebug(String line) {
        if (debug) {
            log.info("[SeruBans] DEBUG: " + line);
        }
    }

    /**
     * Prints message to player when called. Will not print message to a player
     * without them having permissions. If the silent argument is true, it will
     * only be broadcasted to those with the silent permission.
     * 
     * @param line Message to be broadcasted
     * @param silent
     */
    public static void printServer(String line, boolean silent) {
        Player[] players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            if ((player.hasPermission(BROADCASTPERM) || player
                    .hasPermission(BROADCASTPERMSILENT)) && !silent) {
                player.sendMessage(ArgProcessing.GetColor(line));
            }
            if (player.hasPermission(BROADCASTPERMSILENT) && silent) {
                player.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA
                        + "Silent" + ChatColor.DARK_AQUA + "] "
                        + ChatColor.WHITE + ArgProcessing.GetColor(line));
            }

        }
    }

    public static void printError(String line) {
        self.log.severe("[SeruBans] " + line);
    }

    public static void printWarning(String line) {
        self.log.warning("[SeruBans] " + line);
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission) || sender.isOp()
                || (!(sender instanceof Player))) {
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission!");
            return false;
        }
    }

    public static String getVersion() {
        return version;
    }
    /*
     * API GETTER / SETTER
     */

}
