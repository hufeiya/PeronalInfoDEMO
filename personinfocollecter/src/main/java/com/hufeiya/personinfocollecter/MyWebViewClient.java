package com.hufeiya.personinfocollecter;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by hufeiya on 16/6/4.
 */
public final class MyWebViewClient extends WebViewClient {
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return false;
        }
        return true;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("WebView", "onPageStarted");
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        Log.d("WebView", "onPageFinished ");
        view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        super.onPageFinished(view, url);
    }
}