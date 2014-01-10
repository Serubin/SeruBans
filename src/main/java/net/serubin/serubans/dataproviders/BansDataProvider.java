package net.serubin.serubans.dataproviders;

import java.util.List;

import net.serubin.serubans.util.BanInfo;

public interface BansDataProvider {
    /**
     * Gets ban info for player name on current ban
     * 
     * <pre>
     *  Note getPlayerTempBannedInfo
     * </pre>
     * 
     * @param name
     *            Player name
     * @return BanInfo Type for ban
     */
    public BanInfo getPlayerBannedInfo(String name);

    /**
     * Gets ban info for player name on current tempban
     * 
     * <pre>
     *  Note getPlayerBannedInfo
     * </pre>
     * 
     * @param name
     *            Player name
     * @return BanInfo Type for tempban
     */
    public BanInfo getPlayerTempBannedInfo(String name);

    /**
     * Gets all warns for given player
     * 
     * @param name
     *            Player name
     * @return a list of warns. Null if none
     */
    public List<BanInfo> getPlayerWarnsInfo(String name);

    /**
     * Gets all player ban data
     * 
     * @param name
     *            player name
     * @return ban data
     */
    public List<BanInfo> getPlayerInfo(String name);

    /**
     * Gets all player ban data for type
     * 
     * @param name
     *            player name
     * @return ban data
     */
    public List<BanInfo> getPlayerInfo(String name, int type);

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
     * Will create new id if the player is not in the database
     * @param name player name
     * @return player id
     */
    public int getPlayer(String name);

    /**
     * Checks if player is banned
     * 
     * @param name
     *            player
     * @return true : false
     */
    public boolean getPlayerStatus(String name);

    /**
     * Get BanInfo for players current ban.
     * 
     * @param name
     *            player
     * @return null of not found.
     */
    public BanInfo getCurrentBan(String name);

    /**
     * Unban a player (temp or perm ban)
     * 
     * @param victim
     *            player
     */
    public boolean unbanPlayer(String victim);

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
    public boolean addBan(String victim, int type, long length, String mod,
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
    public int getCurrentBanId(String name);

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
