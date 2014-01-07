package net.serubin.serubans.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.MysqlBansDataProvider;
import net.serubin.serubans.util.HashMaps;

public class UpdateCommand implements CommandExecutor {

    private SeruBans plugin;

    public UpdateCommand(SeruBans plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("bupdate")) {
            if (SeruBans.hasPermission(sender, SeruBans.UPDATEPERM)) {
                if (args.length == 0) {
                    return false;
                } else if (args.length > 2) {

                }
                int bId;
                try {
                    bId = Integer.parseInt(args[0]);
                } catch (NumberFormatException ex) {
                    // Item was not an int, do nothing
                    sender.sendMessage(ChatColor.RED + "Id must be a number!");
                    return true;
                }
                if (!HashMaps.checkId(bId)) {
                    sender.sendMessage(ChatColor.RED + Integer.toString(bId)
                            + " is not a valid ban id.");
                    return false;
                }
                StringBuilder reasonRaw = new StringBuilder();
                String reason;
                // combine args into a string
                for (String s : args) {
                    reasonRaw.append(" " + s);
                }
                String repl = " " + args[0] + " ";
                reason = reasonRaw.toString().replace(repl, "");

                MysqlBansDataProvider.updateReason(bId, reason);
                sender.sendMessage(ChatColor.GREEN + "Reason for Id " + bId
                        + " changed to '" + reason + "'");
                plugin.printInfo(sender.getName()
                        + " updated reason of ban number "
                        + Integer.toString(bId) + " to " + reason);
                return true;
            }
            return true;
        }
        return false;
    }

}
