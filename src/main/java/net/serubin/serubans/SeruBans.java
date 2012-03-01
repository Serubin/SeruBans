package net.serubin.serubans;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.serubin.serubans.commands.BanCommand;
import net.serubin.serubans.commands.DebugCommand;
import net.serubin.serubans.commands.KickCommand;
import net.serubin.serubans.commands.TempBanCommand;
import net.serubin.serubans.commands.UnbanCommand;
import net.serubin.serubans.commands.WarnCommand;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.CheckPlayer;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SeruBans extends JavaPlugin {

    public static SeruBans plugin;
    public Logger log = Logger.getLogger("Minecraft");
    private static boolean debug;

    private static String name;
    private static String version;

    // defines config variables
    public static String BanMessage;
    public static String GlobalBanMessage;
    public static String TempBanMessage;
    public static String GlobalTempBanMessage;
    public static String KickMessage;
    public static String GlobalKickMessage;
    public static String WarnMessage;
    public static String WarnPlayerMessage;
    public static String UnBanMessage;

    // sql variables
    public static String username;
    public static String password;
    public static String database;
    public static String host;

    // Per command variables
    public static Object config;
    static ArgProcessing ap;
    static Server server = Bukkit.getServer();

    public void onDisable() {
        reloadConfig();
        saveConfig();
        log.info(name + " has been disabled");
    }

    public void onEnable() {

        version = this.getDescription().getVersion();
        name = this.getDescription().getName();

        log.info(name + " version " + version + " has started...");
        PluginManager pm = getServer().getPluginManager();
        getConfig().options().copyDefaults(true);
        saveConfig();
        // Ban messages
        BanMessage = getConfig().getString("SeruBans.messages.ban.BanMessage");
        GlobalBanMessage = getConfig().getString(
                "SeruBans.messages.ban.GlobalBanMessage");
        TempBanMessage = getConfig().getString(
                "SeruBans.messages.tempban.TempBanMessage");
        GlobalTempBanMessage = getConfig().getString(
                "SeruBans.messages.tempban.GlobalTempBanMessage");
        // kick messages
        KickMessage = getConfig().getString(
                "SeruBans.messages.kick.KickMessage");
        GlobalKickMessage = getConfig().getString(
                "SeruBans.messages.kick.GlobalKickMessage");
        // warn message
        WarnMessage = getConfig().getString(
                "SeruBans.messages.warn.WarnMessage");
        WarnPlayerMessage = getConfig().getString(
                "SeruBans.messages.warn.WarnPlayerMessage");
        UnBanMessage = getConfig().getString("SeruBans.messages.UnBanMessage");
        // MySql
        host = getConfig().getString("SeruBans.database.host");
        username = getConfig().getString("SeruBans.database.username");
        password = getConfig().getString("SeruBans.database.password");
        database = getConfig().getString("SeruBans.database.database");
        // Other
        debug = getConfig().getBoolean("SeruBans.debug");

        // Add Classes
        BanCommand Ban = new BanCommand(BanMessage, GlobalBanMessage, name,
                plugin);
        TempBanCommand TempBan = new TempBanCommand(TempBanMessage,
                GlobalTempBanMessage, name, plugin);
        KickCommand Kick = new KickCommand(KickMessage, GlobalKickMessage,
                name, plugin);
        WarnCommand Warn = new WarnCommand(WarnMessage, WarnPlayerMessage,
                name, plugin);
        UnbanCommand Unban = new UnbanCommand(plugin);
        MySqlDatabase sqldb = new MySqlDatabase(host, username, password,
                database, plugin);
        CheckPlayer CheckPlayer = new CheckPlayer();
        DebugCommand DebugC = new DebugCommand(plugin);
        // init commands
        getCommand("ban").setExecutor(Ban);
        // getCommand("tempban").setExecutor(TempBan);
        getCommand("kick").setExecutor(Kick);
        getCommand("warn").setExecutor(Warn);
        getCommand("unban").setExecutor(Unban);
        getCommand("sbuser").setExecutor(DebugC);

        // create SQL Connection
        MySqlDatabase.startSQL();

        // Create listener
        getServer().getPluginManager().registerEvents(
                new SeruBansPlayerListener(plugin, BanMessage), this);
    }

    public static void printInfo(String line) {
        ArgProcessing.GetColor(line);
        System.out.println("[SeruBans] " + line);
    }

    public static void printDebug(String line) {
        if (debug) {
            ArgProcessing.GetColor(line);
            System.out.println("[SeruBans]DEBUG: " + line);
        }
    }

    public static void printServer(String line) {
        Player[] players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            if (player.hasPermission("serubans.broadcast") || player.isOp()) {
                player.sendMessage(ArgProcessing.GetColor(line));
            }
        }
    }

    public static void printError(String line) {
        System.out.println("[ERROR] [SeruBans] " + line);
    }

    public static void printWarning(String line) {
        System.out.println("[warning] [SeruBans] " + line);
    }

}
