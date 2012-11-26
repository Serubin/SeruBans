package net.serubin.serubans.search;

import org.bukkit.command.CommandSender;

import net.serubin.serubans.util.MySqlDatabase;

public class DataManager {

    private DisplayManager dm = null;

    public DataManager() {
        dm = new DisplayManager();
    }

    public boolean searchPlayer(String player, CommandSender sender) {
        String name = MySqlDatabase.getPlayer(player);
        String[] names = name.split(",");
        if (names.length > 1) {
            dm.sendLine(sender, dm.createTitle("Player Search"));
            dm.sendLine(sender, "&cDid you mean:");
            for (int i = 0; i < names.length; i++) {
                dm.sendLine(sender, "&6" + names[i]);
            }
            dm.sendLine(sender, "&cRe-type command with one of these names...");
            return false;
        } else {
            return true;
        }

    }

    public void getPlayerData() {

    }
}
