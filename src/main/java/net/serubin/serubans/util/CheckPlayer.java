package net.serubin.serubans.util;

import net.serubin.serubans.dataproviders.MysqlBansDataProvider;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckPlayer {

    public CheckPlayer() {
        
    }

    public static void checkPlayer(Player victim, CommandSender player) {
        if (!HashMaps.keyIsInPlayerList(player.getName().toLowerCase())) {
            MysqlBansDataProvider.addPlayer(player.getName().toLowerCase());
        }
        if (!HashMaps.keyIsInPlayerList(victim.getName().toLowerCase())) {
            MysqlBansDataProvider.addPlayer(victim.getName().toLowerCase());
        }
    }

    public static void checkPlayerOffline(String victim, CommandSender player) {
        if (!HashMaps.keyIsInPlayerList(player.getName().toLowerCase())) {
            MysqlBansDataProvider.addPlayer(player.getName().toLowerCase());
        }
        if (!HashMaps.keyIsInPlayerList(victim.toLowerCase())) {
            MysqlBansDataProvider.addPlayer(victim.toLowerCase());
        }
    }
}
