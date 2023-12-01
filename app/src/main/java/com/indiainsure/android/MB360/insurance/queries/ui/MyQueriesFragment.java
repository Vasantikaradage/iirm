package com.indiainsure.android.MB360.insurance.queries.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentMyQueriesBinding;
import com.indiainsure.android.MB360.insurance.queries.repository.QueryViewModel;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.utilities.UtilMethods;


public class MyQueriesFragment extends Fragment implements OnQuerySubmittedSuccessfully {

    FragmentMyQueriesBinding binding;
    View view;
    MyQueriesViewPagerAdapter viewPagerAdapter;
    LoadSessionViewModel loadSessionViewModel;
    QueryViewModel queryViewModel;
    String empSrNo = "";


    public MyQueriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyQueriesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        queryViewModel = new ViewModelProvider(requireActivity()).get(QueryViewModel.class);

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), response -> {
            empSrNo = response.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData()
                    .get(0).getEmployeeSrNo();


        });

        //viewpager
        setUpViewPager();

        queryViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });


        return view;
    }

    private void setUpViewPager() {
        viewPagerAdapter = new MyQueriesViewPagerAdapter(getChildFragmentManager(), getLifecycle(), this);

        binding.myQueriesViewPager.setAdapter(viewPagerAdapter);
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText(getString(R.string.my_queries)));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText(getString(R.string.new_queries)));


        binding.tabs.viewPagerTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.myQueriesViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.myQueriesViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabs.viewPagerTabs.selectTab(binding.tabs.viewPagerTabs.getTabAt(position));
            }
        });

    }

    @Override
    public void onSuccess() {
        queryViewModel.getQueries(empSrNo);
        binding.myQueriesViewPager.setCurrentItem(0);
    }
}