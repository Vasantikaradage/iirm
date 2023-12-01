package com.indiainsure.android.MB360.insurance.claims.repository.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.ClaimsInfo;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.List;

public class ClaimAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<ClaimsInfo> claimsInfoList = new ArrayList<>();

    public ClaimAdapter(Context mContext, List<ClaimsInfo> claimsInfoList) {
        this.claimsInfoList = claimsInfoList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_intimated_claims, parent, false);

        return new ClaimsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        final ClaimsInfo intimateClaimViewHolder = claimsInfoList.get(position);

        ((ClaimsViewHolder) holder).claim_id.setText(intimateClaimViewHolder.getIntimationSrNo());
        ((ClaimsViewHolder) holder).employee.setText(String.format("%s - %s", intimateClaimViewHolder.getEmployeeNo(), intimateClaimViewHolder.getEmployeeName()));
        ((ClaimsViewHolder) holder).claimname.setText(intimateClaimViewHolder.getPersonName());
        ((ClaimsViewHolder) holder).claimedon.setText(intimateClaimViewHolder.getIntimations().replace("Pm", "PM")
                .replace("Am", "AM"));


        ((ClaimsViewHolder) holder).tvNOH.setText(intimateClaimViewHolder.getNameOfHospital());
        ((ClaimsViewHolder) holder).tvDOA.setText(intimateClaimViewHolder.getDoaLikelydoa());
        ((ClaimsViewHolder) holder).tvestdAMT.setText(UtilMethods.PriceFormat(intimateClaimViewHolder.getClaimAmount()));
        ((ClaimsViewHolder) holder).tvdiag.setText(intimateClaimViewHolder.getDiagnosisOrAilment());

        ((ClaimsViewHolder) holder).openArrow.setOnClickListener(v -> {
            ((ClaimsViewHolder) holder).openDialog.performClick();
        });
        ((ClaimsViewHolder) holder).openDialog.setOnClickListener(v -> {
            boolean expanded = intimateClaimViewHolder.isExpanded();

            intimateClaimViewHolder.setExpanded(!expanded);
            notifyItemChanged(position);
        });

        if (intimateClaimViewHolder.isExpanded()) {
            final RotateAnimation rotateAnim = new RotateAnimation(180, 0,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            rotateAnim.setDuration(300);
            rotateAnim.setFillAfter(true);
            ((ClaimsViewHolder) holder).openArrow.startAnimation(rotateAnim);
            ((ClaimsViewHolder) holder).openArrow.setImageResource(R.drawable.ic_minus);
            ((ClaimsViewHolder) holder).closeDialog.setVisibility(View.VISIBLE);

        } else {
            final RotateAnimation rotateAnim = new RotateAnimation(0, 180,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);

            rotateAnim.setDuration(300);
            rotateAnim.setFillAfter(true);
            ((ClaimsViewHolder) holder).openArrow.startAnimation(rotateAnim);
            ((ClaimsViewHolder) holder).openArrow.setImageResource(R.drawable.ic_plus);
            ((ClaimsViewHolder) holder).closeDialog.setVisibility(View.GONE);
        }

        if (intimateClaimViewHolder.getDiagnosisOrAilment().length() < 100)
            ((ClaimsViewHolder) holder).btnShowMore.setVisibility(View.GONE);

        ((ClaimsViewHolder) holder).btnShowMore.setOnClickListener(v -> {
            if (((ClaimsViewHolder) holder).tvdiag.getMaxLines() == 3) {
                ((ClaimsViewHolder) holder).tvdiag.setMaxLines(Integer.MAX_VALUE);
                ((ClaimsViewHolder) holder).btnShowMore.setText(mContext.getString(R.string.read_less));
            } else {
                ((ClaimsViewHolder) holder).tvdiag.setMaxLines(3);
                ((ClaimsViewHolder) holder).btnShowMore.setText(mContext.getString(R.string.read_more));
            }

        });

    }

    @Override
    public int getItemCount() {
        if (claimsInfoList != null) {
            return claimsInfoList.size();
        } else {
            return 0;
        }
    }

    public static class ClaimsViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView claim_id, claimname, claimedon, tvNOH, tvDOA, tvestdAMT, tvdiag, btnShowMore, employee;
        private LinearLayout openDialog, closeDialog;
        private AppCompatImageView openArrow;


        public ClaimsViewHolder(@NonNull View itemView) {
            super(itemView);
            openArrow = itemView.findViewById(R.id.openArrow);
            claim_id = itemView.findViewById(R.id.claim_id);
            claimname = itemView.findViewById(R.id.claimname);
            claimedon = itemView.findViewById(R.id.claimedon);
            tvNOH = itemView.findViewById(R.id.tvNOH);
            tvDOA = itemView.findViewById(R.id.tvDOA);
            tvestdAMT = itemView.findViewById(R.id.tvestdAMT);
            tvdiag = itemView.findViewById(R.id.tvdiag);
            openDialog = itemView.findViewById(R.id.openDialog);
            closeDialog = itemView.findViewById(R.id.closeDialog);
            btnShowMore = itemView.findViewById(R.id.btnShowMore);
            employee = itemView.findViewById(R.id.tvEmployee);

        }
    }
}
