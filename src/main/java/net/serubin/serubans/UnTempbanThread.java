package net.serubin.serubans;

import java.util.Iterator;
import java.util.List;
import net.serubin.serubans.util.HashMaps;
import net.serubin.serubans.util.MySqlDatabase;

public class UnTempbanThread implements Runnable {

    private SeruBans plugin;

    public UnTempbanThread(SeruBans plugin) {
        this.plugin = plugin;
    }

    public void run() {
        plugin.printDebug("Check tempban thread has started.");
        plugin.printDebug(Long.toString(System.currentTimeMillis()/1000));
        int b_Id;
        List<String> toUnban = HashMaps.getTempBannedTimeUnbans();
        Iterator<String> iterator = toUnban.iterator();
        while(iterator.hasNext()){
           String player = iterator.next();
           b_Id = HashMaps.getBannedPlayers(player);
           HashMaps.removeBannedPlayerItem(player);
           HashMaps.removeTempBannedTimeItem(b_Id);
           MySqlDatabase.updateBan(SeruBans.UNTEMPBAN, b_Id);
           plugin.printDebug(player + "has been unbanned by per minute tempban checker");
        }
        plugin.printDebug("Check tempban thread has stopped");
    }

}
