package com.hufeiya.taobaodemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hufeiya.personinfocollecter.Collector;
import com.hufeiya.personinfocollecter.JDCollector;
import com.hufeiya.personinfocollecter.Mail163Collector;
import com.hufeiya.personinfocollecter.MeiTuanCollector;
import com.hufeiya.personinfocollecter.QQMailCollector;
import com.hufeiya.personinfocollecter.TaoBaoCollector;
import com.hufeiya.personinfocollecter.beans.PersonalInfo;
import com.hufeiya.personinfocollecter.XueXinCollector;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static com.hufeiya.taobaodemo.AppName.*;

public class WebActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{

    private static final int IMAGE_DOWNLOADED = 777;
    private WebView webView;
    private int appName;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    private TextView loadingDiscription;
    private FrameLayout xuexinFrame;
    private AutoCompleteTextView username;
    private AutoCompleteTextView password;
    private AutoCompleteTextView vertifyText;
    private ImageView vertifyCodeImage;
    private Button login;
    private Collector collector;
    private String cookies;
    private Bitmap bitMap;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case IMAGE_DOWNLOADED:
                    vertifyCodeImage.setImageBitmap(bitMap);
                    Log.d("fuck","已经加载图片!!");
                    vertifyCodeImage.invalidate();
                    break;
            }
            return false;
        }
    });

    private Collector.OnCollectedListener listener = new Collector.OnCollectedListener() {
        @Override
        public void onCollectedInfo(List<PersonalInfo> personalInfoList) {
            Log.d("WebActivity","starting ItemFragment...");
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("personalInfoList",(ArrayList<? extends Parcelable>)personalInfoList);
            ItemFragment fragment = ItemFragment.newInstance(1);
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragment_replace, fragment).commit();
        }

        @Override
        public void onProcessMailInfo(int pageNum) {
            frameLayout.setVisibility(View.VISIBLE);
            progressBar.setProgress(pageNum);
            loadingDiscription.setText("当前读取第" + pageNum + "页邮件");
        }

        @Override
        public void onGotImageURL(final String url) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    URL imgUrl = null;
                    try {
                        imgUrl = new URL(url);
                        HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();
                        conn.setDoInput(true);
                        conn.setRequestProperty("Cookie",cookies);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitMap = BitmapFactory.decodeStream(is);
                        Log.d("fuck","bitmap大小：" + bitMap.getByteCount());
                        is.close();
                        Message message = new Message();
                        message.what = IMAGE_DOWNLOADED;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @Override
        public void onGotCookies(String newCookies) {
            cookies = newCookies;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        xuexinFrame  = (FrameLayout)findViewById(R.id.xuexin_frame);
        username = (AutoCompleteTextView)findViewById(R.id.username);
        password = (AutoCompleteTextView)findViewById(R.id.password);
        vertifyText = (AutoCompleteTextView)findViewById(R.id.vertify_code_text);
        vertifyCodeImage = (ImageView)findViewById(R.id.vertify_code_image);
        login = (Button)findViewById(R.id.login);
        appName =  getIntent().getIntExtra("AppName",TAOBAO);
        webView = (WebView) findViewById(R.id.web_view);
        frameLayout = (FrameLayout)findViewById(R.id.fragment_replace);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        loadingDiscription = (TextView)findViewById(R.id.loading_discription);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XueXinCollector xuexinCollector = (XueXinCollector)collector;
                xuexinCollector.startLoginXueXin(username.getText().toString(),password.getText().toString(),vertifyText.getText().toString());
            }
        });
        switch (appName){
            case TAOBAO:
               collector = new TaoBaoCollector(webView);
                break;
            case JINGDONG:
                collector = new JDCollector(webView);
                break;
            case MEITUAN:
                collector = new MeiTuanCollector(webView);
                break;
            case MAIL:
                xuexinFrame.setVisibility(View.INVISIBLE);
                collector = new QQMailCollector(webView);
                break;
            case MAIL_163:
                xuexinFrame.setVisibility(View.INVISIBLE);
                collector = new Mail163Collector(webView);
                break;
            case XUEXIN:
                xuexinFrame.setVisibility(View.VISIBLE);
                collector = new XueXinCollector(webView);
        }
        if (collector != null){
            collector.startCollection(listener);
        }



    }

    @Override
    public void onListFragmentInteraction(PersonalInfo item) {

    }
}
