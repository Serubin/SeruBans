package net.serubin.serubans;

import java.util.logging.Logger;

import net.serubin.serubans.commands.BanCommand;
import net.serubin.serubans.commands.CheckBanCommand;
import net.serubin.serubans.commands.KickCommand;
import net.serubin.serubans.commands.SearchCommand;
import net.serubin.serubans.commands.SeruBansCommand;
import net.serubin.serubans.commands.TempBanCommand;
import net.serubin.serubans.commands.UnbanCommand;
import net.serubin.serubans.commands.UpdateCommand;
import net.serubin.serubans.commands.WarnCommand;
import net.serubin.serubans.dataproviders.BansDataProvider;
import net.serubin.serubans.dataproviders.DataProviderTimers;
import net.serubin.serubans.dataproviders.MysqlBansDataProvider;
import net.serubin.serubans.util.TextProcessor;

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

	// TODO add reload config / database
	public SeruBans plugin;
	public Logger log = Logger.getLogger("Minecraft");
	private boolean debug;

	private String name;
	private String version;

	/*
	 * Class Short Cuts
	 */
	private BansDataProvider db = null;
	private TextProcessor text = null;

	/*
	 * defines config variables
	 */

	public String BanMessage;
	public String GlobalBanMessage;
	public String TempBanMessage;
	public String GlobalTempBanMessage;
	public String KickMessage;
	public String GlobalKickMessage;
	public String WarnMessage;
	public String WarnPlayerMessage;
	public String UnBanMessage;

	/*
	 * sql variables
	 */

	private String username;
	private String password;
	public String database;
	public String host;

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
		getServer().getScheduler().cancelTask(dbKeepAliveTask.getTaskId());
		log.info(name + " has been disabled");

	}

	public void onEnable() {

		version = this.getDescription().getVersion();
		name = this.getDescription().getName();

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

		// Creates database
		// TODO ADD FLAT FILE
		this.db = new MysqlBansDataProvider(host, username, password, database,
				this);
		this.text = new TextProcessor(this);

		UnTempbanThread UnTempanThread = new UnTempbanThread(this, db);

		BanCommand Ban = new BanCommand(this, db);
		TempBanCommand TempBan = new TempBanCommand(this, db);
		KickCommand Kick = new KickCommand(this, db);
		WarnCommand Warn = new WarnCommand(this, db);
		UnbanCommand Unban = new UnbanCommand(this, db);
		SeruBansCommand DebugC = new SeruBansCommand(this, db);
		CheckBanCommand CheckBan = new CheckBanCommand(this, db);
		SearchCommand Search = new SearchCommand(this, db);
		UpdateCommand Update = new UpdateCommand(this, db);

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

		/*
		 * Create listener
		 */
		pm.registerEvents(new SeruBansPlayerListener(this, this.db, BanMessage,
				TempBanMessage), this);

		/*
		 * Create Thread
		 */

		checkTempBansTask = getServer().getScheduler()
				.runTaskTimerAsynchronously(this, UnTempanThread, 1200, 1200);
		dbKeepAliveTask = getServer().getScheduler()
				.runTaskTimerAsynchronously(this, new DataProviderTimers(db),
						5800, 5800);

		this.printInfo("Plugin loaded...");
	}

	public void reload() {
		printInfo("Plugin reloading...");
		onDisable();
		onEnable();
	}

	public void printInfo(String line) {
		log.info("[SeruBans] " + line);
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
	 * @param line
	 *            Message to be broadcasted
	 * @param silent
	 */
	public void printServer(String line, boolean silent) {
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			if ((player.hasPermission(BROADCASTPERM) || player
					.hasPermission(BROADCASTPERMSILENT)) && !silent) {
				player.sendMessage(text.GetColor(line));
			}
			if (player.hasPermission(BROADCASTPERMSILENT) && silent) {
				player.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA
						+ "Silent" + ChatColor.DARK_AQUA + "] "
						+ ChatColor.WHITE + text.GetColor(line));
			}

		}
	}

	public void printError(String line) {
		log.severe("[SeruBans] " + line);
	}

	public void printWarning(String line) {
		log.warning("[SeruBans] " + line);
	}

	public boolean hasPermission(CommandSender sender, String permission) {
		if (sender.hasPermission(permission) || sender.isOp()
				|| (!(sender instanceof Player))) {
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "You do not have permission!");
			return false;
		}
	}

	public String getVersion() {
		return version;
	}

	public TextProcessor text() {
		return this.text;
	}
}
