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

public class SeruBansCommand implements CommandExecutor {

    private SeruBans plugin;

    public SeruBansCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("serubans")) {
            if (sender.hasPermission(SeruBans.DEBUGPERM) || sender.isOp()
                    || (!(sender instanceof Player))) {
                if (args.length == 0) {
                    boolean banPerm = false;
                    sender.sendMessage(ChatColor.GOLD + "Serubans "
                            + ChatColor.YELLOW + " version "
                            + SeruBans.getVersion());
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.BANPERM)) {
                        // Ban help
                        banPerm = true;
                        sender.sendMessage(ChatColor.GOLD
                                + "/ban [-options] <player> <reason>");
                        sender.sendMessage(ChatColor.GOLD
                                + "    Usage: "
                                + ChatColor.YELLOW
                                + "Used to ban players permanently from the server.");
                    }
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.TEMPBANPERM)) {
                        // Tempban help
                        banPerm = true;
                        sender.sendMessage(ChatColor.GOLD
                                + "/tempban [-options] <player> <time> <unit> <reason>");
                        sender.sendMessage(ChatColor.GOLD
                                + "    Usage: "
                                + ChatColor.YELLOW
                                + "Used to ban players temporarily, for a defined amount of time, from the server.");
                    }
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.KICKPERM)) {
                        // Kick help
                        banPerm = true;
                        sender.sendMessage(ChatColor.GOLD
                                + "/kick [-options] <player> <reason>");
                        sender.sendMessage(ChatColor.GOLD + "    Usage: "
                                + ChatColor.YELLOW
                                + "Used to kick players from the server.");
                    }
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.WARNPERM)) {
                        // Warn help
                        banPerm = true;
                        sender.sendMessage(ChatColor.GOLD
                                + "/warn [-options] <player> <reason>");
                        sender.sendMessage(ChatColor.GOLD + "    Usage: "
                                + ChatColor.YELLOW + "Used to warn a player.");
                        sender.sendMessage(ChatColor.GOLD
                                + "    Note: "
                                + ChatColor.YELLOW
                                + "When warning an offline player, they will be notified when they next login.");
                    }
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.UNBANPERM)) {
                        // Unban perm
                        banPerm = true;
                        sender.sendMessage(ChatColor.GOLD
                                + "/unban [-options] <player>");
                        sender.sendMessage(ChatColor.GOLD + "    Usage: "
                                + ChatColor.YELLOW + "Used to unban a player.");
                        sender.sendMessage(ChatColor.GOLD
                                + "    Note: "
                                + ChatColor.YELLOW
                                + "Spelling must be exact to the player's name. Also, -h option does not work with this command");
                    }
                    if (banPerm) {
                        sender.sendMessage(ChatColor.GOLD + "Options: ");
                        sender.sendMessage(ChatColor.GOLD
                                + "-s    "
                                + ChatColor.YELLOW
                                + "Hides the ban messages from the displaying to players.");
                        sender.sendMessage(ChatColor.GOLD + "-h    "
                                + ChatColor.YELLOW
                                + "Hides the ban from the ban list.");
                    }
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.CHECKBANPERM)) {
                        sender.sendMessage(ChatColor.GOLD
                                + "/checkban <player>");
                        sender.sendMessage(ChatColor.GOLD
                                + "    Usage: "
                                + ChatColor.YELLOW
                                + "Used to determine if a player is banned or not");

                    }
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.UPDATEPERM)) {

                    }
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.SEARCHPERM)) {

                    }
                    if (SeruBans.hasPermission((Player) sender,
                            SeruBans.DEBUGPERM)) {

                    }

                    sender.sendMessage(ChatColor.YELLOW + "Type "
                            + ChatColor.GOLD + "'/serubans debug' "
                            + ChatColor.YELLOW + "for more debug options");
                    return true;
                }
                if (args[0].equalsIgnoreCase("debug")) {
                    sender.sendMessage(ChatColor.YELLOW + "Use "
                            + ChatColor.GOLD + "'/serubans -option' "
                            + ChatColor.YELLOW + "for debug functionality.");
                    sender.sendMessage(ChatColor.YELLOW + "Options:");
                    sender.sendMessage(ChatColor.YELLOW
                            + "-a    prints full hashmaps lists");
                    sender.sendMessage(ChatColor.YELLOW
                            + "-p    prints player hashmaps lists");
                    sender.sendMessage(ChatColor.YELLOW
                            + "-i     prints id  hashmaps lists");
                    sender.sendMessage(ChatColor.YELLOW
                            + "-b    prints banned player hashmaps lists");
                    sender.sendMessage(ChatColor.YELLOW
                            + "-w    prints warns hashmaps lists");
                    sender.sendMessage(ChatColor.YELLOW
                            + "-e    export bans to minecraft bans files");
                    return true;
                }

                if (args[0].startsWith("-")) {
                    if (args[0].contains("a") && !args[0].contains("api")) {
                        sender.sendMessage("Players: "
                                + HashMaps.getFullPlayerList());
                        sender.sendMessage("Banned Players: "
                                + HashMaps.getFullBannedPlayers());
                        sender.sendMessage("TempBan: "
                                + HashMaps.getFullTempBannedTime());
                        sender.sendMessage("Ids: " + HashMaps.getFullIds());
                        return true;
                    }
                    if (args[0].contains("p") && !args[0].contains("api")) {
                        sender.sendMessage("Players: "
                                + HashMaps.getFullPlayerList());
                    }
                    if (args[0].contains("i") && !args[0].contains("api")) {
                        sender.sendMessage("Ids: " + HashMaps.getFullIds());
                    }
                    if (args[0].contains("b")) {
                        sender.sendMessage("Banned Players: "
                                + HashMaps.getFullBannedPlayers());
                    }
                    if (args[0].contains("t")) {
                        sender.sendMessage("TempBan: "
                                + HashMaps.getFullTempBannedTime());
                    }
                    if (args[0].contains("w")) {
                        sender.sendMessage("Warns: "
                                + HashMaps.getFullWarnList());
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
                return true;
            }
        }
        return false;
    }
}
