package com.indiainsure.android.MB360.insurance.queries.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.ItemFileBinding;
import com.indiainsure.android.MB360.databinding.ItemFileMoreBinding;
import com.indiainsure.android.MB360.insurance.queries.responseclass.Attachment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FileAttachmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Attachment> fileList = new ArrayList<>();
    Context context;
    FileDownloadHelper fileDownloadHelper;
    private boolean[] isDownloading;
    Activity activity;
    int queryPosition;

    public FileAttachmentAdapter(List<Attachment> fileList, Context context, FileDownloadHelper fileDownloadHelper, Activity activity, int queryPosition) {
        this.fileList = fileList;
        this.context = context;
        this.activity = activity;
        isDownloading = new boolean[fileList.size()];
        Arrays.fill(isDownloading, false);
        this.fileDownloadHelper = fileDownloadHelper;
        this.queryPosition = queryPosition;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ItemFileBinding binding = ItemFileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new FileAttachmentViewHolder(binding);
        } else {
            ItemFileMoreBinding binding = ItemFileMoreBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ShowMoreFileViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (getItemViewType(position) == 0) {

            try {
                String[] file_name = fileList.get(position).getFileName().split("\\\\");
                String file_name_w_o_e = "" + file_name[file_name.length - 1].split("\\.")[0];
                String file_extension = "" + file_name[file_name.length - 1].split("\\.")[1];
                ((FileAttachmentViewHolder) holder).fileBinding.textFile.setText(file_name_w_o_e + "." + file_extension);


                //file icon image
                switch (file_extension.toLowerCase()) {

                    case "pdf":
                        ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf));
                        break;
                    case "xlsx":
                    case "xls":

                        ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_excel));
                        break;
                    case "docx":
                    case "doc":
                        ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_word));
                        break;
                    case "png":
                    case "jpg":
                    case "jpeg":
                        ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_img));
                        break;
                    default:
                        ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_file));


                }

                if (isDownloading[position]) {
                    ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setVisibility(View.GONE);
                    ((FileAttachmentViewHolder) holder).fileBinding.fileProgress.setVisibility(View.VISIBLE);
                } else {
                    ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setVisibility(View.VISIBLE);
                    ((FileAttachmentViewHolder) holder).fileBinding.fileProgress.setVisibility(View.GONE);
                }

                ((FileAttachmentViewHolder) holder).fileBinding.itemFile.setOnClickListener(v -> {

                    fileDownloadHelper.onStartDownload(position, queryPosition);
                    ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setVisibility(View.GONE);
                    ((FileAttachmentViewHolder) holder).fileBinding.fileProgress.setVisibility(View.VISIBLE);
                    try {

                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {
                            context.startActivity(new DownloadFile(context, activity, file_name[file_name.length - 1],
                                    fileDownloadHelper, position, queryPosition).downloadAttachment(file_name[file_name.length - 1], fileList.get(position).getFileName()));
                        } else {
                            fileDownloadHelper.requestPermission(position, fileList.get(position),queryPosition);

                        }
                    } catch (ActivityNotFoundException e) {
                        fileDownloadHelper.onFinishDownload(position, queryPosition);
                        Toast.makeText(context, "No Application found to open this file", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        fileDownloadHelper.onFinishDownload(position, queryPosition);

                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                ((FileAttachmentViewHolder) holder).fileBinding.textFile.setText("file");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 5) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public int getItemCount() {
        return (fileList != null ? fileList.size() : 0);
    }

    class FileAttachmentViewHolder extends RecyclerView.ViewHolder {
        ItemFileBinding fileBinding;

        public FileAttachmentViewHolder(@NonNull ItemFileBinding fileBinding) {
            super(fileBinding.getRoot());
            this.fileBinding = fileBinding;
        }
    }

    class ShowMoreFileViewHolder extends RecyclerView.ViewHolder {
        ItemFileMoreBinding fileBinding;

        public ShowMoreFileViewHolder(@NonNull ItemFileMoreBinding fileBinding) {
            super(fileBinding.getRoot());
            this.fileBinding = fileBinding;
        }
    }


    static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;
        String extension;
        FileDownloadHelper fileDownloadHelper;
        int position;
        int queryPosition;

        public DownloadFile(Context context, Activity activity, String extension,
                            FileDownloadHelper fileDownloadHelper, int position, int queryPosition) {
            this.context = context;
            this.activity = activity;
            this.extension = extension;
            this.fileDownloadHelper = fileDownloadHelper;
            this.position = position;
            this.queryPosition = queryPosition;
        }

        public Intent downloadAttachment(String fileName, String fileUrl) {
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
                        fileDownloadHelper.onFinishDownload(position, queryPosition);
                        return FileAttachmentDownloader.downloadFileWithoutPermission(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        fileDownloadHelper.onFinishDownload(position, queryPosition);
                        return FileAttachmentDownloader.downloadFile(fileUrl, file, activity, context);

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
