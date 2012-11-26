package net.serubin.serubans.search;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisplayManager {

    /**
     * Creates a title to be sent to player
     * <p/>
     * Does not send title to player!
     * 
     * @param name
     *            Line title
     * @return full line
     */
    public String createTitle(String name) {

        String line = "&8---------------------&6" + name + "&8";
        StringBuilder newLine = new StringBuilder();
        newLine.append(line);
        while (newLine.length() < 56) {
            newLine.append("-");
        }

        return ArgProcessing.GetColor(newLine.toString());
    }

    /**
     * Sends player line
     * <p/>
     * If line is to long, function will cut it to two lines
     * 
     * @param sender
     *            CommandSender
     * @param line
     *            Line to be sent
     */
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
}