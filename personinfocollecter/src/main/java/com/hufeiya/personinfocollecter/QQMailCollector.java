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
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hufeiya.personinfocollecter.beans.QQMailJson;
import com.hufeiya.personinfocollecter.utils.TaobaoParser;
import com.hufeiya.personinfocollecter.utils.mail.QQMailParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private int stepNum = 0;

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
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("fuck","控制台信息:" + consoleMessage.message());
                try {
                    JSONObject object = new JSONObject(consoleMessage.message());
                    Log.d("fuck","第一个id：" + object.getJSONArray("idx").getJSONObject(0).getJSONArray("itms").getString(0));
                    Log.d("fuck","第一个object：" + object.getJSONArray("mls").getJSONObject(0).toString());
                    //String raw = object.getJSONArray("mls").getJSONObject(0).toString();
                    JSONArray notJsonArray = object.getJSONArray("mls");
                    Gson gson = new Gson();
                    for(int i = 0;i < notJsonArray.length();i++){
                        String raw = notJsonArray.getJSONObject(i).toString();
                       // String fuck = "{\"fuck\":[" + raw.substring(1,raw.length()-1) + "]}";
                        //Log.d("fuck","拼的json:" + fuck);
                        QQMailJson qqMailJson = gson.fromJson(raw,QQMailJson.class);
                        if (qqMailJson.getSubj() != null){
                            Log.d("fuck","sss" + qqMailJson.getSubj());
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return super.onConsoleMessage(consoleMessage);
            }


        });
    }

    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html,String url) {
            /*Log.d("fuck","stepNum:" + stepNum + "html代码：" + html);
            int pice = 100;
            int size = html.length();
            for(int i = 0;i < pice;i++){
                Log.d("fuck",i + " " + html.substring(size/pice*i,size/pice*(i+1)));
            }*/
            Log.d("fuck","包含退出:" + html.contains("退出"));
            // Indicate the user already login the mobile version qq mail.
            if (html.contains("退出") && stepNum == 0) {
                Log.d("fuck","邮箱登录成功,url:" +url);
                Message message = new Message();
                message.what = LOGIN_SUCCESS;
                handler.dispatchMessage(message);
                final String mailBoxUrl = url.substring(0,url.length()-5) + "list,1__";
                Log.d("fuck","邮箱地址:" + mailBoxUrl);
                stepNum++;
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(mailBoxUrl);
                    }
                });


            } //Indicate already jumped to the desktop version. Start collect the mails.
            else if (html.contains("退出") && stepNum == 1) {
                //final String mailListPage = "https://set3.mail.qq.com" + parser.getHrefById(html,"folder_1");
                //Log.d("fuck","收件箱地址" + mailListPage);
                Log.d("fuck","是否包含邮件:" + html.contains("干货集中营"));
                stepNum++;
                webView.post(new Runnable() {
                    @Override
                    public void run() {
                        //webView.loadUrl(mailListPage);
                    }
                });
            }else if(html.contains("退出") && stepNum > 1){
                Log.d("fuck","包含加载更多?" + html.contains("加载更多"));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void startCollection(OnCollectedListener listener) {
        webView.loadUrl("https://mail.qq.com/cgi-bin/loginpage");
        onCollectedListener = listener;
    }
}
