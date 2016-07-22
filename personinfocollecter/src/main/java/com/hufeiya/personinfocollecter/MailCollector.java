package com.hufeiya.personinfocollecter;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hufeiya.personinfocollecter.beans.QQMailJson;
import com.hufeiya.personinfocollecter.utils.TaobaoParser;
import com.hufeiya.personinfocollecter.utils.mail.QQMailParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hufeiya on 16/6/4.
 * Collect Personal Information from TaoBao.
 */
public class MailCollector implements Collector{
    private WebView webView;
    private Handler handler;
    private OnCollectedListener onCollectedListener;
    private static final int LOGIN_SUCCESS = 666;
    private static final int READING_MAIL = 667;
    private static final String PAGE_NUM = "pageNum";
    private QQMailParser parser = new QQMailParser();
    private int stepNum = 0;

    /**
     * Constructor with a webView.
     * @param webView
     * The webView you want your user to login from it.
     */
    public MailCollector(WebView webView) {
        this.webView = webView;
        this.handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case LOGIN_SUCCESS:
                        //onCollectedListener.onProcessMailInfo(0);
                        return true;
                    case READING_MAIL:
                        //onCollectedListener.onProcessMailInfo(msg.getData().getInt(PAGE_NUM));
                }
                return false;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        webView.requestFocusFromTouch();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(8388608);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(-1);

        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                Log.d("fuck","alert!!");
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress > 99){
                    //view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    //Log.d("fuck","progress 大于 99!!");
                }
            }
        });

        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
    }

    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            //Log.d("fuck","html代码：" + html);
            // Indicate the user already login the mobile version qq mail.Start to jump to the desktop version.
            if (html.contains(" onclick=\"return ptLogout();\">退出</a>") && stepNum == 0) {
                Log.d("fuck","邮箱登录成功");
                Message message = new Message();
                message.what = LOGIN_SUCCESS;
                handler.dispatchMessage(message);
                final String lineWithComputerVersionURL = parser.getHref(html,"标准版");
                Log.d("fuck",lineWithComputerVersionURL);
                stepNum++;
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(lineWithComputerVersionURL);
                    }
                });


            } //Indicate already jumped to the desktop version. Start collect the mails.
            else if (html.contains("退出") && stepNum == 1) {
                final String mailListPage = "https://set3.mail.qq.com" + parser.getHrefById(html,"folder_1");
                Log.d("fuck","收件箱地址" + mailListPage);
                Log.d("fuck","是否包含邮件:" + html.contains("干货集中营"));
                stepNum++;
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(mailListPage);
                    }
                });
            }else if(html.contains("退出") && stepNum > 1){
                Log.d("fuck","开始读取收件箱第" + (stepNum-1) + "页" + html.contains("下一页"));

                final String nextPage = "https://set3.mail.qq.com" + parser.getHrefById(html,"nextpage");
                Log.d("fuck","收件箱第" + (stepNum-1) + "页地址" + nextPage);
                Bundle bundle = new Bundle();
                bundle.putInt(PAGE_NUM,stepNum-1);
                Message message = new Message();
                message.what = READING_MAIL;
                message.setData(bundle);
                handler.dispatchMessage(message);
                stepNum++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(nextPage);
                    }
                });
            }
        }
    }

    @Override
    public void startCollection(OnCollectedListener listener) {
        webView.loadUrl("https://mail.qq.com/cgi-bin/loginpage");
        onCollectedListener = listener;
    }
}
