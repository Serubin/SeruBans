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
    private static List<Integer> BanIds = new ArrayList<Integer>();
    private static Map<Integer, Long> TempBannedTime = new HashMap<Integer, Long>();

    /*
     * id list
     */
    /**
     * Checks BanIds for id
     * 
     * @param id
     * @return boolean
     */
    public static boolean checkId(int id) {
        return BanIds.contains(id);
    }

    /**
     * Add id to BanIds
     * 
     * @param id
     */
    public static void setIds(int id) {
        BanIds.add(id);
    }

    /**
     * Get all Ids
     * 
     * @return String
     */
    public static String getFullIds() {
        return BanIds.toString();
    }

    /*
     * banned players
     */
    /**
     * Get ban id of a banned player
     * 
     * @param name
     * @return id
     */
    public static Integer getBannedPlayers(String name) {
        return BannedPlayers.get(name);
    }

    /**
     * Add new key to BannedPlayers
     * 
     * @param key
     *            name
     * @param value
     *            id
     */
    public static void setBannedPlayers(String key, int value) {
        BannedPlayers.put(key, value);
    }

    /**
     * Contains id?
     * 
     * @param value
     *            id
     * @return boolean
     */
    public static boolean valueIsInBannedPlayers(int value) {
        return BannedPlayers.containsValue(value);
    }

    /**
     * Contains player?
     * 
     * @param key
     *            player name
     * @return boolean
     */
    public static boolean keyIsInBannedPlayers(String key) {
        return BannedPlayers.containsKey(key);
    }

    /**
     * Remove banned player
     * 
     * @param key
     *            player name
     */
    public static void removeBannedPlayerItem(String key) {
        BannedPlayers.remove(key);
    }

    /**
     * Get full banned player list
     * 
     * @return String
     */
    public static String getFullBannedPlayers() {
        return BannedPlayers.toString();
    }

    /**
     * Get full banned player list via entrySet()
     * 
     * @return Set<Entry<String, Integer>>
     */
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
    /**
     * Get tempban time
     * 
     * @param key
     *            id
     * @return Long
     */
    public static Long getTempBannedTime(int key) {
        return TempBannedTime.get(key);
    }

    /**
     * Add tempban
     * 
     * @param key
     *            ban id
     * @param value
     *            length
     */
    public static void setTempBannedTime(int key, Long value) {
        TempBannedTime.put(key, value);
    }

    /**
     * Contains id?
     * 
     * @param key
     *            id
     * @return boolean
     */
    public static boolean keyIsInTempBannedTime(int key) {
        return TempBannedTime.containsKey(key);
    }

    /**
     * Contains time?
     * 
     * @param value
     *            length
     * @return boolean
     */
    public static boolean valueIsInTempBannedTime(Long value) {
        return TempBannedTime.containsValue(value);
    }

    /**
     * Remove tempban time
     * 
     * @param key
     *            id
     */
    public static void removeTempBannedTimeItem(int key) {
        TempBannedTime.remove(key);
    }

    /**
     * Get full tempban list
     * 
     * @return String
     */
    public static String getFullTempBannedTime() {
        return TempBannedTime.toString();
    }

    /**
     * Get full tempban list via entrySet
     * 
     * @return Set<Entry<Integer, Long>>
     */
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
    /**
     * get player id
     * 
     * @param key
     *            name
     * @return id
     */
    public static Integer getPlayerList(String key) {
        return PlayerList.get(key);
    }

    /**
     * add player
     * 
     * @param key
     *            player name
     * @param value
     *            id
     */
    public static void setPlayerList(String key, int value) {
        PlayerList.put(key, value);
    }
/**
 * Contains key?
 * @param key name
 * @return boolean
 */
    public static boolean keyIsInPlayerList(String key) {
        return PlayerList.containsKey(key);
    }
/**
 * Contains value?
 * @param value
 * @return boolean
 */
    public static boolean valueIsInPlayerList(int value) {
        return PlayerList.containsValue(value);
    }
/**
 * Remove player from list
 * @param key name
 */
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
