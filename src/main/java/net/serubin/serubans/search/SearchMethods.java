package net.serubin.serubans.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

public class SearchMethods {

    /*
     * handels PLAYER searchs
     */
    public static boolean searchPlayer(String player, CommandSender sender) {
        List<Integer> BanTypes = null;
        try {
            BanTypes = MySqlDatabase.searchPlayer(HashMaps
                    .getPlayerList(player));
        } catch (NullPointerException NPE) {
            sender.sendMessage(ChatColor.RED + "No Results");
            SeruBans.self.printDebug(NPE.toString());
        }
        if(BanTypes.isEmpty()){
            sender.sendMessage(ChatColor.RED + "No Results");
            return true;
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

        return true;
    }

    /*
     * Handles out put for TYPE searches
     */
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
        if (PlayerInfo.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No Results");
            return true;
        }

        Iterator<String> playerInfoIterator = PlayerInfo.iterator();
        DisplayManager.sendLine(
                sender,
                DisplayManager.createTitle(player + " Type:"
                        + ArgProcessing.getBanTypeString(type)));
        while (playerInfoIterator.hasNext()) {
            DisplayManager.sendLine(sender, playerInfoIterator.next());
        }
        return true;
    }

    /*
     * Handles ID search output
     */
    public static boolean searchId(int id, CommandSender sender) {
        Map<String, String> BanId = new HashMap<String, String>();
        try {
            BanId = MySqlDatabase.getBanIdInfo(id);
        } catch (NullPointerException NPE) {
            sender.sendMessage(ChatColor.RED + "No Results");
            SeruBans.self.printDebug(NPE.toString());
            return true;
        }
        if (BanId.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No Results");
            return true;
        }
        DisplayManager.sendLine(sender, DisplayManager.createTitle("Id:" + id));
        DisplayManager.sendLine(sender, "Player: " + BanId.get("name"));
        DisplayManager.sendLine(sender, "Mod: " + BanId.get("mod"));
        DisplayManager.sendLine(sender, "Type: " + BanId.get("type"));
        DisplayManager.sendLine(sender, "Date: " + BanId.get("date"));
        if(BanId.containsKey("length")){
            DisplayManager.sendLine(sender, "Length: " + BanId.get("length"));
        }
        DisplayManager.sendLine(sender, "Reason: " + BanId.get("reason"));
        DisplayManager.sendLine(sender, "Is Banned? " + HashMaps.keyIsInBannedPlayers(BanId.get("name")));
        return true;
    }
}
