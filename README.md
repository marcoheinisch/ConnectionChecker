# CheckWlan

## Your Wlan crashs all 45 minutes?
This java programm checks your wlan connection and throws a windows notification just before the next crash.

## How to use it?
Just create a config.txt file with this content:

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
    


replace the router-ip with yours and run Main.java

