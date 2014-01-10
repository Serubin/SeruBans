package net.serubin.serubans.dataproviders;

public class DataProviderTimers implements Runnable {

    private BansDataProvider db;

    public DataProviderTimers(BansDataProvider db) {
        this.db = db;
    }

    public void run() {
        db.maintainConnection();
    }

}
