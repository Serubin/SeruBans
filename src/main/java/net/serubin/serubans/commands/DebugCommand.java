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
        if (commandLabel.equalsIgnoreCase("serubans")) {
            if (args[0].startsWith("-")) {
                if (args[0].contains("a")) {
                    sender.sendMessage("Players: "
                            + HashMaps.PlayerList.toString());
                    sender.sendMessage("Banned Players: "
                            + HashMaps.BannedPlayers.toString());
                    sender.sendMessage("TempBan: "
                            + HashMaps.TempBanned.toString());
                    return true;
                }
                if (args[0].contains("p")) {
                    sender.sendMessage("Players: "
                            + HashMaps.PlayerList.toString());
                    return true;
                }
                if (args[0].contains("b")) {
                    sender.sendMessage("Banned Players: "
                            + HashMaps.BannedPlayers.toString());
                    return true;
                }
                if (args[0].contains("t")) {
                    sender.sendMessage("TempBan: "
                            + HashMaps.TempBanned.toString());
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
