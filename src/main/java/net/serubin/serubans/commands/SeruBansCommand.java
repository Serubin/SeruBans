package net.serubin.serubans.commands;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.MysqlBansDataProvider;
import net.serubin.serubans.util.BanInfo;
import net.serubin.serubans.util.HelpMessages;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeruBansCommand implements CommandExecutor {

    private SeruBans plugin;

    public SeruBansCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("serubans")) {
            if (SeruBans.hasPermission(sender, SeruBans.HELPPERM)
                    || SeruBans.hasPermission(sender, SeruBans.DEBUGPERM)) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.GREEN + "Serubans "
                            + ChatColor.YELLOW + " version "
                            + SeruBans.getVersion());
                    sender.sendMessage(ChatColor.YELLOW + "For help with: ");
                    sender.sendMessage(ChatColor.GREEN + "    banning "
                            + ChatColor.YELLOW + "type " + ChatColor.GREEN
                            + "/serubans ban");
                    sender.sendMessage(ChatColor.GREEN + "    tempbanning "
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
                    if (SeruBans.hasPermission(sender, SeruBans.DEBUGPERM)) {
                        if (args[0].startsWith("-")) {
                            if (args[0].contains("e")) {
                                plugin.log
                                        .info(sender.getName()
                                                + " has detected the export command. Now attempting to export bans to vanilla bans file. Once this has completed unbanning a player in serubans may not unban them. Make sure to remove their name from the vanilla bans file!");
                                exportToBannedPlayersTxtFile(sender);
                            }
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED
                                    + "Help/Debug argument was not found.");
                            return true;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void exportToBannedPlayersTxtFile(CommandSender sender) {
        List<BanInfo> permBans = MysqlBansDataProvider.getPermBans();
        List<BanInfo> tempBans = MysqlBansDataProvider.getTempBans();
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
            sender.sendMessage(ChatColor.GREEN + "Bans were successfully exported!");
        } catch (IOException e) {
            plugin.log.severe("Bans file could not be written!");
            sender.sendMessage(ChatColor.RED + "Bans file could not be written!");
        }
    }
}
