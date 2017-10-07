package com.lltest.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.lltest.appFrameTest.MainActivity;

/**
 *
 */

public class NetworkStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStatusReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: impl
        /*


        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (info != null && info.isConnected()) {
            // Do your work.
            Log.d(TAG, "LL: isConnected " + info.isConnected());

            // e.g. To check the Network Name or other info:
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            Log.d(TAG, "LL: ssid " + ssid);

            int numberOfLevels = ReminderConstants.WIFI_STRENGTH_RANGE;
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            Log.d(TAG, "LL: level " + level);

            Toast.makeText(context.getApplicationContext(), "LL: ssid " + ssid, Toast.LENGTH_SHORT).show();
            //GeneralUtil.showShortToast(context, "ssid: " + ssid + ", level: " + level);

            // ssid example: "S-B2B-LAB1", "S-B2B-LAB2"
        } else if (info != null && !info.isConnected()) {
            Log.d(TAG, "LL: isConnected " + info.isConnected());

            //GeneralUtil.showShortToast(context, "wifi disconnected!");
            Toast.makeText(context.getApplicationContext(), "wifi disconnected!", Toast.LENGTH_SHORT).show();
        }

        int newRssi = intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0);
        Log.d(TAG, "newRssi: " + newRssi);
        //updateDebugLog1(String.valueOf(newRssi));
        String wifiSsid = NetworkUtil.getWifiSSID(context);
        int wifiLevel = NetworkUtil.getWifiStrengthLevel(context);

        StringBuilder sb = new StringBuilder();
        sb.append("WIFI SSID: ").append(wifiSsid).append("\n").append("WIFI STRENGTH: ")
                .append(String.valueOf(wifiLevel));
        //updateDebugLog1(sb.toString());

        //GeneralUtil.showShortToast(context, "Test: " + sb.toString());
        Toast.makeText(context.getApplicationContext(), "LLTest: " + sb.toString(), Toast.LENGTH_SHORT).show();
        */
    }
}
