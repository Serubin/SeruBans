package net.serubin.serubans.search;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.serubin.serubans.SeruBans;
import net.serubin.serubans.dataproviders.BansDataProvider;
import net.serubin.serubans.util.BanInfo;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SearchMethods {

	private SeruBans plugin;
	private BansDataProvider db;
	private DisplayManager dm;

	public SearchMethods(SeruBans plugin, BansDataProvider db) {
		this.plugin = plugin;
		this.db = db;
		this.dm = new DisplayManager(plugin);
	}

	/*
	 * handels PLAYER searchs
	 */
	public boolean searchPlayer(String player, UUID uuid, CommandSender sender) {
		List<BanInfo> BanTypes = null;
		try {
			BanTypes = db.getPlayerInfo(uuid);
		} catch (NullPointerException NPE) {
			plugin.printDebug(NPE.toString());
		}
		if (BanTypes == null || BanTypes.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "No Results");
			return true;
		}
		Iterator<BanInfo> iterator = BanTypes.iterator();
		int TempBans = 0;
		int Bans = 0;
		int Kicks = 0;
		int Warns = 0;
		while (iterator.hasNext()) {
			BanInfo Next = iterator.next();
			if (Next.getType() == SeruBans.BAN)
				Bans++;
			if (Next.getType() == SeruBans.KICK)
				Kicks++;
			if (Next.getType() == SeruBans.TEMPBAN)
				TempBans++;
			if (Next.getType() == SeruBans.WARN)
				Warns++;
			if (Next.getType() == SeruBans.UNBAN)
				Bans++;
			if (Next.getType() == SeruBans.UNTEMPBAN)
				TempBans++;
		}
		String Banned = "not banned";
		if (db.getPlayerStatus(uuid)) {
			Banned = "banned";
		}
		dm.sendLine(sender, dm.createTitle(player));
		dm.sendLine(sender, " Bans: " + ChatColor.GOLD + Bans);
		dm.sendLine(sender, " TempBans: " + ChatColor.GOLD + TempBans);
		dm.sendLine(sender, " Kicks: " + ChatColor.GOLD + Kicks);
		dm.sendLine(sender, " Warns: " + ChatColor.GOLD + Warns);
		dm.sendLine(sender, " Is " + ChatColor.GOLD + Banned);
		if (Banned == "banned")
			dm.sendLine(
					sender,
					" Current Ban Id: " + ChatColor.GOLD
							+ db.getCurrentBanId(uuid));

		return true;
	}

	/*
	 * Handles out put for TYPE searches
	 */
	public boolean searchType(String player, UUID uuid, int type,
			CommandSender sender) {
		List<BanInfo> PlayerInfo = null;
		try {
			PlayerInfo = db.getPlayerInfo(uuid, type);
		} catch (NullPointerException NPE) {
			plugin.printDebug(NPE.toString());
			return true;
		}
		// Handles unbans
		if (type == SeruBans.BAN) {
			PlayerInfo.addAll(db.getPlayerInfo(uuid, SeruBans.UNBAN));
		}
		if (type == SeruBans.TEMPBAN) {
			PlayerInfo.addAll(db.getPlayerInfo(uuid, SeruBans.UNTEMPBAN));
		}

		if (PlayerInfo == null || PlayerInfo.isEmpty()) {
			sender.sendMessage(ChatColor.RED + "No Results");
			return true;
		}

		Iterator<BanInfo> playerInfoIterator = PlayerInfo.iterator();

		dm.sendLine(
				sender,
				dm.createTitle(player + " Type:"
						+ plugin.text().getBanTypeString(type)));
		dm.sendLine(sender, "ID - Mod - Date - (Length) - Reason");
		while (playerInfoIterator.hasNext()) {
			BanInfo info = playerInfoIterator.next();
			String line = info.getBanId()
					+ " - "
					+ info.getModName()
					+ " - "
					+ plugin.text().getStringDate(info.getDate(),
							"MM/dd/yy HH:mm:yy");
			if (type == SeruBans.TEMPBAN) {
				line = line + " - "
						+ plugin.text().getStringDate(info.getLength());
			}
			line = line + " - " + info.getReason();
			dm.sendLine(sender, line);
		}
		return true;
	}

	/*
	 * Handles ID search output
	 */
	public boolean searchId(int id, CommandSender sender) {
		BanInfo BanId = null;
		try {
			BanId = db.getBanInfo(id);
		} catch (NullPointerException NPE) {
			plugin.printDebug(NPE.toString());
			return true;
		}
		if (BanId == null) {
			sender.sendMessage(ChatColor.RED + "No Results");
			return true;
		}
		dm.sendLine(sender, dm.createTitle("Id:" + id));
		dm.sendLine(sender, "Player: " + ChatColor.GOLD + BanId.getPlayerName());
		dm.sendLine(sender, "Mod: " + ChatColor.GOLD + BanId.getModName());
		dm.sendLine(sender, "Type: " + ChatColor.GOLD
				+ plugin.text().getBanTypeString(BanId.getType()));
		dm.sendLine(sender, "Date: " + ChatColor.GOLD
				+ plugin.text().getStringDate(BanId.getDate()));
		if (BanId.getLength() != 0) {
			dm.sendLine(
					sender,
					"Length: "
							+ ChatColor.GOLD
							+ plugin.text().getStringDate(BanId.getLength(),
									"MM/dd/yy HH:mm:yy"));
		}
		dm.sendLine(sender, "Reason: " + ChatColor.GOLD + BanId.getReason());
		dm.sendLine(
				sender,
				"Is Banned? " + ChatColor.GOLD
						+ db.getPlayerStatus(BanId.getPlayerUUID()));
		return true;
	}
}
