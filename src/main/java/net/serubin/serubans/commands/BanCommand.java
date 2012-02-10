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

public class BanCommand implements CommandExecutor {

    ArgProcessing ap;
    MySqlDatabase db;
    CheckPlayer cp;
    OfflinePlayer offPlayer;
    Server server = Bukkit.getServer();
    Player victim;
    String p;
    String mod;
    String reason;
    private String BanMessage;
    private String GlobalBanMessage;
    private String name;
    private SeruBans plugin;

    public BanCommand(String BanMessage, String GlobalBanMessage, String name,
            SeruBans plugin) {
        this.BanMessage = BanMessage;
        this.GlobalBanMessage = GlobalBanMessage;
        this.name = name;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {

        if (commandLabel.equalsIgnoreCase("ban")) {
            Player player = (Player) sender;
            int hide = 0;
            if (args.length == 0) {
                return false;
            } else if (args.length == 1) {
                reason = "undefined";
            } else if (args.length > 1) {
                reason = ArgProcessing.reasonArgs(args);
            } else {
                reason = "undefined";
            }
            if (args[1] == "-h") {
                hide = 1;
            } else {
                hide = 0;
            }

            mod = player.getName();
            victim = server.getPlayer(args[0]);

            String line = "";
            if (victim != null) {
                // adds player to db
                CheckPlayer.checkPlayer(victim, player);
                MySqlDatabase.addBan(victim, 1, mod, reason);
                // kicks and broadcasts message
                SeruBans.printServer(ArgProcessing.GlobalMessage(
                        GlobalBanMessage, reason, mod, victim));
                SeruBans.printInfo(mod + " banned " + victim.getName()
                        + " for " + reason);
                victim.kickPlayer(ArgProcessing.GetColor(ArgProcessing
                        .PlayerMessage(BanMessage, reason, mod)));
                return true;
            } else {
                try {
                    victim = offPlayer.getPlayer();
                } catch (NullPointerException NPE) {
                    victim = null;
                }

                if (victim != null) {
                    // broadcasts message
                    CheckPlayer.checkPlayer(victim, player);
                    ArgProcessing.GlobalMessage(GlobalBanMessage, reason, mod,
                            victim);
                    SeruBans.printServer(line);
                    plugin.log.info(mod + " banned " + victim.getName()
                            + " for " + reason);
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
