package com.indiainsure.android.MB360.insurance.FAQ.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.databinding.FaqItemBinding;
import com.indiainsure.android.MB360.insurance.FAQ.repository.responseclass.FaqData;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<FaqData> faqList;
    Context context;

    private int mExpandedPosition = -1;
    private int previousExpandedPosition = -1;


    public FaqAdapter(List<FaqData> faqList, Context context) {
        this.faqList = faqList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FaqItemBinding binding = FaqItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new FaqViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            final boolean isExpanded = holder.getAdapterPosition() == mExpandedPosition;

            if (isExpanded)
                previousExpandedPosition = holder.getAdapterPosition();

            final FaqData Query = faqList.get(holder.getAdapterPosition());



            String faqAnswer = Query.getFaqAns();
            faqAnswer = faqAnswer.replace("</li>", "\n");
            faqAnswer = faqAnswer.replace("<li>", "");
            faqAnswer = faqAnswer.replace("</ol>", "");
            faqAnswer = faqAnswer.replace("<ol>", "");
            faqAnswer = faqAnswer.replace("<ul>", "");
            faqAnswer = faqAnswer.replace("</ul>", "");
            faqAnswer = faqAnswer.replace("<br/>", "");
            faqAnswer = faqAnswer.replace("<span/>", "");
            faqAnswer = faqAnswer.replace("<span>", "");
            faqAnswer = faqAnswer.replace("</span>", "");
            faqAnswer = faqAnswer.replace("<u>", "");
            faqAnswer = faqAnswer.replace("</u>", "");
            faqAnswer = faqAnswer.replace("&nbsp;a", "");
            faqAnswer = faqAnswer.replace("<div>", "");
            faqAnswer = faqAnswer.replace("</div>", "");
            faqAnswer = faqAnswer.replace("<br>", "");
            faqAnswer = faqAnswer.replace("&nbsp;", "");
            faqAnswer = faqAnswer.replace("b", "");
            faqAnswer = faqAnswer.replace("/b", "");
            faqAnswer = faqAnswer.replace("&amp;", "");
            faqAnswer = faqAnswer.replace("/a", "");
            faqAnswer = faqAnswer.replace("/a", "");
            faqAnswer = faqAnswer.replace("<>", "");
            faqAnswer = faqAnswer.replace("</>", "");
            //faqAnswer = Jsoup.parse(faqAnswer).text();

            ((FaqViewHolder) holder).binding.lblQry.setText(Query.getFaqQuestion());
            ((FaqViewHolder) holder).binding.lblQryAns.setText(faqAnswer);
            ((FaqViewHolder) holder).binding.lblQryAns.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            if (isExpanded) {
                final RotateAnimation rotateAnim = new RotateAnimation(0, 180,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);

                rotateAnim.setDuration(200);
                rotateAnim.setFillAfter(true);
                ((FaqViewHolder) holder).binding.icEdit.setAnimation(rotateAnim);
            }

            ((FaqViewHolder) holder).binding.constLayout.setOnClickListener(v -> {
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
        return (faqList != null ? faqList.size() : 0);
    }


    public static class FaqViewHolder extends RecyclerView.ViewHolder {
        FaqItemBinding binding;

        public FaqViewHolder(@NonNull FaqItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
