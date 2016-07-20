package com.hufeiya.taobaodemo;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hufeiya.personinfocollecter.Collector;
import com.hufeiya.personinfocollecter.JDCollector;
import com.hufeiya.personinfocollecter.MailCollector;
import com.hufeiya.personinfocollecter.MeiTuanCollector;
import com.hufeiya.personinfocollecter.TaoBaoCollector;
import com.hufeiya.personinfocollecter.beans.PersonalInfo;

import java.util.ArrayList;
import java.util.List;
import static com.hufeiya.taobaodemo.AppName.*;

public class WebActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener{

    private WebView webView;
    private int appName;
    private FrameLayout frameLayout;
    private ProgressBar progressBar;
    private TextView loadingDiscription;
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        appName =  getIntent().getIntExtra("AppName",TAOBAO);
        webView = (WebView) findViewById(R.id.web_view);
        frameLayout = (FrameLayout)findViewById(R.id.fragment_replace);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        loadingDiscription = (TextView)findViewById(R.id.loading_discription);
        Collector collector = null;
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
                collector = new MailCollector(webView);
                break;
        }
        if (collector != null){
            collector.startCollection(listener);
        }

    }

    @Override
    public void onListFragmentInteraction(PersonalInfo item) {

    }
}
