package com.indiainsure.android.MB360.utilities.webcustomtab;

import androidx.browser.customtabs.CustomTabsClient;

public interface ServiceConnectionCallback {
    /**
     * Called when the service is connected.
     * @param client a CustomTabsClient
     */
    void onServiceConnected(CustomTabsClient client);

    /**
     * Called when the service is disconnected.
     */
    void onServiceDisconnected();
}
