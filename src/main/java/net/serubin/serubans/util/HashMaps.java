package net.serubin.serubans.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HashMaps {

    private static Map<String, Integer> PlayerList = new HashMap<String, Integer>();
    private static Map<String, Integer> BannedPlayers = new HashMap<String, Integer>();
    private static List< Integer> BanIds = new ArrayList<Integer>();    
    private static Map<Integer, Long> TempBannedTime = new HashMap<Integer, Long>();

    /*
     * id list
     */
    public static boolean checkId(int id){
        return BanIds.contains(id);
    }
    public static void setIds(int id){
        BanIds.add(id);
    }
    public static String getFullIds(){
        return BanIds.toString();
    }
    
    /*
     * banned players
     */
    
    public static Integer getBannedPlayers(String name) {
        return BannedPlayers.get(name);
    }

    public static void setBannedPlayers(String key, int value) {
        BannedPlayers.put(key, value);
    }

    public static boolean valueIsInBannedPlayers(int value) {
        return BannedPlayers.containsValue(value);
    }

    public static boolean keyIsInBannedPlayers(String key) {
        return BannedPlayers.containsKey(key);
    }

    public static void removeBannedPlayerItem(String key) {
        BannedPlayers.remove(key);
    }

    public static String getFullBannedPlayers() {
        return BannedPlayers.toString();
    }

    public static Set<Entry<String, Integer>> getBannedPlayersSet() {
        return BannedPlayers.entrySet();
    }

    public static List<String> getBannedForFile() {
        List<String> ban = new ArrayList<String>();
        for (Entry<String, Integer> entry : BannedPlayers.entrySet()) {
            String key = entry.getKey();
            ban.add(key);
        }
        return ban;
    }

    // temp banned time
    public static Long getTempBannedTime(int key) {
        return TempBannedTime.get(key);
    }

    public static void setTempBannedTime(int key, Long value) {
        TempBannedTime.put(key, value);
    }

    public static boolean keyIsInTempBannedTime(int key) {
        return TempBannedTime.containsKey(key);
    }

    public static boolean valueIsInTempBannedTime(Long value) {
        return TempBannedTime.containsValue(value);
    }

    public static void removeTempBannedTimeItem(int key) {
        TempBannedTime.remove(key);
    }

    public static String getFullTempBannedTime() {
        return TempBannedTime.toString();
    }

    public static Set<Entry<Integer, Long>> getTempBannedTimeSet() {
        return TempBannedTime.entrySet();
    }

    public static List<String> getTempBannedTimeUnbans() {
        List<String> unban = new ArrayList<String>();
        int b_Id;
        for (Entry<String, Integer> entry : BannedPlayers.entrySet()) {
            String key = entry.getKey();
            b_Id = getBannedPlayers(key);
            if (keyIsInTempBannedTime(b_Id)) {

                if (TempBannedTime.get(b_Id) < System.currentTimeMillis() / 1000) {
                    unban.add(key);
                }
            }
        }
        return unban;
    }

    // player list
    public static Integer getPlayerList(String key) {
        return PlayerList.get(key);
    }

    public static void setPlayerList(String key, int value) {
        PlayerList.put(key, value);
    }

    public static boolean keyIsInPlayerList(String key) {
        return PlayerList.containsKey(key);
    }

    public static boolean valueIsInPlayerList(int value) {
        return PlayerList.containsValue(value);
    }

    public static void removePlayerListItem(String key) {
        PlayerList.remove(key);
    }

    public static String getFullPlayerList() {
        return PlayerList.toString();
    }

    public static Set<Entry<String, Integer>> getPlayerListSet() {
        return PlayerList.entrySet();
    }
}
