package net.serubin.serubans;

import java.util.List;

import net.serubin.serubans.dataproviders.IBansDataProvider;
import net.serubin.serubans.dataproviders.MysqlBansDataProvider;
import net.serubin.serubans.util.ArgProcessing;
import net.serubin.serubans.util.BanInfo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class SeruBansPlayerListener implements Listener {

	private SeruBans plugin;
	private IBansDataProvider db;
	private String banMessage;
	private String tempBanMessage;

	public SeruBansPlayerListener(SeruBans plugin, IBansDataProvider db,
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
		String lcName = name.toLowerCase();

		plugin.printDebug(name + " is attempting to login");

		// Checks if player is banned
		BanInfo banInfo = db.getPlayerBannedInfo(lcName);

		if (banInfo != null) {
			plugin.log.warning(name + " LOGIN DENIED - BANNED");

			String reason = banInfo.getReason();
			String mod = banInfo.getModName();

			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ArgProcessing
					.GetColor(ArgProcessing.PlayerMessage(banMessage, reason,
							mod)));
			return;
		}

		// Checks if player is tempbanned
		BanInfo tempbanInfo = db.getPlayerTempBannedInfo(lcName);

		if (tempbanInfo != null) {
			// Checks if tempban is over
			Long length = tempbanInfo.getLength();
			if (length < (System.currentTimeMillis() / 1000)) {
				db.untempBan(tempbanInfo.getBanId());
				return;
			}
			plugin.log.warning(name + " LOGIN DENIED - TEMPBANNED");

			String reason = tempbanInfo.getReason();
			String mod = tempbanInfo.getModName();

			event.disallow(PlayerLoginEvent.Result.KICK_BANNED, ArgProcessing
					.GetColor(ArgProcessing.PlayerTempBanMessage(
							tempBanMessage, reason, mod,
							ArgProcessing.getStringDate(length))));
		}
	}

	/**
	 * Handles warning players of offline warns given
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		String name = player.getName();
		String lcName = name.toLowerCase();
		List<BanInfo> warnInfo = db.getPlayerWarnsInfo(lcName);

		// Checks if players has warns to be notified of
		if (warnInfo == null) {
			return;
		}

		// Goes through warnings and prints them to player
		for (BanInfo warn : warnInfo) {

			plugin.printInfo("Warning player, ban id:"
					+ Integer.toString(warn.getBanId()));

			final String message = ArgProcessing.GetColor(ArgProcessing
					.PlayerMessage(plugin.WarnPlayerMessage, warn.getReason(),
							warn.getModName()));

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
