package com.hufeiya.personinfocollecter;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by hufeiya on 16/6/4.
 */
public final class MyWebViewClient extends WebViewClient {


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return false;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("WebView", "onPageStarted");
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("WebView", "onPageFinished ");
        view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
        //view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>','" + url + "');");

    }
}