package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.BansDataProvider;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnbanCommand implements CommandExecutor {

    private SeruBans plugin;
    private BansDataProvider db;

    public UnbanCommand(SeruBans plugin, BansDataProvider db) {
        this.plugin = plugin;
        this.db = db;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {

        boolean silent = false;
        if (commandLabel.equalsIgnoreCase("unban")) {
            if (!plugin.hasPermission(sender, SeruBans.UNBANPERM)) {
                return true;
            }

            silent = false;
            if (args[0].startsWith("-")) {
                if (args[0].contains("s")) {
                    silent = true;
                }
                args = plugin.text().stripFirstArg(args);
            }

            if (args.length > 1) {
                return false;
            }
            String victim = args[0];

            // Unbans player
            plugin.printDebug("Attempting to unban " + victim);
            if (!db.unbanPlayer(victim)) {
                sender.sendMessage(ChatColor.RED + "This player is not banned!");
                return true;
            }

            // prints to players on server with perms
            plugin.printServer(ChatColor.YELLOW + victim + ChatColor.GOLD
                    + " was unbanned!", silent);
            plugin.printInfo(victim + " was unbanned by " + sender.getName());
            return true;

        }
        return false;
    }

}
