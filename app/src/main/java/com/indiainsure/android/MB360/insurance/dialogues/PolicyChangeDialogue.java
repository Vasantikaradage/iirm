package com.indiainsure.android.MB360.insurance.dialogues;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.recyclerview.widget.DividerItemDecoration;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.PolicyChangeDialogueBinding;
import com.indiainsure.android.MB360.insurance.dialogues.adapter.SelectedPolicyAdapter;
import com.indiainsure.android.MB360.insurance.dialogues.interfaces.OnPolicySelected;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.utilities.CustomDividerItemDecoration;

import java.util.List;

public class PolicyChangeDialogue implements OnPolicySelected {

    private final Activity activity;
    private AlertDialog view;
    PolicyChangeDialogueBinding binding;
    SelectedPolicyAdapter adapter;
    SelectedPolicyViewModel selectedPolicyViewModel;


    public PolicyChangeDialogue(Activity activity, SelectedPolicyViewModel selectedPolicyViewModel) {
        this.activity = activity;
        this.selectedPolicyViewModel = selectedPolicyViewModel;
    }

    public void showPolicyAlert(List<GroupPolicyData> policyList, int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        binding = PolicyChangeDialogueBinding.inflate(LayoutInflater.from(activity.getApplicationContext()));
        builder.setView(binding.getRoot());
        builder.setCancelable(true);
        view = builder.create();
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams wmlp = view.getWindow().getAttributes();
        wmlp.windowAnimations = com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        view.show();


        //set up the recyclerview for the policy visible list were user can select the policy
        adapter = new SelectedPolicyAdapter(policyList, selectedPolicyViewModel, index, this);
        binding.policyCycle.setAdapter(adapter);

        if (policyList != null) {
            if (policyList.size() > 1) {

                CustomDividerItemDecoration itemDecoration = new CustomDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, R.drawable.divider);
                itemDecoration.setLastItemPosition(policyList.size() - 1);
                binding.policyCycle.addItemDecoration(itemDecoration);
            }
        }


    }

    @Override
    public void onSelected() {
        view.dismiss();
    }
}
