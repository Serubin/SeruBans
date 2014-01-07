package net.serubin.serubans.util;

import java.util.List;

public class PlayerInfo {
    private int playerId;
    private String playerName;

    private List<BanInfo> bans = null;
    
    public PlayerInfo(int playerId, String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    /**
     * Gets player Id
     * 
     * @return player id
     */
    public int getPlayerId() {
        return this.playerId;
    }

    /**
     * Gets player name
     * 
     * @return player name
     */
    public String getPlayerName() {
        return this.playerName;
    }
}
