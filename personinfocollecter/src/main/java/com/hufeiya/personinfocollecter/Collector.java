package com.hufeiya.personinfocollecter;

import com.hufeiya.personinfocollecter.beans.PersonalInfo;

import java.util.List;

/**
 * Created by hufeiya on 16/6/4.
 * Collect Personal Information from specified websites.
 */
public interface Collector {

    /**
     * Start collect personal information from specified websites.
     *
     * @param listener The async callback listener.When the collection is done,the listener will call its method.
     */
    void startCollection(OnCollectedListener listener);

    /**
     * The async callback listener.When the collection is done,the listener will call its method.
     */
    interface OnCollectedListener {
        /**
         * This method is called from your current thread when the collection is done.
         *
         * @param personalInfoList The result of personal information in this collection.
         */
        void onCollectedInfo(List<PersonalInfo> personalInfoList);
    }

}
