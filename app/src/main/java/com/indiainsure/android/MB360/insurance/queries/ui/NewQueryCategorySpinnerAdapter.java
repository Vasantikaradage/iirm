package com.indiainsure.android.MB360.insurance.queries.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.indiainsure.android.MB360.R;

import java.util.List;

public class NewQueryCategorySpinnerAdapter extends ArrayAdapter<String> {
    public NewQueryCategorySpinnerAdapter(@NonNull Context context, List<String> category) {
        super(context, 0, category);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        //custom view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.intimate_claim_spinner_item, parent, false);
        }
        TextView categoryText = convertView.findViewById(R.id.claims_beneficiary_name);
        String category = getItem(position);

        categoryText.setText(category);

        if (category != null) {
            if (position == 0) {
                convertView.findViewById(R.id.dropdown_image).setVisibility(View.GONE);
            } else {
                convertView.findViewById(R.id.dropdown_image).setVisibility(View.GONE);
            }
        }
        return convertView;
    }
}

