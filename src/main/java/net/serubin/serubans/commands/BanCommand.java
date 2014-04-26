package net.serubin.serubans.commands;

import java.util.UUID;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.BansDataProvider;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

	private SeruBans plugin;
	private BansDataProvider db;

	public BanCommand(SeruBans plugin, BansDataProvider db) {
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
		boolean silent = false;
		int display = SeruBans.SHOW;
		if (commandLabel.equalsIgnoreCase("ban")) {
			if (!plugin.hasPermission(sender, SeruBans.BANPERM)) {
				return true;
			}
			// Checks for invalid arguments
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
			if (args.length > 1) {
				reason = plugin.text().reasonArgs(args);
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

			// prints to players on server with perms
			plugin.printServer(
					plugin.text().GlobalMessage(plugin.GlobalBanMessage,
							reason, mod.getName(), args[0]), silent);
			// Adds ban
			db.addBan(uuid, SeruBans.BAN, 0, mod.getUniqueId(), reason, display);

			// logs it
			plugin.printInfo(mod + " banned " + args[0] + " for " + reason);

			// sends kicker ban id
			sender.sendMessage(ChatColor.GOLD + "Ban Id: " + ChatColor.YELLOW
					+ Integer.toString(db.getLastBanId()));
			// kicks player
			if (online) {
				victim.kickPlayer(plugin.text().GetColor(
						plugin.text().PlayerMessage(plugin.BanMessage, reason,
								mod.getName())));
			}
			return true;
		}
		return false;
	}
}
