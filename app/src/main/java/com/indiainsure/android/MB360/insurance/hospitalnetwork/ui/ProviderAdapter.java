package com.indiainsure.android.MB360.insurance.hospitalnetwork.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.ItemNetworkProviderBinding;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.List;

public class ProviderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<HospitalInformation> nHospDetails, nHospfiltered;
    OnHospitalSelectedListener onHospitalSelectedListener;


    public ProviderAdapter(Context context, List<HospitalInformation> providerList, OnHospitalSelectedListener onHospitalSelectedListener) {
        this.context = context;
        this.nHospDetails = providerList;
        this.onHospitalSelectedListener = onHospitalSelectedListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNetworkProviderBinding binding = ItemNetworkProviderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProviderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        LogMyBenefits.i(LogTags.HOSPITAL_NETWORK, "setItemsToList: " + position);
        final HospitalInformation listItem = nHospDetails.get(position);

        if (!listItem.getHospName().isEmpty())
            ((ProviderViewHolder) holder).binding.txtHospName.setText(listItem.getHospName());

        if (!listItem.getHospAddress().isEmpty())
            ((ProviderViewHolder) holder).binding.txtHospAddr.setText(listItem.getHospAddress().replace("NOT AVALIABLE", ""));

        if (listItem.getHospPhoneNo() != null)
            ((ProviderViewHolder) holder).binding.txtHospCont.setText(listItem.getHospPhoneNo().replace(",", "/"));

        Linkify.addLinks(((ProviderViewHolder) holder).binding.txtHospCont, Linkify.PHONE_NUMBERS);
        UtilMethods.stripUnderlines(((ProviderViewHolder) holder).binding.txtHospCont);
        //((ProviderViewHolder) holder).binding.imgPhone.setColorFilter(ContextCompat.getColor(this.context, R.color.primary_variant));
       // ((ProviderViewHolder) holder).binding.IVloc.setColorFilter(ContextCompat.getColor(this.context, R.color.primary_variant));
        ((ProviderViewHolder) holder).binding.tvbadgecount.setText("" + (position + 1));
        switch (listItem.getHospLevelOfCare()) {
            case "Primary":
                ((ProviderViewHolder) holder).binding.tvPC.setText("Primary Care");
                ((ProviderViewHolder) holder).binding.v1.setBackgroundColor(ContextCompat.getColor(this.context, R.color.primary_variant));
                ((ProviderViewHolder) holder).binding.tvPC.setTextColor(ContextCompat.getColor(this.context, R.color.primary_variant));
                ((ProviderViewHolder) holder).binding.llbadge.setBackgroundResource(R.drawable.badge_ribbon);
                break;

            case "Secondary":
                ((ProviderViewHolder) holder).binding.tvPC.setText("Secondary Care");
                ((ProviderViewHolder) holder).binding.v1.setBackgroundColor(ContextCompat.getColor(this.context, R.color.icon_color));
                ((ProviderViewHolder) holder).binding.tvPC.setTextColor(ContextCompat.getColor(this.context, R.color.icon_color));
                ((ProviderViewHolder) holder).binding.llbadge.setBackgroundResource(R.drawable.badge_secondary);

                break;

            case "Tertiary":
                ((ProviderViewHolder) holder).binding.tvPC.setText("Tertiary Care");
                ((ProviderViewHolder) holder).binding.v1.setBackgroundColor(ContextCompat.getColor(this.context, R.color.icon_color_primary));
                ((ProviderViewHolder) holder).binding.tvPC.setTextColor(ContextCompat.getColor(this.context, R.color.icon_color_primary));
                ((ProviderViewHolder) holder).binding.llbadge.setBackgroundResource(R.drawable.badge_tertiary);
                break;

            default:
                ((ProviderViewHolder) holder).binding.tvPC.setText("Other");
                ((ProviderViewHolder) holder).binding.v1.setBackgroundColor(ContextCompat.getColor(this.context, R.color.sc));
                ((ProviderViewHolder) holder).binding.tvPC.setTextColor(ContextCompat.getColor(this.context, R.color.sc));
                ((ProviderViewHolder) holder).binding.llbadge.setBackgroundResource(R.drawable.badge_other);
                break;
        }


        ((ProviderViewHolder) holder).binding.txtHospName.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://www.google.com/search?q=" + listItem.getHospName());
            Intent gSearchIntent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(gSearchIntent);
        });


        ((ProviderViewHolder) holder).binding.btnMap.setOnClickListener(view -> {
            onHospitalSelectedListener.selectedHospital(nHospDetails.get(position).getHospName(), nHospDetails.get(position).getHospAddress(),nHospDetails.get(position).getLatitude(),nHospDetails.get(position).getLongitude());
        });

    }

    @Override
    public int getItemCount() {
        if (nHospDetails != null) {
            return nHospDetails.size();
        } else {
            return 0;
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    nHospfiltered = nHospDetails;
                } else {
                    List<HospitalInformation> filteredList = new ArrayList<>();
                    for (HospitalInformation row : nHospDetails) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getHospAddress().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getHospName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    nHospfiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = nHospfiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                nHospfiltered = (ArrayList<HospitalInformation>) filterResults.values;
                notifyItemRangeChanged(0, nHospfiltered.size());
            }
        };
    }

    public static class ProviderViewHolder extends RecyclerView.ViewHolder {
        ItemNetworkProviderBinding binding;

        public ProviderViewHolder(@NonNull ItemNetworkProviderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
