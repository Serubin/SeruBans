package net.serubin.serubans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (!(sender instanceof Player)) {
            SeruBans.printInfo("Commands can only be issued in game!!");
        }

        Player player = (Player) sender;
        int type;
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
                    if(HashMaps.TempBanned.containsKey(bId)){
                        
                        type = 12;
                        
                    } else {
                        type = 11;
                    }
                    MySqlDatabase.updateBan(type, bId);
                    HashMaps.BannedPlayers.remove(BannedVictim.toLowerCase());
                    SeruBans.printServer(ChatColor.YELLOW + BannedVictim
                            + ChatColor.GOLD + " was unbanned!");
                    SeruBans.printInfo(BannedVictim + " was unbanned");
                } else {
                   player.sendMessage(ChatColor.RED + "This player is not banned!");
                }
            }
        }
        return false;
    }

}
