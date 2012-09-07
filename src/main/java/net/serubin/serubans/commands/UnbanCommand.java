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

    private SeruBans plugin;

    public UnbanCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {

        int type;
        if (commandLabel.equalsIgnoreCase("unban")) {
            if (sender.hasPermission(SeruBans.UNBANPERM) || sender.isOp()
                    || (!(sender instanceof Player))) {
                if (args.length == 0) {
                    return false;
                } else if (args.length > 1) {
                    return false;
                } else {
                    String BannedVictim = args[0];
                    plugin.log.info("Attempting to unban " + BannedVictim);
                    if (HashMaps.keyIsInBannedPlayers(BannedVictim
                            .toLowerCase())) {
                        int bId = HashMaps.getBannedPlayers(BannedVictim
                                .toLowerCase());
                        if (HashMaps.keyIsInTempBannedTime(bId)) {

                            type = SeruBans.UNTEMPBAN;
                            HashMaps.removeTempBannedTimeItem(bId);

                        } else {
                            type = SeruBans.UNBAN;
                        }
                        MySqlDatabase.updateBan(type, bId);
                        HashMaps.removeBannedPlayerItem(BannedVictim
                                .toLowerCase());
                        SeruBans.printServer(ChatColor.YELLOW + BannedVictim
                                + ChatColor.GOLD + " was unbanned!");
                        plugin.log.info(BannedVictim + " was unbanned by "
                                + sender.getName());
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED
                                + "This player is not banned!");
                        return true;
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission!");
                return true;
            }
        }
        return false;
    }

}
