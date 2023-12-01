package com.indiainsure.android.MB360.insurance.escalations;

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

import com.google.android.material.tabs.TabLayout;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentSupportBinding;
import com.indiainsure.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.indiainsure.android.MB360.insurance.escalations.repository.EscalationsViewModel;
import com.indiainsure.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;
import com.indiainsure.android.MB360.insurance.escalations.repository.responseclass.GroupEscalationInfo;
import com.indiainsure.android.MB360.insurance.escalations.repository.ui.EscalationAdapter;
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


public class SupportFragment extends Fragment {
    FragmentSupportBinding binding;
    View view;
    NavController navController;

    //Escalation ViewModel
    EscalationsViewModel escalationsViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;

    EscalationAdapter adapter;
    String PRODUCT_CODE = "GMC";

    TabLayout.OnTabSelectedListener onTabSelectedListener;

    List<GroupPolicyData> policyData = new ArrayList<>();
    int selectedIndex;


    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSupportBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        escalationsViewModel = new ViewModelProvider(SupportFragment.this).get(EscalationsViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);


        //adding the tabs
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GHI"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GPA"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GTL"));

        onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getEscalationsWithTabs("GMC");
                        break;
                    case 1:
                        getEscalationsWithTabs("GPA");
                        break;
                    case 2:
                        getEscalationsWithTabs("GTL");
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


        escalationsViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        escalationsViewModel.getErrorState().observe(getViewLifecycleOwner(), error -> {

            if (error) {
                binding.errorLayout.setVisibility(View.VISIBLE);
                // binding.escalationRecyclerView.setVisibility(View.GONE);
            } else {
                binding.errorLayout.setVisibility(View.GONE);
                // binding.escalationRecyclerView.setVisibility(View.VISIBLE);
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getEscalations();

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

        escalationsViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });


    }

    private void getEscalationsWithTabs(String code) {
        try {
            String oegrpbasinfosrno = "";

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
                    // escalationsViewModel.getEscalations(groupChildSrnNo, oegrpbasinfosrno);

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
                    //escalationsViewModel.getEscalations(groupChildSrnNo, oegrpbasinfosrno);
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
                    //  escalationsViewModel.getEscalations(groupChildSrnNo, oegrpbasinfosrno);

                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEscalations() {
        //to get the  policyFeatures data we need some parameters from load session values
        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), policy -> {

            String oeGrpBasInfSrNo = policy.getOeGrpBasInfSrNo();

            PRODUCT_CODE = policy.getProductCode();

            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

                try {


                    Log.d(LogTags.ESCALATION_ACTIVITY, "onCreateView: " + loadSessionResponse.toString());
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

                    try {
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
                        }

                    } catch (Exception e) {
                        //we don't get the loadSession.
                        e.printStackTrace();

                    }


                    String groupChildSrvNo = "";

                    String employeeSrNo = "";


                    switch (PRODUCT_CODE) {
                        case "GMC":
                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(onTabSelectedListener);
                            binding.tabs.viewPagerTabs.getTabAt(0).select();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(onTabSelectedListener);

                            break;
                        case "GPA":
                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(onTabSelectedListener);
                            binding.tabs.viewPagerTabs.getTabAt(1).select();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(onTabSelectedListener);

                            break;
                        case "GTL":

                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(onTabSelectedListener);
                            binding.tabs.viewPagerTabs.getTabAt(2).select();
                            groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(onTabSelectedListener);

                            break;
                        default:
                            Toast.makeText(getContext(), "Something Wrong Happened", Toast.LENGTH_SHORT).show();
                            //error
                    }

                    escalationsViewModel.getEscalations(groupChildSrvNo, oeGrpBasInfSrNo);

                } catch (Exception e) {
                    e.printStackTrace();
                    //  binding.errorLayout.setVisibility(View.VISIBLE);

                }
            });

            escalationsViewModel.getEscalationsData().observe(getViewLifecycleOwner(), escalationsResponse -> {

                if (escalationsResponse != null) {
                    adapter = new EscalationAdapter(requireContext(), getEscalationList(escalationsResponse.getGroupEscalationInfo()));
                    binding.escalationRecyclerView.setAdapter(adapter);
                    adapter.notifyItemRangeChanged(0, escalationsResponse.getGroupEscalationInfo().size());

                    if (escalationsResponse.getGroupEscalationInfo().isEmpty()) {
                        //binding.errorLayout.setVisibility(View.VISIBLE);
                        // binding.escalationRecyclerView.setVisibility(View.GONE);

                    } else {
                        binding.errorLayout.setVisibility(View.GONE);
                        // binding.escalationRecyclerView.setVisibility(View.VISIBLE);

                    }

                } else {
                    //binding.escalationRecyclerView.setVisibility(View.GONE);
                    binding.errorLayout.setVisibility(View.VISIBLE);
                }
            });
        });

    }

    private void getEscalationResponse(EscalationsResponse escalationsResponse) {

    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.errorLayout.setVisibility(View.GONE);
        binding.escalationRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.escalationRecyclerView.setVisibility(View.VISIBLE);

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

    private void getPolicyFeaturesSpinner(GroupPolicyData groupPolicyData) {
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            String PRODUCT_CODE = groupPolicyData.getProductCode();
            String OE_GRP_BAS_INFO_SR_NO = groupPolicyData.getOeGrpBasInfSrNo();
            String GROUP_CHILD_SR_NO = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            String EMPLOYEE_SR_NO = "";

            //  escalationsViewModel.getEscalations(GROUP_CHILD_SR_NO, OE_GRP_BAS_INFO_SR_NO);


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

    private List<GroupEscalationInfo> getEscalationList(List<GroupEscalationInfo> groupEscalationInfo) {

        List<GroupEscalationInfo> escalationList = new ArrayList<>();

        for (GroupEscalationInfo lstContac : groupEscalationInfo
        ) {
            if (lstContac.getDispEmail().equalsIgnoreCase("0") &&
                    lstContac.getDispMob().equalsIgnoreCase("0") &&
                    lstContac.getDispFax().equalsIgnoreCase("0") &&
                    lstContac.getDispAdd().equalsIgnoreCase("0")
            ) {
                //nothing to do
            } else {
                escalationList.add(lstContac);
            }

        }


        return escalationList;
    }

}