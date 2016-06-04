package com.hufeiya.taobaodemo;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.hufeiya.taobaodemo.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private WebView webView;
    private Handler htmlHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        htmlHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //webView.setVisibility(View.INVISIBLE);
                ItemFragment fragment = ItemFragment.newInstance(1);
                fragment.setArguments(msg.getData());
                getFragmentManager().beginTransaction().replace(R.id.fragment_replace, fragment).commit();
                return true;
            }
        });
        webView = (WebView) findViewById(R.id.web_view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (getApplicationInfo().flags == ApplicationInfo.FLAG_DEBUGGABLE) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());

        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.loadUrl("https://h5.m.taobao.com/awp/mtb/mtb.htm");
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    final class MyWebViewClient extends WebViewClient {
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


    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Log.d("HTML", "哈哈" + html);

            //表示已经登录,跳转到管理收货地址
            if (html.contains("<a href=\"//login.m.taobao.com/logout.htm\">退出</a>")) {
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("https://h5.m.taobao.com/mtb/address.html");
                    }
                });
            } else if (html.contains("管理收货地址")) {

                Bundle bundle = new Bundle();
                bundle.putString("html", html);
                Message htmlMessage = new Message();
                htmlMessage.setData(bundle);
                htmlHandler.dispatchMessage(htmlMessage);

            }
        }
    }

}
