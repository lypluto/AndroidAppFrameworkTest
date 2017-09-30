package com.lltest.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by louis.lu on 3/27/17.
 */

public class GeneralUtil {

    private static final String TAG = "GeneralUtil";

    public static String getAndroidVersion() {
        int buildVer = Build.VERSION.SDK_INT;
        switch(buildVer) {
            case Build.VERSION_CODES.N:
                return "N";
            case Build.VERSION_CODES.M:
                return "M";
            case Build.VERSION_CODES.LOLLIPOP:
                return "Lollipop";
            case Build.VERSION_CODES.KITKAT:
                return "KitKat";
            default:
                return "< KitKat";
        }
    }

    public static String getCurrentDisplayLanguage() {
        return Locale.getDefault().getDisplayLanguage();
    }

    public static boolean isRTL() {
        return isRTL(Locale.getDefault());
    }

    // internal API used by isRTL().
    private static boolean isRTL(Locale locale) {
        android.util.Log.v(TAG, "Current Display Language: " + getCurrentDisplayLanguage());
        int direction = Character.getDirectionality(locale.getDisplayName().charAt(0));
        boolean dirFlag = Character.DIRECTIONALITY_RIGHT_TO_LEFT == direction ||
                Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC == direction;
        android.util.Log.v(TAG, "Current Language direction: " + (dirFlag ? "RTL" : "LTR"));
        return dirFlag;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isForceRTL(Context ctx) {
        int dir = ctx.getResources().getConfiguration().getLayoutDirection();
        android.util.Log.v(TAG, "Layout dir value: " + dir);
        if (View.LAYOUT_DIRECTION_LTR == dir) {
            android.util.Log.d(TAG, "Layout dir: LAYOUT_DIRECTION_LTR");
            return false;
        } else {
            android.util.Log.d(TAG, "Layout dir: LAYOUT_DIRECTION_RTL");
            return true;
        }
    }

    private int[] getScreenSize() {
        /*
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int[] tempSize = new int[2];
        tempSize[0] = displaymetrics.widthPixels;
        tempSize[1] = displaymetrics.heightPixels;
        return tempSize;
        */
        return null;
    }

    public static String getBatteryStatusString(Context inContext) {
        boolean result = false;
        Intent battStatus = inContext.getApplicationContext().registerReceiver(null, new
                IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return getBatteryLevelString(battStatus);
    }

    private static String getBatteryLevelString(Intent battStatus) {
        if (null == battStatus) {
            Log.e(TAG, "null battStatus Intent input!");
            return null;
        }
        String result;

        int level = battStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = battStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int chargingStatus = battStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = chargingStatus == BatteryManager.BATTERY_STATUS_CHARGING || chargingStatus == BatteryManager.BATTERY_STATUS_FULL;
        int chargerPlugged = battStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean isChargerPlugged = chargerPlugged == BatteryManager.BATTERY_PLUGGED_AC || chargerPlugged == BatteryManager.BATTERY_PLUGGED_USB || chargerPlugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;
        int percent = (level * 100) / scale;

        result = "battery level = " + percent + ", isCharging = " + isCharging + ", " +
                "isChargerPlugged = " + isChargerPlugged;
        Log.v(TAG, result);
        return result;
    }

    /**
     * Display alert dialog with title and message
     *
     * @param context
     * @param title
     * @param message
     */
    public static void showMessage(Context context, String title, String message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private static long sClickStamp = 0L;

    /**
     * This method is used to check if click happens within 1 sec.
     *
     * @return
     */
    public static boolean checkClickValidate() {

        // Returns milliseconds since boot, including time spent in sleep.
        long current = SystemClock.elapsedRealtime();
        boolean valid = false;
        if (current - sClickStamp > Constants.CLICK_INTERVAL_LIMIT) {
            sClickStamp = current;
            valid = true;
        } else {
            Log.d(TAG, "click too fast: " + (current - sClickStamp) + "ms");
        }
        return valid;
    }

    /**
     * This method is used to reset the content of a TextView to empty string.
     * @param ctx
     * @param logView
     */
    public static void clearTextViewInfo(Context ctx, final TextView logView) {
        if (null == logView) {
            Log.e(TAG, "invalid TextView input!");
            return;
        }
        if (null == ctx || !(ctx instanceof Activity)) {
            Log.e(TAG, "invalid context input!");
            return;
        }
        // Update UI:
        ((Activity) ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logView.setText("");
            }
        });
    }

    /**
     * This method is used to reset the contents of a list of TextViews all to empty strings.
     *
     * @param ctx
     * @param viewList
     */
    public static  void clearMultiTextViewInfo(Context ctx, final List<TextView> viewList) {
        if (null == viewList || 0 == viewList.size()) {
            Log.e(TAG, "invalid TextView input!");
            return;
        }
        if (null == ctx || !(ctx instanceof Activity)) {
            Log.e(TAG, "invalid context input!");
            return;
        }
        for (TextView v : viewList) {
            if (null != v) {
                clearTextViewInfo(ctx, v);
            }
        }
    }

    /**
     * This method is used to reset the content of a TextView to empty string.
     * @param ctx
     * @param logView
     */
    public static void appendTextViewInfo(Context ctx, final TextView logView, final String inStr) {
        if (null == logView) {
            Log.e(TAG, "invalid TextView input!");
            return;
        }
        if (null == ctx || !(ctx instanceof Activity)) {
            Log.e(TAG, "invalid context input!");
            return;
        }
        // Update UI:
        ((Activity) ctx).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String existingStr = (String) logView.getText();     // cache
                StringBuilder sb;
                sb = new StringBuilder("");
                sb.append("\n").append(inStr).append("\n\n").append(existingStr);
                logView.setText(sb.toString());
            }
        });
    }

}
