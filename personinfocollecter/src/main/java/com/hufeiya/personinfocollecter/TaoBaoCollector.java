package com.hufeiya.personinfocollecter;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hufeiya.personinfocollecter.utils.TaobaoParser;

/**
 * Created by hufeiya on 16/6/4.
 * Collect Personal Information from TaoBao.
 */
public class TaoBaoCollector implements Collector{
    private WebView webView;
    private Handler handler;
    private OnCollectedListener onCollectedListener;

    /**
     * Constructor with a webView.
     * @param webView
     * The webView you want your user to login from it.
     */
    public TaoBaoCollector(WebView webView) {
        this.webView = webView;
        this.handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                onCollectedListener.onCollectedInfo(new TaobaoParser().parse(msg.getData().getString("html")));
                return true;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
    }

    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            // Indicate the user already login.Start to collect personal information,
            if (html.contains("<a href=\"//login.m.taobao.com/logout.htm\">退出</a>")) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("https://h5.m.taobao.com/mtb/address.html");
                    }
                });
            } //Indicate already navigated the personal information page.Start to parse.
            else if (html.contains("管理收货地址")) {

                Bundle bundle = new Bundle();
                bundle.putString("html", html);
                Message htmlMessage = new Message();
                htmlMessage.setData(bundle);
                handler.dispatchMessage(htmlMessage);

            }
        }
    }

    @Override
    public void startCollection(OnCollectedListener listener) {
        webView.loadUrl("https://h5.m.taobao.com/awp/mtb/mtb.htm");
        onCollectedListener = listener;
    }
}
