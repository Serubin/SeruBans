package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.CheckPlayer;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

    ArgProcessing ap;
    MySqlDatabase db;
    CheckPlayer cp;
    OfflinePlayer offPlayer;
    Server server = Bukkit.getServer();
    Player victim;
    String mod;
    String reason = "";
    private String WarnMessage;
    private String name;
    private SeruBans plugin;
    private String WarnPlayerMessage;

    public WarnCommand(String WarnMessage, String WarnPlayerMessage,
            String name, SeruBans plugin) {
        this.WarnMessage = WarnMessage;
        this.WarnPlayerMessage = WarnPlayerMessage;
        this.name = name;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {

        if (commandLabel.equalsIgnoreCase("Warn")) {
            Player player = (Player) sender;
            if (args.length == 0) {
                return false;
            } else if (args.length > 1) {
                reason = ArgProcessing.reasonArgs(args);
            } else {
                reason = "undefined";
            }
            mod = player.getName();
            victim = server.getPlayer(args[0]);
            // processes Warn message

            String line = "";
            if (victim != null) {
                CheckPlayer.checkPlayer(victim, player);
                MySqlDatabase.addBan(victim, 3, mod, reason);
                // Warns and broadcasts message
                SeruBans.printServer(ArgProcessing.GlobalMessage(WarnMessage, reason, mod, victim));
                SeruBans.printInfo(mod + " warned " + victim.getName() + " for "
                        + reason);
                SeruBans.printInfo(WarnMessage);
                victim.sendMessage(ArgProcessing.GetColor(ArgProcessing
                        .PlayerMessage(WarnPlayerMessage, reason, mod)));
                victim.sendMessage(reason);

                // adds player to db
                return true;
            } else {
                try {
                    victim = offPlayer.getPlayer();
                } catch (NullPointerException NPE) {
                    victim = null;
                }
                if (victim != null) {
                    CheckPlayer.checkPlayer(victim, player);
                    MySqlDatabase.addBan(victim, 3, mod, reason);
                    // broadcasts message
                    ArgProcessing.GlobalMessage(WarnMessage, reason, mod,
                            victim);
                    SeruBans.printServer(line);
                    plugin.log.info(mod + " warned " + victim.getName()
                            + " for " + reason);
                    ;
                    return true;
                } else {
                    player.sendMessage("This Player was not found!");
                    return true;
                }
            }

        }

        return false;
    }

}
