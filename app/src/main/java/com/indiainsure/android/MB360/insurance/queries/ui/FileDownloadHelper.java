package com.indiainsure.android.MB360.insurance.queries.ui;

import com.indiainsure.android.MB360.insurance.queries.responseclass.Attachment;

public interface FileDownloadHelper {
    void onStartDownload(int position,int queryPosition);

    void onFinishDownload(int position,int queryPosition);

    void requestPermission(int position, Attachment attachment, int queryPosition);
}
