package net.serubin.serubans.util;

public class BanInfo {

    private int banId;
    private int playerId;
    private int type;
    private int modId;
    private long length;
    private String reason;

    private String modName;
    private String playerName;

    public BanInfo(int banId, int playerId, int type, int modId, String reason) {
        this.banId = banId;
        this.playerId = playerId;
        this.type = type;
        this.modId = modId;
        this.reason = reason;
    }

    public BanInfo(int banId, int playerId, int type, int modId, long length,
            String reason) {
        this.banId = banId;
        this.playerId = playerId;
        this.type = type;
        this.modId = modId;
        this.length = length;
        this.reason = reason;
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
