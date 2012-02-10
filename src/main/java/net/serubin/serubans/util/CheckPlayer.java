package net.serubin.serubans.util;

import java.util.Map;

import org.bukkit.entity.Player;

public class CheckPlayer {
    static MySqlDatabase db;

    private static Map<String, Integer> playerList;

    public CheckPlayer(Map<String, Integer> playerList) {
        this.playerList = playerList;
    }

    public static boolean checkPlayer(Player victim, Player player) {
        if (!playerList.containsValue(victim)) {
            MySqlDatabase.addPlayer(victim);
            player.sendMessage("ERROR! Please see console for details.");
        } else {
            return true;
        }
        return false;
    }

}
