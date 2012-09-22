package net.serubin.serubans.commands;

import java.awt.Color;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.CheckPlayer;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    private String KickMessage;
    private String GlobalKickMessage;
    private String name;
    private SeruBans plugin;

    public KickCommand(String KickMessage, String GlobalKickMessage,
            String name, SeruBans plugin) {
        this.KickMessage = KickMessage;
        this.GlobalKickMessage = GlobalKickMessage;
        this.name = name;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player victim;
        String mod;
        String reason;
        int display = SeruBans.SHOW;
        boolean silent = false;

        if (commandLabel.equalsIgnoreCase("kick")) {
            if (sender.hasPermission(SeruBans.KICKPERM) || sender.isOp()
                    || (!(sender instanceof Player))) {
                silent = false;
                display = SeruBans.SHOW;
                if (args[0].startsWith("-")) {
                    if (args[0].contains("s")) {
                        silent = true;
                    }
                    if (args[0].contains("h")) {
                        display = SeruBans.HIDE;
                    }
                    args = ArgProcessing.stripFirstArg(args);
                }
                if (args.length == 0) {
                    return false;
                } else if (args.length > 1) {
                    reason = ArgProcessing.reasonArgs(args);
                } else {
                    reason = "undefined";
                }
                String line = "";
                mod = sender.getName();
                victim = plugin.getServer().getPlayer(args[0]);
                if (victim != null) {
                    // checks players for id in database
                    CheckPlayer.checkPlayer(victim, sender);
                    // adds ban to database
                    MySqlDatabase.addBan(victim.getName(), SeruBans.KICK, 0,
                            mod, reason, display);
                    // prints to players on server with perms
                    SeruBans.printServer(ArgProcessing.GlobalMessage(
                            GlobalKickMessage, reason, mod, victim.getName()),
                            silent);
                    // logs its
                    plugin.log.info(mod + " kicked " + victim.getName()
                            + " for " + reason);
                    // sends kicker ban id
                    sender.sendMessage(ChatColor.GOLD + "Ban Id: "
                            + ChatColor.YELLOW
                            + Integer.toString(MySqlDatabase.getLastBanId()));
                    // kicks player of the server
                    victim.kickPlayer(ArgProcessing.GetColor(ArgProcessing
                            .PlayerMessage(KickMessage, reason, mod)));
                    // adds player to db
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED
                            + "This Player was not found!");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED
                        + "You do not have permission!");
                return true;
            }
        }

        return false;
    }

}
