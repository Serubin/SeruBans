package net.serubin.serubans.search;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;

public class DisplayManager {

    private DroxPermsAPI perms = null;

    public String createTitle(String name) {

        String line = "&8---------------------&6" + getPlayerGroup(name) + "&8";
        StringBuilder newLine = new StringBuilder();
        newLine.append(line);
        while (newLine.length() < 56) {
            newLine.append("-");
        }

        return ArgProcessing.GetColor(newLine.toString());
    }

    public void sendLine(CommandSender sender, String line) {
        int len = 56;
        if (line.length() < len)
            sender.sendMessage(ArgProcessing.GetColor("&8|&7" + line));
        else {
            for (int i = 0; i < line.length(); i += len) {
                String str = (i + len > line.length() ? line.substring(i)
                        : line.substring(i, i + len));
                sender.sendMessage(ArgProcessing.GetColor("&8|&7 " + str));
            }
        }
    }

    public String getPlayerGroup(String player) {

        StringBuffer playerString = new StringBuffer();
        
        if (perms != null) {
            String group = perms.getPlayerGroup(player);
            String groupPrefix = perms.getGroupInfo(group, "prefix");
            String playerPrefix = perms.getPlayerInfo(player,
                    "prefix");
            if (playerPrefix != null) {
                playerPrefix = playerPrefix.replace("&", "\247");
            } else if (groupPrefix != null) {
                groupPrefix = groupPrefix.replace("&", "\247");
                playerString.append(groupPrefix);
            } else {
                playerString.append(ChatColor.WHITE);
            }
        }else{
            playerString.append(ChatColor.WHITE);
        }
        playerString.append(player);
        return playerString.toString();
    }
}