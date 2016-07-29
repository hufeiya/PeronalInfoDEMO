package com.hufeiya.personinfocollecter;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hufeiya.personinfocollecter.beans.QQMailDetailJson;
import com.hufeiya.personinfocollecter.beans.QQMailJson;
import com.hufeiya.personinfocollecter.utils.TaobaoParser;
import com.hufeiya.personinfocollecter.utils.mail.Mail163Json;
import com.hufeiya.personinfocollecter.utils.mail.NetEaseMailParser;
import com.hufeiya.personinfocollecter.utils.mail.QQMailParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hufeiya on 16/6/4.
 * Collect Personal Information from TaoBao.
 */
public class Mail163Collector implements Collector{
    private WebView webView;
    private Handler handler;
    private OnCollectedListener onCollectedListener;
    private static final int LOGIN_SUCCESS = 666;
    private static final int READING_MAIL = 667;
    private static final String PAGE_NUM = "pageNum";
    private QQMailParser parser = new QQMailParser();
    private List<Mail163Json> mailJsonList = new ArrayList<>();
    private List<QQMailDetailJson> qqMailDetailJsonList = new ArrayList<>();
    private String cookies;
    private String sid;
    private final int MAX_PAGE = 2;
    private int currentPage = 0;

    /**
     * Constructor with a webView.
     * @param webView
     * The webView you want your user to login from it.
     */
    public Mail163Collector(WebView webView) {
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
        webView.setWebViewClient(new WebViewClient(){
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
                CookieManager cookieManager = CookieManager.getInstance();
                cookies = cookieManager.getCookie(url);
                Log.d("fuck", "cookie: " + cookies);
                view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>','" + url + "');");
            }
        });
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
    }

    private void getNextPage(){
        if(currentPage > MAX_PAGE){
            //startFetchMailDetails();
            return;
        }
        currentPage++;
        RequestParams params = new RequestParams("http://mail.163.com/m/s");
        if(mailJsonList == null || mailJsonList.size() == 0){
            params.addBodyParameter("var","<?xml version=\"1.0\"?><object><string name=\"order\">date</string><boolean name=\"desc\">true</boolean>" +
                    "<int name=\"start\">0</int><int name=\"limit\">2</int><int name=\"fid\">1</int><int name=\"offset\">0</int></object>");
        }else{
            params.addBodyParameter("var","<?xml version=\"1.0\"?><object><string name=\"order\">date</string><boolean name=\"desc\">true</boolean>" +
                    "<int name=\"start\">" + mailJsonList.size() + "</int><int name=\"limit\">2</int><int name=\"fid\">1</int><int name=\"offset\">0</int></object>");
        }

        params.addHeader("Cookie",cookies);
        params.addHeader("Accept","text/javascript");
        params.addQueryStringParameter("func","mbox:listMessages");
        params.addQueryStringParameter("sid",sid);
        try {
            String result = x.http().getSync(params,String.class);
            Thread.sleep(1000);
            Log.d("fuck","json:         " + result);
            NetEaseMailParser parser = new NetEaseMailParser();
            result = parser.parseDateToTimeStamp(result);
            JSONObject object = new JSONObject(result);
            parseMailJsonObject(object);
            getNextPage();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void parseMailJsonObject(JSONObject object) throws JSONException {
        JSONArray notJsonArray = object.getJSONArray("var");
        Gson gson = new Gson();
        for(int i = 0;i < notJsonArray.length();i++){
            String raw = notJsonArray.getJSONObject(i).toString();
            Mail163Json mail163Json = gson.fromJson(raw,Mail163Json.class);
            if (mail163Json != null){
                mailJsonList.add(mail163Json);
                Log.d("fuck"," " + mailJsonList.size() + " 邮件标题：" + mail163Json.getSubject());
                Log.d("fuck","发件人：" + mail163Json.getReceivedDate().toString());
            }

        }
    }

    public static String timeStamp2Date(String seconds) {

        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

/*
    private void startFetchMailDetails(){

        for(int i = 0;i < 2;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            QQMailJson item = qqMailJsonList.get(i);
            RequestParams params = new RequestParams("https://w.mail.qq.com/cgi-bin/readmail");
            params.addHeader("Cookie",cookies);
            params.addQueryStringParameter("ef","js");
            params.addQueryStringParameter("sid",sid);
            params.addQueryStringParameter("t","mobile_data.json");
            params.addQueryStringParameter("s","read");
            params.addQueryStringParameter("showreplyhead","1");
            params.addQueryStringParameter("disptype","html");
            final Gson gson = new Gson();
            params.addQueryStringParameter("mailid",item.getInf().getId());
            try {
                String result = x.http().getSync(params,String.class);
                Log.d("fuck","邮件详情json：" + result);
                QQMailDetailJson detail = gson.fromJson(result,QQMailDetailJson.class);
                Log.d("fuck","邮件详情摘要:" + detail.getMls().get(0).getInf().getAbs());
                Log.d("fuck","邮件详情:" + detail.getMls().get(0).getContent().getBody());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                Log.d("fuck","读取邮件详细信息失败！");
            }
        }
    }
*/

    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html,String url) {
            // Indicate the user already login the mobile version 163 mail.
            if (html.contains("收件箱")) {
                Log.d("fuck","邮箱登录成功,url:" +url);
                Message message = new Message();
                message.what = LOGIN_SUCCESS;
                handler.dispatchMessage(message);
                String tempSid = parser.getSidFromURL(url);
                if(tempSid != null){
                    sid = tempSid;
                }
                Log.d("fuck","sid:  " + sid);
                getNextPage();
            }
        }
    }

    @Override
    public void startCollection(OnCollectedListener listener) {
        webView.loadUrl("http://smart.mail.163.com/");
        onCollectedListener = listener;
    }
}
