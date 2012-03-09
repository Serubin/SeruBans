package net.serubin.serubans.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.CheckPlayer;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

public class TempBanCommand implements CommandExecutor {

    Server server = Bukkit.getServer();
    Player victim;
    String mod;
    String reason;
    private String tempBanMessage;
    private String globalTempBanMessage;
    private SeruBans plugin;

    public TempBanCommand(String tempBanMessage, String globalTempBanMessage,
            String name, SeruBans plugin) {
        this.tempBanMessage = tempBanMessage;
        this.globalTempBanMessage = globalTempBanMessage;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            SeruBans.printInfo("Commands can only be issued in game!!");
        }

        if (commandLabel.equalsIgnoreCase("tempban")) {
            Player player = (Player) sender;
            if (args.length == 0) {
                return false;
            } else if (args.length > 3) {
                reason = ArgProcessing.reasonArgsTB(args);
            } else {
                reason = "undefined";
            }

            mod = player.getName();
            victim = server.getPlayer(args[0]);

            String line = "";
            if (victim != null) {
                // adds player to db
                CheckPlayer.checkPlayer(victim, player);
                if (!HashMaps.BannedPlayers.containsKey(victim.getName())) {
                    long length = ArgProcessing.parseTimeSpec(args[1], args[2]);
                    player.sendMessage(Long.toString(length));
                    if (length == 0)
                        return false;
                    length = System.currentTimeMillis() / 1000 + length;

                    MySqlDatabase.addBan(victim.getName(), 2, length, mod,
                            reason);
                    // kicks and broadcasts message
                    SeruBans.printServer(ArgProcessing.GlobalMessage(
                            globalTempBanMessage, reason, mod, victim.getName()));
                    SeruBans.printInfo(mod + " banned " + victim.getName()
                            + " for " + reason);
                    victim.kickPlayer(ArgProcessing.GetColor(ArgProcessing
                            .PlayerMessage(tempBanMessage, reason, mod)));
                    return true;
                } else {
                    player.sendMessage(ChatColor.GOLD
                            + victim.getName()
                            + ChatColor.RED
                            + " is already banned! Also, This player is banned and on your server... Might want to look into that.");
                    return true;
                }
            } else {
                // broadcasts message
                CheckPlayer.checkPlayerOffline(args[0], player);
                if (!HashMaps.BannedPlayers.containsKey(args[0])) {
                    long length = ArgProcessing.parseTimeSpec(args[1], args[2]);
                    if (length == 0)
                        return false;
                    length = System.currentTimeMillis() / 1000 + length;

                    MySqlDatabase.addBan(args[0], 2, length, mod, reason);
                    SeruBans.printServer(ArgProcessing.GlobalMessage(
                            globalTempBanMessage, reason, mod, args[0]));
                    SeruBans.printInfo(mod + " banned " + args[0] + " for "
                            + reason);
                    return true;
                } else {
                    player.sendMessage(ChatColor.GOLD + args[0] + ChatColor.RED
                            + " is already banned!");
                    return true;
                }

            }
        }
        return false;
    }

}
