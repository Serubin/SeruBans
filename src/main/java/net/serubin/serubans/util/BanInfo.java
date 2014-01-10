package net.serubin.serubans.util;

import net.serubin.serubans.dataproviders.BansDataProvider;

public class BanInfo {

    private int banId;
    private int playerId;
    private int type;
    private int modId;
    private long length;
    private String reason;

    private String modName;
    private String playerName;
    private long date;

    public BanInfo(int banId, int playerId, int type, int modId, String reason,
            BansDataProvider db) {
        this.banId = banId;
        this.playerId = playerId;
        this.type = type;
        this.modId = modId;
        this.reason = reason;

        setPlayerName(db.getPlayer(playerId));
        setModName(db.getPlayer(modId));
    }

    public BanInfo(int banId, int playerId, int type, int modId, long length,
            String reason, BansDataProvider db) {
        this.banId = banId;
        this.playerId = playerId;
        this.type = type;
        this.modId = modId;
        this.length = length;
        this.reason = reason;

        setPlayerName(db.getPlayer(playerId));
        setModName(db.getPlayer(modId));
    }

    public BanInfo(int banId, int playerId, String playerName, int type,
            int modId, String modName, long length, long date, String reason,
            BansDataProvider db) {
        this.banId = banId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.type = type;
        this.modId = modId;
        this.modName = modName;
        this.length = length;
        this.date = date;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
