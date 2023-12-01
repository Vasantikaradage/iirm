package com.indiainsure.android.MB360.insurance.queries.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.ItemFileUploadBinding;

import java.util.ArrayList;


public class UplaodFileAttachmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<String> fileList ;
    Context context;
    DeleteFileListerner deleteFileListerner;


    public UplaodFileAttachmentAdapter(Context context, ArrayList<String> pathHolder,DeleteFileListerner deleteFileListerner) {
        this.context = context;
        this.fileList = pathHolder;
        this.deleteFileListerner=deleteFileListerner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFileUploadBinding binding = ItemFileUploadBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FileAttachmentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String path=fileList.get(position);
        String filename=path.substring(path.lastIndexOf("/")+1);
        String extension = path.substring(path.lastIndexOf("."));


        ((FileAttachmentViewHolder) holder).fileBinding.textFile.setText(filename);

        //file icon image
        switch (extension.toLowerCase()) {

            case ".pdf":
                ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_pdf));
                break;
            case ".xlsx":
            case ".xls":

                ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_xlsx));
                break;
            case ".docx":
            case ".doc":
                ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_text));
                break;
            case ".png":
            case ".jpg":
            case ".jpeg":
                ((FileAttachmentViewHolder) holder).fileBinding.imageFile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_by_file_other));
                break;


        }

        ((FileAttachmentViewHolder) holder).fileBinding.imageFileCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFileListerner.deleteFile(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (fileList != null ? fileList.size() : 0);
    }

    class FileAttachmentViewHolder extends RecyclerView.ViewHolder {
        ItemFileUploadBinding fileBinding;

        public FileAttachmentViewHolder(@NonNull ItemFileUploadBinding fileBinding) {
            super(fileBinding.getRoot());
            this.fileBinding = fileBinding;
        }
    }

}
