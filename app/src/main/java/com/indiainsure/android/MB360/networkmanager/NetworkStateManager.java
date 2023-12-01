package com.indiainsure.android.MB360.networkmanager;

import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

public class NetworkStateManager {

    private static NetworkStateManager INSTANCE;
    private static final MutableLiveData<Boolean> activeNetworkStatusMLD = new MutableLiveData<>();


    public NetworkStateManager() {
    }

    public static synchronized NetworkStateManager getInstance() {
        if (INSTANCE == null) {
            LogMyBenefits.d(LogTags.NETWORK_MANAGER, "getInstance() called :Creating new instance for monitoring the network");
            INSTANCE = new NetworkStateManager();
        }
        return INSTANCE;
    }


    /**
     * Updates the active network status live-data
     */
    public void setNetworkConnectivityStatus(boolean connectivityStatus) {
        LogMyBenefits.d(LogTags.NETWORK_MANAGER, "setNetworkConnectivityStatus() called with: connectivityStatus = [" + connectivityStatus + "]");

        if (Looper.myLooper() == Looper.getMainLooper()) {
            activeNetworkStatusMLD.setValue(connectivityStatus);
        } else {
            activeNetworkStatusMLD.postValue(connectivityStatus);
        }
    }

    /**
     * Returns the current network status
     */
    public LiveData<Boolean> getNetworkConnectivityStatus() {
        LogMyBenefits.d(LogTags.NETWORK_MANAGER, "getNetworkConnectivityStatus() called");
        return activeNetworkStatusMLD;
    }
}
