package net.serubin.serubans.commands;

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
            if(args.length == 0){
                return false;
            }
            if (args[0].startsWith("-")) {
                if (args[0].contains("a")) {
                    sender.sendMessage("Players: "
                            + HashMaps.getFullPlayerList());
                    sender.sendMessage("Banned Players: "
                            + HashMaps.getFullBannedPlayers());
                    sender.sendMessage("TempBan: "
                            + HashMaps.getFullTempBannedTime());
                    return true;
                }
                if (args[0].contains("p")) {
                    sender.sendMessage("Players: "
                            + HashMaps.getFullPlayerList());
                }
                if (args[0].contains("b")) {
                    sender.sendMessage("Banned Players: "
                            + HashMaps.getFullBannedPlayers());
                }
                if (args[0].contains("t")) {
                    sender.sendMessage("TempBan: "
                            + HashMaps.getFullTempBannedTime());
                }
                return true;
            }
            return false;
        }
        return false;
    }
}
