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
        Log.d("fuck","正试图加载url:" + url);
        return false;
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("fuck", "onPageStarted");
        super.onPageStarted(view, url, favicon);
    }

    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("fuck", "onPageFinished ");
        //view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.documentElement.outerHTML+'</head>');");
        view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>','" + url + "');");
        /*view.loadUrl("javascript:var iframe = document.getElementById(\"mainFrame\"); \n" +
                "if (iframe.attachEvent){ \n" +
                "iframe.attachEvent(\"onload\", function(){ \n" +
                "alert(iframe.getElementById(\"ut_c\")); \n" +
                "}); \n" +
                "} else { \n" +
                "iframe.onload = function(){ \n" +
                "var fuck = document.getElementById(\"composebtn\").innerHTML;" +
                "alert(\"Local iframe is now loaded.\"); \n" +
                "}; \n" +
                "} \n" +
                "document.body.appendChild(iframe); ");
        */
        /*view.loadUrl("javascript:var oFrm = document.getElementById('mainFrame');\n" +
                "oFrm.onload = oFrm.onreadystatechange = function() {\n" +
                "if (this.readyState && this.readyState != 'complete') return;\n" +
                "else {\n" +
                "alert(\"fuck\");\n" +
                "}\n" +
                "}");*/
        /*view.loadUrl("javascript:var iframe = document.getElementById('mainFrame');\\n\"" +
                "iframe.addEventListener( \"load\", function(){\n" +
                "         //代码能执行到这里说明已经载入成功完毕了\n" +
                "      this.removeEventListener( \"load\", arguments.call, false);\n" +
                "      alert(\"fuck\")" +
                "   }, false);" +
                "document.body.appendChild(iframe); ");*/
    }

}