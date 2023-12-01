package com.indiainsure.android.MB360.insurance.claimsprocedure;


import static com.indiainsure.android.MB360.BuildConfig.DOWNLOAD_BASE_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
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

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentClaimsProcedureBinding;
import com.indiainsure.android.MB360.insurance.claimsprocedure.adapters.ClaimProcedureHelplineNumberAdapters;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.ClaimProcedureViewModel;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcLayoutInfo;
import com.indiainsure.android.MB360.insurance.dialogues.PolicyChangeDialogue;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ClaimsProcedureFragment extends Fragment {


    FragmentClaimsProcedureBinding binding;
    View view;

    //viewModels
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    ClaimProcedureViewModel claimProcedureViewModel;
    String groupChildSrNo = "";
    String PROCEDURE_TYPE = "CASHLESS";
    NavController navController;

    String css_class = "<style>@font-face { font-family:MyFont; src: url('file:///android_res/font/poppins.ttf');}\n" + ".list-inline, .list-unstyled {\n" + " font-family:MyFont;   padding-left: 0;\n" + "    list-style: none;\n" + "}\n" + ".margin-bottom-10 {\n" + "    margin-bottom: 10px!important;\n" + "}\n" + ".col-xs-2 {\n" + "    width: 16.66667%;\n" + "}\n" + ".col-xs-10 {\n" + "    width: 83.33333%;\n" + "}\n" + ".text-right {\n" + " font-family:MyFont;   text-align: center;\n" + "}\n" + ".btn-group>.btn-group, .btn-toolbar .btn, .btn-toolbar .btn-group, .btn-toolbar .input-group, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9, .dropdown-menu {\n" + "    float: left;\n" + "}\n" + ".badge-success {\n" + " font-family:MyFont;   background-color: #E2046E;\n" + "}\n" + ".badge {\n" + " font-family:MyFont;   /* display: inline-block; */\n" +
            "    display: inline-block;\n" +
            "    text-align: center;\n" +
            "    line-height: 12px;\n" +
            "    width: 12px;\n" +
            "    border-radius: 50%;\n" +
            "    background-color: #0070d5;\n" +
            "    color: #fff;\n" +
            "    padding: 4px; font-family:MyFont; " +
            "" + "}\n" + ".badge {\n" + "    display: inline-block;\n" + " font-family:MyFont;   min-width: 10px;\n" + "    \n" + "}\n" + ".note.note-info {\n" + " font-family:MyFont;   background-color: #f5f8fd;\n" +
            "    border-color: #0070d5;\n" +
            "    color: #010407;" + "}" +
            ".note{ height: 128px;   margin:  20px 0px 20px;\n" +
            "    padding: 15px 30px 15px 15px;\n" +
            "    border-left: 5px solid #eee;\n" +
            "    border-radius: 0 4px 4px 0; font-family:MyFont; }  " +
            ".row {\n" +
            "    margin-left: 0px; font-family:MyFont; \n" +
            "    margin-right: 0px;\n" +
            "}" +
            "</style>";

    String oeGrpBasInfSrNo = "";
    String product_code = "";
    String tpa_code = "";

    ClaimProcedureHelplineNumberAdapters adapter;

    SpinnerAdapter spinnerAdapter;


    int selectedIndex;
    List<GroupPolicyData> policyData = new ArrayList<>();

    public ClaimsProcedureFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClaimsProcedureBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        claimProcedureViewModel = new ViewModelProvider(requireActivity()).get(ClaimProcedureViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        //default cashless title for header
        binding.claimProcedureStepsHeader.setText(getString(R.string.cashless_procedure_steps));

        //cashless/reimbursement layout
        binding.cashlessStepsLayout.setOnClickListener(v -> {
            PROCEDURE_TYPE = "CASHLESS";
            product_code = "GMC";
            binding.reimbursementStepsLayout.setBackgroundResource(0);
            binding.cashlessText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.reimbursementText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            binding.cashlessStepsLayout.setBackgroundResource(R.drawable.roundedsquare);
            binding.claimProcedureStepsHeader.setText(getString(R.string.cashless_procedure_steps));
            //to check if we have existing categories or no
            claimProcedureViewModel.getClaimProcedureImage(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
            claimProcedureViewModel.getClaimProcedureInformation(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
            setuptabs();

        });

        binding.reimbursementStepsLayout.setOnClickListener(v -> {
            PROCEDURE_TYPE = "REIMBURSEMENT";
            product_code = "GMC";
            binding.cashlessStepsLayout.setBackgroundResource(0);
            binding.reimbursementText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.cashlessText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
            binding.reimbursementStepsLayout.setBackgroundResource(R.drawable.roundedsquare);
            binding.claimProcedureStepsHeader.setText(getString(R.string.reimbursment_procedure_steps));

            claimProcedureViewModel.getClaimProcedureImage(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
            claimProcedureViewModel.getClaimProcedureInformation(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
            setuptabs();
        });


        return view;
    }

    private void setuptabs() {
        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
        if (loadSessionResponse != null) {
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

            groupChildSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();


            //get claim procedure Image
            //claimProcedureViewModel.getClaimProcedureImage(groupChildSrNo, oeGrpBasInfSrNo, product_code, PROCEDURE_TYPE);

        } else {
            //something went wrong
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.noStepsFound.inflate();
        binding.additionalNoStepsFound.inflate();

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            //change the selection chips ui
            selectChip(groupPolicyData);
            setTextWithFancyAnimation(binding.selectedPolicyText, "" + groupPolicyData.getPolicyNumber());


            product_code = groupPolicyData.getProductCode();
           // tpa_code = groupPolicyData.getTpaCode();
            oeGrpBasInfSrNo = groupPolicyData.getOeGrpBasInfSrNo();

            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
            if(loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData().get(0).getPolicyType().equalsIgnoreCase("BASE")) {
                tpa_code = loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData().get(0).getTpaCode();
            }
            if (loadSessionResponse != null) {
                groupChildSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();

                switch (product_code.toLowerCase()) {
                    case "gmc":

                        css_class = "<style>@font-face { font-family:MyFont; src: url('file:///android_res/font/poppins.ttf');}\n" + ".list-inline, .list-unstyled {\n" + " font-family:MyFont;   padding-left: 0;\n" + "    list-style: none;\n" + "}\n" + ".margin-bottom-10 {\n" + "    margin-bottom: 10px!important;\n" + "}\n" + ".col-xs-2 {\n" + "    width: 16.66667%;\n" + "}\n" + ".col-xs-10 {\n" + "    width: 83.33333%;\n" + "}\n" + ".text-right {\n" + " font-family:MyFont;   text-align: center;\n" + "}\n" + ".btn-group>.btn-group, .btn-toolbar .btn, .btn-toolbar .btn-group, .btn-toolbar .input-group, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3, .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9, .dropdown-menu {\n" + "    float: left;\n" + "}\n" + ".badge-success {\n" + " font-family:MyFont;   background-color: #E2046E;\n" + "}\n" + ".badge {\n" + " font-family:MyFont;   /* display: inline-block; */\n" +
                                "    display: inline-block;\n" +
                                "    text-align: center;\n" +
                                "    line-height: 12px;\n" +
                                "    width: 12px;\n" +
                                "    border-radius: 50%;\n" +
                                "    background-color: #0070d5;\n" +
                                "    color: #fff;\n" +
                                "    padding: 4px; font-family:MyFont; " +
                                "" + "}\n" + ".badge {\n" + "    display: inline-block;\n" + " font-family:MyFont;   min-width: 10px;\n" + "    \n" + "}\n" + ".note.note-info {\n" + " font-family:MyFont;   background-color: #f5f8fd;\n" +
                                "    border-color: #0070d5;\n" +
                                "    color: #010407;" + "}" +
                                ".note{ height: 128px;   margin:  20px 0px 20px;\n" +
                                "    padding: 15px 30px 15px 15px;\n" +
                                "    border-left: 5px solid #eee;\n" +
                                "    border-radius: 0 4px 4px 0; font-family:MyFont; }  " +
                                ".row {\n" +
                                "    margin-left: 0px; font-family:MyFont; \n" +
                                "    margin-right: 0px;\n" +
                                "}" +
                                "</style>";
                        binding.gmcStepsToggle.setVisibility(View.VISIBLE);
                        claimProcedureViewModel.getClaimsProcedureLayoutInfo(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        binding.stepsImageCardHolder.setVisibility(View.VISIBLE);
                        showHelpLineSection();
                        binding.cashlessStepsLayout.performClick();

                        //image in gmc only
                        claimProcedureViewModel.getClaimProcedureImage(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);

                        break;
                    case "gpa":

                        css_class = "<link href=\"https://marsh.mybenefits360.com/css/commoncss/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                                "<link rel=\"stylesheet\"\n" +
                                "      href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css\" />\n" +
                                "<style>\n" +
                                "    .row {\n" +
                                "        margin-left: -15px;\n" +
                                "        margin-right: -15px;\n" +
                                "    }\n" +
                                "\n" +
                                "    .font-green {\n" +
                                "        color: #32c5d2 !important;\n" +
                                "    }\n" +
                                "\n" +
                                "    [class*=\" fa-\"]:not(.fa-stack), [class*=\" glyphicon-\"], [class*=\" icon-\"], [class^=fa-]:not(.fa-stack), [class^=glyphicon-], [class^=icon-] {\n" +
                                "        display: inline-block;\n" +
                                "        line-height: 14px;\n" +
                                "        -webkit-font-smoothing: antialiased;\n" +
                                "    }\n" +
                                "\n" +
                                "    li [class*=\" fa-\"], li [class*=\" glyphicon-\"], li [class*=\" icon-\"], li [class^=fa-], li [class^=glyphicon-], li [class^=icon-] {\n" +
                                "        display: inline-block;\n" +
                                "        width: 1.25em;\n" +
                                "        text-align: center;\n" +
                                "    }\n" +
                                "\n" +
                                "    .fa {\n" +
                                "        font-family: FontAwesome !important;\n" +
                                "    }\n" +
                                "\n" +
                                "    .uppercase {\n" +
                                "        text-transform: uppercase !important;\n" +
                                "    }\n" +
                                "\n" +
                                "    .bold {\n" +
                                "        font-weight: 700 !important;\n" +
                                "    }\n" +
                                "\n" +
                                "    .note.note-info {\n" +
                                "        background-color: #f5f8fd;\n" +
                                "        border-color: #8bb4e7;\n" +
                                "        color: #010407;\n" +
                                "    }\n" +
                                "\n" +
                                "    .note {\n" +
                                "        margin: 0 0 20px;\n" +
                                "        padding: 15px 30px 15px 15px;\n" +
                                "        border-left: 5px solid #eee;\n" +
                                "        border-radius: 0 4px 4px 0;\n" +
                                "    }\n" +
                                "\n" +
                                "    .list-inline, .list-unstyled {\n" +
                                "        padding-left: 0;\n" +
                                "        list-style: none;\n" +
                                "    }\n" +
                                "\n" +
                                "    .list-inline, .list-unstyled {\n" +
                                "        padding-left: 0;\n" +
                                "        list-style: none;\n" +
                                "    }\n" +
                                "\n" +
                                "    outline):active:focus, .btn.green:not(.btn-outline):active:hover, .open > .btn.green:not(.btn-outline).dropdown-toggle.focus, .open > .btn.green:not(.btn-outline).dropdown-toggle:focus, .open > .btn.green:not(.btn-outline).dropdown-toggle:hover {\n" +
                                "        color: #FFF;\n" +
                                "        background-color: #1f858e;\n" +
                                "        border-color: #18666d;\n" +
                                "    }\n" +
                                "\n" +
                                "    element.style {\n" +
                                "    }\n" +
                                "\n" +
                                "    .fa-lg {\n" +
                                "        font-size: 1.33333333em;\n" +
                                "        line-height: .75em;\n" +
                                "        vertical-align: -15%;\n" +
                                "    }\n" +
                                "\n" +
                                "    .btn.green:not(.btn-outline).active.focus, .btn.green:not(.btn-outline).active:focus, .btn.green:not(.btn-outline).active:hover, .btn.green:not(.btn-outline):active.focus, .btn.green:not(.btn-outline):active:focus, .btn.green:not(.btn-outline):active:hover, .open > .btn.green:not(.btn-outline).dropdown-toggle.focus, .open > .btn.green:not(.btn-outline).dropdown-toggle:focus, .open > .btn.green:not(.btn-outline).dropdown-toggle:hover {\n" +
                                "        color: #FFF;\n" +
                                "        background-color: #1f858e;\n" +
                                "        border-color: #18666d;\n" +
                                "    }\n" +
                                "\n" +
                                "    .fa {\n" +
                                "        display: inline-block;\n" +
                                "        font: normal normal normal 14px/1 FontAwesome;\n" +
                                "        font-size: inherit;\n" +
                                "        text-rendering: auto;\n" +
                                "        -webkit-font-smoothing: antialiased;\n" +
                                "        -moz-osx-font-smoothing: grayscale;\n" +
                                "    }\n" +
                                "          .margin-bottom-10 {\n" +
                                "        margin-bottom: 10px !important;\n" +
                                "    }\n" +
                                "</style>";

                        binding.claimProcedureStepsHeader.setText(getString(R.string.gpa_claims_steps));
                        PROCEDURE_TYPE = "NOT AVAILABLE";
                        claimProcedureViewModel.getClaimsProcedureLayoutInfo(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                        binding.gmcStepsToggle.setVisibility(View.GONE);
                        hideHelpLineSection();


                        break;
                    case "gtl":

                        css_class = "<link href=\"https://marsh.mybenefits360.com/css/commoncss/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                                "<link rel=\"stylesheet\"\n" +
                                "      href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.css\" />\n" +
                                "<style>\n" +
                                "    .margin-bottom-10 {\n" +
                                "        margin-bottom: 10px !important;\n" +
                                "    }\n" +
                                "\n" +
                                "    .margin-bottom-15 {\n" +
                                "        margin-bottom: 15px !important;\n" +
                                "    }\n" +
                                "      .margin-bottom-10 {\n" +
                                "        margin-bottom: 10px !important;\n" +
                                "    }\n" +
                                "\n" +
                                "\n" +
                                "    .badge-success {\n" +
                                "        background-color: #36c6d3;\n" +
                                "    }\n" +
                                "\n" +
                                "    .font-red {\n" +
                                "        color: red;\n" +
                                "    }\n" +
                                "\n" +
                                "    .badge {\n" +
                                "        font-size: 11px !important;\n" +
                                "        font-weight: 300;\n" +
                                "        height: 18px;\n" +
                                "        color: #fff;\n" +
                                "        padding: 3px 6px;\n" +
                                "        -webkit-border-radius: 12px !important;\n" +
                                "        -moz-border-radius: 12px !important;\n" +
                                "        border-radius: 12px !important;\n" +
                                "        text-shadow: none !important;\n" +
                                "        text-align: center;\n" +
                                "        vertical-align: middle;\n" +
                                "    }\n" +
                                "\n" +
                                "    .badge {\n" +
                                "        display: inline-block;\n" +
                                "        font-family: MyFont;\n" +
                                "        min-width: 10px;\n" +
                                "    }\n" +
                                "\n" +
                                "    .note.note-info {\n" +
                                "        font-family: MyFont;\n" +
                                "        background-color: #f5f8fd;\n" +
                                "        border-color: #0070d5;\n" +
                                "        color: #010407;\n" +
                                "    }\n" +
                                "\n" +
                                "    .note {\n" +
                                "        height: 128px;\n" +
                                "        margin: 20px 0px 20px;\n" +
                                "        padding: 15px 30px 15px 15px;\n" +
                                "        border-left: 5px solid #eee;\n" +
                                "        border-radius: 0 4px 4px 0;\n" +
                                "        font-family: MyFont;\n" +
                                "    }\n" +
                                "\n" +
                                "    .btn.green:not(.btn-outline).active.focus, .btn.green:not(.btn-outline).active:focus, .btn.green:not(.btn-outline).active:hover, .btn.green:not(.btn-outline):active.focus, .btn.green:not(.btn-outline):active:focus, .btn.green:not(.btn-outline):active:hover, .open > .btn.green:not(.btn-outline).dropdown-toggle.focus, .open > .btn.green:not(.btn-outline).dropdown-toggle:focus, .open > .btn.green:not(.btn-outline).dropdown-toggle:hover {\n" +
                                "        color: #FFF;\n" +
                                "        background-color: #1f858e;\n" +
                                "        border-color: #18666d;\n" +
                                "    }\n" +
                                "\n" +
                                "    .list-inline, .list-unstyled {\n" +
                                "        padding-left: 0;\n" +
                                "        list-style: none;\n" +
                                "    }\n" +
                                "\n" +
                                "    .list-inline, .list-unstyled {\n" +
                                "        padding-left: 0;\n" +
                                "        list-style: none;\n" +
                                "    }\n" +
                                "\n" +
                                "    outline):active:focus, .btn.green:not(.btn-outline):active:hover, .open > .btn.green:not(.btn-outline).dropdown-toggle.focus, .open > .btn.green:not(.btn-outline).dropdown-toggle:focus, .open > .btn.green:not(.btn-outline).dropdown-toggle:hover {\n" +
                                "        color: #FFF;\n" +
                                "        background-color: #1f858e;\n" +
                                "        border-color: #18666d;\n" +
                                "    }\n" +
                                "\n" +
                                "    .margin-bottom-10 {\n" +
                                "        margin-bottom: 10px !important;\n" +
                                "    }\n" +
                                "\n" +
                                "    .row {\n" +
                                "        margin-left: -15px;\n" +
                                "        margin-right: -15px;\n" +
                                "    }\n" +
                                "\n" +
                                "    .btn.green:not(.btn-outline).active, .btn.green:not(.btn-outline):active, .open > .btn.green:not(.btn-outline).dropdown-toggle {\n" +
                                "        background-image: none;\n" +
                                "    }\n" +
                                "</style>";
                        binding.claimProcedureStepsHeader.setText(getString(R.string.gtl_claims_steps));
                        PROCEDURE_TYPE = "NOT AVAILABLE";
                        claimProcedureViewModel.getClaimsProcedureLayoutInfo(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                        binding.gmcStepsToggle.setVisibility(View.GONE);
                        hideHelpLineSection();
                        break;
                }

                //procedure information (steps)
                claimProcedureViewModel.getClaimProcedureInformation(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                //additional information
                claimProcedureViewModel.getClaimProcedureText(groupChildSrNo, oeGrpBasInfSrNo, product_code.toUpperCase(), PROCEDURE_TYPE);
                //emergency contact
                claimProcedureViewModel.getEmergencyContact(tpa_code);
            } else {
                //something went wrong
            }
        });

        claimProcedureViewModel.getClaimsProcedureLayoutInfoData().observe(getViewLifecycleOwner(), layoutInfo -> {
            if (layoutInfo != null) {
                if (!layoutInfo.getClaimProcLayoutInfo().isEmpty()) {
                    for (ClaimProcLayoutInfo layout : layoutInfo.getClaimProcLayoutInfo()) {
                        if (!layout.getLayoutPart().isEmpty() || !layout.getLayoutPart().equalsIgnoreCase(""))
                            switch (layout.getLayoutPart().toLowerCase()) {
                                case "part1":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        binding.stepsImageCardHolder.setVisibility(View.GONE);
                                    } else {
                                        binding.stepsImageCardHolder.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "part2":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        binding.stepsCard.setVisibility(View.GONE);
                                    } else {
                                        binding.stepsCard.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "part3":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        binding.additionalStepsCard.setVisibility(View.GONE);
                                    } else {
                                        binding.additionalStepsCard.setVisibility(View.VISIBLE);
                                    }

                                case "part4":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        //downloadable claim forms
                                    }
                                    break;
                                case "part5":
                                    if (layout.getLayoutPartVisibility().equalsIgnoreCase("0")) {
                                        binding.emergencyContactCard.setVisibility(View.GONE);
                                    } else {
                                        binding.emergencyContactCard.setVisibility(View.VISIBLE);
                                    }
                                    break;
                            }
                    }

                } else {
                    //here we get the layout as empty List
                }
            }
        });

        claimProcedureViewModel.getClaimProcedureImageData().observe(getViewLifecycleOwner(), imageResponse -> {

            try {

                String claims_procedure_image = DOWNLOAD_BASE_URL + "mybenefits/claimprocedures/" + groupChildSrNo + "/" + groupChildSrNo + "-" + oeGrpBasInfSrNo + "/displayimage/" + imageResponse.getClaimProcImagePath().get(0).getClmProcP1ImgPath();

                LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "GLIDE IMAGE: " + claims_procedure_image);
                Shimmer shimmer = new Shimmer.AlphaHighlightBuilder().setDuration(1000).setBaseAlpha(0.85f).setHighlightAlpha(0.6f).setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setAutoStart(true).build();

                // This is the placeholder for the imageView
                ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
                shimmerDrawable.setShimmer(shimmer);

                Glide.with(this).load(claims_procedure_image).placeholder(shimmerDrawable).error(R.drawable.ic_placeholder).into(binding.claimsProcedureImage);


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        claimProcedureViewModel.getClaimProcedureLayoutInstructionInfoData().observe(getViewLifecycleOwner(), textResponse -> {

            if (textResponse.getClaimProcInstructionInfo().get(0).getIsDefInstTextFromFile().equals("1")) {
                //binding.claimsStepsText.loadData(claimProcedureTextFileData, "text/html", "utf-8");
                LogMyBenefits.d(LogTags.DOWNLOAD_ACTIVITY, "onViewCreated: " + textResponse);
                String url = DOWNLOAD_BASE_URL + "mybenefits/claimprocedures/" + groupChildSrNo + "/" + groupChildSrNo + "-" + oeGrpBasInfSrNo + "/displayinstructions/" + textResponse.getClaimProcInstructionInfo().get(0).getDefInstTextFromFilePath();

                LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "STEPS FILE: " + url);

                try {
                    claimProcedureViewModel.getClaimStepsHtmlData(groupChildSrNo, groupChildSrNo + "-" + oeGrpBasInfSrNo, textResponse.getClaimProcInstructionInfo().get(0).getDefInstTextFromFilePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        });

        //observer of html txt response
        claimProcedureViewModel.getClaimStepsHtmlDataObserver().observe(getViewLifecycleOwner(), claimProcedureStepsData -> {
            if (claimProcedureStepsData.equalsIgnoreCase("")) {
                binding.noStepsFound.setVisibility(View.VISIBLE);
                binding.claimsStepsText.setVisibility(View.GONE);
            } else {
                binding.noStepsFound.setVisibility(View.GONE);
                binding.claimsStepsText.setVisibility(View.VISIBLE);


                binding.claimsStepsText.setWebViewClient(new WebViewClient());
                binding.claimsStepsText.getSettings().setJavaScriptEnabled(true);
                binding.claimsStepsText.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                binding.claimsStepsText.getSettings().setPluginState(WebSettings.PluginState.ON);
                binding.claimsStepsText.getSettings().setMediaPlaybackRequiresUserGesture(false);
                binding.claimsStepsText.setWebChromeClient(new WebChromeClient());
                binding.claimsStepsText.loadDataWithBaseURL(null, css_class +
                                claimProcedureStepsData.replace("class='col-lg-3 col-md-3 col-sm-2 col-xs-2 text-left'",
                                        "class='col-lg-12 col-md-12 col-sm-12 col-xs-12'")
                        , "text/html", "UTF-8", null);


            }
        });

        //additional information
        claimProcedureViewModel.getClaimProcedureTextData().observe(getViewLifecycleOwner(), additionalInstruction -> {
            if (!additionalInstruction.getClaimProcTextPath().get(0).getClmProcP1WysiwygTextPath().isEmpty()) {
                //binding.claimsStepsText.loadData(claimProcedureTextFileData, "text/html", "utf-8");
                LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "additional instruction: " + additionalInstruction);

                try {
                    claimProcedureViewModel.getClaimStepsHtmlAdditionalData(groupChildSrNo, groupChildSrNo + "-" + oeGrpBasInfSrNo, additionalInstruction.getClaimProcTextPath().get(0).getClmProcP1WysiwygTextPath()).observe(getViewLifecycleOwner(), claimProcedureStepsData -> {
                        if (claimProcedureStepsData.equalsIgnoreCase("")) {

                            binding.additionalNoStepsFound.setVisibility(View.VISIBLE);

                        } else {
                            binding.additionalNoStepsFound.setVisibility(View.GONE);
                            binding.additionalClaimsStepsText.loadData(css_class + claimProcedureStepsData, "text/html", "utf-8");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.additionalNoStepsFound.setVisibility(View.VISIBLE);
                }

            } else {
                binding.additionalNoStepsFound.setVisibility(View.VISIBLE);
            }


        });


        claimProcedureViewModel.getClaimProcedureEmergencyContactData().observe(getViewLifecycleOwner(), contact -> {
            if (contact != null) {
                if (contact.getEmergencyContactNo() != null) {
                    if (!contact.getEmergencyContactNo().isEmpty()) {
                        adapter = new ClaimProcedureHelplineNumberAdapters(requireContext(), contact.getEmergencyContactNo());
                        binding.claimProceduresHelplineCycle.setAdapter(adapter);
                        adapter.notifyItemRangeChanged(0, contact.getEmergencyContactNo().size());
                    }
                }
            }
        });

        claimProcedureViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });


        selectedPolicyViewModel.getPolicyData().observe(getViewLifecycleOwner(), policyData -> {
            this.policyData = policyData;
        });

        selectedPolicyViewModel.getSelectedIndex().observe(getViewLifecycleOwner(), index -> {
            this.selectedIndex = index;
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

        claimProcedureViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });


    }


    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
        binding.claimsProcedureContentLayout.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.claimsProcedureContentLayout.setVisibility(View.GONE);
    }

    private void hideHelpLineSection() {
        binding.emergencyContactCard.setVisibility(View.GONE);
    }

    private void showHelpLineSection() {
        binding.emergencyContactCard.setVisibility(View.VISIBLE);
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