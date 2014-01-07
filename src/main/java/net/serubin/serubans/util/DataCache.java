package net.serubin.serubans.util;

public class DataCache {
    private TwoWayHashMap<String, Integer> PlayerList = new TwoWayHashMap<String, Integer>();

    public DataCache() {

    }

    public void addPlayer(int id, String name) {
        PlayerList.add(name, id);
    }

    public String getPlayerName(int id) {
        return (String) PlayerList.get(id);
    }

    public int getPlayerId(String name) {
        return (Integer) PlayerList.get(name);
    }

    public boolean checkPlayer(String name) {
        return PlayerList.contains(name);
    }

    public boolean checkPlayer(int id) {
        return PlayerList.contains(id);
    }

}
