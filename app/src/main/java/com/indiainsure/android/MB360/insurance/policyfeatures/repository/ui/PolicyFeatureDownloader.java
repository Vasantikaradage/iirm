package com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.AppLocalConstant;
import com.indiainsure.android.MB360.utilities.LogTags;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PolicyFeatureDownloader {
    private static final int MEGABYTE = 1024 * 1024;

    public static Intent downloadFile(String fileUrl, File directory, Activity activity, Context context) throws ActivityNotFoundException {

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            try {
                ExecutorService executors = Executors.newSingleThreadExecutor();

                Future<Intent> future = executors.submit(() -> {
                    Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): invoked");
                    Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): file-URL " + fileUrl);
                    Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): directory " + directory);

                    URL url = new URL(fileUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();

                    try {


                        InputStream inputStream = urlConnection.getInputStream();
                        FileOutputStream fileOutputStream = context.openFileOutput(directory.getName(), Context.MODE_PRIVATE);


                        int totalSize = urlConnection.getContentLength();
                        Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): urlConnectionContentLength: " + String.valueOf(totalSize));

                        byte[] buffer = new byte[MEGABYTE];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, bufferLength);
                        }
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        activity.runOnUiThread(() -> Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show());

                    }
                    Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): File Download Completed");

                    Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", directory);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);

                    ContentResolver contentResolver = context.getContentResolver();
                    String mimeType = contentResolver.getType(path);

                    Log.d("CONTENT-RESOLVER", "mime type: " + mimeType);
                    pdfIntent.setDataAndType(path, mimeType);
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    return pdfIntent;
                });

                return future.get();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                activity.runOnUiThread(() -> Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show());
                return null;
            }
        } else {
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): Permission not granted asking for permission");
            AppLocalConstant.verifyStoragePermissions(activity);
            return null;
        }
    }

    public static Intent downloadFileWithoutPermission(String fileUrl, File directory, Activity activity, Context context) throws ActivityNotFoundException {

        try {

            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): invoked");
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): file-URL " + fileUrl);
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): directory " + directory);

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = context.openFileOutput(directory.getName(), Context.MODE_PRIVATE);


            int totalSize = urlConnection.getContentLength();
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): urlConnectionContentLength: " + String.valueOf(totalSize));

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
            Log.d(LogTags.DOWNLOAD_ACTIVITY, "downloadFile(): File Download Completed");

            Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", directory);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);

            ContentResolver contentResolver = context.getContentResolver();
            String mimeType = contentResolver.getType(path);

            Log.d("CONTENT-RESOLVER", "mime type: " + mimeType);
            pdfIntent.setDataAndType(path, mimeType);
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            return pdfIntent;

        } catch (FileNotFoundException e) {
            activity.runOnUiThread(() -> Toast.makeText(context, "File not found!", Toast.LENGTH_SHORT).show());

            directory.delete();

            e.printStackTrace();
            Log.e(LogTags.DOWNLOAD_ACTIVITY, "downloadFile() : error ", e);
            return null;
        } catch (IOException e) {
            directory.delete();
            e.printStackTrace();
            Log.e(LogTags.DOWNLOAD_ACTIVITY, "downloadFile() : error io Exception ", e);
            return null;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            activity.runOnUiThread(() -> Toast.makeText(context, "No Application found to open this file.", Toast.LENGTH_SHORT).show());
            return null;
        }
    }

}
