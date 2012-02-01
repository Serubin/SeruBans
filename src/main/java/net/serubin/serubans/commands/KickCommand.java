package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

	ArgProcessing ap;
	OfflinePlayer offPlayer;
    Server server = Bukkit.getServer();
	String p;
	Player victim;
    String mod;
	String reason;
	private String KickMessage;
	private String GlobalKickMessage;
	private String name;
	private SeruBans plugin;

	public KickCommand(String KickMessage, String GlobalKickMessage, String name, SeruBans plugin) {
		// TODO Auto-generated constructor stub
		this.KickMessage = KickMessage;
		this.GlobalKickMessage = GlobalKickMessage;
		this.name = name;
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel,
			String[] args) {
		// TODO Auto-generated method stub
		if (commandLabel.equalsIgnoreCase("kick")) {
			Player player = (Player) sender;
			if(args.length == 0){
				return false;
			}
			else if(args.length == 1){
				reason = "undefined";
			}
			else if(args.length > 1){
				reason = ArgProcessing.reasonArgs(args);
			}
			mod = player.getName();

			//finds victim
			victim = server.getPlayer(args[0]);
			
			//processes kick message
			KickMessage = KickMessage.replaceAll("%reason%", reason);
			KickMessage = KickMessage.replaceAll("%kicker%", mod);
			String line = "";
			if(victim != null){
				//kicks and broadcasts message
				GlobalMessage(GlobalKickMessage, reason, mod, victim);
				SeruBans.printServer(line);
				SeruBans.printInfo( mod + " kicked " + victim.getName() + " for " + reason);
				victim.kickPlayer(SeruBans.GetColor(KickMessage));
				//adds player to db
				return true;
			}else{
					player.sendMessage(ChatColor.RED + "This Player was not found!");
					return true;
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
