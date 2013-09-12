package net.serubin.serubans.util;

public class BanInfo {

    private int type;
    private int banId;
    private int modId;
    private String modName;
    private int playerId;
    private String playerName;
    private long length;
    private String reason;

    public BanInfo() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBanId() {
        return banId;
    }

    public void setBanId(int banId) {
        this.banId = banId;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
