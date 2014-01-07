package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.IBansDataProvider;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.DataCache;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

    private SeruBans plugin;
    private IBansDataProvider db;
    private DataCache dc;

    public BanCommand(SeruBans plugin, IBansDataProvider db, DataCache dc) {
        this.plugin = plugin;
        this.db = db;
        this.dc = dc;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player victim;
        String mod;
        String reason;
        boolean silent = false;
        int display = SeruBans.SHOW;
        if (commandLabel.equalsIgnoreCase("ban")) {
            if (!plugin.hasPermission(sender, SeruBans.BANPERM)) {
                return true;
            }
            // Checks for invalid arguments
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
            if (args.length > 1) {
                reason = ArgProcessing.reasonArgs(args);
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

            // Checks to see if player is registered in database
            if (!dc.checkPlayer(args[0].toLowerCase())) {
                db.addPlayer(args[0]);
            }
            // Checks to see if player is already banned
            if (db.getPlayerBannedInfo(args[0]) == null) {
                sender.sendMessage(ChatColor.GOLD + args[0] + ChatColor.RED
                        + " is already banned!");
                return true;
            }

            // prints to players on server with perms
            plugin.printServer(ArgProcessing.GlobalMessage(
                    plugin.GlobalBanMessage, reason, mod, victim.getName()),
                    silent);
            // Adds ban
            db.addBan(victim.getName(), SeruBans.BAN, 0, mod, reason, display);

            // logs it
            plugin.printInfo(mod + " banned " + victim.getName() + " for "
                    + reason);

            // sends kicker ban id
            sender.sendMessage(ChatColor.GOLD + "Ban Id: " + ChatColor.YELLOW
                    + Integer.toString(db.getLastBanId()));
            // kicks player
            if (online) {
                victim.kickPlayer(ArgProcessing.GetColor(ArgProcessing.PlayerMessage(
                        plugin.BanMessage, reason, mod)));
            }
            return true;
        }
        return false;
    }
}
