package com.indiainsure.android.MB360.insurance.queries.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.ItemQueryDetailsBinding;
import com.indiainsure.android.MB360.insurance.queries.responseclass.Attachment;
import com.indiainsure.android.MB360.insurance.queries.responseclass.ListOfQuery;
import com.indiainsure.android.MB360.utilities.FileDownloader;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueryDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ListOfQuery> qry_details;
    Context context;
    private AppCompatTextView[] dots;
    private AppCompatImageView[] fileType;
    private LinearLayout[] fileview;
    private int max_show = 2;
    Activity activity;

    public QueryDetailsAdapter(List<ListOfQuery> listOfQueries, Context context, Activity activity) {
        this.qry_details = listOfQueries;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQueryDetailsBinding binding = ItemQueryDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QueryDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        try {
            final String qry_details_txt = qry_details.get(i).getQuery();

            if (qry_details.get(i).getRole().equals("EMPLOYEE")) {

                if (qry_details_txt.length() > 100) {
                    ((QueryDetailsViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt.subSequence(0, 100));
                    ((QueryDetailsViewHolder) holder).binding.btnShowMore.setText("Read More...");
                    ((QueryDetailsViewHolder) holder).binding.btnShowMore.setVisibility(View.VISIBLE);
                } else {
                    ((QueryDetailsViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt);
                    ((QueryDetailsViewHolder) holder).binding.btnShowMore.setVisibility(View.GONE);
                }


                ((QueryDetailsViewHolder) holder).binding.tvSentMsg.setBackground(ContextCompat.getDrawable(context, R.drawable.message_request));
                ((QueryDetailsViewHolder) holder).binding.tvSentName.setText(qry_details.get(i).getPostedBy());
                ((QueryDetailsViewHolder) holder).binding.tvSentTime.setText(qry_details.get(i).getPostedOn());
                ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvRcvName.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvRCVTime.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.usrImg2.setVisibility(View.VISIBLE);
                ((QueryDetailsViewHolder) holder).binding.usrImg.setVisibility(View.GONE);
                Glide.with(context).load(qry_details.get(i).getPostedByimage()).into(((QueryDetailsViewHolder) holder).binding.usrImg2);
                ((QueryDetailsViewHolder) holder).binding.attachmentLayout.setGravity(Gravity.RIGHT);

            }else if (qry_details.get(i).getRole().equals("SR-HR")|| qry_details.get(i).getRole().equals("BROKER-IT-ADMIN")) {
                ((QueryDetailsViewHolder) holder).binding.btnShowMore.setVisibility(View.GONE);

                if (qry_details_txt.length() > 100) {
                    ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setText(qry_details_txt.subSequence(0, 100));
                    ((QueryDetailsViewHolder) holder).binding.btnShowMoreRCV.setText("Read More...");
                    ((QueryDetailsViewHolder) holder).binding.btnShowMoreRCV.setVisibility(View.VISIBLE);
                } else {
                    ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setText(qry_details_txt);
                    ((QueryDetailsViewHolder) holder).binding.btnShowMoreRCV.setVisibility(View.GONE);
                }

                ((QueryDetailsViewHolder) holder).binding.tvSentMsg.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvSentName.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvSentTime.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setText(qry_details.get(i).getQuery());
                ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setBackground(ContextCompat.getDrawable(context, R.drawable.message_response));
                ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setTextColor(ContextCompat.getColor(context, R.color.dark_grey));
                ((QueryDetailsViewHolder) holder).binding.tvRcvName.setText(qry_details.get(i).getPostedBy());
                ((QueryDetailsViewHolder) holder).binding.tvRCVTime.setText(qry_details.get(i).getPostedOn());
                ((QueryDetailsViewHolder) holder).binding.usrImg2.setVisibility(View.GONE);
            } else {

                if (qry_details_txt.length() > 100) {
                    ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setText(qry_details_txt.subSequence(0, 100));
                    ((QueryDetailsViewHolder) holder).binding.btnShowMoreRCV.setText("Read More...");
                    ((QueryDetailsViewHolder) holder).binding.btnShowMoreRCV.setVisibility(View.VISIBLE);
                } else {
                    ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setText(qry_details_txt);
                    ((QueryDetailsViewHolder) holder).binding.btnShowMoreRCV.setVisibility(View.GONE);
                }

                ((QueryDetailsViewHolder) holder).binding.tvSentMsg.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvSentName.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvSentTime.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setText(qry_details.get(i).getQuery());
                ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setBackground(ContextCompat.getDrawable(context, R.drawable.message_response));
                ((QueryDetailsViewHolder) holder).binding.tvRcvName.setText(qry_details.get(i).getPostedBy());
                ((QueryDetailsViewHolder) holder).binding.tvRCVTime.setText(qry_details.get(i).getPostedOn());
                ((QueryDetailsViewHolder) holder).binding.usrImg2.setVisibility(View.GONE);
            }

            createMultipleFileView(((QueryDetailsViewHolder) holder), i);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            layoutParams.setMargins(4, 6, 4, 6);

            AppCompatTextView tv1 = new AppCompatTextView(context);
            tv1.setText(MessageFormat.format("+{0} More", dots.length - max_show));

            tv1.setTextSize(10.0f);

            fileview[dots.length] = new LinearLayout(context);
            fileview[dots.length].setOrientation(LinearLayout.HORIZONTAL);
            fileview[dots.length].setPadding(8, 4, 8, 4);

            if (qry_details.get(i).getAttachments().size() > max_show) {
                fileview[dots.length].addView(tv1, layoutParams);
                ((QueryDetailsViewHolder) holder).binding.attachmentLayout.addView(fileview[dots.length], layoutParams);
            }

            tv1.setOnClickListener(view -> {
                try {
                    max_show = max_show == 2 ? 5 : 2;
                    createMultipleFileView(((QueryDetailsViewHolder) holder), i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            ((QueryDetailsViewHolder) holder).binding.btnShowMoreRCV.setOnClickListener(v -> {
                ((QueryDetailsViewHolder) holder).binding.tvRcvMsg.setText(qry_details_txt);
                ((QueryDetailsViewHolder) holder).binding.btnShowMoreRCV.setVisibility(View.GONE);
            });

            ((QueryDetailsViewHolder) holder).binding.btnShowMore.setOnClickListener(v -> {
                ((QueryDetailsViewHolder) holder).binding.btnShowMore.setVisibility(View.GONE);
                ((QueryDetailsViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void createMultipleFileView(QueryDetailsViewHolder holder, int i) throws Exception {
        fileview = new LinearLayout[qry_details.get(i).getAttachments().size() + 1];

        dots = new AppCompatTextView[qry_details.get(i).getAttachments().size()];
        fileType = new AppCompatImageView[qry_details.get(i).getAttachments().size()];

        holder.binding.attachmentLayout.removeAllViews();

        int pixels1 = (int) (15 * context.getResources().getDisplayMetrics().density + 0.5f);
        int pixels2 = (int) (15 * context.getResources().getDisplayMetrics().density + 0.5f);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                pixels1, pixels2);

        layoutParams2.setMargins(4, 6, 4, 6);
        layoutParams.setMargins(4, 10, 4, 10);

        for (int j = 0; j < dots.length; j++) {
            dots[j] = new AppCompatTextView(context);
            fileType[j] = new AppCompatImageView(context);
            fileview[j] = new LinearLayout(context);
            fileview[j].setOrientation(LinearLayout.HORIZONTAL);

            if (dots.length > 1)
                ((QueryDetailsViewHolder) holder).binding.attachmentLayout.setWeightSum((float) dots.length);
            else
                ((QueryDetailsViewHolder) holder).binding.attachmentLayout.setWeightSum(2.0f);

            fileview[j].setGravity(Gravity.CENTER_VERTICAL);
            dots[j].setTextSize(10.0f);

            Attachment attachment = qry_details.get(i).getAttachments().get(j);

            String[] s = attachment.getFileName().split("\\\\");
            ;
            dots[j].setText(s[s.length - 1]);
            dots[j].setPadding(10, 0, 10, 0);
            fileview[j].setPadding(8, 0, 8, 0);
            switch (attachment.getFileType()) {
                case ".pdf":
                    fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pdf));
                    break;
                case ".xlsx":
                case ".xls":
                    fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_excel));
                    break;
                case ".docx":
                case ".doc":
                    fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_word));
                    break;
                case ".png":
                case ".jpg":
                case ".jpeg":
                    fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_img));
                    break;

            }

            fileview[j].setBackground(ContextCompat.getDrawable(context, R.drawable.ripple_effect_file));

            fileview[j].addView(fileType[j], layoutParams2);
            fileview[j].addView(dots[j], layoutParams);

            ((QueryDetailsViewHolder) holder).binding.attachmentLayout.addView(fileview[j], layoutParams);

            if (j >= max_show) {
                fileview[j].setVisibility(View.GONE);
            }

            fileview[j].setOnClickListener(view -> {
                try {
                    String fileUrl = attachment.getFileName().replace("\\", File.separator);
                    LogMyBenefits.d("FILE-URL", "FILE-URL: ");
                    String extension = attachment.getFileType();
                    String name = s[s.length - 1];
                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "createMultipleFileView: " + fileUrl);
                    new DownloadFile(context, activity).downloadFilePDF(name, fileUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (qry_details != null) {
            return qry_details.size();
        } else {
            return 0;
        }
    }

    public static class QueryDetailsViewHolder extends RecyclerView.ViewHolder {
        ItemQueryDetailsBinding binding;

        public QueryDetailsViewHolder(@NonNull ItemQueryDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    //download file class
    //creates background thread for downloading the files
    static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;


        public DownloadFile(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
        }

        public void downloadFilePDF(String fileName, String fileUrl) {
            //we can show the loading animation here
            //showLoading()
            ExecutorService executors = Executors.newSingleThreadExecutor();

            Handler handler = new Handler(Looper.getMainLooper());
            executors.execute(() -> {
                //runnable thread
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    this.fileUrl = fileUrl;
                    this.fileName = fileName.toLowerCase();

                    file = new File(context.getFilesDir(), this.fileName);
                    LogMyBenefits.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());


                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                if (Build.VERSION.SDK_INT > 32) {
                    this.fileUrl = fileUrl;
                    this.fileName = fileName.toLowerCase();

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), this.fileName);
                    LogMyBenefits.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                    try {
                        FileDownloader.downloadFileWithoutPermission(fileUrl, file, activity, context);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No Application found to open this file", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    FileDownloader.downloadFile(fileUrl, file, activity, context);
                }


            });
        }

        private void readFile(String fileName) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    try {
                        File outputFile = new File(context.getFilesDir(), fileName);
                        if (outputFile.exists()) {
                            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                            Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", outputFile);

                            ContentResolver contentResolver = context.getContentResolver();
                            String mimeType = contentResolver.getType(path);

                            LogMyBenefits.d("CONTENT-RESOLVER", "mime type: " + mimeType);

                            pdfIntent.setDataAndType(path, mimeType);
                            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            activity.startActivity(pdfIntent);
                        } else {
                            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                }
                //todo hide loading
                //hideLoading()
            });
        }

        private void readFileWithoutPermission(String fileName) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                try {
                    File outputFile = new File(context.getFilesDir(), fileName);
                    if (outputFile.exists()) {
                        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                        Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", outputFile);

                        ContentResolver contentResolver = context.getContentResolver();
                        String mimeType = contentResolver.getType(path);

                        LogMyBenefits.d("CONTENT-RESOLVER", "mime type: " + mimeType);
                        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        activity.startActivity(pdfIntent);
                    } else {
                        Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof ActivityNotFoundException) {
                        Toast.makeText(context, "No Application found to view this file", Toast.LENGTH_SHORT).show();
                    }
                }


            });
        }
    }
}
