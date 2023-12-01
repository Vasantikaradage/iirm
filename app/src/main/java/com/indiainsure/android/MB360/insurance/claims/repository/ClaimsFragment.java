package com.indiainsure.android.MB360.insurance.claims.repository;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.indiainsure.android.MB360.databinding.FragmentClaimsBinding;
import com.indiainsure.android.MB360.insurance.claims.repository.ui.ClaimIntimationListener;
import com.indiainsure.android.MB360.insurance.claims.repository.ui.IntimationViewPagerAdapter;


public class ClaimsFragment extends Fragment implements ClaimIntimationListener {

    FragmentClaimsBinding binding;
    View view;
    NavController navController;
    //initially to get the employee serial number form selected policy
    String PRODUCT_CODE = "GMC";
    IntimationViewPagerAdapter viewPagerAdapter;


    public ClaimsFragment() {
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
        binding = FragmentClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //viewModel scoped in the fragment.

        //ViewPager
        setupViewPager();

        return view;
    }

    private void setupViewPager() {
        viewPagerAdapter = new IntimationViewPagerAdapter(getChildFragmentManager(), getLifecycle(),this);

        binding.intimateClaimsViewPager.setAdapter(viewPagerAdapter);
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("Intimated Claim"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("Intimate Now"));

        binding.tabs.viewPagerTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.intimateClaimsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.intimateClaimsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabs.viewPagerTabs.selectTab(binding.tabs.viewPagerTabs.getTabAt(position));
            }
        });




    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onClaimIntimatedListener() {
        binding.tabs.viewPagerTabs.selectTab(binding.tabs.viewPagerTabs.getTabAt(0));

    }

   /* private void getClaims() {
        //to get the  policyFeatures data we need some parameters from load session values


        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {
            PRODUCT_CODE = groupPolicyData.getProductCode();

            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
                String employeeSrNo = "";
                String groupChildSrvNo = "";
                String oeGrpBasInfSrNo = "";

                switch (PRODUCT_CODE) {
                    case "GMC":
                        GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum;
                        groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);

                        //queries for claims
                        employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();
                        groupChildSrvNo = groupGMCPolicyEmployeeDatum.getGroupchildsrno();
                        oeGrpBasInfSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();

                        claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
                        break;
                    case "GPA":
                        GroupGPAPolicyEmployeeDatum groupGPAPolicyEmployeeDatum;
                        groupGPAPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0);

                        //queries for claims
                        employeeSrNo = groupGPAPolicyEmployeeDatum.getEmployeeSrNo();
                        groupChildSrvNo = groupGPAPolicyEmployeeDatum.getGroupchildsrno();
                        oeGrpBasInfSrNo = groupGPAPolicyEmployeeDatum.getOeGrpBasInfSrNo();

                        claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
                        break;
                    case "GTL":

                        GroupGTLPolicyEmployeeDatum groupGTLPolicyEmployeeDatum;
                        groupGTLPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0);

                        //queries for claims
                        employeeSrNo = groupGTLPolicyEmployeeDatum.getEmployeeSrNo();
                        groupChildSrvNo = groupGTLPolicyEmployeeDatum.getGroupchildsrno();
                        oeGrpBasInfSrNo = groupGTLPolicyEmployeeDatum.getOeGrpBasInfSrNo();

                        claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
                        break;
                    default:
                        //error
                        Toast.makeText(getContext(), "Something Wrong Happened", Toast.LENGTH_SHORT).show();

                }


            });


        });


        claimsViewModel.getClaimsData().observe(getViewLifecycleOwner(), claimsResponse -> {

            if (claimsResponse != null) {
                adapter = new ClaimAdapter(requireContext(), claimsResponse.getClaimslist());
                binding.claimsRecyclerView.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, claimsResponse.getClaimslist().size());

                if (!claimsResponse.getResult().getStatus()) {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText("" + claimsResponse.getResult().getMessage());
                }
            } else {
                binding.messageTextView.setVisibility(View.VISIBLE);
                binding.messageTextView.setText("Sorry, data not available");
            }


        });
    }*/


}