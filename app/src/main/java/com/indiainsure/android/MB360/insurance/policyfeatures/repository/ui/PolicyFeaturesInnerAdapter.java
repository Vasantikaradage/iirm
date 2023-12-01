package com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.R;

import java.util.ArrayList;

public class PolicyFeaturesInnerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<PolicyInnerRecyclerModel> policyInnerList;

    public PolicyFeaturesInnerAdapter(ArrayList<PolicyInnerRecyclerModel> policyInnerList) {
        this.policyInnerList = policyInnerList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inner_policy_feature, parent, false);

        return new PolicyFeaturesInnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PolicyFeaturesInnerViewHolder) holder).innerHeader.setText(  androidx.core.text.HtmlCompat.fromHtml(policyInnerList.get(position).getInnerHeader(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
        ((PolicyFeaturesInnerViewHolder) holder).innerDescription.setText( androidx.core.text.HtmlCompat.fromHtml(policyInnerList.get(position).getInnerDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString()   );

    }

    @Override
    public int getItemCount() {
        return policyInnerList.size();
    }

    public static class PolicyFeaturesInnerViewHolder extends RecyclerView.ViewHolder {
        TextView innerHeader, innerDescription;

        public PolicyFeaturesInnerViewHolder(@NonNull View itemView) {
            super(itemView);
            innerHeader = itemView.findViewById(R.id.policy_inner_header);
            innerDescription = itemView.findViewById(R.id.policy_inner_description);
        }
    }
}
