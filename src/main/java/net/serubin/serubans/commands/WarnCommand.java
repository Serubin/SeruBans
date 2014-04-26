package net.serubin.serubans.commands;

import java.util.UUID;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.BansDataProvider;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

	private SeruBans plugin;
	private BansDataProvider db;

	public WarnCommand(SeruBans plugin, BansDataProvider db) {
		this.plugin = plugin;
		this.db = db;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player victim;
		UUID uuid;
		Player mod = (Player) sender;

		String reason = "";
		int display = SeruBans.SHOW;
		boolean silent = false;

		if (commandLabel.equalsIgnoreCase("warn")) {
			if (!plugin.hasPermission(sender, SeruBans.WARNPERM)) {
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

			// adds ban to database
			db.addBan(uuid, SeruBans.WARN, 0, mod.getUniqueId(), reason,
					display);
			plugin.printServer(
					plugin.text().GlobalMessage(plugin.WarnMessage, reason,
							mod.getName(), args[0]), silent);
			// logs i t
			plugin.log.info(mod + " warned " + args[0] + " for " + reason);
			// tells victim
			if (online) {
				victim.sendMessage(plugin.text().GetColor(
						plugin.text().PlayerMessage(plugin.WarnPlayerMessage,
								reason, mod.getName())));
			} else {
				db.addWarn(db.getPlayer(uuid), db.getLastBanId());
			}
			// sends kicker ban id
			sender.sendMessage(ChatColor.GOLD + "Ban Id: " + ChatColor.YELLOW
					+ Integer.toString(db.getLastBanId()));
			return true;
		}
		return false;
	}

}
