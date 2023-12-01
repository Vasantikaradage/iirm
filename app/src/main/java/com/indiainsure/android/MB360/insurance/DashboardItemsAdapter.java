package com.indiainsure.android.MB360.insurance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.indiainsure.android.MB360.databinding.DashboardItemVariantBinding;
import com.indiainsure.android.MB360.databinding.ItemDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class DashboardItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<DashBoardModel> dashboardList = new ArrayList<>();
    DashboardItemClickListener dashboardItemClickListener;

    public DashboardItemsAdapter(Context context, List<DashBoardModel> dashboardList, DashboardItemClickListener dashboardItemClickListener) {
        this.context = context;
        this.dashboardList = dashboardList;
        this.dashboardItemClickListener = dashboardItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ItemDashboardBinding binding = ItemDashboardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new DashBoardViewHolder(binding);
        } else {
            DashboardItemVariantBinding binding = DashboardItemVariantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new DashboardVariantViewHolder(binding);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(true);
        if (holder.getItemViewType() == 0) {
            ((DashBoardViewHolder) holder).binding.itemDashboardText.setText(dashboardList.get(position).getDashBoardHeader());
            ((DashBoardViewHolder) holder).binding.itemDashboardImage.setImageDrawable(dashboardList.get(position).getDashBoardImage());

            if (!dashboardList.get(position).getDashBoardTextDescription().equalsIgnoreCase("")) {

                if (!dashboardList.get(position).getDashBoardHeader().equalsIgnoreCase("claim\nprocedures")) {
                    ((DashBoardViewHolder) holder).binding.itemDashboardTextDescription.setText(dashboardList.get(position).getDashBoardTextDescription());
                    ((DashBoardViewHolder) holder).binding.itemDashboardTextDescription.setVisibility(View.VISIBLE);
                    ((DashBoardViewHolder) holder).binding.itemDashboardShimmer.getRoot().setVisibility(View.GONE);
                    ((DashBoardViewHolder) holder).binding.itemDashboardText.setMaxLines(1);
                } else {
                    ((DashBoardViewHolder) holder).binding.itemDashboardText.setMaxLines(2);
                    ((DashBoardViewHolder) holder).binding.itemDashboardTextDescription.setVisibility(View.GONE);

                }
            } else {
                if (position == 3 || position == 2) {
                    ((DashBoardViewHolder) holder).binding.itemDashboardShimmer.getRoot().setVisibility(View.GONE);
                    ((DashBoardViewHolder) holder).binding.itemDashboardTextDescription.setVisibility(View.GONE);
                    ((DashBoardViewHolder) holder).binding.itemDashboardText.setMaxLines(2);
                } else {
                    ((DashBoardViewHolder) holder).binding.itemDashboardShimmer.getRoot().setVisibility(View.VISIBLE);

                }

            }

            if (dashboardList.get(position).getDashBoardHeader().equalsIgnoreCase("claim\nprocedures")){
                ((DashBoardViewHolder) holder).binding.itemDashboardShimmer.getRoot().setVisibility(View.GONE);
            }

            ((DashBoardViewHolder) holder).binding.itemDashboardCard.setOnClickListener(v -> {
                dashboardItemClickListener.onDashboardItemClicked(dashboardList.get(position).getDashBoardHeader());
            });
        } else {

            ((DashboardVariantViewHolder) holder).binding.itemDashboardText.setText(dashboardList.get(position).getDashBoardHeader());
            ((DashboardVariantViewHolder) holder).binding.itemDashboardImage.setImageDrawable(dashboardList.get(position).getDashBoardImage());

            ((DashboardVariantViewHolder) holder).binding.itemDashboardCard.setOnClickListener(v -> {
                dashboardItemClickListener.onDashboardItemClicked(dashboardList.get(position).getDashBoardHeader());
            });
        }

    }

    @Override
    public int getItemViewType(int position) {
        return (position > 3 ? 1 : 0);

       /* if (dashboardList.get(position).getDashBoardHeader().equalsIgnoreCase(context.getString(R.string.my_coverages))) {
            return 0;
        } else if (dashboardList.get(position).getDashBoardHeader().equalsIgnoreCase(context.getString(R.string.provider_network))) {
            return 0;
        } else if (dashboardList.get(position).getDashBoardHeader().equalsIgnoreCase(context.getString(R.string.my_claims))) {
            return 0;
        } else if (dashboardList.get(position).getDashBoardHeader().equalsIgnoreCase(context.getString(R.string.my_queries))) {
            return 0;
        } else {
            return 1;
        }*/


    }

    @Override
    public int getItemCount() {
        return (dashboardList != null ? dashboardList.size() : 0);
    }

    public static class DashBoardViewHolder extends RecyclerView.ViewHolder {
        ItemDashboardBinding binding;

        public DashBoardViewHolder(@NonNull ItemDashboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public static class DashboardVariantViewHolder extends RecyclerView.ViewHolder {
        DashboardItemVariantBinding binding;

        public DashboardVariantViewHolder(@NonNull DashboardItemVariantBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
