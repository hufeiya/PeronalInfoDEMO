package com.hufeiya.personinfocollecter;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.hufeiya.personinfocollecter.utils.JDParser;

/**
 * Created by hufeiya on 16/6/5.
 * Collect Personal Information from Jing Dong.
 */
public class JDCollector implements Collector {
    private WebView webView;
    private Handler handler;
    private OnCollectedListener onCollectedListener;

    /**
     * Constructor with a webView.
     * @param webView
     * The webView you want your user to login from it.
     */
    public JDCollector(WebView webView) {
        this.webView = webView;
        this.handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                onCollectedListener.onCollectedInfo(new JDParser().parse(msg.getData().getString("html")));
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
            if (html.contains("https://passport.m.jd.com/user/logout.action?") && ! html.contains("收货地址管理")) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("http://home.m.jd.com/address/addressList.action");
                    }
                });
            } //Indicate already navigated the personal information page.Start to parse.
            else if (html.contains("收货地址管理")) {

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
        webView.loadUrl("http://home.m.jd.com/myJd/home.action");
        onCollectedListener = listener;
    }
}
