package com.hufeiya.personinfocollecter;

import android.webkit.WebView;

import com.hufeiya.personinfocollecter.beans.PersonalInfo;

import java.util.List;

/**
 * Created by hufeiya on 16/6/4.
 */
public interface Collector {
    void startCollection(OnCollectedListener listener);

    interface OnCollectedListener{
        void onCollectedInfo(List<PersonalInfo> personalInfoList);
    }

}
