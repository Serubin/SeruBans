package net.serubin.serubans.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArgProcessing {

	public static String reasonArgs(String[] args) {
        StringBuilder reason = new StringBuilder();

        // combine args into a string
        for (String s: args) {
             reason.append(" " + s);
        }
       
        // return string
        return  reason.toString().replaceFirst(" " + args[0], "");
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
}
