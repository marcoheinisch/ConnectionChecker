package io.github.marcoheinisch;

import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class WinNotification {

    private SystemTray tray = SystemTray.getSystemTray();
    private TrayIcon trayIcon;

    private Image image;
    private String header = "";
    private String tooltip = "";
    private boolean hotspotusable;
    private boolean reconnect;

    public WinNotification(String header) {
        this.image = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/trayicon.png"));
        this.reconnect=true;
        this.hotspotusable=false;
        this.header = header;

        trayIcon = new TrayIcon(image, header);
        trayIcon.setImageAutoSize(true);
        trayIcon.setPopupMenu(createPopupMenu());

        refreshTray();
    }

    private PopupMenu createPopupMenu() {
        final PopupMenu popup = new PopupMenu();
        MenuItem aboutItem = new MenuItem("Beenden");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popup.add(aboutItem);
        popup.addSeparator();
        CheckboxMenuItem menuItem1 = new CheckboxMenuItem("Hotspot nutzen?");
        menuItem1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                hotspotusable = !hotspotusable;
            }
        });
        popup.add(menuItem1);
        CheckboxMenuItem menuItem2 = new CheckboxMenuItem("Wlan reconnecten?");
        menuItem2.setState(true);
        menuItem2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                reconnect = !reconnect;
            }
        });
        popup.add(menuItem2);
        return popup;
    }

    public void setImage(boolean active){
        this.image = Toolkit.getDefaultToolkit().createImage(getClass().getResource(( !active ? "/trayicon.png" : "/trayiconon.png")));
        refreshTray();
    }

    public boolean isHotspotusable(){
        return hotspotusable;
    }

    public boolean isReconnect() {
        return reconnect;
    }

    public void displayTrayMessage(String message)  {
        trayIcon.displayMessage(this.header, message, MessageType.WARNING);
    }

    public void changeTrayToolTip(String tooltip) {
        this.tooltip = tooltip;
        refreshTray();
    }

    private void refreshTray() {
        //Try to remove old SysTray (Maybe there is some better way...)
        try {
            tray.remove(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        trayIcon.setToolTip(this.tooltip);
        trayIcon.setImage(this.image);
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}