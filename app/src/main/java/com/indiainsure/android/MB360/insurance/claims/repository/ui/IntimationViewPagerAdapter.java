package com.indiainsure.android.MB360.insurance.claims.repository.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class IntimationViewPagerAdapter extends FragmentStateAdapter {
    ClaimIntimationListener claimIntimationListener;

    public IntimationViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ClaimIntimationListener claimIntimationListener) {
        super(fragmentManager, lifecycle);
        this.claimIntimationListener = claimIntimationListener;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new NewIntimatedClaimsFragment(claimIntimationListener);
        } else {
            return new LoadIntimatedClaimsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
