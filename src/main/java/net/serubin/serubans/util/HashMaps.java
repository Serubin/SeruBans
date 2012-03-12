package net.serubin.serubans.util;

import java.util.HashMap;
import java.util.Map;

public class HashMaps {

    private static Map<String, Integer> PlayerList = new HashMap<String, Integer>();
    private static Map<String, Integer> BannedPlayers = new HashMap<String, Integer>();
    private static Map<Integer, Long> TempBanned = new HashMap<Integer, Long>();
    
    public static Map<String, Integer> getBannedPlayers() {
        return BannedPlayers;
    }
    public static void setBannedPlayers(Map<String, Integer> bannedPlayers) {
        BannedPlayers = bannedPlayers;
    }
    public static Map<Integer, Long> getTempBanned() {
        return TempBanned;
    }
    public static void setTempBanned(Map<Integer, Long> tempBanned) {
        TempBanned = tempBanned;
    }
    public static Map<String, Integer> getPlayerList() {
        return PlayerList;
    }
    public static void setPlayerList(Map<String, Integer> playerList) {
        PlayerList = playerList;
    }
    
}
