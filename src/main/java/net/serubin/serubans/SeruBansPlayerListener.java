package net.serubin.serubans;

import java.util.List;
import java.util.UUID;

import net.serubin.serubans.dataproviders.BansDataProvider;
import net.serubin.serubans.util.BanInfo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class SeruBansPlayerListener implements Listener {

	private SeruBans plugin;
	private BansDataProvider db;
	private String banMessage;
	private String tempBanMessage;

	public SeruBansPlayerListener(SeruBans plugin, BansDataProvider db,
			String banMessage, String tempBanMessage) {
		this.plugin = plugin;
		this.db = db;
		this.banMessage = banMessage;
		this.tempBanMessage = tempBanMessage;
	}

	/**
	 * Handles pre-login even, disallowing banned users
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		UUID uuid = player.getUniqueId();

		plugin.printDebug(name + " is attempting to login");

		// Checks if player is banned
		BanInfo banInfo = db.getPlayerBannedInfo(uuid);

		if (banInfo != null) {
			plugin.log.warning(name + " LOGIN DENIED - BANNED");

			String reason = banInfo.getReason();
			String mod = banInfo.getModName();

			event.disallow(
					PlayerLoginEvent.Result.KICK_BANNED,
					plugin.text().GetColor(
							plugin.text()
									.PlayerMessage(banMessage, reason, mod)));
			return;
		}

		// Checks if player is tempbanned
		BanInfo tempbanInfo = db.getPlayerTempBannedInfo(uuid);

		if (tempbanInfo != null) {
			// Checks if tempban is over
			Long length = tempbanInfo.getLength();
			if (length < (System.currentTimeMillis() / 1000)) {
				db.unbanPlayer(uuid);
				return;
			}
			plugin.log.warning(name + " LOGIN DENIED - TEMPBANNED");

			String reason = tempbanInfo.getReason();
			String mod = tempbanInfo.getModName();

			event.disallow(
					PlayerLoginEvent.Result.KICK_BANNED,
					plugin.text().GetColor(
							plugin.text().PlayerTempBanMessage(tempBanMessage,
									reason, mod,
									plugin.text().getStringDate(length))));
		}
	}

	/**
	 * Handles warning players of offline warns given
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		String name = player.getName();
		UUID uuid = player.getUniqueId();
		List<BanInfo> warnInfo = db.getPlayerWarnsInfo(uuid);

		// Checks if players has warns to be notified of
		if (warnInfo == null) {
			return;
		}

		// Goes through warnings and prints them to player
		for (BanInfo warn : warnInfo) {

			plugin.printInfo("Warning player, ban id:"
					+ Integer.toString(warn.getBanId()));

			final String message = plugin.text().GetColor(
					plugin.text().PlayerMessage(plugin.WarnPlayerMessage,
							warn.getReason(), warn.getModName()));

			plugin.getServer().getScheduler()
					.scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							player.sendMessage(message);
						}
					});

			db.removeWarn(warn.getPlayerId(), warn.getBanId());
		}
	}
}
