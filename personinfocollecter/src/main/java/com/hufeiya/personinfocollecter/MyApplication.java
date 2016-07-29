package com.hufeiya.personinfocollecter;

import android.app.Application;
import android.content.Context;

import org.xutils.x;

/**
 * Created by hufeiya on 16-7-22.
 */
public class MyApplication extends Application{
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }

}
