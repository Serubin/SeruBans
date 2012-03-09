package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.HashMaps;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckBanCommand implements CommandExecutor {

    private SeruBans plugin;

    public CheckBanCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            SeruBans.printInfo("Commands can only be issued in game!!");
        }

        Player player = (Player) sender;
        
        if (commandLabel.equalsIgnoreCase("checkban")) {
            boolean isBanned = HashMaps.BannedPlayers.containsKey(args[0]);

            if (isBanned) {
                int id = HashMaps.BannedPlayers.get(args[0]);
                player.sendMessage(ChatColor.RED + args[0] + " is banned.");
                player.sendMessage(ChatColor.RED + "Ban id: " + ChatColor.YELLOW + id);
            } else {
                player.sendMessage(ChatColor.GREEN + args[0] + " is not banned.");
            }
        }
        return false;
    }

}
