package net.serubin.serubans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.CheckPlayer;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

public class TempBanCommand implements CommandExecutor {

    private String tempBanMessage;
    private String globalTempBanMessage;
    private SeruBans plugin;

    public TempBanCommand(String tempBanMessage, String globalTempBanMessage,
            String name, SeruBans plugin) {
        this.tempBanMessage = tempBanMessage;
        this.globalTempBanMessage = globalTempBanMessage;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        String reason;
        Player victim;
        String mod;
        int display = SeruBans.SHOW;
        boolean silent = false;

        if (commandLabel.equalsIgnoreCase("tempban")) {
            if (sender.hasPermission(SeruBans.TEMPBANPERM) || sender.isOp()
                    || (!(sender instanceof Player))) {

                // checks for options
                // TODO Make this more efficient
                if (args.length == 0
                        || (args.length == 1 && args[0].startsWith("-"))) {
                    return false;
                } else {
                    silent = false;
                    display = SeruBans.SHOW;
                    if (args[0].startsWith("-")) {
                        if (args[0].contains("s")) {
                            silent = true;
                        }
                        if (args[0].contains("h")) {
                            display = SeruBans.HIDE;
                        }
                        args = ArgProcessing.stripFirstArg(args);
                    }
                }

                if (args.length == 0) {
                    return false;
                } else if (args.length > 3) {
                    reason = ArgProcessing.reasonArgsTB(args);
                } else {
                    reason = "undefined";
                }

                mod = sender.getName();
                victim = plugin.getServer().getPlayer(args[0]);

                if (victim != null) {
                    // adds player to db
                    CheckPlayer.checkPlayer(victim, sender);
                    // checks if banned
                    if (!HashMaps.keyIsInBannedPlayers(victim.getName())) {
                        long length = ArgProcessing.parseTimeSpec(args[1],
                                args[2]);
                        plugin.printDebug(Long.toString(length));
                        if (length == 0)
                            return false;
                        length = System.currentTimeMillis() / 1000 + length;
                        MySqlDatabase.addBan(victim.getName(),
                                SeruBans.TEMPBAN, length, mod, reason, display);
                        // kicks and broadcasts message

                        String date = ArgProcessing.getStringDate(length);
                        // sends kicker ban id
                        SeruBans.printServer(ArgProcessing
                                .GlobalTempBanMessage(globalTempBanMessage,
                                        reason, mod, victim.getName(), date),
                                silent);
                        // logs it
                        plugin.log.info(mod + " banned " + victim.getName()
                                + " for " + reason);
                        // sends kicker ban id
                        sender.sendMessage(ChatColor.GOLD
                                + "Ban Id: "
                                + ChatColor.YELLOW
                                + Integer.toString(MySqlDatabase.getLastBanId()));
                        // kicks player of the server
                        victim.kickPlayer(ArgProcessing.GetColor(ArgProcessing
                                .PlayerTempBanMessage(tempBanMessage, reason,
                                        mod, date)));
                        return true;
                    } else {
                        // TODO fix spelling - lazy
                        sender.sendMessage(ChatColor.GOLD
                                + victim.getName()
                                + ChatColor.RED
                                + " This player is banned and on your server, Please inform your admin imidiatly!");
                        return true;
                    }
                } else {
                    // checks player for id in database
                    CheckPlayer.checkPlayerOffline(args[0], sender);
                    // checks if banned
                    if (!HashMaps.keyIsInBannedPlayers(args[0])) {
                        long length = ArgProcessing.parseTimeSpec(args[1],
                                args[2]);
                        if (length == 0)
                            return false;
                        length = System.currentTimeMillis() / 1000 + length;
                        // adds ban to database
                        MySqlDatabase.addBan(args[0], SeruBans.TEMPBAN, length,
                                mod, reason, display);
                        // prints to players on server with perms
                        SeruBans.printServer(ArgProcessing.GlobalMessage(
                                globalTempBanMessage, reason, mod, args[0]),
                                silent);
                        // lgos its
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
