package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.IBansDataProvider;
import net.serubin.serubans.dataproviders.MysqlBansDataProvider;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.CheckPlayer;
import net.serubin.serubans.util.DataCache;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    private SeruBans plugin;
    private DataCache dc;
    private IBansDataProvider db;

    public KickCommand(SeruBans plugin, IBansDataProvider db, DataCache dc) {
        this.plugin = plugin;
        this.db = db;
        this.dc = dc;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player victim;
        String mod;
        String reason;
        int display = SeruBans.SHOW;
        boolean silent = false;

        if (commandLabel.equalsIgnoreCase("kick")) {
            if (!plugin.hasPermission(sender, SeruBans.KICKPERM)) {
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

            // Checks to see if player is online, cancels otherwise
            if (!online) {
                sender.sendMessage(ChatColor.RED + "This Player was not found!");
                return true;
            }
            // adds ban to database
            db.addBan(victim.getName(), SeruBans.KICK, 0, mod, reason, display);
            
            // prints to players on server with perms
            plugin.printServer(ArgProcessing.GlobalMessage(
                    plugin.GlobalKickMessage, reason, mod, victim.getName()),
                    silent);
            // logs its
            plugin.printInfo(mod + " kicked " + victim.getName() + " for "
                    + reason);
            // sends kicker ban id
            sender.sendMessage(ChatColor.GOLD + "Ban Id: " + ChatColor.YELLOW
                    + Integer.toString(db.getLastBanId()));
            // kicks player of the server
            victim.kickPlayer(ArgProcessing.GetColor(ArgProcessing.PlayerMessage(
                    plugin.KickMessage, reason, mod)));
            // adds player to db
            return true;

        }

        return false;
    }

}
