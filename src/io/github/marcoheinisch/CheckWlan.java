package io.github.marcoheinisch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

class CheckWlan{
  private Config c;

  private final SimpleDateFormat FORMAT = new SimpleDateFormat("HH:mm:ss z"); //or "HH:mm:ss z"
  private final SimpleDateFormat FORMAT_SMALL = new SimpleDateFormat("HH:mm"); //or "HH:mm:ss z"
  private LogFile logfile = new LogFile();
  private WinNotification winnotific = new WinNotification("CheckWlan-Info");
  private boolean[] laststatus = new boolean[]{true, true};
  private Date[] lastdisconnect, lastprediction;

  public CheckWlan(){
    this.c = new Config();
    changeWLAN(c.get("ssid_wlan"));

    this.lastdisconnect = new Date[] {new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())};
    this.lastprediction = new Date[] {new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())};

    winnotific.changeTrayToolTip("Nächster Ausfall noch nicht vorhersehbar.");
    winnotific.displayTrayMessage("CheckWlan ist aktiv.");
  }

  /**
   * Makes ping-request and compares last connection status with the current connection status.
   */
  public void makeCheck(){
    boolean[] status;
    Object estatus = "";

    try {
      status = new boolean[]{ping(c.get("ip_test")), ping(c.get("ip_router"))};
    } catch (IOException e) {
      status = new boolean[]{false, false};
      estatus = e;
    }

    Date date = new Date(System.currentTimeMillis());
    log(FORMAT.format(date), status[0], status[1], estatus);


    if(laststatus[0] && laststatus[1]){
      // Last status: internet, wlan
      if (!status[0]){
        // no internet, wlan
        this.lastdisconnect[0] = date;
        log("##change##",   "11 -> 01/00   : Wlan-Ausfall.");
        winnotific.displayTrayMessage("Wlan-Ausfall registriert!");
        if (winnotific.isHotspotusable()){
          changeWLAN(c.get("ssid_hotspot"));
        }
        winnotific.setImage(true);
      }
    }else{
      // Last status: other cases
      if(status[0]){
        if(status[1]){
          // internet, wlan
          log("##change##", "00/01/10 -> 11: Wlan-Ausfall vorbei.");
          this.lastdisconnect[1] = date;
          winnotific.displayTrayMessage("Wlan-Ausfall vorbei.");
          winnotific.setImage(false);
          prepareNextDisconnect();
        }else{
          // internet, but not with wlan connected
          long hotspottime = (date.getTime()-this.lastdisconnect[0].getTime())/(1000);//in seconds
          int hotspotperiod = (winnotific.isHotspotusable()? c.getInt("hotspot_period_minutes")*60 : 0);
          if(hotspottime > hotspotperiod){// && (hotspottime-hotspotperiod) < c.getInt("crash_period_minutes")){
            log("##change##", "10 && timeout : Teste Wlan.");
            if (winnotific.isReconnect()) changeWLAN(c.get("ssid_wlan"));
            winnotific.setImage(false);
          }
        }
      }else{
        if(status[1]){
          // no internet but wlan connected
          this.lastdisconnect[0] = date;
          log("##change##", "00/01 -> 01   : Wlan-Ausfall dauert an.");
          if (winnotific.isHotspotusable()) changeWLAN(c.get("ssid_hotspot"));
          winnotific.setImage(true);
        }else{
          // no internet, no wlan
          log("##change##", " -> 10 undefined - reconnect!");
          if (winnotific.isReconnect()) changeWLAN(c.get("ssid_wlan"));
        }
      }
    }

    laststatus = status;
  }

  private boolean ping(String ip) throws IOException {
    Runtime rt = Runtime.getRuntime();{};
    Process proc = rt.exec("ping -n 1 "+ip); //ping -w 100 -n 2

    BufferedReader stdInput = new BufferedReader(new
            InputStreamReader(proc.getInputStream()));
    String s = null;
    while ((s = stdInput.readLine()) != null) {
      if(s.contains("Zielhost nicht erreichbar") || s.contains("Zeitüberschreitung") || s.contains("100% Verlust") || s.contains("nicht finden")){
        return false;
      }
    }
    return true;
  }


  private void changeWLAN(String wlan_ssid){
    try {
      Runtime.getRuntime().exec("netsh wlan connect "+wlan_ssid);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    log("## wlan ##", "- > "+wlan_ssid);
  }


  private void prepareNextDisconnect(){
    this.lastprediction[0] = new Date(this.lastdisconnect[0].getTime() + 1000*60*c.getInt("crash_period_minutes"));
    this.lastprediction[1] = new Date(this.lastdisconnect[1].getTime() + 1000*60*c.getInt("crash_period_minutes"));

    winnotific.changeTrayToolTip("Nächster Ausfall: \n"+FORMAT_SMALL.format(lastprediction[0])+" bis "+FORMAT_SMALL.format(lastprediction[1])+" Uhr!");

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run(){
        winnotific.displayTrayMessage( "Nächster Ausfall bald (oder bis "+FORMAT_SMALL.format(lastprediction[1])+" Uhr)!");
      }
    }, lastprediction[0]);

    log("## pred ##", "- > "+FORMAT_SMALL.format(lastprediction[0])+" - > "+FORMAT_SMALL.format(lastprediction[1]));
  }


  private void log(Object... objects){
    String output= "";
    for(Object o:objects){
      output = output + (o + " - ");
    }
    System.out.println(output);
    logfile.append(output);
  }
}
