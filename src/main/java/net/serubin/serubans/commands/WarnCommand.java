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

public class WarnCommand implements CommandExecutor {

	ArgProcessing ap;
	OfflinePlayer offPlayer;
    Server server = Bukkit.getServer();
	Player victim;
	String mod;
	String reason;
	private String WarnMessage;
	private String name;
	private SeruBans plugin;

	public WarnCommand(String WarnMessage, String name, SeruBans plugin) {
		// TODO Auto-generated constructor stub
		this.WarnMessage = WarnMessage;
		this.name = name;
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel,
			String[] args) {
		// TODO Auto-generated method stub
		
		if (commandLabel.equalsIgnoreCase("Warn")) {
			Player player = (Player) sender;
			if(args.length == 0){
				return false;
			}
			else if(args.length == 1){
				reason = "undefined";
			}
			mod = player.getName();
			String p = args[0];
			//processes Warn message
			WarnMessage = WarnMessage.replaceAll("%victim%", p);
			WarnMessage = WarnMessage.replaceAll("%reason%", reason);
			WarnMessage = WarnMessage.replaceAll("%kicker%", mod);

			//finds victim
			victim = server.getPlayer(args[0]);
			if(victim != null){
				//Warns and broadcasts message
				server.broadcastMessage(WarnMessage);
				plugin.log.info("[" + name + "]:" + mod + " warned " + victim.toString() + " for " + reason);
				//adds player to db
				return true;
			}else{
				victim = offPlayer.getPlayer();
				if(victim !=null){
					//broadcasts message
					server.broadcastMessage(WarnMessage);
					plugin.log.info("[" + name + "]:" + mod + " warned " + victim.toString() + " for " + reason);
					
					return true;
				}else{
					player.sendMessage("This Player was not found!");
					return true;
				}
			}
			

		}
		
		return false;
	}

}
