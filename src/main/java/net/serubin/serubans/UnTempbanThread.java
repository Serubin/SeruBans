package net.serubin.serubans;

import java.util.List;
import net.serubin.serubans.util.BanInfo;
import net.serubin.serubans.util.MySqlDatabase;

public class UnTempbanThread implements Runnable {

    private SeruBans plugin;

    public UnTempbanThread(SeruBans plugin) {
        this.plugin = plugin;
    }

    public void run() {
        plugin.printDebug("Check tempban thread has started.");
        plugin.printDebug(Long.toString(System.currentTimeMillis()/1000));

        List<BanInfo> tempbanInfo = MySqlDatabase.getTempBans();
        if (tempbanInfo == null) {
            return;
        }

        // checks if temp ban time is up
        for (BanInfo tempban : tempbanInfo) {
            if (tempban.getLength() < (System.currentTimeMillis() / 1000)) {
                MySqlDatabase.updateBan(SeruBans.UNTEMPBAN, tempban.getBanId());
                plugin.printDebug(tempban.getPlayerName() + "has been unbanned by per minute tempban checker");
            }
        }

        plugin.printDebug("Check tempban thread has stopped");
    }
}
