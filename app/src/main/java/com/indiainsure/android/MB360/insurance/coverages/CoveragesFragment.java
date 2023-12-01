package com.indiainsure.android.MB360.insurance.coverages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.tabs.TabLayout;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentCoveragesBinding;
import com.indiainsure.android.MB360.insurance.coverages.repository.CoveragesViewModel;
import com.indiainsure.android.MB360.insurance.coverages.repository.ui.CoveragesDetailsAdapter;
import com.indiainsure.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupGPAPolicyEmployeeDatum;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupGTLPolicyEmployeeDatum;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class CoveragesFragment extends Fragment {

    FragmentCoveragesBinding binding;
    NavController navController;
    View view;

    //initially to get the coverages details
    String PRODUCT_CODE = "GMC";

    //Coverages ViewModel
    CoveragesViewModel coveragesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;

    //listeners
    TabLayout.OnTabSelectedListener tabSelectedListener;

    //adapters
    CoveragesDetailsAdapter adapter;
    SpinnerAdapter spinnerAdapter;
    List<GroupPolicyData> policyData = new ArrayList<>();
    int selectedIndex;

    public CoveragesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCoveragesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //viewModel scoped in the fragment.
        coveragesViewModel = new ViewModelProvider(requireActivity()).get(CoveragesViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);


        //adding the tabs
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GHI"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GPA"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GTL"));


        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
            if (loadSessionResponse != null) {
                if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                    for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                        switch (groupProduct.getProductCode().toLowerCase()) {
                            case "gmc":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                    binding.tabs.viewPagerTabs.getTabAt(0).view.setEnabled(false);
                                    binding.tabs.viewPagerTabs.getTabAt(0).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    binding.ghiChip.setVisibility(View.GONE);
                                }
                                break;
                            case "gpa":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                    binding.tabs.viewPagerTabs.getTabAt(1).view.setEnabled(false);
                                    binding.tabs.viewPagerTabs.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    binding.gpaChip.setVisibility(View.GONE);
                                }
                                break;
                            case "gtl":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                    binding.tabs.viewPagerTabs.getTabAt(2).view.setEnabled(false);
                                    binding.tabs.viewPagerTabs.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                                    binding.gtlChip.setVisibility(View.GONE);
                                }
                                break;
                        }

                    }
                }
            }

        });

        //this need to be below as we need no overwrite of coverages
        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        //getCoveragesPolicyWithTabs("GMC");
                        break;
                    case 1:
                        // getCoveragesPolicyWithTabs("GPA");
                        break;
                    case 2:
                        // getCoveragesPolicyWithTabs("GTL");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };

        binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);

        getCoverage();

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        coveragesViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.tabsBlocker.setVisibility(View.VISIBLE);

            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.tabsBlocker.setVisibility(View.GONE);

            }
        });

        coveragesViewModel.getErrorState().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.errorLayout2.setVisibility(View.GONE);
                binding.coveragesDetailsCycle.setVisibility(View.GONE);
                binding.policyHolderCard.setVisibility(View.GONE);
                binding.policyHolderCard.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.sumInsuredCardHold.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.errorLayout.setVisibility(View.GONE);
                if (binding.errorLayout2.getVisibility() == View.GONE) {
                    binding.coveragesDetailsCycle.setVisibility(View.VISIBLE);
                    binding.policyHolderCard.setVisibility(View.VISIBLE);
                    binding.policyHolderCard.setVisibility(View.VISIBLE);
                    binding.sumInsuredCardHold.setVisibility(View.VISIBLE);
                } else {
                    binding.coveragesDetailsCycle.setVisibility(View.GONE);
                    binding.policyHolderCard.setVisibility(View.GONE);
                    binding.policyHolderCard.setVisibility(View.GONE);
                    binding.sumInsuredCardHold.setVisibility(View.GONE);
                }


            }
        });


        return view;
    }


    private void getCoverage() {

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {
            PRODUCT_CODE = groupPolicyData.getProductCode();

            String oeGrpBasInfoSrNo = groupPolicyData.getOeGrpBasInfSrNo();

            //to get the  coverages data we need some parameters from load session values
            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
                Log.d(LogTags.COVERAGE_ACTIVITY, "onCreateView: " + loadSessionResponse.toString());

                String groupChildSrvNo = "";

                String employeeSrNo = "";
                String gmc_employee_srNo = "";

                if (loadSessionResponse != null) {


                    try {
                        groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno() != null ? loadSessionResponse.getGroupInfoData().getGroupchildsrno() : "";
                        gmc_employee_srNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                        switch (PRODUCT_CODE) {
                            case "GMC":
                                binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                                binding.tabs.viewPagerTabs.getTabAt(0).select();
                                List<GroupGMCPolicyEmployeeDatum> gmcPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData();


                                employeeSrNo = gmcPolicy.get(0).getEmployeeSrNo();
                                if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                    //todo somethings wrong with the server-response.
                                }
                                binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);

                                break;
                            case "GPA":
                                binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                                binding.tabs.viewPagerTabs.getTabAt(1).select();
                                List<GroupGPAPolicyEmployeeDatum> gpaPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData();

                                if (gpaPolicy.size() != 0) {
                                    employeeSrNo = gpaPolicy.get(0).getEmployeeSrNo();
                                }
                                if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                    //todo somethings wrong with the server-response.
                                }
                                binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);
                                break;
                            case "GTL":
                                binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                                binding.tabs.viewPagerTabs.getTabAt(2).select();
                                List<GroupGTLPolicyEmployeeDatum> gtlPolicy = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData();

                                if (gtlPolicy.size() != 0) {
                                    employeeSrNo = gtlPolicy.get(0).getEmployeeSrNo();
                                }
                                if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                    //todo somethings wrong with the server-response.
                                }

                                binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);
                                break;
                        }

                        coveragesViewModel.getCoveragePolicyData(groupChildSrvNo, oeGrpBasInfoSrNo).observe(getViewLifecycleOwner(), coverageResponse -> {
                            if (coverageResponse != null) {
                                if (!coverageResponse.getCoveragePolicyData().isEmpty()) {
                                    binding.policyNumber.setText(coverageResponse.getCoveragePolicyData().get(0).getPolicyNumber());
                                    String policyValidUpTo = "[" + coverageResponse.getCoveragePolicyData().get(0).getPolicyCommencementDate() + " To " + coverageResponse.getCoveragePolicyData().get(0).getPolicyValidUpto() + "]";
                                    binding.policyDateValid.setText(policyValidUpTo);
                                    binding.brokerName.setText(coverageResponse.getCoveragePolicyData().get(0).getBrokerName().equalsIgnoreCase("not available") ? "-" : coverageResponse.getCoveragePolicyData().get(0).getBrokerName());
                                    binding.insurerName.setText(coverageResponse.getCoveragePolicyData().get(0).getInsuranceCoName().equalsIgnoreCase("not available") ? "-" : coverageResponse.getCoveragePolicyData().get(0).getInsuranceCoName());
                                    binding.tpaName.setText(coverageResponse.getCoveragePolicyData().get(0).getTpaName().equalsIgnoreCase("not available") ? "-" : coverageResponse.getCoveragePolicyData().get(0).getTpaName());

                                    binding.errorLayout2.setVisibility(View.GONE);
                                    binding.policyHolderCard.setVisibility(View.VISIBLE);
                                    binding.sumInsuredCardHold.setVisibility(View.VISIBLE);
                                    binding.coveragesDetailsCycle.setVisibility(View.VISIBLE);
                                }
                            } else {
                                binding.errorLayout2.setVisibility(View.VISIBLE);
                                binding.policyHolderCard.setVisibility(View.GONE);
                                binding.sumInsuredCardHold.setVisibility(View.GONE);
                                binding.coveragesDetailsCycle.setVisibility(View.GONE);
                                Log.d(LogTags.COVERAGE_ACTIVITY, "NULL VALUE");
                            }
                        });

                        coveragesViewModel.getCoverageDetails(groupChildSrvNo, oeGrpBasInfoSrNo, PRODUCT_CODE, employeeSrNo, gmc_employee_srNo).observe(getViewLifecycleOwner(), coverageDetailsResponse -> {
                            try {


                                if (coverageDetailsResponse != null) {

                                    //set up the recycler view for coverages details
                                    //set adapter
                                    adapter = new CoveragesDetailsAdapter(coverageDetailsResponse.getCoveragesData(), getContext());
                                    binding.coveragesDetailsCycle.setAdapter(adapter);
                                    //adapter.notifyItemRangeChanged(0, coverageDetailsResponse.getCoveragesData().size());


                                    if (!coverageDetailsResponse.getCoveragesData().isEmpty()) {
                                        binding.baseSumInsured.setText(getString(R.string.base_sum_insured, UtilMethods.PriceFormat(coverageDetailsResponse.getCoveragesData().get(0).getBaseSumInsured())));
                                        if (coverageDetailsResponse.getCoveragesData().get(0).getTopUpFlag() == -1) {
                                            binding.topUpAmount.setText(getString(R.string.top_up_amount, "N/A"));

                                        }
                                        if (coverageDetailsResponse.getCoveragesData().get(0).getTopUpFlag() == 0) {
                                            binding.topUpAmount.setText(getString(R.string.top_up_amount, "0"));
                                        } else {
                                            binding.errorLayout2.setVisibility(View.GONE);
                                            binding.txtSecondary.setVisibility(View.VISIBLE);
                                            binding.topUpAmount.setVisibility(View.VISIBLE);
                                            binding.topUpAmount.setText(getString(R.string.top_up_amount, UtilMethods.PriceFormat(coverageDetailsResponse.getCoveragesData().get(0).getTopUpBaseSumInsured())));
                                        }

                                        if (coverageDetailsResponse.getCoveragesData().get(0).getTopUpBaseSumInsured().equalsIgnoreCase("-1")) {
                                            binding.topUpAmount.setVisibility(View.VISIBLE);
                                            binding.topUpAmount.setText("-");
                                        }
                                    } else {
                                        binding.errorLayout2.setVisibility(View.VISIBLE);
                                        binding.policyHolderCard.setVisibility(View.GONE);
                                        binding.sumInsuredCardHold.setVisibility(View.GONE);
                                        binding.coveragesDetailsCycle.setVisibility(View.GONE);
                                        Log.d(LogTags.COVERAGE_ACTIVITY, "NULL VALUE");
                                    }
                                } else {
                                    binding.errorLayout2.setVisibility(View.VISIBLE);
                                    binding.policyHolderCard.setVisibility(View.GONE);
                                    binding.sumInsuredCardHold.setVisibility(View.GONE);
                                    binding.coveragesDetailsCycle.setVisibility(View.GONE);
                                    Log.d(LogTags.COVERAGE_ACTIVITY, "NULL VALUE");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(LogTags.COVERAGE_ACTIVITY, "ERROR", e);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        coveragesViewModel.setError(true);
                        binding.coveragesDetailsCycle.setVisibility(View.GONE);
                        binding.errorLayout.setVisibility(View.VISIBLE);
                        binding.policyHolderCard.setVisibility(View.GONE);
                        binding.sumInsuredCardHold.setVisibility(View.GONE);
                    }
                } else {
                    coveragesViewModel.setError(true);
                    binding.coveragesDetailsCycle.setVisibility(View.GONE);
                    binding.errorLayout.setVisibility(View.VISIBLE);
                    binding.policyHolderCard.setVisibility(View.GONE);
                    binding.sumInsuredCardHold.setVisibility(View.GONE);
                }
            });

        });


    }


    /**
     * @link {{@link #sort(List)}}
     * to sort the list before setting up to the spinner.
     **/
    private List<GroupPolicyData> sort(List<GroupPolicyData> list) {

        List<GroupPolicyData> list_active = new ArrayList<>();

        for (GroupPolicyData policyData : list
        ) {
            if (policyData.getActive().equalsIgnoreCase("1") || policyData.getActive().equalsIgnoreCase("ACTIVE")) {
                list_active.add(policyData);
            }

        }


        list_active.sort(Comparator.comparing(GroupPolicyData::getOeGrpBasInfSrNo));

        return list_active;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.spinnerHolder.setOnClickListener(v -> {

            PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
            policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);

        });

        selectedPolicyViewModel.getPolicyData().observe(getViewLifecycleOwner(), policyData -> {
            this.policyData = policyData;
        });

        selectedPolicyViewModel.getSelectedIndex().observe(getViewLifecycleOwner(), index -> {
            this.selectedIndex = index;
        });

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            //change the selection chips ui
            selectChip(groupPolicyData);
            setTextWithFancyAnimation(binding.selectedPolicyText, "" + groupPolicyData.getPolicyNumber());


            binding.policySelectionText.setText(groupPolicyData.getPolicyNumber());
        });

        binding.ghiChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData().isEmpty()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    setPolicyWithChips("gmc");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }

        });
        binding.gpaChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData().isEmpty()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    setPolicyWithChips("gpa");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }
        });
        binding.gtlChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData().isEmpty()) {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    setPolicyWithChips("gtl");
                } else {
                    Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(requireActivity(), "Policy not available!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.selectPolicyChip.setOnClickListener(v -> {
            PolicyChangeDialogue policyChangeDialogue = new PolicyChangeDialogue(requireActivity(), selectedPolicyViewModel);
            policyChangeDialogue.showPolicyAlert(policyData, selectedIndex);
        });

        coveragesViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });

    }


    private void selectChip(GroupPolicyData groupPolicyData) {
        if (groupPolicyData != null) {
            switch (groupPolicyData.getProductCode().toLowerCase()) {
                case "gmc":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));

                    break;
                case "gpa":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    break;
                case "gtl":
                    binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
                    binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));

                    //text color
                    binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
                    binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                    break;
            }
        } else {
            //selecting gmc default

            binding.ghiChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background_selected));
            binding.gpaChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));
            binding.gtlChip.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.chips_background));

            //text color
            binding.ghiChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.gpaChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
            binding.gtlChipText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_grey));
        }
    }

    private void setTextWithFancyAnimation(TextView codeView, String value) {
        Animation translateIn = new TranslateAnimation(0, 0, codeView.getHeight(), 0);
        translateIn.setInterpolator(new OvershootInterpolator());
        translateIn.setDuration(500);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(200);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(translateIn);
        animationSet.reset();
        animationSet.setStartTime(0);

        codeView.setText(String.valueOf(value));
        codeView.clearAnimation();
        codeView.startAnimation(animationSet);
    }

    private void setPolicyWithChips(String code) {
        try {
            loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
                if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                    for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                        switch (groupProduct.getProductCode().toLowerCase()) {
                            case "gmc":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.ghiChip.setVisibility(View.GONE);

                                }
                                break;
                            case "gpa":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.gpaChip.setVisibility(View.GONE);
                                }
                                break;
                            case "gtl":
                                if (!groupProduct.getActive().equalsIgnoreCase("1")) {
                                    binding.gtlChip.setVisibility(View.GONE);
                                }
                                break;
                        }

                    }
                } else {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }


                switch (code.toUpperCase()) {
                    case "GMC":
                        List<GroupPolicyData> gmcPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());

                        selectedPolicyViewModel.setGroupGMCPoliciesData(gmcPolicy);
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gmcPolicy.get(0));

                        break;
                    case "GPA":
                        List<GroupPolicyData> gpaPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData());
                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(gpaPolicy);
                        selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gpaPolicy.get(0));


                        break;
                    case "GTL":
                        List<GroupPolicyData> gtlPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());

                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(gtlPolicy);
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gtlPolicy.get(0));


                        break;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}