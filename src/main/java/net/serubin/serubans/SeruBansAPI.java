package net.serubin.serubans;

import net.serubin.serubans.util.HashMaps;

public class SeruBansAPI {

    private SeruBans plugin;

    public SeruBansAPI(SeruBans plugin) {
        this.plugin = plugin;
    }

    /*
     * Returns if the player is banned.
     * 
     * @param player - Player to be queried
     * 
     * @return true is player is banned, false if not found or not banned
     * 
     * @since 2.0.1
     */
    public boolean checkBan(String player) {

        if (HashMaps.keyIsInBannedPlayers(player.toLowerCase())) {
            return true;
        } else {
            return false;
        }

    }
    
    /*
     * Returns length of ban.
     * 
     * @param player - Player to be queried
     * 
     * @return Long or null if not tempbanned
     * 
     * @since 2.0.1
     */
    
    public Long getLength(String player){
        
        int id = HashMaps.getBannedPlayers(player.toLowerCase());
        if(id == 0){
            return null;
        }
        long EndDate = HashMaps.getTempBannedTime(id);
        long Length = EndDate - System.currentTimeMillis() / 1000;
        
        return Length;
    }
}
