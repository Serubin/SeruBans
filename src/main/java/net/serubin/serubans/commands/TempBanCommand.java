package net.serubin.serubans.commands;

import java.util.UUID;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.BansDataProvider;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TempBanCommand implements CommandExecutor {

	private SeruBans plugin;
	private BansDataProvider db;

	public TempBanCommand(SeruBans plugin, BansDataProvider db) {
		this.plugin = plugin;
		this.db = db;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player victim;
		UUID uuid;
		Player mod = (Player) sender;

		String reason;
		int display = SeruBans.SHOW;
		boolean silent = false;

		if (commandLabel.equalsIgnoreCase("tempban")) {
			if (!plugin.hasPermission(sender, SeruBans.TEMPBANPERM)) {
				return true;
			}
			// Checks for invalid arugments
			if (args.length == 0
					|| (args.length == 1 && args[0].startsWith("-"))) {
				return false;

				// Checks for options
			} else {

				silent = false;
				display = SeruBans.SHOW;

				if (args[0].startsWith("-")) {
					if (args[0].contains("s")) {
						silent = true;
					}
					if (args[0].contains("h")) {
						display = SeruBans.HIDE;
					}
					args = plugin.text().stripFirstArg(args);
				}
			}

			// Checks for user defined reason.
			if (args.length > 3) {
				reason = plugin.text().reasonArgsTB(args);
			} else {
				reason = "Undefined";
			}

			victim = plugin.getServer().getPlayer(args[0]);
			boolean online = false;

			// Checks to see if player is online
			if (victim != null) {
				online = true;
				args[0] = victim.getName();
				uuid = victim.getUniqueId();
			} else {
				// Gets offline UUID and checks for null
				uuid = plugin.getServer().getOfflinePlayer(args[0])
						.getUniqueId();
				if (uuid == null) {
					sender.sendMessage(ChatColor.RED + args[0]
							+ " is not a player!");
					return true;
				}
			}

			// Checks to see if player is already banned
			if (db.getPlayerStatus(uuid)) {
				sender.sendMessage(ChatColor.GOLD + args[0] + ChatColor.RED
						+ " is already banned!");
				return true;
			}

			// Handles ban length
			long length = plugin.text().parseTimeSpec(args[1], args[2]);
			plugin.printDebug("Ban length " + Long.toString(length));

			if (length == 0)
				return false;
			length = System.currentTimeMillis() / 1000 + length;
			String date = plugin.text().getStringDate(length);

			// prints to players on server with perms
			plugin.printServer(
					plugin.text().GlobalTempBanMessage(
							plugin.GlobalTempBanMessage, reason, mod.getName(),
							args[0], date), silent);

			// Adds ban to database
			db.addBan(uuid, SeruBans.TEMPBAN, length, mod.getUniqueId(),
					reason, display);

			// logs it
			plugin.log.info(mod + " banned " + args[0] + " for " + reason);

			// sends kicker ban id
			sender.sendMessage(ChatColor.GOLD + "Ban Id: " + ChatColor.YELLOW
					+ Integer.toString(db.getLastBanId()));

			// kicks player of the server
			if (online) {
				victim.kickPlayer(plugin.text().GetColor(
						plugin.text().PlayerTempBanMessage(
								plugin.TempBanMessage, reason, mod.getName(),
								date)));
			}
			return true;

		}
		return false;
	}

}
