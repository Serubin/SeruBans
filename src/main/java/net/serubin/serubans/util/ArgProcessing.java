package net.serubin.serubans.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArgProcessing {

	public String reasonArgs(String[] args) {
        StringBuilder reason = new StringBuilder();

        // combine args into a string
        for (String s: args) {
             reason.append(" " + s);
        }

        // return string
        return reason.toString();
    }
	
}
