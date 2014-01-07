package net.serubin.serubans.commands;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.IBansDataProvider;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.BanInfo;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckBanCommand implements CommandExecutor {

    private SeruBans plugin;
    private IBansDataProvider db;

    public CheckBanCommand(SeruBans plugin, IBansDataProvider db) {
        this.plugin = plugin;
        this.db = db;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {

        if (commandLabel.equalsIgnoreCase("checkban")) {
            if (!plugin.hasPermission(sender, SeruBans.CHECKBANPERM)) {
                return true;
            }
            if (args.length != 1) {
                return false;
            }

            boolean isBanned = true;
            String player = args[0].toLowerCase();
            BanInfo banInfo = db.getPlayerBannedInfo(player);
            if (banInfo == null) {
                banInfo = db.getPlayerTempBannedInfo(player);
                if (banInfo == null) {
                    isBanned = false;
                }
            }

            if (isBanned) {
                sender.sendMessage(ChatColor.RED + args[0] + " is banned.");
                // Additional ban info
                sender.sendMessage(ChatColor.RED + "Ban id: "
                        + ChatColor.YELLOW + banInfo.getBanId());
                sender.sendMessage(ChatColor.RED + "Reason: "
                        + ChatColor.YELLOW + banInfo.getReason());
                if (banInfo.getType() == SeruBans.TEMPBAN) {
                    sender.sendMessage(ChatColor.RED + "Length: "
                            + ChatColor.YELLOW
                            + ArgProcessing.getStringDate(banInfo.getLength()));
                }
            } else {
                sender.sendMessage(ChatColor.GREEN + args[0]
                        + " is not banned.");
            }
            return true;
        }

        return false;
    }

}
