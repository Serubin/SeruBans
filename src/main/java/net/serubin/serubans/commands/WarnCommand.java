package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.BansDataProvider;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

    private SeruBans plugin;
    private BansDataProvider db;

    public WarnCommand(SeruBans plugin, BansDataProvider db) {
        this.plugin = plugin;
        this.db = db;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player victim;
        String mod;
        String reason = "";
        int display = SeruBans.SHOW;
        boolean silent = false;

        if (commandLabel.equalsIgnoreCase("warn")) {
            if (!plugin.hasPermission(sender, SeruBans.WARNPERM)) {
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
                    args = plugin.text().stripFirstArg(args);
                }
            }

            // Checks for user defined reason.
            if (args.length > 1) {
                reason = plugin.text().reasonArgs(args);
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

            // adds ban to database
            db.addBan(victim.getName(), SeruBans.WARN, 0, mod, reason, display);
            plugin.printServer(plugin.text().GlobalMessage(plugin.WarnMessage,
                    reason, mod, victim.getName()), silent);
            // logs i t
            plugin.log.info(mod + " warned " + victim.getName() + " for "
                    + reason);
            // tells victim
            if (online) {
                victim.sendMessage(plugin.text().GetColor(plugin.text().PlayerMessage(
                        plugin.WarnPlayerMessage, reason, mod)));
            } else {
                db.addWarn(db.getPlayer(args[0].toLowerCase()),
                        db.getLastBanId());
            }
            // sends kicker ban id
            sender.sendMessage(ChatColor.GOLD + "Ban Id: " + ChatColor.YELLOW
                    + Integer.toString(db.getLastBanId()));
            return true;
        }
        return false;
    }

}
