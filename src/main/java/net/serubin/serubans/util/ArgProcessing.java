package net.serubin.serubans.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.serubin.serubans.SeruBans;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class ArgProcessing {
	
	static OfflinePlayer offPlayer;
	
	public static Player getVictim(String[] args){
		Server server = Bukkit.getServer();
		Player victim = server.getPlayer(args[0]);
		
		if(victim != null){
			//if victim is on return victim
			return victim;
		}else{
			//if not check offline players (try catch)
			try{
				victim = offPlayer.getPlayer();
				} catch(NullPointerException NPE) {
					//if not found victim is null
					victim = null;
					
				}
			return victim;
		}
	}
	public static String reasonArgs(String[] args) {
        StringBuilder reasonRaw = new StringBuilder();
        String reason;
        // combine args into a string
        for (String s: args) {
             reasonRaw.append(" " + s);
        }
       reason = reasonRaw.toString().replaceFirst(" " + args[0], "");
       if(reason.startsWith("-h")){
    	   reason.replaceFirst("-h", "");
       }
        // return string
        return  reason;
    }
	
	public static String GetColor(String line) {
		line = line;
		line = line.replace("&0", "§0");
		line = line.replace("&1", "§1");
		line = line.replace("&2", "§2");
		line = line.replace("&3", "§3");
		line = line.replace("&4", "§4");
		line = line.replace("&5", "§5");
		line = line.replace("&6", "§6");
		line = line.replace("&7", "§7");
		line = line.replace("&8", "§8");
		line = line.replace("&9", "§9");
		line = line.replace("&a", "§a");
		line = line.replace("&b", "§b");
		line = line.replace("&c", "§c");
		line = line.replace("&d", "§d");
		line = line.replace("&e", "§e");
		line = line.replace("&f", "§f");
		return line;
	}
	
	public static String GlobalMessage(String line, String reason, String mod, Player victim){
		line = line.replaceAll("%victim%", victim.getName());
		line = line.replaceAll("%reason%", reason);
		line = line.replaceAll("%kicker%", mod);
		return line;
	}
	public static String PlayerMessage(String line, String reason, String mod){
		
		line = line.replaceAll("%reason%", reason);
		line = line.replaceAll("%kicker%", mod);
		return line;
	}
	 public static java.sql.Date getDateTime() {
	        Date date = new Date();
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String today = dateFormat.format(date);
	        java.sql.Date dt = java.sql.Date.valueOf(new String(today));
	        return dt;
	    }
}
