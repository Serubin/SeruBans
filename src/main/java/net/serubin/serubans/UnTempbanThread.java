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
        int b_Id;
        List<String> toUnban = HashMaps.getTempBannedTimeUnbans();
        Iterator<String> iterator = toUnban.iterator();
        while(iterator.hasNext()){
           b_Id = HashMaps.getBannedPlayers(iterator.next());
           HashMaps.removeBannedPlayerItem(iterator.next());
           HashMaps.removeTempBannedTimeItem(b_Id);
           MySqlDatabase.updateBan(SeruBans.UNTEMPBAN, b_Id);
           plugin.printDebug(iterator.next() + "has been unbanned by per minute tempban checker");
        }
        plugin.printDebug("Check tempban thread has stopped");
    }

}
