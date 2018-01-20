package com.lltest.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by louis.lu on 1/19/18.
 */

public class Frag1WebViewClient extends WebViewClient {
    private String TAG = "Frag1WebViewClient";

    private Context mContext;
    private Frag1WebViewListener mListener;

    public Frag1WebViewClient(Context context, Frag1WebViewListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i(TAG, "onPageStarted()");

        // trigger listener callback:
        if (mListener != null) {
            mListener.onPageStarted(url);
        }

        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d(TAG, "page loading is done");

        if (mListener != null) {
            mListener.onPageFinished(url);
        }

        super.onPageFinished(view, url);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.e(TAG, "onReceivedError " + error.getErrorCode());

        if (mListener != null) {
            mListener.onReceivedError(request.getUrl().toString(), error.getErrorCode());
        }

        super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.e(TAG, "onReceivedError " + errorCode);

        if (mListener != null) {
            mListener.onReceivedError(failingUrl, errorCode);
        }

        super.onReceivedError(view, errorCode, description, failingUrl);
    }


}
