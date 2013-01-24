package net.serubin.serubans.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpMessages {
    public static void banHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN
                + "/ban [-options] <player> <reason>");
        sender.sendMessage(ChatColor.GREEN + "    Usage: " + ChatColor.YELLOW
                + "Used to ban players permanently from the server.");
    }

    public static void tempBanHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN
                + "/tempban [-options] <player> <time> <unit> <reason>");
        sender.sendMessage(ChatColor.GREEN
                + "    Usage: "
                + ChatColor.YELLOW
                + "Used to ban players temporarily, for a defined amount of time, from the server.");
    }

    public static void kickHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN
                + "/kick [-options] <player> <reason>");
        sender.sendMessage(ChatColor.GREEN + "    Usage: " + ChatColor.YELLOW
                + "Used to kick players from the server.");
    }

    public static void warnHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN
                + "/warn [-options] <player> <reason>");
        sender.sendMessage(ChatColor.GREEN + "    Usage: " + ChatColor.YELLOW
                + "Used to warn a player.");
        sender.sendMessage(ChatColor.GREEN
                + "    Note: "
                + ChatColor.YELLOW
                + "When warning an offline player, they will be notified when they next login.");

    }

    public static void unbanHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "/unban [-options] <player>");
        sender.sendMessage(ChatColor.GREEN + "    Usage: " + ChatColor.YELLOW
                + "Used to unban a player.");
        sender.sendMessage(ChatColor.GREEN
                + "    Note: "
                + ChatColor.YELLOW
                + "Spelling must be exact to the player's name. Also, -h option does not work with this command");

    }

    public static void banOptions(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Options: ");
        sender.sendMessage(ChatColor.GREEN + "    -s    " + ChatColor.YELLOW
                + "Hides the ban messages from the displaying to players.");
        sender.sendMessage(ChatColor.GREEN + "    -h    " + ChatColor.YELLOW
                + "Hides the ban from the ban list.");
    }

    public static void checkbanHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "/checkban <player>");
        sender.sendMessage(ChatColor.GREEN + "    Usage: " + ChatColor.YELLOW
                + "Used to determine if a player is banned or not");
    }

    public static void updateHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "/bupdate <ban id> <new reason>");
        sender.sendMessage(ChatColor.GREEN + "    Usage: " + ChatColor.YELLOW
                + "Used to update ban reasons.");
    }

    public static void searchHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN
                + "/bsearch p:<player> t:<type> i:<id>");
        sender.sendMessage(ChatColor.YELLOW + "Can be used in 3 diffrent ways:");
        sender.sendMessage(ChatColor.YELLOW + "    Search player: "
                + ChatColor.GREEN + "p:<player>");
        sender.sendMessage(ChatColor.YELLOW + "    Search type: "
                + ChatColor.GREEN + "p:<player> t:<type>");
        sender.sendMessage(ChatColor.YELLOW + "    Search id: "
                + ChatColor.GREEN + "i:<id>");
    }

    public static void debugHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Use " + ChatColor.GREEN
                + "'/serubans -option' " + ChatColor.YELLOW
                + "for debug functionality.");
        sender.sendMessage(ChatColor.YELLOW + "Options:");
        sender.sendMessage(ChatColor.YELLOW
                + "-a    prints full hashmaps lists");
        sender.sendMessage(ChatColor.YELLOW
                + "-p    prints player hashmaps lists");
        sender.sendMessage(ChatColor.YELLOW
                + "-i     prints id  hashmaps lists");
        sender.sendMessage(ChatColor.YELLOW
                + "-b    prints banned player hashmaps lists");
        sender.sendMessage(ChatColor.YELLOW
                + "-w    prints warns hashmaps lists");
        sender.sendMessage(ChatColor.YELLOW
                + "-e    export bans to minecraft bans files");

    }
}
