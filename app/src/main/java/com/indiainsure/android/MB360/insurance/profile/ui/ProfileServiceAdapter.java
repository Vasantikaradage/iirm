package com.indiainsure.android.MB360.insurance.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.databinding.ItemServicesProfileBinding;
import com.indiainsure.android.MB360.insurance.profile.response.ProfileServiceModel;

import java.util.ArrayList;


public class ProfileServiceAdapter extends RecyclerView.Adapter<ProfileServiceAdapter.ProfileServiceViewHolder> {

    Context context;
    ArrayList<ProfileServiceModel> menuList;
    ProfileServiceClickListener profileServiceClickListener;


    public ProfileServiceAdapter(Context context, ArrayList<ProfileServiceModel> menuList, ProfileServiceClickListener profileServiceClickListener) {
        this.context = context;
        this.menuList = menuList;
        this.profileServiceClickListener = profileServiceClickListener;
    }


    @NonNull
    @Override
    public ProfileServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServicesProfileBinding binding = ItemServicesProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProfileServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileServiceViewHolder holder, int position) {

        try {

            holder.binding.profileIcon.setImageDrawable(menuList.get(position).getMenuIcon());
            holder.binding.profileServiceName.setText(menuList.get(position).getMenuName());


            holder.binding.profileItem.setOnClickListener(v -> {
                profileServiceClickListener.onProfileMenuClicked(menuList.get(position).getMenuName());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return (menuList != null ? menuList.size() : 0);
    }

    public static class ProfileServiceViewHolder extends RecyclerView.ViewHolder {
        ItemServicesProfileBinding binding;

        public ProfileServiceViewHolder(@NonNull ItemServicesProfileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
