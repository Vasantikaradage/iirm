package com.indiainsure.android.MB360.insurance.myclaims.ui;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.tabs.TabLayout;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentMyClaimsBinding;
import com.indiainsure.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.indiainsure.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.ClaimInformation;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MyClaimsFragment extends Fragment implements OnClaimSelected {

    FragmentMyClaimsBinding binding;
    String PRODUCT_CODE = "GMC";
    View view;

    MyClaimsViewModel claimsViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    MyClaimsAdapter adapter;
    NavController navController;
    TabLayout.OnTabSelectedListener onTabSelectedListener;

    List<GroupPolicyData> policyData = new ArrayList<>();
    int selectedIndex;


    public MyClaimsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getClaimsData();

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

        claimsViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });
    }

    private void getClaimsData() {
        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), policy -> {

            String oeGrpBasInfSrNo = policy.getOeGrpBasInfSrNo();

            PRODUCT_CODE = policy.getProductCode();

            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

                try {

                    Log.d(LogTags.CLAIM_ACTIVITY, "onCreateView: " + loadSessionResponse.toString());


                    String groupChildSrnNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                    String empSrNo = "";

                    empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                    claimsViewModel.getMyClaims(groupChildSrnNo, empSrNo);

                  /*  switch (policy.getProductCode().toUpperCase()) {
                        case "GMC":

                            empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                            claimsViewModel.getMyClaims(groupChildSrnNo, empSrNo);

                            break;
                        case "GPA":

                            empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().get(0).getEmployeeSrNo();
                            claimsViewModel.getMyClaims(groupChildSrnNo, empSrNo);

                            break;
                        case "GTL":

                            empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().get(0).getEmployeeSrNo();
                            claimsViewModel.getMyClaims(groupChildSrnNo, empSrNo);

                            break;
                        default:
                            Toast.makeText(getContext(), "Something Wrong Happened", Toast.LENGTH_SHORT).show();
                            //error
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        claimsViewModel.getMyClaimsData().observe(getViewLifecycleOwner(), claims -> {
            if (claims != null) {
                if (claims.getMessage() != null) {

                    if (claims.getMessage().getStatus()) {
                        if (claims.getClaimInformation().isEmpty()) {
                            binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                            binding.noClaimsFoundText.setText(getString(R.string.no_claims_reported));
                        } else {
                            binding.myClaimsCycle.setVisibility(View.VISIBLE);
                            adapter = new MyClaimsAdapter(claims.getClaimInformation(), requireContext(), this);
                            binding.myClaimsCycle.setAdapter(adapter);
                            binding.emptyClaimsLayout.setVisibility(View.GONE);
                            binding.noClaimsFoundText.setText("");
                        }
                    } else {
                        //something happened in the server and the status is false
                        binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                        binding.noClaimsFoundText.setText(getString(R.string.no_claims_reported));
                    }

                } else {
                    //something happened in the server and the status is false
                    binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                    binding.noClaimsFoundText.setText(getString(R.string.no_claims_reported));
                }


            } else {
                //claims returned as null most probably some error happened within the response
                binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                binding.noClaimsFoundText.setText(claimsViewModel.getErrorDescription().getValue() == null ? getString(R.string.something_went_wrong) : claimsViewModel.getErrorDescription().getValue())
                ;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        claimsViewModel = new ViewModelProvider(requireActivity()).get(MyClaimsViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);


        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        //adding the tabs
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GHI"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GPA"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GTL"));

        onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getMyClaimsWithTabs("GMC");
                        break;
                    case 1:
                        getMyClaimsWithTabs("GPA");
                        break;
                    case 2:
                        getMyClaimsWithTabs("GTL");
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

        binding.tabs.viewPagerTabs.addOnTabSelectedListener(onTabSelectedListener);


        //    getClaims();


        claimsViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        binding.ghiChip.setOnClickListener(v -> {
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            try {

                if (!loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData().isEmpty()) {
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


        return view;

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
                        if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGPAPolicyEmployeeData().isEmpty()) {
                            binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                            binding.noClaimsFoundText.setText(getString(R.string.no_claims_reported));
                            binding.myClaimsCycle.setVisibility(View.GONE);
                        }


                        break;
                    case "GTL":
                        List<GroupPolicyData> gtlPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());

                        selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                        selectedPolicyViewModel.setGroupGTLPoliciesData(gtlPolicy);
                        selectedPolicyViewModel.getAllPoliciesData();
                        selectedPolicyViewModel.setSelectedIndex(0);
                        selectedPolicyViewModel.setSelectedPolicyFromDropDown(gtlPolicy.get(0));

                        if (loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGTLPolicyEmployeeData().isEmpty()) {
                            binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                            binding.noClaimsFoundText.setText(getString(R.string.no_claims_reported));
                            binding.myClaimsCycle.setVisibility(View.GONE);
                        }
                        break;
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMyClaimsWithTabs(String code) {
        try {
            String oegrpbasinfosrno = "";
            String empSrNo = "";

            String employeeSrno = "";
            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            assert loadSessionResponse != null;
            if (!loadSessionResponse.getGroupProducts().isEmpty()) {
                for (GroupProduct groupProduct : loadSessionResponse.getGroupProducts()) {

                    switch (groupProduct.getProductCode().toLowerCase()) {
                        case "gmc":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(0).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(0).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                        case "gpa":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(1).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(1).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                        case "gtl":
                            if (!groupProduct.getActive().equalsIgnoreCase("1")) {

                                binding.tabs.viewPagerTabs.getTabAt(2).view.setEnabled(false);
                                binding.tabs.viewPagerTabs.getTabAt(2).setTabLabelVisibility(TabLayout.TAB_LABEL_VISIBILITY_UNLABELED);
                            }
                            break;
                    }

                }
            }
            String groupChildSrnNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();

            switch (code) {
                case "GMC":
                    List<GroupPolicyData> gmcPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData());

                    selectedPolicyViewModel.setGroupGMCPoliciesData(gmcPolicy);
                    selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gmcPolicy.get(0));

                    for (GroupPolicyData policy : gmcPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oegrpbasinfosrno = policy.getOeGrpBasInfSrNo();
                        }
                    }

                    break;
                case "GPA":
                    List<GroupPolicyData> gpaPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGPAPoliciesData());

                    selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGPAPoliciesData(gpaPolicy);
                    selectedPolicyViewModel.setGroupGTLPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gpaPolicy.get(0));

                    for (GroupPolicyData policy : gpaPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oegrpbasinfosrno = policy.getOeGrpBasInfSrNo();
                        }
                    }

                    break;

                case "GTL":
                    List<GroupPolicyData> gtlPolicy = sort(loadSessionResponse.getGroupPolicies().get(0).getGroupGTLPoliciesData());

                    selectedPolicyViewModel.setGroupGMCPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGPAPoliciesData(new ArrayList<>());
                    selectedPolicyViewModel.setGroupGTLPoliciesData(gtlPolicy);
                    selectedPolicyViewModel.getAllPoliciesData();
                    selectedPolicyViewModel.setSelectedIndex(0);
                    selectedPolicyViewModel.setSelectedPolicyFromDropDown(gtlPolicy.get(0));

                    for (GroupPolicyData policy : gtlPolicy) {
                        if (policy.getPolicyType().equalsIgnoreCase("base")) {
                            oegrpbasinfosrno = policy.getOeGrpBasInfSrNo();
                        }
                    }

                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getClaims() {
        claimsViewModel.getMyClaimsData().observe(getViewLifecycleOwner(), claims -> {
            if (claims != null) {
                if (claims.getMessage() != null) {

                    if (claims.getMessage().getStatus()) {
                        if (claims.getClaimInformation().isEmpty()) {
                            binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                            binding.noClaimsFoundText.setText(getString(R.string.no_claims_reported));
                        } else {
                            adapter = new MyClaimsAdapter(claims.getClaimInformation(), requireContext(), this);
                            binding.myClaimsCycle.setAdapter(adapter);
                            binding.emptyClaimsLayout.setVisibility(View.GONE);
                            binding.noClaimsFoundText.setText("");
                        }
                    } else {
                        //something happened in the server and the status is false
                        binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                        binding.noClaimsFoundText.setText(getString(R.string.something_went_wrong));
                    }

                } else {
                    //something happened in the server and the status is false
                    binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                    binding.noClaimsFoundText.setText(getString(R.string.something_went_wrong));
                }


            } else {
                //claims returned as null most probably some error happened within the response
                binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                binding.noClaimsFoundText.setText(claimsViewModel.getErrorDescription().getValue() == null ? getString(R.string.something_went_wrong) : claimsViewModel.getErrorDescription().getValue())
                ;
            }
        });
    }

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
    public void onClaimSelected(ClaimInformation claims) {

        claimsViewModel.setSelectedClaim(claims);
        NavDirections actions = MyClaimsFragmentDirections.actionMyClaimsFragmentToMyClaimsdetails(claims.getClaimSrNo());
        navController.navigate(actions);
    }
}