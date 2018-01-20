package com.lltest.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by louis.lu on 1/19/18.
 */

public class WebViewUtil {

    public static void showUrlDialog(Context ctx, String url, WebViewClient webViewClient) {
        AlertDialog.Builder alert = new AlertDialog.Builder(ctx);
        alert.setTitle("Title here");

        WebView wv = new WebView(ctx);
        wv.loadUrl(url);
        wv.setWebViewClient(webViewClient);

        alert.setView(wv);
        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
