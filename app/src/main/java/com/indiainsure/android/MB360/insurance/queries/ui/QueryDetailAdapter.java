package com.indiainsure.android.MB360.insurance.queries.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.indiainsure.android.MB360.databinding.ItemChatIncomingBinding;
import com.indiainsure.android.MB360.databinding.ItemChatOutgoingBinding;
import com.indiainsure.android.MB360.insurance.queries.responseclass.ListOfQuery;

import java.util.List;

public class QueryDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ListOfQuery> qry_details;
    Context context;
    Activity activity;
    private int max_show = 2;
    FileDownloadHelper fileDownloadHelper;
    FileAttachmentAdapter adapter;

    public QueryDetailAdapter(List<ListOfQuery> listOfQueries, Context context, Activity activity, FileDownloadHelper fileDownloadHelper) {
        this.qry_details = listOfQueries;
        this.context = context;
        this.activity = activity;
        this.fileDownloadHelper = fileDownloadHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ItemChatIncomingBinding binding = ItemChatIncomingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new IncomingViewHolder(binding);
        } else {
            ItemChatOutgoingBinding binding = ItemChatOutgoingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new OutGoingViewHolder(binding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (qry_details.get(position).getRole().equalsIgnoreCase("EMPLOYEE")) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        holder.setIsRecyclable(true);
        if (getItemViewType(i) == 0) {
            try {
                final String qry_details_txt = qry_details.get(i).getQuery();

                if (qry_details_txt.length() > 100) {
                    ((IncomingViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt.subSequence(0, 100));
                    ((IncomingViewHolder) holder).binding.btnShowMore.setText("Read More...");
                    ((IncomingViewHolder) holder).binding.btnShowMore.setVisibility(View.VISIBLE);
                } else {
                    ((IncomingViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt);
                    ((IncomingViewHolder) holder).binding.btnShowMore.setVisibility(View.GONE);
                }


                ((IncomingViewHolder) holder).binding.tvSentName.setText(qry_details.get(i).getPostedBy());
                ((IncomingViewHolder) holder).binding.tvSentTime.setText(qry_details.get(i).getPostedOn());

                ((IncomingViewHolder) holder).binding.usrImg2.setVisibility(View.VISIBLE);

                Glide.with(context).load(qry_details.get(i).getPostedByimage()).into(((IncomingViewHolder) holder).binding.usrImg2);


                ((IncomingViewHolder) holder).binding.btnShowMore.setOnClickListener(v -> {
                    if (((IncomingViewHolder) holder).binding.btnShowMore.getText().toString().toLowerCase().contains("read more")) {
                        ((IncomingViewHolder) holder).binding.btnShowMore.setText("Read Less...");
                        ((IncomingViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt);
                    } else {
                        ((IncomingViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt.subSequence(0, 100));
                        ((IncomingViewHolder) holder).binding.btnShowMore.setText("Read More...");
                    }
                });

                if (!qry_details.get(i).getAttachments().isEmpty()) {
                    ((IncomingViewHolder) holder).binding.fileCycle.setVisibility(View.VISIBLE);
                    adapter = new FileAttachmentAdapter(
                            qry_details.get(holder.getAdapterPosition()).getAttachments(), context, fileDownloadHelper, activity, i);
                    ((IncomingViewHolder) holder).binding.fileCycle.setAdapter(adapter);
                } else {
                    ((IncomingViewHolder) holder).binding.fileCycle.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                final String qry_details_txt = qry_details.get(i).getQuery();

                if (qry_details_txt.length() > 100) {
                    ((OutGoingViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt.subSequence(0, 100));
                    ((OutGoingViewHolder) holder).binding.btnShowMore.setText("Read More...");
                    ((OutGoingViewHolder) holder).binding.btnShowMore.setVisibility(View.VISIBLE);
                } else {
                    ((OutGoingViewHolder) holder).binding.tvSentMsg.setText(qry_details_txt);
                    ((OutGoingViewHolder) holder).binding.btnShowMore.setVisibility(View.GONE);
                }


                ((OutGoingViewHolder) holder).binding.tvSentName.setText(qry_details.get(i).getPostedBy());
                ((OutGoingViewHolder) holder).binding.tvSentTime.setText(qry_details.get(i).getPostedOn());

                ((OutGoingViewHolder) holder).binding.usrImg2.setVisibility(View.VISIBLE);

                Glide.with(context).load(qry_details.get(i).getPostedByimage()).into(((OutGoingViewHolder) holder).binding.usrImg2);

                if (!qry_details.get(i).getAttachments().isEmpty()) {
                    ((OutGoingViewHolder) holder).binding.fileCycle.setVisibility(View.VISIBLE);
                    adapter = new FileAttachmentAdapter(
                            qry_details.get(holder.getAdapterPosition()).getAttachments(), context, fileDownloadHelper, activity, i);
                    ((OutGoingViewHolder) holder).binding.fileCycle.setAdapter(adapter);
                } else {
                    ((OutGoingViewHolder) holder).binding.fileCycle.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public int getItemCount() {
        return (qry_details != null ? qry_details.size() : 0);
    }

    public static class IncomingViewHolder extends RecyclerView.ViewHolder {
        ItemChatIncomingBinding binding;

        public IncomingViewHolder(@NonNull ItemChatIncomingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class OutGoingViewHolder extends RecyclerView.ViewHolder {
        ItemChatOutgoingBinding binding;

        public OutGoingViewHolder(@NonNull ItemChatOutgoingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void startDownloading(int position) {
        adapter.startDownloading(position);

    }

    public void finishDownloading(int position) {
        adapter.finishDownloading(position);


    }

}
