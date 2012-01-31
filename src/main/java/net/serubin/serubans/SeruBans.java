package net.serubin.serubans;

import java.util.logging.Logger;

import net.serubin.serubans.commands.BanCommand;
import net.serubin.serubans.commands.KickCommand;
import net.serubin.serubans.commands.WarnCommand;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SeruBans extends JavaPlugin{

	
	public static SeruBans plugin;
	public Logger log = Logger.getLogger("Minecraft");
	private String username;
	private String password;
	private String database;
	private static String name;
	private static String version;
	//defines config variables
	public static String BanMessage;
	public static String GlobalBanMessage;
	public static String TempBanMessage;
	public static String KickMessage;
	public static String GlobalKickMessage;
	public static String WarnMessage;
	public static String UnBanMessage;
	//Per command variables
	public static Object config;
	
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
		//Ban messages
		BanMessage = getConfig().getString("SeruBans.messages.ban.BanMessage");
		GlobalBanMessage = getConfig().getString("SeruBans.messages.ban.GlobalBanMessage");
		TempBanMessage = getConfig().getString("SeruBans.messages.TempBanMessage");
		//kick messages
		KickMessage = getConfig().getString("SeruBans.messages.kick.KickMessage");
		GlobalKickMessage = getConfig().getString("SeruBans.messages.kick.GlobalKickMessage");
		//warn message
		WarnMessage = getConfig().getString("SeruBans.messages.warn.WarnMessage");
		UnBanMessage = getConfig().getString("SeruBans.messages.UnBanMessage");
		//MySql
		username = getConfig().getString("SeruBans.MySql.username");
		password = getConfig().getString("SeruBans.MySql.password");
		database = getConfig().getString("SeruBans.MySql.database");
		
		
		
		BanCommand Ban = new BanCommand(BanMessage, GlobalBanMessage, name, plugin);
		KickCommand Kick = new KickCommand(KickMessage, GlobalKickMessage, name, plugin);
		WarnCommand Warn = new WarnCommand(WarnMessage, name, plugin);
		MySqlDatabase sqldb = new MySqlDatabase(username, password, database, plugin);
		
		getCommand("ban").setExecutor(Ban);
		getCommand("kick").setExecutor(Kick);
		getCommand("warn").setExecutor(Warn);
	}
	
	public static void printInfo(String line){
		System.out.println("[SeruBans] " + line);
	}
	public static void printError(String line){
		System.out.println("[ERROR] [SeruBans] " + line);
	}
	public static void printWarning(String line){
		System.out.println("[warning] [SeruBans] " + line);
	}
		
}

