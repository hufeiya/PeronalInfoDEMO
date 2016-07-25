package com.hufeiya.personinfocollecter;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hufeiya.personinfocollecter.Collector;

/**
 * Created by hufeiya on 16-7-25.
 */
public class XueXinCollector implements Collector{

    private WebView webView;
    private OnCollectedListener listener;
    private String cookies;
    private boolean isFirstLoad = true;
    private static final int SEND_IMAGE_URL = 666;
    private static final int SEND_COOKIES = 667;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case SEND_IMAGE_URL:
                    String url = msg.getData().getString("image");
                    listener.onGotImageURL(url);
                    break;
                case SEND_COOKIES:
                    listener.onGotCookies(cookies);
            }
            return false;
        }
    });

    public XueXinCollector(final WebView webView){
        this.webView = webView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        //清除cookie
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.setWebViewClient(new WebViewClient(){


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if(url.startsWith("https://account.chsi.com.cn/passport/captcha.image?id=")){
                    Log.d("fuck","图片url是    ：" + url);
                    Bundle bundle = new Bundle();
                    bundle.putString("image",url);
                    Message message = new Message();
                    message.what = SEND_IMAGE_URL;
                    message.setData(bundle);
                    handler.sendMessage(message);

                    return new WebResourceResponse(null,null,null);
                }
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                CookieManager cookieManager = CookieManager.getInstance();
                cookies = cookieManager.getCookie(url);
                Message message = new Message();
                message.what = SEND_COOKIES;
                handler.sendMessage(message);
                view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>','" + url + "');");
                if(isFirstLoad){
                    isFirstLoad = false;
                   /*view.loadUrl("javascript:" +
                            "document.getElementById('username').value =" + "'320321199307252418'" + ";"  +
                            "document.getElementById('password').value =" + "'4241799'" + ";" +
                            "var frms = document.getElementById('fm1');" +
                           "var subbutton = document.getElementsByName('submit')[0];" +
                           "subbutton.name='btnsubmit';" +//这个button的name也是submit，需要把它改了，不然提交会冲突
                            "frms.submit();" +
                           "");*/
                    //view.loadUrl("javascript:alert(\"fffff\");");
                    Log.d("fuck","自动提交" + url);
                }
            }
        });

    }
    @Override
    public void startCollection(OnCollectedListener listener) {
        this.listener = listener;
        webView.loadUrl("https://account.chsi.com.cn/passport/login");

    }
    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html,String url) {
            // Indicate we need the vertify code.
            if (html.contains("stu_reg_vcode")) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("fuck","正在获取验证码焦点");
                        webView.loadUrl("javascript:document.getElementById(\"captcha\").focus();");
                    }
                });
            } //Indicate already navigated the personal information page.Start to parse.

        }
    }

    public void startLoginXueXin(final String username,final String password,final String vertifyCode){
        webView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("fuck","开始登录学信网");
                webView.loadUrl("javascript:" +
                        "document.getElementById('username').value =" + "'" + username + "'" + ";"  +
                        "document.getElementById('password').value =" + "'" + password + "'" + ";" +
                        "document.getElementById('captcha').value =" + "'" + vertifyCode + "'" + ";" +
                        "var frms = document.getElementById('fm1');" +
                        "var subbutton = document.getElementsByName('submit')[0];" +
                        "subbutton.name='btnsubmit';" +//这个button的name也是submit，需要把它改了，不然提交会冲突
                        "frms.submit();" +
                        "");
            }
        });
    }

}
