package com.hufeiya.taobaodemo;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import com.hufeiya.personinfocollecter.Collector;
import com.hufeiya.personinfocollecter.TaoBaoCollector;
import com.hufeiya.personinfocollecter.beans.PersonalInfo;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.web_view);
        Collector collector = new TaoBaoCollector(webView);
        collector.startCollection(new Collector.OnCollectedListener() {
            @Override
            public void onCollectedInfo(List<PersonalInfo> personalInfoList) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("personalInfoList",(ArrayList<? extends Parcelable>)personalInfoList);
                ItemFragment fragment = ItemFragment.newInstance(1);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.fragment_replace, fragment).commit();
            }
        });
    }

    @Override
    public void onListFragmentInteraction(PersonalInfo item) {

    }



}
