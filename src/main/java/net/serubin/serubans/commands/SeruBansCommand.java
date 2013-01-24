package net.serubin.serubans.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.HelpMessages;

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
            if (sender.hasPermission(SeruBans.HELPPERM) || sender.isOp()
                    || (!(sender instanceof Player))) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.GREEN + "Serubans "
                            + ChatColor.YELLOW + " version "
                            + SeruBans.getVersion());
                    sender.sendMessage(ChatColor.YELLOW + "For help with: ");
                    sender.sendMessage(ChatColor.GREEN + "    baning "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans ban");
                    sender.sendMessage(ChatColor.GREEN + "    tempbaning "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans tempban");
                    sender.sendMessage(ChatColor.GREEN + "    kicking "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans kick");
                    sender.sendMessage(ChatColor.GREEN + "    warning "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans warn");
                    sender.sendMessage(ChatColor.GREEN + "    unbaning "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans unban");
                    sender.sendMessage(ChatColor.GREEN + "    checking bans "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans checkban");
                    sender.sendMessage(ChatColor.GREEN + "    updating bans "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans update");
                    sender.sendMessage(ChatColor.GREEN + "    searching "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans search");
                    sender.sendMessage(ChatColor.GREEN + "    debug "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans debug");
                    return true;
                }
                if (args[0].equalsIgnoreCase("ban")) {
                    // Ban help
                    HelpMessages.banHelp(sender);
                    HelpMessages.banOptions(sender);

                } else if (args[0].equalsIgnoreCase("tempban")) {
                    // Tempban help
                    HelpMessages.tempBanHelp(sender);
                    HelpMessages.banOptions(sender);

                } else if (args[0].equalsIgnoreCase("kick")) {
                    // Kick help
                    HelpMessages.kickHelp(sender);
                    HelpMessages.banOptions(sender);
                } else if (args[0].equalsIgnoreCase("warn")) {
                    // Warn help
                    HelpMessages.warnHelp(sender);
                    HelpMessages.banOptions(sender);
                } else if (args[0].equalsIgnoreCase("unban")) {
                    // Unban help
                    HelpMessages.unbanHelp(sender);
                    HelpMessages.banOptions(sender);
                }

                else if (args[0].equalsIgnoreCase("checkban")) {
                    // Checkban help
                    HelpMessages.checkbanHelp(sender);

                } else if (args[0].equalsIgnoreCase("update")) {
                    // Update help
                    HelpMessages.updateHelp(sender);
                } else if (args[0].equalsIgnoreCase("search")) {
                    // Search help
                    HelpMessages.searchHelp(sender);
                } else if (args[0].equalsIgnoreCase("debug")) {
                    // Debug help
                    HelpMessages.debugHelp(sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "Help page not found!");
                }
                return true;
            } else if (sender.hasPermission(SeruBans.DEBUGPERM)
                    || sender.isOp() || (!(sender instanceof Player))) {
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
