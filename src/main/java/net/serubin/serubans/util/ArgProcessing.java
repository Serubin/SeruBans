package net.serubin.serubans.util;

import org.bukkit.Bukkit;

public class ArgProcessing {

	String reasonArgs(Integer index, String[] args) {
        StringBuilder reason = new StringBuilder();

        // combine args into a string
        for (int i = index; i < args.length; i++) {
             reason.append(" " + args[i]);
        }

        // return string
        return reason.toString();
    }
	
}
