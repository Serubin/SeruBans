package net.serubin.serubans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.IBansDataProvider;
import net.serubin.serubans.dataproviders.MysqlBansDataProvider;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.CheckPlayer;
import net.serubin.serubans.util.DataCache;
import net.serubin.serubans.util.HashMaps;

public class TempBanCommand implements CommandExecutor {

    private SeruBans plugin;
    private IBansDataProvider db;
    private DataCache dc;

    public TempBanCommand(SeruBans plugin, IBansDataProvider db, DataCache dc) {
        this.plugin = plugin;
        this.db = db;
        this.dc = dc;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        String reason;
        Player victim;
        String mod;
        int display = SeruBans.SHOW;
        boolean silent = false;

        if (commandLabel.equalsIgnoreCase("tempban")) {
            if (!plugin.hasPermission(sender, SeruBans.TEMPBANPERM)) {
                return true;
            }
            // Checks for invalid arugments
            if (args.length == 0
                    || (args.length == 1 && args[0].startsWith("-"))) {
                return false;

                // Checks for options
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

            // Checks for user defined reason.
            if (args.length > 3) {
                reason = ArgProcessing.reasonArgsTB(args);
            } else {
                reason = "Undefined";
            }

            mod = sender.getName();
            victim = plugin.getServer().getPlayer(args[0]);
            boolean online = false;
            // Checks to see if player is online
            if (victim != null) {
                online = true;
                args[0] = victim.getName();
            }

            // Checks to see if player is registered
            if (!dc.checkPlayer(args[0].toLowerCase())) {
                db.addPlayer(args[0]);
            }

            // Checks to see if player is already banned
            if (db.getPlayerBannedInfo(args[0]) != null
                    || db.getPlayerTempBannedInfo(args[0]) != null) {
                sender.sendMessage(ChatColor.GOLD + args[0] + ChatColor.RED
                        + " is already banned!");
                return true;
            }

            // Handles ban length
            long length = ArgProcessing.parseTimeSpec(args[1], args[2]);
            plugin.printDebug("Ban length " + Long.toString(length));

            if (length == 0)
                return false;
            length = System.currentTimeMillis() / 1000 + length;
            String date = ArgProcessing.getStringDate(length);

            // prints to players on server with perms
            plugin.printServer(ArgProcessing.GlobalTempBanMessage(
                    plugin.GlobalTempBanMessage, reason, mod, victim.getName(),
                    date), silent);

            // Adds ban to database
            db.addBan(args[0], SeruBans.TEMPBAN, length, mod, reason, display);

            // logs it
            plugin.log.info(mod + " banned " + victim.getName() + " for "
                    + reason);

            // sends kicker ban id
            sender.sendMessage(ChatColor.GOLD + "Ban Id: " + ChatColor.YELLOW
                    + Integer.toString(db.getLastBanId()));

            // kicks player of the server
            if (online) {
                victim.kickPlayer(ArgProcessing.GetColor(ArgProcessing.PlayerTempBanMessage(
                        plugin.TempBanMessage, reason, mod, date)));
            }
            return true;

        }
        return false;
    }

}
