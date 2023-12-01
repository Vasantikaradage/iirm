package com.indiainsure.android.MB360.utilities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;

import com.indiainsure.android.MB360.databinding.InsuranceLoadingBinding;


public class LoadingInsuranceDialogue {

    Context context;
    Activity activity;
    InsuranceLoadingBinding binding;
    private AlertDialog view;

    public LoadingInsuranceDialogue(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void showLoading(String loading) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        binding = InsuranceLoadingBinding.inflate(inflater);

        builder.setView(binding.getRoot());
        builder.setCancelable(false);
        view = builder.create();
        view.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        view.show();

        // Creating Dynamic
        Rect displayRectangle = new Rect();

        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        view.getWindow().setLayout((int) (displayRectangle.width() *
                0.8f), view.getWindow().getAttributes().height);

        if (loading.equalsIgnoreCase("")) {
            binding.loadMessage.setText("Loading, Please wait...");
        } else {
            binding.loadMessage.setText(loading);
        }


    }

    public void hideLoading() {
        if (view != null) {
            view.dismiss();
        }


    }
}
