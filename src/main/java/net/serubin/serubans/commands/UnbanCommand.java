package net.serubin.serubans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

public class UnbanCommand implements CommandExecutor {

    private static SeruBans plugin;

    public UnbanCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("unban")) {
            if (args.length == 0) {
                return false;
            } else if (args.length > 1) {
                return false;
            } else {
                String BannedVictim = args[0];
               SeruBans.printInfo("Attempting to unban " + BannedVictim);
                if (HashMaps.BannedPlayers.containsKey(BannedVictim.toLowerCase())) {
                    int bId = HashMaps.BannedPlayers.get(BannedVictim.toLowerCase());
                    MySqlDatabase.updateBan(11, bId);
                    HashMaps.BannedPlayers.remove(BannedVictim.toLowerCase());
                    SeruBans.printServer(ChatColor.YELLOW + BannedVictim
                            + ChatColor.GOLD + " was unbanned!");
                    SeruBans.printInfo(BannedVictim + " was unbanned");
                    return true;
                }
            }
        }
        return false;
    }

}
