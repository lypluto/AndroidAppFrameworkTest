package com.lltest.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by louis.lu on 10/6/17.
 */

public class NetworkUtil {
    private static final String TAG = Constants.APP_PREFIX + "NetworkUtil";

    public static String getWifiSSID(final Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context
                .WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        Log.v(TAG, "WIFI SSID: " + ssid);
        return ssid;
    }

    public static int getWifiStrengthLevel(final Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getApplicationContext().getSystemService(Context
                .WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int numberOfLevels = ReminderConstants.WIFI_STRENGTH_RANGE;
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        Log.v(TAG, "WIFI strength level index: " + level);
        return level;
    }
}
