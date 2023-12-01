package com.indiainsure.android.MB360.insurance.queries.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.databinding.ItemQueryFaqBinding;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryFaqModel;

import java.util.List;

public class QueriesFaqAdapter extends RecyclerView.Adapter<QueriesFaqAdapter.QueriesFaqViewHolder> {

    List<QueryFaqModel> faqQueryList ;
    Context context;

    private int mExpandedPosition = 0;
    private int previousExpandedPosition = -1;

    public QueriesFaqAdapter(List<QueryFaqModel> faqQueryList, Context context) {
        this.faqQueryList = faqQueryList;
        this.context = context;
    }

    @NonNull
    @Override
    public QueriesFaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQueryFaqBinding binding = ItemQueryFaqBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new QueriesFaqViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QueriesFaqViewHolder holder, int position) {

        try {
            final boolean isExpanded = holder.getAdapterPosition() == mExpandedPosition;

            if (isExpanded) previousExpandedPosition = holder.getAdapterPosition();

            holder.binding.lblQry.setText(faqQueryList.get(position).getQuestion());
            holder.binding.lblQryAns.setText(faqQueryList.get(position).getAnswer());
            holder.binding.lblQryAns.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            if (isExpanded) {
                final RotateAnimation rotateAnim = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                rotateAnim.setDuration(200);
                rotateAnim.setFillAfter(true);
                holder.binding.icEdit.setAnimation(rotateAnim);
            }

            holder.binding.constLayout.setOnClickListener(v -> {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyItemChanged(previousExpandedPosition);
                notifyItemChanged(position);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return (faqQueryList == null ? 0 : faqQueryList.size());
    }

    public static class QueriesFaqViewHolder extends RecyclerView.ViewHolder {
        ItemQueryFaqBinding binding;

        public QueriesFaqViewHolder(@NonNull ItemQueryFaqBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
