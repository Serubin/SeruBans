package net.serubin.serubans;

import java.util.Map;

import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class SeruBansPlayerListener implements Listener {

	private Map<String, Integer> bannedPlayers;
	private SeruBans plugin;
	private Map<Integer, String> playerList;
	private String banMessage;

	public SeruBansPlayerListener(Map<String, Integer> bannedPlayers,
			Map<Integer, String> playerList, String banMessage, SeruBans plugin) {
		// TODO Auto-generated constructor stub
		this.bannedPlayers = bannedPlayers;
		this.playerList = playerList;
		this.banMessage = banMessage;
		this.plugin = plugin;
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		if (bannedPlayers.containsKey(player.getName())) {
			int b_Id = bannedPlayers.get(player.getName());
			String reason = MySqlDatabase.getReason(b_Id);
			String mod = MySqlDatabase.getMod(b_Id);
			String kickMsg = ArgProcessing.GetColor(ArgProcessing.PlayerMessage(banMessage,
					reason, mod));
			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMsg);
		}
	}
}
