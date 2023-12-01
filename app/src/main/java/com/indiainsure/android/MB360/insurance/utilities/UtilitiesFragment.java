package com.indiainsure.android.MB360.insurance.utilities;


import static com.indiainsure.android.MB360.BuildConfig.DOWNLOAD_BASE_URL;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import com.indiainsure.android.MB360.databinding.FragmentUtilitiesBinding;
import com.indiainsure.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.insurance.utilities.repository.UtilitiesViewModel;
import com.indiainsure.android.MB360.insurance.utilities.repository.responseclass.UTILITIESDATum;
import com.indiainsure.android.MB360.insurance.utilities.ui.PermissionHelper;
import com.indiainsure.android.MB360.insurance.utilities.ui.UtilDownloadHelper;
import com.indiainsure.android.MB360.insurance.utilities.ui.UtilDownloader;
import com.indiainsure.android.MB360.insurance.utilities.ui.UtilitiesAdapter;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UtilitiesFragment extends Fragment implements UtilDownloadHelper {

    FragmentUtilitiesBinding binding;
    View view;
    NavController navController;

    UtilitiesAdapter utilitiesAdapter;

    //Utilities ViewModel
    UtilitiesViewModel utilitiesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;

    //listeners
    TabLayout.OnTabSelectedListener tabSelectedListener;

    SpinnerAdapter spinnerAdapter;

    List<GroupPolicyData> policyData = new ArrayList<>();

    int selectedIndex;

    int position;
    UTILITIESDATum utility;


    // Declare a permission request launcher using the new API
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    PermissionHelper permissionHelper = new PermissionHelper() {
        @Override
        public void granted() {
            try {

                Intent pdfIntent = UtilDownloader.downloadFile(DOWNLOAD_BASE_URL + utility.getFilePath(),
                        new File(requireContext().getFilesDir(), utility.getSysGenFileName()),
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



    public UtilitiesFragment() {
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
        binding = FragmentUtilitiesBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        utilitiesViewModel = new ViewModelProvider(UtilitiesFragment.this).get(UtilitiesViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);

        //adding the tabs
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GHI"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GPA"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("GTL"));


        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            switch (groupPolicyData.getProductCode()) {
                case "GMC":
                    binding.tabs.viewPagerTabs.getTabAt(0).select();
                    break;
                case "GPA":
                    binding.tabs.viewPagerTabs.getTabAt(1).select();
                    break;
                case "GTL":
                    binding.tabs.viewPagerTabs.getTabAt(2).select();
                    break;
            }

        });


        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getUtilitiesWithTab("GMC");
                        break;
                    case 1:
                        getUtilitiesWithTab("GPA");
                        break;
                    case 2:
                        getUtilitiesWithTab("GTL");
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


        utilitiesViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUtilities();


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

        utilitiesViewModel.getReloginState().observe(requireActivity(), relogin -> {
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


    private void getUtilitiesSpinner(GroupPolicyData groupPolicyData) {
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

            utilitiesViewModel.getUtilities(GROUP_CHILD_SR_NO, OE_GRP_BAS_INFO_SR_NO);


        });
    }


    private void getUtilitiesWithTab(String code) {
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


    private void getUtilities() {

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {
            String PRODUCT_CODE = groupPolicyData.getProductCode();

            String oeGrpBasInfoSrNo = groupPolicyData.getOeGrpBasInfSrNo();

            //to get the  coverages data we need some parameters from load session values
            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
                LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onCreateView: " + loadSessionResponse.toString());
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
                String groupChildSrvNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();


                try {
                    switch (PRODUCT_CODE) {
                        case "GMC":
                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                            binding.tabs.viewPagerTabs.getTabAt(0).select();
                            if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                //todo somethings wrong with the server-response.
                            }
                            utilitiesViewModel.getUtilities(groupChildSrvNo, oeGrpBasInfoSrNo);
                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);

                            break;
                        case "GPA":
                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                            binding.tabs.viewPagerTabs.getTabAt(1).select();
                            if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                //todo somethings wrong with the server-response.
                            }
                            utilitiesViewModel.getUtilities(groupChildSrvNo, oeGrpBasInfoSrNo);
                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);
                            break;
                        case "GTL":
                            binding.tabs.viewPagerTabs.removeOnTabSelectedListener(tabSelectedListener);
                            binding.tabs.viewPagerTabs.getTabAt(2).select();
                            if (oeGrpBasInfoSrNo.equalsIgnoreCase("")) {
                                //todo somethings wrong with the server-response.
                            }
                            utilitiesViewModel.getUtilities(groupChildSrvNo, oeGrpBasInfoSrNo);

                            binding.tabs.viewPagerTabs.addOnTabSelectedListener(tabSelectedListener);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

        });


        utilitiesViewModel.getUtilitiesData().observe(getViewLifecycleOwner(), utilitiesResponse -> {
            if (utilitiesResponse != null) {
                utilitiesAdapter = new UtilitiesAdapter(requireContext(), utilitiesResponse.getUtilitiesData(), requireActivity(), this);
                binding.utilitiesRecyclerView.setAdapter(utilitiesAdapter);
                utilitiesAdapter.notifyItemRangeChanged(0, utilitiesResponse.getUtilitiesData().size());

                if (utilitiesResponse.getUtilitiesData().isEmpty()) {
                    binding.errorLayout.setVisibility(View.VISIBLE);
                    binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nodocument));
                    binding.utilitiesRecyclerView.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    binding.errorLayout.setVisibility(View.GONE);
                    binding.utilitiesRecyclerView.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                }

            } else {
                binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_api_error_state));
                binding.messageTextView.setText("Something went wrong.\nPlease try again later.");
                binding.utilitiesRecyclerView.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.errorLayout.setVisibility(View.GONE);
        binding.utilitiesRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
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
    public void onUtilitiesClicked(int position) {

    }

    @Override
    public void onStartDownload(int position) {

        utilitiesAdapter.startDownloading(position);
        utilitiesAdapter.notifyItemChanged(position);
    }

    @Override
    public void onFinishDownload(int position) {
        utilitiesAdapter.finishDownloading(position);
        utilitiesAdapter.notifyItemChanged(position);
    }

    @Override
    public void requestPermission(int position, UTILITIESDATum utility) {
        this.utility = utility;
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
                        permissionHelper.granted();

                    } else {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                        onFinishDownload(position);
                    }
                });
    }
}



