package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.search.SearchMethods;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SearchCommand implements CommandExecutor {

    private SeruBans plugin;

    public SearchCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("search")) {
            boolean idB = false;
            boolean playerB = false;
            boolean typeB = false;
            int id = 0;
            String player = null;
            String type = null;
            if (args.length < 1) {
                return false;
            }
            if (args.length > 3) {
                return false;
            }
            for (String s : args) {
                if (s.startsWith("i:") || s.startsWith("id:")) {
                    String idString = s.replaceFirst("i:", "");
                    try {
                        id = Integer.parseInt(idString);
                    } catch (NumberFormatException ex) {
                        // Item was not an int, do nothing
                        sender.sendMessage(ChatColor.RED
                                + "Id must be a number!");
                        return true;
                    }
                    idB = true;
                    continue;
                }
                if (s.startsWith("p:")) {
                    player = s.replaceFirst("p:", "");
                    playerB = true;
                    continue;
                }
                if (s.startsWith("t:")) {
                    type = s.replaceFirst("t:", "");
                    typeB = true;
                    continue;
                }
            }
            if (playerB && !typeB && !idB) {
                plugin.printDebug(sender.getName() + "is searching player "
                        + player);
                SearchMethods.searchPlayer(player, sender);
                return true;
            } else if (playerB && typeB && !idB) {
                plugin.printDebug(sender.getName() + " is searching " + type
                        + " player " + player);
                int typeInt = 0;
                if (type.equalsIgnoreCase("temp")
                        || type.equalsIgnoreCase("tempban"))
                    typeInt = SeruBans.TEMPBAN;
                if (type.equalsIgnoreCase("ban")
                        || type.equalsIgnoreCase("bans"))
                    typeInt = SeruBans.BAN;
                if (type.equalsIgnoreCase("warn")
                        || type.equalsIgnoreCase("warning"))
                    typeInt = SeruBans.WARN;
                if (type.equalsIgnoreCase("kick")
                        || type.equalsIgnoreCase("kicks"))
                    typeInt = SeruBans.KICK;
                else {
                    sender.sendMessage(ChatColor.RED + "Type not recognized!");
                    sender.sendMessage(ChatColor.RED + "Try: "
                            + ChatColor.YELLOW + "tempban" + ChatColor.RED
                            + ", " + ChatColor.YELLOW + "ban" + ChatColor.RED
                            + ", " + ChatColor.YELLOW + "warning"
                            + ChatColor.RED + ", or " + ChatColor.YELLOW
                            + "kicks");
                    return true;
                }
                SearchMethods.searchType(player, typeInt, sender);
                return true;
            } else if (idB && !typeB && !playerB) {
                SearchMethods.searchId(id, sender);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
