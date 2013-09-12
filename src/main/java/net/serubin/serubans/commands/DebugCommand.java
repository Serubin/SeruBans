package net.serubin.serubans.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.BanInfo;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DebugCommand implements CommandExecutor {

    private SeruBans plugin;

    public DebugCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("serubans")) {
            if (sender.hasPermission(SeruBans.DEBUGPERM) || sender.isOp()
                    || (!(sender instanceof Player))) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.GOLD + "Serubans "
                            + ChatColor.YELLOW + " version "
                            + SeruBans.getVersion());
                    sender.sendMessage(ChatColor.YELLOW + "Type "
                            + ChatColor.GOLD + "'/serubans ?' "
                            + ChatColor.YELLOW + "for more debug options");
                    return true;
                }
                if (args[0].equalsIgnoreCase("?")) {
                    sender.sendMessage(ChatColor.YELLOW + "Use "
                            + ChatColor.GOLD + "'/serubans -option' "
                            + ChatColor.YELLOW + "for debug functionality.");
                    sender.sendMessage(ChatColor.YELLOW + "Options:");
                    sender.sendMessage(ChatColor.YELLOW
                            + "-e    export bans to minecraft bans files");
                    return true;
                }
                if (args[0].startsWith("-")) {
                    if (args[0].contains("e")) {
                        exportToBannedPlayersTxtFile();
                    }
                    return true;
                }
                return false;
            } else {
                sender.sendMessage(ChatColor.RED
                        + "You do not have permission!");
                return true;
            }
        }
        return false;
    }

    public void exportToBannedPlayersTxtFile() {
        List<BanInfo> permBans = MySqlDatabase.getPermBans();
        List<BanInfo> tempBans = MySqlDatabase.getTempBans();
        if ((permBans == null) && (tempBans == null)) {
            plugin.log.info("There are no bans to export.");
            return;
        }

        try {
            BufferedWriter banlist = new BufferedWriter(
                    new FileWriter("banned-players.txt", true));

            // write perm bans
            for (BanInfo permBan : permBans) {
                banlist.write(permBan.getPlayerName());
                banlist.newLine();
            }

            // write temp bans
            for (BanInfo tempBan : tempBans) {
                banlist.write(tempBan.getPlayerName());
                banlist.newLine();
            }

            banlist.close();
        } catch (IOException e) {
            plugin.log.severe("Bans file could not be written!");
        }
    }
}
