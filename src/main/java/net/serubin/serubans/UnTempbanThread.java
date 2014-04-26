package net.serubin.serubans;

import java.util.List;

import net.serubin.serubans.dataproviders.BansDataProvider;
import net.serubin.serubans.util.BanInfo;

public class UnTempbanThread implements Runnable {

	private SeruBans plugin;
	private BansDataProvider db;

	public UnTempbanThread(SeruBans plugin, BansDataProvider db) {
		this.plugin = plugin;
		this.db = db;
	}

	public void run() {
		plugin.printDebug("Check tempban thread has started.");
		plugin.printDebug(Long.toString(System.currentTimeMillis() / 1000));

		List<BanInfo> tempbanInfo = db.getTempBans();
		if (tempbanInfo == null) {
			return;
		}

		// checks if temp ban time is up
		for (BanInfo tempban : tempbanInfo) {
			if (tempban.getLength() < (System.currentTimeMillis() / 1000)) {
				db.unbanPlayer(tempban.getPlayerUUID());

				plugin.printDebug(tempban.getPlayerName()
						+ "has been unbanned by per minute tempban checker");
			}
		}

		plugin.printDebug("Check tempban thread has stopped");
	}
}
