package net.serubin.serubans.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.HashMaps;

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
                    return false;
                }
                if (args[0].startsWith("-")) {
                    if (args[0].contains("a") && !args[0].contains("api")) {
                        sender.sendMessage("Players: "
                                + HashMaps.getFullPlayerList());
                        sender.sendMessage("Banned Players: "
                                + HashMaps.getFullBannedPlayers());
                        sender.sendMessage("TempBan: "
                                + HashMaps.getFullTempBannedTime());
                        return true;
                    }
                    if (args[0].contains("p") && !args[0].contains("api")) {
                        sender.sendMessage("Players: "
                                + HashMaps.getFullPlayerList());
                    }
                    if (args[0].contains("b")) {
                        sender.sendMessage("Banned Players: "
                                + HashMaps.getFullBannedPlayers());
                    }
                    if (args[0].contains("t")) {
                        sender.sendMessage("TempBan: "
                                + HashMaps.getFullTempBannedTime());
                    }
                    if (args[0].contains("e")) {
                        List<String> ban = HashMaps.getBannedForFile();
                        Iterator<String> iterator = ban.iterator();
                        try {
                            BufferedWriter banlist = new BufferedWriter(
                                    new FileWriter("banned-players.txt", true));

                            while (iterator.hasNext()) {
                                String player = iterator.next();
                                banlist.write(player);
                                banlist.newLine();
                            }
                            banlist.close();
                        } catch (IOException e) {
                            plugin.log.severe("File Could not be writen!");
                        }
                    }
                    return true;
                }
                return false;
            } else {
                sender.sendMessage(ChatColor.RED
                        + "You do not have permission!");
            }
        }
        return false;
    }
}
