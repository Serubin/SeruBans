package net.serubin.serubans.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckPlayer {

    public CheckPlayer() {
    }

    public static void checkPlayer(Player victim, CommandSender player) {
        if (!HashMaps.getPlayerList().containsKey(victim.getName().toLowerCase())) {
            MySqlDatabase.addPlayer(victim.getName().toLowerCase());
        }
    }

    public static void checkPlayerOffline(String victim, CommandSender player) {
        if (!HashMaps.getPlayerList().containsKey(victim.toLowerCase())) {
            MySqlDatabase.addPlayer(victim.toLowerCase());
        }
    }
}
