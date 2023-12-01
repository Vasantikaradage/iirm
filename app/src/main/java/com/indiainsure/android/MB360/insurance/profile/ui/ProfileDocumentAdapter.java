package com.indiainsure.android.MB360.insurance.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.databinding.DocumentsItemBinding;
import com.indiainsure.android.MB360.insurance.profile.response.UserDocumentsDetail;

import java.util.List;

public class ProfileDocumentAdapter extends RecyclerView.Adapter<ProfileDocumentAdapter.ProfileDocumentViewHolder> {

    List<UserDocumentsDetail> documentsList;
    Context context;
    DocumentOnClickListener documentOnClickListener;

    public ProfileDocumentAdapter(List<UserDocumentsDetail> documentsList, Context context, DocumentOnClickListener documentOnClickListener) {
        this.documentsList = documentsList;
        this.context = context;
        this.documentOnClickListener = documentOnClickListener;
    }

    @NonNull
    @Override
    public ProfileDocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DocumentsItemBinding binding = DocumentsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProfileDocumentViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull ProfileDocumentViewHolder holder, int position) {
        holder.binding.documentName.setText(documentsList.get(position).getDocumentType());

        holder.binding.documentItem.setOnClickListener(v -> {
            documentOnClickListener.onDocumentClicked(documentsList.get(position));
        });
        holder.binding.documentView.setOnClickListener(v -> {
            documentOnClickListener.onDocumentClicked(documentsList.get(position));
        });
        holder.binding.documentUpload.setOnClickListener(v -> {
            documentOnClickListener.onDocumentUploadClick(documentsList.get(position).getDocumentType());
        });
    }

    @Override
    public int getItemCount() {
        return (documentsList == null ? 0 : documentsList.size());
    }

    public static class ProfileDocumentViewHolder extends RecyclerView.ViewHolder {
        DocumentsItemBinding binding;

        public ProfileDocumentViewHolder(@NonNull DocumentsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
