package com.indiainsure.android.MB360.insurance.queries.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyQueriesViewPagerAdapter extends FragmentStateAdapter {

    OnQuerySubmittedSuccessfully onQuerySubmittedSuccessfully;

    public MyQueriesViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, OnQuerySubmittedSuccessfully onQuerySubmittedSuccessfully) {
        super(fragmentManager, lifecycle);
        this.onQuerySubmittedSuccessfully = onQuerySubmittedSuccessfully;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            //to return new Query fragment
            return new QueryFaqFragment(onQuerySubmittedSuccessfully);
        } else {
            return new QueryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
