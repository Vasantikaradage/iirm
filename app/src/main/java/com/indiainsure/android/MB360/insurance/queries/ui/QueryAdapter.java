package com.indiainsure.android.MB360.insurance.queries.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.ItemQueriesBinding;
import com.indiainsure.android.MB360.insurance.queries.responseclass.AllQuery;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<AllQuery> queryList;
    Context context;
    private String status = "New";
    OnQuerySelected onQuerySelected;


    public QueryAdapter(List<AllQuery> queryList, Context context, OnQuerySelected onQuerySelected) {
        this.queryList = queryList;
        this.context = context;
        this.onQuerySelected = onQuerySelected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQueriesBinding binding = ItemQueriesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new QueryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {

            AllQuery qry = queryList.get(position);
            status = "New";
            ((QueryViewHolder) holder).binding.tvQRYid.setText(qry.getTicketNumber());
            ((QueryViewHolder) holder).binding.tvTimeStamp.setText(qry.getPostedDate());
            String value = qry.getLastReply().trim().replace("<br/>", "\n");
            if (!qry.getLastReply().isEmpty()) {
                if (value.equals("This query has no replies")) {
                    if (qry.getEqCustQrySolved().equals("1") || qry.getEqCustQryEnded().equals("1")) {
                        setLastQueryReply(value, holder);

                    } else {
                        ((QueryViewHolder) holder).binding.tvQry.setText(qry.getCompleteQueryText());
                    }
                } else {
                    setLastQueryReply(value, holder);
                }
            } else {
                ((QueryViewHolder) holder).binding.tvQry.setText(qry.getCompleteQueryText());
            }
            /*String ne = "";
            if (value.length() > 66) {
                ne = value.substring(0, 66) + "...";
                ((QueryViewHolder) holder).binding.tvQry.setText(ne);
            } else {
                ((QueryViewHolder) holder).binding.tvQry.setText(value);
            }*/

            //        holder.txtMore.setVisibility(qry.getCOMP_QRY_TXT().length() < 100 ? View.GONE : View.VISIBLE);
          /*  if (!qry.getNoOfReplies().equals("0"))
                status = "Replied";
            else if (qry.getEqCustQrySolved().equals("1"))
                status = "Solved";
            else if (qry.getEqCustQryEnded().equals("1"))
                status = "Ended";*/

            if (qry.getNoOfReplies().equals("0")) {
                status = "New";

                if (qry.getEqCustQrySolved().equals("1")) {

                    status = "Solved";
                }
                if (qry.getEqCustQryEnded().equals("1")) {
                    status = "Ended";

                }
            } else if (qry.getEqCustQrySolved().equals("1")) {
                status = "Solved";
            } else if (qry.getEqCustQryEnded().equals("1")) {
                status = "Ended";

            } else {
                status = "Replied";

                if (qry.getEqCustQrySolved().equals("1")) {
                    status = "Solved";

                }
                if (qry.getEqCustQryEnded().equals("1")) {
                    status = "Ended";

                }
            }


            ((QueryViewHolder) holder).binding.tvQRYstatus.setText(status);
            switch (status) {
                case "New":
                    ((QueryViewHolder) holder).binding.tvQRYstatus.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.new_query));
                    break;
                case "Replied":
                    ((QueryViewHolder) holder).binding.tvQRYstatus.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.replied_query));
                    break;
                case "Solved":
                    ((QueryViewHolder) holder).binding.tvQRYstatus.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.solved_query));
                    break;
                case "Ended":
                    ((QueryViewHolder) holder).binding.tvQRYstatus.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ended_query));
                    break;
            }

            ((QueryViewHolder) holder).binding.btnView.setOnClickListener(view -> {

                onQuerySelected.selectedQuery(qry);

            });
            ((QueryViewHolder) holder).binding.tvQryReplies.setText(String.format(" %s Replies ", qry.getNoOfReplies()));

        } catch (Exception e) {
            LogMyBenefits.e(LogTags.QUERY_ACTIVITY, e.toString());
        }
    }

    private void setLastQueryReply(String value, RecyclerView.ViewHolder holder) {
        String ne = "";
        if (value.length() > 66) {
            ne = value.substring(0, 66) + "...";
            ((QueryViewHolder) holder).binding.tvQry.setText(ne);
        } else {
            ((QueryViewHolder) holder).binding.tvQry.setText(value);
        }
    }

    @Override
    public int getItemCount() {
        if (queryList != null) {
            return queryList.size();
        } else {
            return 0;
        }
    }

    public static class QueryViewHolder extends RecyclerView.ViewHolder {
        ItemQueriesBinding binding;

        public QueryViewHolder(@NonNull ItemQueriesBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
