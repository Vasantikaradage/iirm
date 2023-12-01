package com.indiainsure.android.MB360.insurance.policyfeatures;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.google.android.material.tabs.TabLayout;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentPolicyFeaturesBinding;
import com.indiainsure.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.PolicyFeaturesViewModel;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponse;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui.PolicyFeatureDownloader;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui.PolicyFeaturesOuterAdapter;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui.PolicyFeaturesOuterModel;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui.PolicyInnerRecyclerModel;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui.PolicyfeatureDownloadHelper;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PolicyFeaturesFragment extends Fragment implements PolicyfeatureDownloadHelper {
    FragmentPolicyFeaturesBinding binding;
    NavController navController;
    View view;
    PolicyFeaturesOuterAdapter policyAdapter;
    SpinnerAdapter spinnerAdapter;

    //Policy ViewModel
    PolicyFeaturesViewModel policyFeaturesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;

    TabLayout.OnTabSelectedListener tabSelectedListener;

    List<GroupPolicyData> policyData = new ArrayList<>();
    int selectedIndex;

    int position;
    PolicyFeaturesOuterModel policyFeaturesOuterModel;

    // Declare a permission request launcher using the new API
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    PolicyFeaturePermissionHelper privacyPolicyPermissionHelper=new PolicyFeaturePermissionHelper() {
        @Override
        public void granted() {
            try {

                String url = policyFeaturesOuterModel.getInnerList().get(0).getInnerDescription();
                url = url.replaceAll(" ", "%20");
                Uri uri = Uri.parse(url);
/*
                Intent pdfIntent = PolicyFeatureDownloader.downloadFile("https://grouphealth.staypinc.com/mybenefits/Downloadables/1194/1194-1606/Sample%20Form/Get_Started_With_Smallpdf.pdf",
                        new File(requireContext().getFilesDir(),*//*String.valueOf(System.currentTimeMillis())*//*"Get_Started_With_Smallpdf.pdf"),
                        requireActivity(), requireContext());*/


                Intent pdfIntent = PolicyFeatureDownloader.downloadFile(uri.toString(),
                        new File(requireContext().getFilesDir(),policyFeaturesOuterModel.getPolicyType()+"-"+position+".pdf"),
                        requireActivity(), requireContext());
                if (pdfIntent != null) {
                    requireContext().startActivity(pdfIntent);
                } else {
                    //null
                }
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "No application found to view this file!", Toast.LENGTH_SHORT).show();
            }
            onFinishDownload(position);
        }
    };


    public PolicyFeaturesFragment() {
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
        binding = FragmentPolicyFeaturesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //viewModel scoped in the fragment.
        policyFeaturesViewModel = new ViewModelProvider(PolicyFeaturesFragment.this).get(PolicyFeaturesViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);

        //adding the tabs
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GHI"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GPA"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GTL"));

        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        binding.policyFeaturesHeaderText.setText(String.format("GMC", R.string.all_you_ever_wanted_to_know_about_your_benefits));
                        getPolicyFeaturesWithTabs("GMC");
                        binding.policyFeaturesHeaderText.setText("All you ever wanted to know about your Health Insurance Benefits");
                        break;
                    case 1:
                        getPolicyFeaturesWithTabs("GPA");
                        binding.policyFeaturesHeaderText.setText("All you ever wanted to know about your Personal Accident Benefits");
                        break;
                    case 2:
                        getPolicyFeaturesWithTabs("GTL");
                        binding.policyFeaturesHeaderText.setText("All you ever wanted to know about your Term life Benefits");
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

        policyFeaturesViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        policyFeaturesViewModel.getErrorState().observe(getViewLifecycleOwner(), error -> {

            if (error) {
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.policyFeatureRecyclerView.setVisibility(View.GONE);
            } else {
                binding.errorLayout.setVisibility(View.GONE);
                binding.policyFeatureRecyclerView.setVisibility(View.VISIBLE);
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

    private void getPolicyFeaturesSpinner(GroupPolicyData groupPolicyData) {
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

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

            String PRODUCT_CODE = groupPolicyData.getProductCode();
            String OE_GRP_BAS_INFO_SR_NO = groupPolicyData.getOeGrpBasInfSrNo();
            String GROUP_CHILD_SR_NO = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            String EMPLOYEE_SR_NO = "";

            policyFeaturesViewModel.getPolicyFeatures(GROUP_CHILD_SR_NO, OE_GRP_BAS_INFO_SR_NO, PRODUCT_CODE);


        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPolicyFeatures();

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

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), policyData -> {

            //change the selection chips ui
            selectChip(policyData);
            setTextWithFancyAnimation(binding.selectedPolicyText,  policyData.getPolicyNumber());


            binding.policySelectionText.setText(policyData.getPolicyNumber());
        });

        policyFeaturesViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });


    }

    private void getPolicyFeaturesWithTabs(String code) {
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

                    policyFeaturesViewModel.getPolicyFeatures(groupChildSrnNo, oegrpbasinfosrno, "GMC");

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

                    policyFeaturesViewModel.getPolicyFeatures(groupChildSrnNo, oegrpbasinfosrno, "GPA");
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
                    policyFeaturesViewModel.getPolicyFeatures(groupChildSrnNo, oegrpbasinfosrno, "GTL");

                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void getPolicyFeatures() {
        //to get the  policyFeatures data we need some parameters from load session values
        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {
            binding.policyFeaturesHeaderText.setText(getString(R.string.all_you_ever_wanted_to_know_about_your_benefits, groupPolicyData.getProductCode()));
            String oeGrpBasInfSrNo = groupPolicyData.getOeGrpBasInfSrNo();
            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
                LogMyBenefits.d(LogTags.POLICY_FEATURES_ACTIVITY, "onCreateView: " + loadSessionResponse.toString());
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

                String productType = "";

                switch (groupPolicyData.getProductCode()) {
                    case "GMC":
                        binding.policyFeaturesHeaderText.setText("All you ever wanted to know about your Health Insurance Benefits");

                        binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                        binding.tabs.viewPagerTabs.getTabAt(0).select();
                        //now we have access to load sessions inside the fragments with same view model in activity
                        groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                        productType = groupPolicyData.getProductCode();
                        policyFeaturesViewModel.getPolicyFeatures(groupChildSrvNo, oeGrpBasInfSrNo, productType);
                        binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);
                        break;
                    case "GPA":
                        binding.policyFeaturesHeaderText.setText("All you ever wanted to know about your Personal Accident Benefits");

                        binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                        binding.tabs.viewPagerTabs.getTabAt(1).select();
                        //now we have access to load sessions inside the fragments with same view model in activity
                        groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                        productType = groupPolicyData.getProductCode();
                        policyFeaturesViewModel.getPolicyFeatures(groupChildSrvNo, oeGrpBasInfSrNo, productType);
                        binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);
                        break;
                    case "GTL":
                        binding.policyFeaturesHeaderText.setText("All you ever wanted to know about your Term Life Benefits");


                        binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                        binding.tabs.viewPagerTabs.getTabAt(2).select();
                        //now we have access to load sessions inside the fragments with same view model in activity
                        groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                        productType = groupPolicyData.getProductCode();
                        policyFeaturesViewModel.getPolicyFeatures(groupChildSrvNo, oeGrpBasInfSrNo, productType);
                        binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);
                        break;
                }

            });

        });

        policyFeaturesViewModel.getPolicyFeaturesData().observe(getViewLifecycleOwner(), policyFeaturesResponse -> {
            if (policyFeaturesResponse != null) {
                setUpPolicyFeaturesList(policyFeaturesResponse);
            } else {
                LogMyBenefits.d(LogTags.POLICY_FEATURES_ACTIVITY, "NULL VALUE");
            }
        });


    }

    private void setUpPolicyFeaturesList(List<PolicyFeaturesResponse> policyDataList) {

        //policy points merging logic
        try {
            List<String> policyHeader = new ArrayList<>();
            ArrayList<PolicyFeaturesOuterModel> policyFeaturesMainList = new ArrayList<>();

            for (PolicyFeaturesResponse policyData : policyDataList) {
                if (policyHeader.contains(policyData.getPolFeatType())) {
                    LogMyBenefits.d("POLICY LIST", "setUpPolicyFeaturesList: " + "already exist " + policyData.getPolFeatType());
                } else {
                    if (policyData.getPolInfoDisplayTo().equals("1") || policyData.getPolInfoDisplayTo().equals("3")) {
                        policyHeader.add(policyData.getPolFeatType());
                        LogMyBenefits.d("POLICY LIST", "setUpPolicyFeaturesList: " + "added " + policyData.getPolFeatType());
                    }
                }
            }

            policyHeader.remove("BASE POLICY INFORMATION");
            policyHeader.remove("CORPORATE BUFFER");

            for (String header : policyHeader) {
                ArrayList<PolicyInnerRecyclerModel> innerlist = new ArrayList<>();
                for (PolicyFeaturesResponse policyData : policyDataList) {
                    if (header.equals(policyData.getPolFeatType())) {

                        if (policyData.getPolInfoDisplayTo().equals("1") || policyData.getPolInfoDisplayTo().equals("3")) {
                            innerlist.add(new PolicyInnerRecyclerModel(header, policyData.getPolInformation(), policyData.getPolTermsConditions()));
                        } else {

                        }
                    }
                }
                policyFeaturesMainList.add(new PolicyFeaturesOuterModel(header, innerlist));
            }

            LogMyBenefits.d("SIZE", "setUpPolicyFeaturesList: " + policyFeaturesMainList.size());
            policyAdapter = new PolicyFeaturesOuterAdapter(policyFeaturesMainList, getContext(), requireActivity(),this);
            binding.policyFeatureRecyclerView.setAdapter(policyAdapter);
            // policyAdapter.notifyItemRangeChanged(0, policyFeaturesMainList.size());
            binding.policyFeatureLoading.setVisibility(View.GONE);

            //to show the correct error state while list is empty
            policyFeaturesViewModel.getErrorState().removeObservers(getViewLifecycleOwner());
            if (policyFeaturesMainList.size() == 0) {
                binding.errorLayout.setVisibility(View.VISIBLE);
                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nodocument));
                binding.policyFeatureRecyclerView.setVisibility(View.GONE);
            } else {

                binding.errorLayout.setVisibility(View.GONE);
                binding.policyFeatureRecyclerView.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {
            e.printStackTrace();
            LogMyBenefits.e("ERROR", "setUpPolicyFeaturesList: ", e);
            binding.policyFeatureLoading.setVisibility(View.GONE);
            binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_api_error_state));
            binding.textError.setText("Something went wrong.\nPlease try again later.");
            binding.errorLayout.setVisibility(View.VISIBLE);
            binding.policyFeatureRecyclerView.setVisibility(View.GONE);
        }
    }

    private void showLoading() {
        binding.policyFeatureRecyclerView.setVisibility(View.GONE);
        binding.policyFeatureLoading.setVisibility(View.VISIBLE);
        binding.errorLayout.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.policyFeatureLoading.setVisibility(View.GONE);
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

    @Override
    public void onStartDownload(int position) {
        policyAdapter.startDownloading(position);
        policyAdapter.notifyItemChanged(position);
    }

    @Override
    public void onFinishDownload(int position) {
        policyAdapter.finishDownloading(position);
        policyAdapter.notifyItemChanged(position);
    }

    @Override
    public void requestPermission(int position, PolicyFeaturesOuterModel policyFeaturesOuterModel) {
        this.policyFeaturesOuterModel = policyFeaturesOuterModel;
        this.position = position;
        String[] permissionsArray = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissionLauncher.launch(permissionsArray);

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {

                    if (Boolean.TRUE.equals(permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false))
                            && Boolean.TRUE.equals(permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false))) {
                        onStartDownload(position);
                        privacyPolicyPermissionHelper.granted();

                    } else {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                        onFinishDownload(position);
                    }
                });
    }
}