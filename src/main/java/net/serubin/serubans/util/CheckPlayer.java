package net.serubin.serubans.util;

import org.bukkit.entity.Player;

public class CheckPlayer {

    public CheckPlayer() {
    }

    public static boolean checkPlayer(Player victim, Player player) {
        if (!HashMaps.PlayerList.containsKey(victim.getName().toLowerCase())) {
            if(MySqlDatabase.addPlayer(victim.getName().toLowerCase())){
                MySqlDatabase.addPlayerHash(victim.getName().toLowerCase());
            }
            return true;
        } else {
            return true;
        }
    }
    
    public static boolean checkPlayerOffline(String victim, Player player) {
        if (!HashMaps.PlayerList.containsKey(victim.toLowerCase())) {
            if(MySqlDatabase.addPlayer(victim.toLowerCase())){
                MySqlDatabase.addPlayerHash(victim.toLowerCase());
            }
            return true;
        } else {
            return true;
        }
    }

}
