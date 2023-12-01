package com.indiainsure.android.MB360.networkmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

public class NetworkMonitoringUtil extends ConnectivityManager.NetworkCallback {

    private final NetworkRequest mNetworkRequest;
    private final ConnectivityManager mConnectivityManager;
    private final NetworkStateManager mNetworkStateManager;

    // Constructor
    public NetworkMonitoringUtil(Context context) {
        mNetworkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkStateManager = NetworkStateManager.getInstance();
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        LogMyBenefits.d(LogTags.NETWORK_MANAGER, "Connected to network");
        mNetworkStateManager.setNetworkConnectivityStatus(true);
    }

    @Override
    public void onLosing(@NonNull Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
        LogMyBenefits.d(LogTags.NETWORK_MANAGER, "Network will loose in " + String.valueOf(maxMsToLive) + "ms");

    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        LogMyBenefits.e(LogTags.NETWORK_MANAGER, "Lost network connection");
        mNetworkStateManager.setNetworkConnectivityStatus(false);
    }

    /**
     * Registers the Network-Request callback
     * (Note: Register only once to prevent duplicate callbacks)
     */
    public void registerNetworkCallbackEvents() {
        LogMyBenefits.d(LogTags.NETWORK_MANAGER, "Registered the callback");
        mConnectivityManager.registerNetworkCallback(mNetworkRequest, this);
    }

    /**
     * Check current Network state
     */
    public void checkNetworkState() {
        try {
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            mNetworkStateManager.setNetworkConnectivityStatus(networkInfo != null
                    && networkInfo.isConnected());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
