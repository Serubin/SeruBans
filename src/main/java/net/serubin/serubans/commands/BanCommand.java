package net.serubin.serubans.commands;

import java.awt.Color;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.CheckPlayer;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

    private String BanMessage;
    private String GlobalBanMessage;
    private String name;
    private SeruBans plugin;

    public BanCommand(String BanMessage, String GlobalBanMessage, String name,
            SeruBans plugin) {
        this.BanMessage = BanMessage;
        this.GlobalBanMessage = GlobalBanMessage;
        this.name = name;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player victim;
        String mod;
        String reason;
        boolean silent = false;
        int display = SeruBans.SHOW;
        if (commandLabel.equalsIgnoreCase("ban")) {
            if (sender.hasPermission(SeruBans.BANPERM) || sender.isOp()
                    || (!(sender instanceof Player))) {
                
                // checks for options
                silent = false;
                display = SeruBans.SHOW;
                if (args[0].startsWith("-")) {
                    if(args[0].contains("s")){
                        silent = true;   
                    }
                    if(args[0].contains("h")){
                        display = SeruBans.HIDE;
                    }
                    args = ArgProcessing.stripFirstArg(args);
                }

                if (args.length == 0) {
                    return false;
                } else if (args.length > 1) {
                    reason = ArgProcessing.reasonArgs(args);
                } else {
                    reason = "undefined";
                }

                mod = sender.getName();
                victim = plugin.getServer().getPlayer(args[0]);

                String line = "";
                if (victim != null) {
                 // checks players for id in database
                    CheckPlayer.checkPlayer(victim, sender);
                    //checks if banned
                    if (!HashMaps.keyIsInBannedPlayers(victim.getName())) {
                        // adds ban to database
                        MySqlDatabase.addBan(victim.getName(), SeruBans.BAN, 0,
                                mod, reason, display);
                        
                        // prints to players on server with perms
                        SeruBans.printServer(
                                ArgProcessing.GlobalMessage(GlobalBanMessage,
                                        reason, mod, victim.getName()), silent);
                        // logs it
                        plugin.log.info(mod + " banned " + victim.getName()
                                + " for " + reason);
                        // sends kicker ban id
                        sender.sendMessage(ChatColor.GOLD
                                + "Ban Id: "
                                + ChatColor.YELLOW
                                + Integer.toString(MySqlDatabase.getLastBanId()));
                        //kicks player
                        victim.kickPlayer(ArgProcessing.GetColor(ArgProcessing
                                .PlayerMessage(BanMessage, reason, mod)));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.GOLD
                                + victim.getName()
                                + ChatColor.RED
                                + " is already banned! Also, This player is banned and on your server... Might want to look into that.");
                        return true;
                    }
                } else {
                    // checks player for id in database
                    CheckPlayer.checkPlayerOffline(args[0], sender);
                    // checks if banned
                    if (!HashMaps.keyIsInBannedPlayers(args[0])) {
                        // adds ban to database
                        MySqlDatabase.addBan(args[0], SeruBans.BAN, 0, mod,
                                reason, display);
                        // prints to players on server with perms
                        SeruBans.printServer(ArgProcessing.GlobalMessage(
                                GlobalBanMessage, reason, mod, args[0]), silent);
                        // logs it
                        plugin.log.info(mod + " banned " + args[0] + " for "
                                + reason);
                        // sends kicker ban id
                        sender.sendMessage(ChatColor.GOLD
                                + "Ban Id: "
                                + ChatColor.YELLOW
                                + Integer.toString(MySqlDatabase.getLastBanId()));
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.GOLD + args[0]
                                + ChatColor.RED + " is already banned!");
                        return true;
                    }

                }
            } else {
                sender.sendMessage(ChatColor.RED
                        + "You do not have permission!");
                return true;
            }
        }
        return false;
    }

}
