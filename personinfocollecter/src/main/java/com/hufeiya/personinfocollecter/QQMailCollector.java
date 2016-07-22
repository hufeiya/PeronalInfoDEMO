package com.hufeiya.personinfocollecter;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
public class QQMailCollector implements Collector{
    private WebView webView;
    private Handler handler;
    private OnCollectedListener onCollectedListener;
    private static final int LOGIN_SUCCESS = 666;
    private static final int READING_MAIL = 667;
    private static final String PAGE_NUM = "pageNum";
    private QQMailParser parser = new QQMailParser();
    private List<QQMailJson> qqMailJsonList = new ArrayList<>();
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
    public QQMailCollector(WebView webView) {
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
            startFetchMailDetails();
            return;
        }
        currentPage++;
        RequestParams params = new RequestParams("https://w.mail.qq.com/cgi-bin/mail_list");
        if(qqMailJsonList == null || qqMailJsonList.size() == 0){

        }else{
            QQMailJson last = qqMailJsonList.get(qqMailJsonList.size()-1);
            params.addQueryStringParameter("cursorutc","" + last.getInf().getUTC());
            params.addQueryStringParameter("cursorid",last.getInf().getId());
        }

        params.addHeader("Cookie",cookies);
        params.addQueryStringParameter("ef","js");
        params.addQueryStringParameter("sid",sid);
        params.addQueryStringParameter("t","mobile_data.json");
        params.addQueryStringParameter("s","list");
        params.addQueryStringParameter("cursor","max");
        params.addQueryStringParameter("cursorcount","99");//最大只能是99
        params.addQueryStringParameter("folderid","1");
        params.addQueryStringParameter("device","android");
        params.addQueryStringParameter("app","phone");
        params.addQueryStringParameter("ver","app");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("fuck","抓取的邮件json:" + result);
                try {
                    Thread.sleep(1000);
                    JSONObject object = new JSONObject(result);
                    parseMailJsonObject(object);
                    getNextPage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("fuck","抓取邮件error");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void parseMailJsonObject(JSONObject object) throws JSONException {
        JSONArray notJsonArray = object.getJSONArray("mls");
        Gson gson = new Gson();
        for(int i = 0;i < notJsonArray.length();i++){
            String raw = notJsonArray.getJSONObject(i).toString();
            QQMailJson qqMailJson = gson.fromJson(raw,QQMailJson.class);
            if (qqMailJson.getInf().getSubj() != null){
                qqMailJsonList.add(qqMailJson);
                Log.d("fuck"," " + qqMailJsonList.size() + " 邮件标题：" + qqMailJson.getInf().getSubj());
                Log.d("fuck","发件人：" + qqMailJson.getInf().getFrom().getAddr());
                String date = timeStamp2Date(String.valueOf(qqMailJson.getInf().getUTC()));
                Log.d("fuck","时间：" + date);
                Log.d("fuck","摘要：" + qqMailJson.getInf().getAbs());
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
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Log.d("fuck","邮件详情json：" + result);
                    QQMailDetailJson detail = gson.fromJson(result,QQMailDetailJson.class);
                    Log.d("fuck","邮件详情摘要:" + detail.getMls().get(0).getInf().getAbs());
                    Log.d("fuck","邮件详情:" + detail.getMls().get(0).getContent().getBody());
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.d("fuck","读取邮件详细信息失败！");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.d("fuck","读取邮件详细取消！");
                }

                @Override
                public void onFinished() {
                    Log.d("fuck","读取邮件详情完成！");
                }
            });
        }
    }


    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html,String url) {
            Log.d("fuck","包含退出:" + html.contains("退出"));
            // Indicate the user already login the mobile version qq mail.
            if (html.contains("退出")) {
                Log.d("fuck","邮箱登录成功,url:" +url);
                Message message = new Message();
                message.what = LOGIN_SUCCESS;
                handler.dispatchMessage(message);
                String tempSid = parser.getSidFromURL(url);
                if(tempSid != null){
                    sid = tempSid;
                }
                Log.d("fuck","sid" + sid);
                getNextPage();
            }
        }
    }

    @Override
    public void startCollection(OnCollectedListener listener) {
        webView.loadUrl("https://mail.qq.com/cgi-bin/loginpage");
        onCollectedListener = listener;
    }
}
