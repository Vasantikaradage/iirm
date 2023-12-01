package com.indiainsure.android.MB360.insurance.dialogues.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.databinding.PolicyListItemBinding;
import com.indiainsure.android.MB360.insurance.dialogues.interfaces.OnPolicySelected;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SelectedPolicyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<GroupPolicyData> policyList;
    Activity activity;
    int index;
    SelectedPolicyViewModel selectedPolicyViewModel;
    OnPolicySelected onPolicySelected;

    SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    public SelectedPolicyAdapter(List<GroupPolicyData> policyList, SelectedPolicyViewModel selectedPolicyViewModel, int index, OnPolicySelected onPolicySelected) {
        this.policyList = policyList;
        this.activity = activity;
        this.index = index;
        this.selectedPolicyViewModel = selectedPolicyViewModel;
        this.onPolicySelected = onPolicySelected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PolicyListItemBinding binding = PolicyListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SelectedPolicyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        ((SelectedPolicyViewHolder) holder).binding.policyType.setText(MessageFormat.format("{0} {1}", policyList.get(position).getPolicyType(), "POLICY"));
        ((SelectedPolicyViewHolder) holder).binding.policyNumber.setText(policyList.get(position).getPolicyNumber());
        String validUpto = policyList.get(position).getPolicyCommencementDate() + " To " + policyList.get(position).getPolicyValidUpto();


        try {
            Date startDate = inputDateFormat.parse(policyList.get(position).getPolicyCommencementDate());
            Date endDate = inputDateFormat.parse(policyList.get(position).getPolicyValidUpto());

            String formattedStartDate = outputDateFormat.format(startDate);
            String formattedEndDate = outputDateFormat.format(endDate);

            validUpto = "[" + formattedStartDate + " To " + formattedEndDate + "]";
        } catch (Exception e) {
            e.printStackTrace();
        }


        ((SelectedPolicyViewHolder) holder).binding.policyValidDate.setText(validUpto);
        ((SelectedPolicyViewHolder) holder).binding.policyRadio.setChecked(index == position);


        ((SelectedPolicyViewHolder) holder).binding.policyCard.setOnClickListener(view -> {
            index = holder.getAdapterPosition();
            notifyItemRangeChanged(0, policyList.size());
            selectPolicy(index);
        });

        ((SelectedPolicyViewHolder) holder).binding.policyRadio.setOnClickListener(view -> {
            index = holder.getAdapterPosition();
            notifyItemRangeChanged(0, policyList.size());
            selectPolicy(index);
        });


    }

    public void selectPolicy(int index) {
        //this function will handle the selection of the policies inside the view model;
        //function should be taken care of or called using an interface since this adapter is getting called from a dialogue view
        // interface can be implemented but view-model complexity will still exist!.
        selectedPolicyViewModel.setSelectedPolicy(index);
        selectedPolicyViewModel.setSelectedIndex(index);
        onPolicySelected.onSelected();

    }

    @Override
    public int getItemCount() {
        if (policyList != null) {
            return policyList.size();
        } else {
            return 0;
        }
    }

    public static class SelectedPolicyViewHolder extends RecyclerView.ViewHolder {

        PolicyListItemBinding binding;

        public SelectedPolicyViewHolder(@NonNull PolicyListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
