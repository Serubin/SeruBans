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

    private DisplayManager dm = new DisplayManager();

    /*
     * handels PLAYER searchs
     */
    public boolean searchPlayer(String player, CommandSender sender) {
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
        dm.sendLine(sender, dm.createTitle(player));
        dm.sendLine(sender, " Bans: " + ChatColor.GOLD + Bans);
        dm.sendLine(sender, " TempBans: " + ChatColor.GOLD + TempBans);
        dm.sendLine(sender, " Kicks: " + ChatColor.GOLD + Kicks);
        dm.sendLine(sender, " Warns: " + ChatColor.GOLD + Warns);
        dm.sendLine(sender, " Is " + ChatColor.GOLD + Banned);

        return true;
    }

    /*
     * Handles out put for TYPE searches
     */
    public boolean searchType(String player, int type,
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
        dm.sendLine(
                sender,
                dm.createTitle(player + " Type:"
                        + ArgProcessing.getBanTypeString(type)));
        while (playerInfoIterator.hasNext()) {
            dm.sendLine(sender, playerInfoIterator.next());
        }
        return true;
    }

    /*
     * Handles ID search output
     */
    public boolean searchId(int id, CommandSender sender) {
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
        dm.sendLine(sender, dm.createTitle("Id:" + id));
        dm.sendLine(sender, "Player: " + ChatColor.GOLD + BanId.get("name"));
        dm.sendLine(sender, "Mod: " + ChatColor.GOLD + BanId.get("mod"));
        dm.sendLine(sender, "Type: " + ChatColor.GOLD + BanId.get("type"));
        dm.sendLine(sender, "Date: " + ChatColor.GOLD + BanId.get("date"));
        if(BanId.containsKey("length")){
            dm.sendLine(sender, "Length: " + ChatColor.GOLD + BanId.get("length"));
        }
        dm.sendLine(sender, "Reason: " + ChatColor.GOLD + BanId.get("reason"));
        dm.sendLine(sender, "Is Banned? " + ChatColor.GOLD + HashMaps.keyIsInBannedPlayers(BanId.get("name")));
        return true;
    }
}
