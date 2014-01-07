package net.serubin.serubans.dataproviders;

import java.util.List;

import net.serubin.serubans.util.BanInfo;

public interface IBansDataProvider {
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
     * Get a list of tempBanned players.
     * 
     * @return A list of tempbans in BanInfo type
     */
    public List<BanInfo> getTempBans();

    /**
     * UntempBan a player
     * 
     * @param banId
     *            BanId
     */
    public boolean untempBan(int banId);

    /**
     * Gets all warns for given player
     * 
     * @param name
     *            Player name
     * @return a list of warns. Null if none
     */
    public List<BanInfo> getPlayerWarnsInfo(String name);

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
     * Adds player
     * 
     * @param player
     *            Player name
     */
    public boolean addPlayer(String player);

    /**
     * Gets last ban id logged
     * 
     * @return ban id
     */
    public int getLastBanId();

    public boolean unbanPlayer(String victim);
}
