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
			// processes kick message
			// finds victim
			victim = server.getPlayer(args[0]);
			
			BanMessage = BanMessage.replaceAll("%reason%", reason);
			BanMessage = BanMessage.replaceAll("%kicker%", mod);
			
			// processes global message

			String line = "";
			if (victim != null) {
				// kicks and broadcasts message
				GlobalMessage(GlobalBanMessage, reason, mod, victim);
				SeruBans.printServer(line);

				SeruBans.printInfo(mod + " banned "
						+ victim.getName() + " for " + reason);

				victim.kickPlayer(SeruBans.GetColor(BanMessage));
				// adds player to db
				return true;
			} else {
				try{
				victim = offPlayer.getPlayer();
				} catch(NullPointerException NPE) {
					victim = null;
				}
				
				if (victim != null) {
					// broadcasts message
					GlobalMessage(GlobalBanMessage, reason, mod, victim);
					SeruBans.printServer(line);

					SeruBans.printInfo(mod + " banned "
							+ victim.getName() + " for " + reason);

					return true;
				} else {
					player.sendMessage("This Player was not found!");
					return true;
				}
			}

		}
		return false;
	}
	public String GlobalMessage(String line, String reason, String mod, Player victim){
		line = line.replaceAll("%victim%", victim.getName());
		line = line.replaceAll("%reason%", reason);
		line = line.replaceAll("%kicker%", mod);
		return line;
	}

}
