package com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui;

public interface PolicyfeatureDownloadHelper {
    void onStartDownload(int position);

    void onFinishDownload(int position);
    void requestPermission(int position, PolicyFeaturesOuterModel policyFeaturesOuterModel);
}