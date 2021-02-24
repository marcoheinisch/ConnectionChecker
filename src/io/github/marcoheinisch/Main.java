package io.github.marcoheinisch;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static final int CHECK_PERIOD_SEC = 20;

    /* Example config.txt:
    |
    | ip_router="666.666.666.1"
    | testip="172.217.16.195"
    |
    | ssid_wlan="Vodafone-XXXX"
    | ssid_hotspot="MyMobileHotspot"
    |
    | crash_period_minutes=45
    | hotspot_period_minutes=5
    |
    */

    public static void main(String[] args) {
        CheckWlan wlan = new CheckWlan();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                wlan.makeCheck();
            }
        }, 0, CHECK_PERIOD_SEC * 1000);
    }
}
