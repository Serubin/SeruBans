package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

	ArgProcessing ap;
	OfflinePlayer offPlayer;
	Server server = Bukkit.getServer();
	Player victim;
	String p;
	String mod;
	String reason;
	private String BanMessage;
	private String GlobalBanMessage;
	private String name;
	private SeruBans plugin;

	public BanCommand(String BanMessage, String GlobalBanMessage, String name,
			SeruBans plugin) {
		// TODO Auto-generated constructor stub
		this.BanMessage = BanMessage;
		this.GlobalBanMessage = GlobalBanMessage;
		this.name = name;
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		// TODO Auto-generated method stub

		if (commandLabel.equalsIgnoreCase("ban")) {
			Player player = (Player) sender;
			if (args.length == 0) {
				return false;
			} else if (args.length == 1) {
				reason = "undefined";
			}
			mod = player.getName();
			p = args[0];
			// processes kick message
			BanMessage = BanMessage.replaceAll("%victim%", p);
			BanMessage = BanMessage.replaceAll("%reason%", reason);
			BanMessage = BanMessage.replaceAll("%kicker%", mod);
			// processes global message
			GlobalBanMessage = GlobalBanMessage.replaceAll("%victim%", p);
			GlobalBanMessage = GlobalBanMessage.replaceAll("%reason%", reason);
			GlobalBanMessage = GlobalBanMessage.replaceAll("%kicker%", mod);

			// finds victim
			victim = server.getPlayer(args[0]);
			if (victim != null) {
				// kicks and broadcasts message
				server.broadcastMessage(GlobalBanMessage);

				plugin.log.info("[" + name + "]:" + mod + " banned "
						+ victim.toString() + " for " + reason);

				victim.kickPlayer(BanMessage);
				// adds player to db
				return true;
			} else {
				victim = offPlayer.getPlayer();
				if (victim != null) {
					// broadcasts message
					server.broadcastMessage(GlobalBanMessage);

					plugin.log.info("[" + name + "]:" + mod + " banned "
							+ victim.toString() + " for " + reason);

					return true;
				} else {
					player.sendMessage("This Player was not found!");
					return true;
				}
			}

		}
		return false;
	}

}
