package com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.indiainsure.android.MB360.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PolicyFeaturesOuterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<PolicyFeaturesOuterModel> policyFeatureList;
    Context context;
    Activity activity;
    private boolean[] isDownloading;
    PolicyfeatureDownloadHelper policyfeatureDownloadHelper;

    public PolicyFeaturesOuterAdapter(ArrayList<PolicyFeaturesOuterModel> policyFeatureList, Context context, Activity activity, PolicyfeatureDownloadHelper policyfeatureDownloadHelper) {
        this.policyFeatureList = policyFeatureList;
        this.context = context;
        this.activity = activity;
        isDownloading = new boolean[policyFeatureList.size()];
        Arrays.fill(isDownloading, false);
        this.policyfeatureDownloadHelper = policyfeatureDownloadHelper;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_policy_features, parent, false);
                viewHolder = new PolicyFeatureOuterViewHolder(view);
                break;
            case 1:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_policy_link, parent, false);
                viewHolder = new PolicyFeatureLinkViewHolder(view2);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final PolicyFeaturesOuterModel policyFeaturesOuterModel = policyFeatureList.get(position);


        switch (holder.getItemViewType()) {
            case 0:
                ((PolicyFeatureOuterViewHolder) holder).policyIndex.setText(String.valueOf(position + 1));
                ((PolicyFeatureOuterViewHolder) holder).itemPolicyMainHeader.setText(policyFeatureList.get(position).getPolicyType().toUpperCase());

                PolicyFeaturesInnerAdapter adapter = new PolicyFeaturesInnerAdapter(policyFeatureList.get(position).getInnerList());
                ((PolicyFeatureOuterViewHolder) holder).innerPolicyRecyclerView.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, policyFeatureList.size());

                if (policyFeatureList.get(position).isExpanded()) {
                    ((PolicyFeatureOuterViewHolder) holder).innerPolicyLayout.setVisibility(View.VISIBLE);
                    final RotateAnimation rotateAnim = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnim.setDuration(200);
                    rotateAnim.setFillAfter(true);

                    ((PolicyFeatureOuterViewHolder) holder).dropdown.startAnimation(rotateAnim);
                } else {
                    ((PolicyFeatureOuterViewHolder) holder).innerPolicyLayout.setVisibility(View.GONE);
                    final RotateAnimation rotateAnim = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                    rotateAnim.setDuration(200);
                    rotateAnim.setFillAfter(true);

                    ((PolicyFeatureOuterViewHolder) holder).dropdown.startAnimation(rotateAnim);
                }


                ((PolicyFeatureOuterViewHolder) holder).policyLayout.setOnClickListener(view -> {

                    policyFeatureList.get(position).setExpanded(!policyFeatureList.get(position).isExpanded());


                    if (((PolicyFeatureOuterViewHolder) holder).innerPolicyLayout.getVisibility() == View.VISIBLE) {

                        ((PolicyFeatureOuterViewHolder) holder).innerPolicyLayout.setVisibility(View.GONE);

                        final RotateAnimation rotateAnim = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                        rotateAnim.setDuration(200);
                        rotateAnim.setFillAfter(true);

                        ((PolicyFeatureOuterViewHolder) holder).dropdown.startAnimation(rotateAnim);

                    } else {
                        ((PolicyFeatureOuterViewHolder) holder).innerPolicyLayout.setVisibility(View.VISIBLE);

                        final RotateAnimation rotateAnim = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                        rotateAnim.setDuration(200);
                        rotateAnim.setFillAfter(true);

                        ((PolicyFeatureOuterViewHolder) holder).dropdown.startAnimation(rotateAnim);
                    }
                });
                break;

            case 1:


                if (isDownloading[position]) {
                    ((PolicyFeatureLinkViewHolder) holder).downloadProgress.setVisibility(View.VISIBLE);
                    ((PolicyFeatureLinkViewHolder) holder).download.setVisibility(View.GONE);
                } else {
                    ((PolicyFeatureLinkViewHolder) holder).downloadProgress.setVisibility(View.GONE);
                    ((PolicyFeatureLinkViewHolder) holder).download.setVisibility(View.VISIBLE);
                }

                ((PolicyFeatureLinkViewHolder) holder).item_policy_layout.setOnClickListener(v -> {
                    ((PolicyFeatureLinkViewHolder) holder).download.performClick();
                });

                ((PolicyFeatureLinkViewHolder) holder).download.setOnClickListener(view -> {
                    ((PolicyFeatureLinkViewHolder) holder).downloadProgress.setVisibility(View.VISIBLE);
                    ((PolicyFeatureLinkViewHolder) holder).download.setVisibility(View.GONE);
                    String url = policyFeatureList.get(position).getInnerList().get(0).getInnerDescription();
                    url = url.replaceAll(" ", "%20");
                    Uri uri = Uri.parse(url);
                    policyfeatureDownloadHelper.onStartDownload(position);

                    try {
                        context.startActivity(new PolicyFeaturesOuterAdapter.DownloadFile(context, activity, policyfeatureDownloadHelper, position).downloadFilePDF(String.valueOf(System.currentTimeMillis()), uri.toString()));

                    } catch (ActivityNotFoundException e) {
                        policyfeatureDownloadHelper.onFinishDownload(position);
                        Toast.makeText(context, "No Application found to open this file", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        policyfeatureDownloadHelper.onFinishDownload(position);
                    }

                    //new DownloadFile(context, activity).downloadFilePDF(String.valueOf(System.currentTimeMillis()), uri.toString());
                });
        }
    }

    @Override
    public int getItemViewType(int position) {

        return (policyFeatureList.get(position).getPolicyType().equalsIgnoreCase("POLICY FEATURE LINK") ? 1 : 0);
    }

    @Override
    public int getItemCount() {
        return (policyFeatureList != null ? policyFeatureList.size() : 0);
    }

    public static class PolicyFeatureOuterViewHolder extends RecyclerView.ViewHolder {
        TextView itemPolicyMainHeader, policyIndex;
        RecyclerView innerPolicyRecyclerView;
        RelativeLayout policyLayout, innerPolicyLayout;
        ImageView dropdown;

        public PolicyFeatureOuterViewHolder(@NonNull View itemView) {
            super(itemView);
            itemPolicyMainHeader = itemView.findViewById(R.id.item_policy_main_header);
            innerPolicyRecyclerView = itemView.findViewById(R.id.inner_policy_recycle);
            policyLayout = itemView.findViewById(R.id.item_policy_layout);
            innerPolicyLayout = itemView.findViewById(R.id.inner_policy_details_layout);
            dropdown = itemView.findViewById(R.id.down_arrow_icon);
            policyIndex = itemView.findViewById(R.id.item_policy_index);
        }
    }


    public static class PolicyFeatureLinkViewHolder extends RecyclerView.ViewHolder {
        ImageView download;
        RelativeLayout item_policy_layout;
        LottieAnimationView download_animation;
        ProgressBar downloadProgress;

        public PolicyFeatureLinkViewHolder(@NonNull View itemView) {
            super(itemView);
            download = itemView.findViewById(R.id.image_download);
            item_policy_layout = itemView.findViewById(R.id.item_policy_layout);
            download_animation = itemView.findViewById(R.id.download_animation);
            downloadProgress = itemView.findViewById(R.id.item_download_progress);
        }
    }


    //download file class
    //creates background thread for downloading the files
    static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;
        String extension;
        PolicyfeatureDownloadHelper policyfeatureDownloadHelper;
        int position;


        public DownloadFile(Context context, Activity activity, PolicyfeatureDownloadHelper policyfeatureDownloadHelper, int position) {
            this.context = context;
            this.activity = activity;
            this.extension = extension;
            this.policyfeatureDownloadHelper = policyfeatureDownloadHelper;
            this.position = position;
        }

        public Intent downloadFilePDF(String fileName, String fileUrl) {
            //we can show the loading animation here
            //showLoading()
            ExecutorService executors = Executors.newSingleThreadExecutor();

            Future<Intent> future = executors.submit(() -> {
                //runnable thread
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                if (Build.VERSION.SDK_INT > 32) {

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                    try {
                        policyfeatureDownloadHelper.onFinishDownload(position);
                        return PolicyFeatureDownloader.downloadFileWithoutPermission(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        policyfeatureDownloadHelper.onFinishDownload(position);
                        return PolicyFeatureDownloader.downloadFile(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }


            });


            try {
                return future.get();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

    }

    public void startDownloading(int position) {
        isDownloading[position] = true;

    }

    public void finishDownloading(int position) {
        isDownloading[position] = false;

    }
}
