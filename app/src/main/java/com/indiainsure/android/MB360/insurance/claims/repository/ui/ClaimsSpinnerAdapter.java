package com.indiainsure.android.MB360.insurance.claims.repository.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.ClaimBeneficiary;

import java.util.List;
import java.util.StringTokenizer;

public class ClaimsSpinnerAdapter extends ArrayAdapter<ClaimBeneficiary> {

    public ClaimsSpinnerAdapter(Context context, List<ClaimBeneficiary> personsList) {
        super(context, 0, personsList);
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
        //custom View
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.intimate_claim_spinner_item, parent, false);

        }

        TextView name = convertView.findViewById(R.id.claims_beneficiary_name);
        ClaimBeneficiary person = getItem(position);

        // when current item is not null
        if (person != null) {
            StringTokenizer nameTokenizer = new StringTokenizer(person.getPersonName(), "-");
            //to remove the designation
            String name_person = nameTokenizer.nextToken();
            name.setText(name_person);
            if (position == 0) {
                convertView.findViewById(R.id.dropdown_image).setVisibility(View.VISIBLE);
            } else {
                convertView.findViewById(R.id.dropdown_image).setVisibility(View.GONE);
            }
        }


        return convertView;
    }
}
