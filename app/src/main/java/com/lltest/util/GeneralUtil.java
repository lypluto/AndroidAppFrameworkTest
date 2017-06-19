package com.lltest.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

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

}
