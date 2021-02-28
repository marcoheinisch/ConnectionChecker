# CheckWlan

## Your Wlan crashs all 45 minutes?
This Java program checks your WiFi connection and issues a Windows notification in the event of a WiFi crash. If the crashes occur cyclically (like every 45 min), the next WiFi interruption is predicted and a notification is triggered shortly before the next interruption.

By automatically switching to mobile hotspot (or another WiFi) and back, users can make a video call (e.g uni lecture) with unstable WiFi.

## Configuration (first step)
Just create a config.txt file with this content:

    
    ip_router="666.666.666.1"
    ip_test="142.250.185.99"
    
    ssid_wlan="Vodafone-XXXX"
    ssid_hotspot="MyMobileHotspot"
    
    crash_period_minutes=45
    hotspot_period_minutes=5
    
crash_period_minutes: Time between cyclically crashes.
hotspot_period_minutes: Time after which a reconnection to wifi is attempted.

## Run it
Trayicon with menu (crash detected):

![grafik](https://user-images.githubusercontent.com/57726217/109417338-86146800-79c3-11eb-8de0-355c9883194b.png)

Trayicon (no crash detected):

![grafik](https://user-images.githubusercontent.com/57726217/109418114-e9ec6000-79c6-11eb-804f-e9d290372752.png)
