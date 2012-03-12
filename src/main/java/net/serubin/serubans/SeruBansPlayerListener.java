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
        //checks if player is banned
        if (HashMaps.getBannedPlayers().containsKey(player.getName().toLowerCase())) {
            int bId = HashMaps.getBannedPlayers()
                    .get(player.getName().toLowerCase());
            if (HashMaps.getTempBanned().containsKey(bId)) {
                if (HashMaps.getTempBanned().get(bId) < System.currentTimeMillis() / 1000){
                    HashMaps.getBannedPlayers().remove(player.getName().toLowerCase());
                    HashMaps.getTempBanned().remove(bId);
                    MySqlDatabase.updateBan(12, bId);
                    return;
                } else {
                    
                }
            }
            plugin.log.warning(player.getName() + " LOGIN DENIED - BANNED");
            int b_Id = HashMaps.getBannedPlayers().get(player.getName()
                    .toLowerCase());
            String reason = MySqlDatabase.getReason(b_Id);
            String mod = MySqlDatabase.getMod(b_Id);

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ArgProcessing.GetColor(ArgProcessing
                    .PlayerMessage(banMessage, reason, mod)));
        }
    }
}
