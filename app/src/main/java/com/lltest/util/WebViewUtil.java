package com.lltest.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by louis.lu on 1/19/18.
 */

public class WebViewUtil {

    public static final String TAG = Constants.APP_PREFIX + "WebViewUtil";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void showUrlDialog(Context ctx, String url, WebViewClient webViewClient) {

        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);

        WebView wv = new CvvWebView(ctx);
        wv.loadUrl(url);
        wv.setWebViewClient(webViewClient);
        wv.setFocusable(true);
        wv.setFocusableInTouchMode(true);

        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        alert.setView(wv);
        alert.setTitle("WebView Dialog Test");

        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "onCancel...");
            }
        });
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.d(TAG, "onDismiss...");
            }
        });
        alert.show();
    }

    public static class CvvWebView extends WebView {
        public CvvWebView(Context context) {
            super(context);
        }

        @Override
        public boolean onCheckIsTextEditor() {
            return true;
        }
    }
}
