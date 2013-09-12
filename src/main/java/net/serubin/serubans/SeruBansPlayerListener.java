package net.serubin.serubans;

import java.util.List;

import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.BanInfo;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class SeruBansPlayerListener implements Listener {

    private SeruBans plugin;
    private String banMessage;
    public boolean tempban = false;
    private String tempBanMessage;

    public SeruBansPlayerListener(SeruBans plugin, String banMessage,
            String tempBanMessage) {
        this.plugin = plugin;
        this.banMessage = banMessage;
        this.tempBanMessage = tempBanMessage;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        tempban = false;

        Player player = event.getPlayer();
        String name = player.getName();
        String lcName = name.toLowerCase();

        plugin.printDebug(name + " is attempting to login");

        // checks if player is banned
        BanInfo banInfo = MySqlDatabase.getPlayerBannedInfo(lcName);
        if (banInfo != null) {
            plugin.log.warning(name + " LOGIN DENIED - BANNED");
            String reason = banInfo.getReason();
            String mod = banInfo.getModName();
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                       ArgProcessing.GetColor(ArgProcessing.PlayerMessage(
                               banMessage, reason, mod)));
            return;
        }

        // checks if player is tempbanned
        BanInfo tempbanInfo = MySqlDatabase.getPlayerTempBannedInfo(lcName);
        if (tempbanInfo != null) {
            plugin.printDebug(name + "is tempbanned");
            Long length = tempbanInfo.getLength();
            if (length < (System.currentTimeMillis() / 1000)) {
                MySqlDatabase.updateBan(SeruBans.UNTEMPBAN, tempbanInfo.getBanId());
                return;
            }
            String reason = tempbanInfo.getReason();
            String mod = tempbanInfo.getModName();
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                    ArgProcessing.GetColor(ArgProcessing
                            .PlayerTempBanMessage(tempBanMessage, reason,
                                    mod,
                                    ArgProcessing.getStringDate(length))));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        int pId = 0;
        if (!HashMaps.keyIsInPlayerList(player.getName().toLowerCase())) {
            return;
        }
        pId = HashMaps.getPlayerList(player.getName().toLowerCase());
        if (HashMaps.isWarn(pId)) {
            final List<Integer> bId = HashMaps.getWarn(pId);
            for (int i : bId) {
                SeruBans.printInfo("Warning player, ban id:"
                        + Integer.toString(i));
                final String message = ArgProcessing.GetColor(ArgProcessing
                        .PlayerMessage(SeruBans.WarnPlayerMessage,
                                MySqlDatabase.getReason(i),
                                MySqlDatabase.getMod(i)));
                plugin.getServer().getScheduler()
                        .scheduleSyncDelayedTask(plugin, new Runnable() {
                            public void run() {
                                player.sendMessage(message);
                            }
                        });
                MySqlDatabase.removeWarn(pId, i);
                HashMaps.remWarn(pId);
            }
        }

    }
}
