package net.serubin.serubans.search;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

public class SearchMethods {

    public static void searchPlayer(String player, CommandSender sender) {
        List<Integer> BanTypes = null;
        try {
            BanTypes = MySqlDatabase.searchPlayer(HashMaps
                    .getPlayerList(player));
        } catch (NullPointerException NPE) {
            sender.sendMessage(ChatColor.RED + "No Results");
            SeruBans.self.printDebug(NPE.toString());
        }

        Iterator<Integer> iterator = BanTypes.iterator();
        int TempBans = 0;
        int Bans = 0;
        int Kicks = 0;
        int Warns = 0;
        while (iterator.hasNext()) {
            int Next = iterator.next();
            if (Next == SeruBans.BAN)
                Bans++;
            if (Next == SeruBans.KICK)
                Kicks++;
            if (Next == SeruBans.TEMPBAN)
                TempBans++;
            if (Next == SeruBans.WARN)
                Warns++;
        }
        String Banned = "not banned";
        if (HashMaps.keyIsInBannedPlayers(player))
            Banned = "banned";
        DisplayManager.sendLine(sender, DisplayManager.createTitle(player));
        DisplayManager.sendLine(sender, " Bans: " + Bans);
        DisplayManager.sendLine(sender, " TempBans: " + TempBans);
        DisplayManager.sendLine(sender, " Kicks: " + Kicks);
        DisplayManager.sendLine(sender, " Warns: " + Warns);
        DisplayManager.sendLine(sender, " Is " + Banned);

    }

    public static boolean searchType(String player, int type,
            CommandSender sender) {
        List<String> PlayerInfo = null;
        try {
            PlayerInfo = MySqlDatabase.searchType(
                    HashMaps.getPlayerList(player), type);
        } catch (NullPointerException NPE) {
            sender.sendMessage(ChatColor.RED + "No Results");
            SeruBans.self.printDebug(NPE.toString());
            return true;
        }

        Iterator<String> playerInfoIterator = PlayerInfo.iterator();
        DisplayManager.sendLine(sender,
                DisplayManager.createTitle(player + " Type:Bans"));
        while (playerInfoIterator.hasNext()) {
            DisplayManager.sendLine(sender, playerInfoIterator.next());
        }
        return true;
    }

    public static void searchId(int id, CommandSender sender) {

    }
}
