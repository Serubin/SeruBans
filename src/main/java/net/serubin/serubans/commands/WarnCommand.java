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
	String reason = "";
	private String WarnMessage;
	private String name;
	private SeruBans plugin;
	private String WarnPlayerMessage;

	public WarnCommand(String WarnMessage, String WarnPlayerMessage, String name, SeruBans plugin) {
		// TODO Auto-generated constructor stub
		this.WarnMessage = WarnMessage;
		this.WarnPlayerMessage = WarnPlayerMessage;
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
			else if(args.length > 1){
				reason = ArgProcessing.reasonArgs(args);
			}
			mod = player.getName();
			//processes Warn message
			
			WarnPlayerMessage = WarnPlayerMessage.replaceAll("%reason%", reason);
			WarnPlayerMessage = WarnPlayerMessage.replaceAll("%kicker%", mod);
			
			String line = "";
			//finds victim
			victim = server.getPlayer(args[0]);
			if(victim != null){
				//Warns and broadcasts message
				GlobalMessage(WarnMessage, reason, mod, victim);
				SeruBans.printServer(line);
				SeruBans.printInfo(mod + " warned " + victim.getName() + " for " + reason);
				SeruBans.printInfo(WarnPlayerMessage);
				SeruBans.printInfo(WarnMessage);
				victim.sendMessage(SeruBans.GetColor(WarnPlayerMessage));
				victim.sendMessage(reason);
				
				//adds player to db
				return true;
			}else{
				try{
					victim = offPlayer.getPlayer();
					} catch(NullPointerException NPE) {
						victim = null;
					}
				if(victim !=null){
					//broadcasts message
					GlobalMessage(WarnMessage, reason, mod, victim);
					SeruBans.printServer(line);
					SeruBans.printInfo(mod + " warned " + victim.getName() + " for " + reason);;
					return true;
				}else{
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
