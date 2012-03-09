package net.serubin.serubans;

import java.util.Map;

import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class SeruBansPlayerListener implements Listener {

    private static SeruBans plugin;
    private String banMessage;

    public SeruBansPlayerListener(SeruBans plugin, String banMessage) {
        this.plugin = plugin;
        this.banMessage = banMessage;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        SeruBans.printInfo(player.getName() + " is attempting to login");

        if (HashMaps.BannedPlayers.containsKey(player.getName().toLowerCase())) {
            boolean untempban = false;
            int bId = HashMaps.BannedPlayers
                    .get(player.getName().toLowerCase());
            if (HashMaps.TempBanned.containsKey(bId)) {
                long tempTime = HashMaps.TempBanned.get(bId);
                long now = System.currentTimeMillis() / 1000;
                long diff = tempTime - now;
                //TODO Fix this process
                if (diff <= 0) {
                    HashMaps.BannedPlayers.remove(player.getName().toLowerCase());
                    HashMaps.TempBanned.remove(bId);
                    MySqlDatabase.updateBan(12, bId);
                    untempban = true;
                }
            }
            if(!untempban){
            SeruBans.printInfo(player.getName() + " LOGIN DENIED - BANNED");
            int b_Id = HashMaps.BannedPlayers.get(player.getName()
                    .toLowerCase());
            String reason = MySqlDatabase.getReason(b_Id);
            String mod = MySqlDatabase.getMod(b_Id);

            String kickMsg = ArgProcessing.GetColor(ArgProcessing
                    .PlayerMessage(banMessage, reason, mod));
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMsg);
            }
        }
    }
}
