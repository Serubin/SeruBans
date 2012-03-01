package net.serubin.serubans.commands;

import java.util.Map;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.HashMaps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DebugCommand implements CommandExecutor {

    private SeruBans plugin;

    public DebugCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("sbuser")) {
            sender.sendMessage(HashMaps.PlayerList.toString());
            sender.sendMessage(HashMaps.BannedPlayers.toString());
           //plugin.log.info(playerlist.toString());
            return true;
        }
        return false;
    }
}
