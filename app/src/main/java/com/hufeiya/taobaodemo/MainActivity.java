package com.hufeiya.taobaodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.hufeiya.taobaodemo.AppName.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button taobao;
    private Button jd;
    private Button meituan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taobao = (Button)findViewById(R.id.taobao);
        jd = (Button)findViewById(R.id.jd);
        meituan = (Button)findViewById(R.id.meituan);
        taobao.setTag(TAOBAO);
        jd.setTag(JINGDONG);
        meituan.setTag(MEITUAN);
        taobao.setOnClickListener(this);
        jd.setOnClickListener(this);
        meituan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this,WebActivity.class);
        intent.putExtra("AppName",(int)v.getTag());
        startActivity(intent);
    }
}
