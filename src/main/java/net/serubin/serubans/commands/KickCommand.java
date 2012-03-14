package net.serubin.serubans.commands;

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

        if (commandLabel.equalsIgnoreCase("kick")) {
            if (sender.hasPermission(SeruBans.KICKPERM) || sender.isOp()
                    || (!(sender instanceof Player))) {
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
                    // kicks and broadcasts message
                    CheckPlayer.checkPlayer(victim, sender);
                    MySqlDatabase.addBan(victim.getName(), SeruBans.KICK, 0,
                            mod, reason);
                    SeruBans.printServer(ArgProcessing.GlobalMessage(
                            GlobalKickMessage, reason, mod, victim.getName()));
                    plugin.log.info(mod + " kicked " + victim.getName()
                            + " for " + reason);
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
                sender.sendMessage(ChatColor.RED + "You do not have permission!");
            }
        }

        return false;
    }

}
