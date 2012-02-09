package net.serubin.serubans.util;

import java.util.Map;

import org.bukkit.entity.Player;

public class CheckPlayer {
	static MySqlDatabase db;

	private static Map<Integer, String> playerList;

	public CheckPlayer(Map<Integer, String> playerList) {
		this.playerList = playerList;
	}
	public static boolean checkPlayer(Player victim, Player player){
		if(!playerList.containsValue(victim)){
			if(!db.addPlayer(victim)){
				player.sendMessage("ERROR! Please see console for details.");
			}
		}
		else
		{
		return true;
		}
		return false;
	}


}
