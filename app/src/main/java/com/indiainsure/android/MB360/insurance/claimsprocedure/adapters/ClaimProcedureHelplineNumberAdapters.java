package com.indiainsure.android.MB360.insurance.claimsprocedure.adapters;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.databinding.ItemClaimProcedureHelplineNumberBinding;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.EmergencyContactNo;

import java.util.List;

public class ClaimProcedureHelplineNumberAdapters extends RecyclerView.Adapter<ClaimProcedureHelplineNumberAdapters.HelpLineViewHolder> {

    Context context;
    List<EmergencyContactNo> contactNoList;

    public ClaimProcedureHelplineNumberAdapters(Context context, List<EmergencyContactNo> contactNoList) {
        this.context = context;
        this.contactNoList = contactNoList;
    }

    @NonNull
    @Override
    public HelpLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemClaimProcedureHelplineNumberBinding binding = ItemClaimProcedureHelplineNumberBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HelpLineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpLineViewHolder holder, int position) {
        try {
            holder.binding.claimsProcedureEmergencyContact.setText(contactNoList.get(position).getContactNumber());
            Linkify.addLinks(holder.binding.claimsProcedureEmergencyContact, Linkify.PHONE_NUMBERS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (contactNoList == null ? 0 : contactNoList.size());
    }

    public static class HelpLineViewHolder extends RecyclerView.ViewHolder {
        ItemClaimProcedureHelplineNumberBinding binding;

        public HelpLineViewHolder(@NonNull ItemClaimProcedureHelplineNumberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
