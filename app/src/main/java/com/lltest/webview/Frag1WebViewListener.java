package com.lltest.webview;

public interface Frag1WebViewListener {

    void onReceivedError(String url, int errorCode);

    void onPageFinished(String url);

    void onPageStarted(String url);

    void onLoadResource(String url);

    void onTokenReady(String token);

    void onPinningPrevented(String host);
}
