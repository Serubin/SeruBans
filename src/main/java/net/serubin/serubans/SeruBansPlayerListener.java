package net.serubin.serubans;

import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class SeruBansPlayerListener implements Listener {

    private SeruBans plugin;
    private String banMessage;

    public SeruBansPlayerListener(SeruBans plugin, String banMessage) {
        this.plugin = plugin;
        this.banMessage = banMessage;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        plugin.log.info(player.getName() + " is attempting to login");
        // checks if player is banned
        if (HashMaps.keyIsInBannedPlayers(player.getName().toLowerCase())) {
            int bId = HashMaps.getBannedPlayers(player.getName().toLowerCase());
            if (HashMaps.keyIsInTempBannedTime(bId)) {
                if (HashMaps.getTempBannedTime(bId) < System
                        .currentTimeMillis() / 1000) {
                    HashMaps.removeBannedPlayerItem(player.getName()
                            .toLowerCase());
                    HashMaps.removeTempBannedTimeItem(bId);
                    MySqlDatabase.updateBan(SeruBans.UNTEMPBAN, bId);
                    return;
                } else {

                }
            }
            plugin.log.warning(player.getName() + " LOGIN DENIED - BANNED");
            int b_Id = HashMaps.getBannedPlayers(
                    player.getName().toLowerCase());
            String reason = MySqlDatabase.getReason(b_Id);
            String mod = MySqlDatabase.getMod(b_Id);

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ArgProcessing
                    .GetColor(ArgProcessing.PlayerMessage(banMessage, reason,
                            mod)));
        }
    }
}
