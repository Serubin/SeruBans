package net.serubin.serubans.dataproviders;

import java.util.List;
import java.util.UUID;

import net.serubin.serubans.util.BanInfo;

public interface BansDataProvider {
	/**
	 * Gets ban info for player name on current ban
	 * 
	 * <pre>
	 *  Note getPlayerTempBannedInfo
	 * </pre>
	 * 
	 * @param player
	 *            Player uuid
	 * @return BanInfo Type for ban
	 */
	public BanInfo getPlayerBannedInfo(UUID player);

	/**
	 * Gets ban info for player name on current tempban
	 * 
	 * <pre>
	 *  Note getPlayerBannedInfo
	 * </pre>
	 * 
	 * @param player
	 *            Player uuid
	 * @return BanInfo Type for tempban
	 */
	public BanInfo getPlayerTempBannedInfo(UUID player);

	/**
	 * Gets all warns for given player
	 * 
	 * @param player
	 *            Player uuid
	 * @return a list of warns. Null if none
	 */
	public List<BanInfo> getPlayerWarnsInfo(UUID player);

	/**
	 * Gets all player ban data
	 * 
	 * @param player
	 *            player uuid
	 * @return ban data
	 */
	public List<BanInfo> getPlayerInfo(UUID player);

	/**
	 * Gets all player ban data for type
	 * 
	 * @param player
	 *            player uuid
	 * @return ban data
	 */
	public List<BanInfo> getPlayerInfo(UUID player, int type);

	/**
	 * Gets ban info for id
	 * 
	 * @param id
	 *            ban id
	 * @return null if not found
	 */
	public BanInfo getBanInfo(int id);

	/**
	 * Get player name from id
	 * 
	 * <pre>
	 *  will return null if it doesn't exist.
	 *  use getPlayer(String name) to add a player to the database.
	 * </pre>
	 * 
	 * @param id
	 *            player id
	 * @return player name
	 */
	public String getPlayer(int id);

	/**
	 * Get player id from name
	 * 
	 * <pre>
	 *  Will create new id if the player is not in the database
	 * @param player player uuid
	 * @return player id
	 */
	public int getPlayer(UUID player);

	/**
	 * Checks if player is banned
	 * 
	 * @param player
	 *            player uuid
	 * @return true : false
	 */
	public boolean getPlayerStatus(UUID player);

	/**
	 * Get BanInfo for players current ban.
	 * 
	 * @param player
	 *            player uuid
	 * @return null of not found.
	 */
	public BanInfo getCurrentBan(UUID player);

	/**
	 * Gets uuid from internal database
	 * 
	 * @param id
	 *            player
	 * @return uuid or null
	 */
	public UUID getUUID(int id);

	/**
	 * Unban a player (temp or perm ban)
	 * 
	 * @param player
	 *            player uuid
	 */
	public boolean unbanPlayer(UUID player);

	/**
	 * Adds ban
	 * 
	 * @param victim
	 *            Player being banned
	 * @param type
	 *            Ban type
	 * @param length
	 *            (if tempban) Length of ban
	 * @param mod
	 *            Player that banned
	 * @param reason
	 *            Ban reason
	 * @param display
	 */
	public boolean addBan(UUID victim, int type, long length, UUID mod,
			String reason, int display);

	/**
	 * Update ban reason
	 * 
	 * @param id
	 *            ban id
	 * @param reason
	 *            ban reason
	 * @return
	 */
	public boolean updateReason(int id, String reason);

	/**
	 * Add warn to database
	 * 
	 * @param playerId
	 *            player
	 * @param banId
	 *            warn id
	 */
	public boolean addWarn(int playerId, int banId);

	/**
	 * Remove warn warning
	 * 
	 * @param playerId
	 *            player Id
	 * @param banId
	 *            warn Id
	 */
	public boolean removeWarn(int playerId, int banId);

	/**
	 * 
	 * Retrieves a list of permanent bans
	 * 
	 * @return list of bans
	 */
	public List<BanInfo> getPermBans();

	/**
	 * Retrieves a list of temporary bans
	 * 
	 * @return a list of bans
	 */
	public List<BanInfo> getTempBans();

	/**
	 * Get ban Id based on player name and status
	 * 
	 * @param name
	 * @param status
	 * @return
	 */
	public int getCurrentBanId(UUID player);

	/**
	 * Validate a ban id
	 * 
	 * @param ban
	 *            Id
	 * @return true : false
	 */
	public boolean validateBanId(int id);

	/**
	 * Gets last ban id logged
	 * 
	 * @return ban id
	 */
	public int getLastBanId();

	public void maintainConnection();
}
