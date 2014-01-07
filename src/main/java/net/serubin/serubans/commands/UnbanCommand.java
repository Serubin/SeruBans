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
import net.serubin.serubans.util.DataCache;
import net.serubin.serubans.util.HashMaps;

public class UnbanCommand implements CommandExecutor {

    private SeruBans plugin;
    private IBansDataProvider db;
    private DataCache dc;

    public UnbanCommand(SeruBans plugin, IBansDataProvider db, DataCache dc) {
        this.plugin = plugin;
        this.db = db;
        this.dc = dc;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {

        int type;
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
                args = ArgProcessing.stripFirstArg(args);
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
