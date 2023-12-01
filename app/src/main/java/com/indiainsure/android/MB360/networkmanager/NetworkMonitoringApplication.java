package com.indiainsure.android.MB360.networkmanager;

import android.app.Application;

//import com.jakewharton.threetenabp.AndroidThreeTen;


public class NetworkMonitoringApplication extends Application {

    public NetworkMonitoringUtil mNetworkMonitoringUtil;

    @Override
    public void onCreate() {
        super.onCreate();
       // AndroidThreeTen.init(this);
        mNetworkMonitoringUtil = new NetworkMonitoringUtil(getApplicationContext());
        mNetworkMonitoringUtil.checkNetworkState();
        mNetworkMonitoringUtil.registerNetworkCallbackEvents();



    }
}
