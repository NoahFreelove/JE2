package org.JE.JE2.Utility;

import java.awt.*;

public class NotificationSender {

    public static void sendNotification(String title, String description, byte[] icon, TrayIcon.MessageType messageType){
        if(SystemTray.isSupported())
        {
            SystemTray tray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage(icon);
            TrayIcon trayIcon = new TrayIcon(image, "JE2");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("JE2");
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            trayIcon.displayMessage(title, description, messageType);
        }
    }
}
