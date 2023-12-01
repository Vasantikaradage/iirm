package com.indiainsure.android.MB360.insurance.ecards.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentEcardBinding;
import com.indiainsure.android.MB360.insurance.ecards.FileDownloaderAll;
import com.indiainsure.android.MB360.insurance.ecards.repository.EcardViewModel;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui.PolicyFeatureDownloader;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.UtilMethods;
import com.indiainsure.android.MB360.utilities.webcustomtab.CustomTabActivityHelper;
import com.indiainsure.android.MB360.utilities.webcustomtab.WebviewFallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EcardFragment extends Fragment {


    FragmentEcardBinding binding;
    View view;

    //viewModel
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    EcardViewModel ecardViewModel;

    // creating object of WebView
    WebView printWeb;

    // object of print job
    PrintJob printJob;

    // a boolean to check the status of printing
    boolean printBtnPressed = false;


    private CustomTabActivityHelper mCustomTabActivityHelper;
    private static final int INITIAL_HEIGHT_DEFAULT_PX = 1200;
    private static final int CORNER_RADIUS_MAX_DP = 8;
    private static final int CORNER_RADIUS_DEFAULT_DP = CORNER_RADIUS_MAX_DP;
    private static final int BACKGROUND_INTERACT_OFF_VALUE = 2;


    public EcardFragment() {
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
        binding = FragmentEcardBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        ecardViewModel = new ViewModelProvider(this).get(EcardViewModel.class);


        //  getEcard();

        getEcardForJson();


        binding.btnSavePdf.setOnClickListener(v -> {
            if (printWeb != null) {
                // Calling createWebPrintJob()
                PrintTheWebPage(printWeb);
            } else {
                // Showing Toast message to user
                Toast.makeText(requireContext(), "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
            }

        });

        ecardViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });


        return view;

    }

    private void getEcardForJson() {
        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

            try {
                String employee_sr = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();
                String group_code = loadSessionResponse.getGroupInfoData().getGroupcode();

                String tpa_code = "MDH";
                try {
                    Map<String, String> ecardOptions = new HashMap<>();

                    //  for testing
                   /* ecardOptions.put("tpacode", "MEDI");
                    ecardOptions.put("employeeno", "IND6025");
                    ecardOptions.put("policynumber", "0239736210_00");
                    ecardOptions.put("policycommencementdt", "29/10/2022");
                    ecardOptions.put("policyvaliduptodt", "28/10/2023");
                    ecardOptions.put("groupcode", "GSIPL");*/

                    ecardOptions.put("tpacode", AesNew.encrypt(groupPolicyData.getTpaCode(), getString(R.string.pass_phrase)));
                    ecardOptions.put("employeeno", AesNew.encrypt(employee_sr, getString(R.string.pass_phrase)));
                    ecardOptions.put("policynumber", AesNew.encrypt(groupPolicyData.getPolicyNumber(), getString(R.string.pass_phrase)));
                    ecardOptions.put("policycommencementdt", AesNew.encrypt(groupPolicyData.getPolicyCommencementDate(), getString(R.string.pass_phrase)));
                    ecardOptions.put("policyvaliduptodt", AesNew.encrypt(groupPolicyData.getPolicyValidUpto(), getString(R.string.pass_phrase)));
                    ecardOptions.put("groupcode", AesNew.encrypt(group_code, getString(R.string.pass_phrase)));
                    ecardOptions.put("OeGrpBasInfSrNo", AesNew.encrypt(groupPolicyData.getOeGrpBasInfSrNo(), getString(R.string.pass_phrase)));
                   // ecardOptions.put("empSrno", AesNew.encrypt(employee_sr, getString(R.string.pass_phrase)));

                    ecardViewModel.getEcard(ecardOptions).observe(requireActivity(), ecardResponse -> {
                        if (ecardResponse != null) {

                            if (ecardResponse.getMessage().getECard().startsWith("/mybenefits")) {
                                getEcardDownload();
                            } else {
                                if (ecardResponse.getMessage().getECard().contains("E-card under process")) {

                                   // Toast.makeText(getActivity(), "E-card under process.", Toast.LENGTH_SHORT).show();


                                } else if (ecardResponse.getMessage().getECard().contains("No records Found")) {

                                   // Toast.makeText(getActivity(), "No records Found.", Toast.LENGTH_SHORT).show();

                                } else if (ecardResponse.getMessage().getECard().equalsIgnoreCase("")) {
                                   // Toast.makeText(getActivity(), "E-card not available", Toast.LENGTH_SHORT).show();

                                } else {

                                    if (ecardResponse.getMessage().getECard() == null ||
                                            ecardResponse.getMessage().getECard().equalsIgnoreCase("NA") ||
                                            ecardResponse.getMessage().getECard().equalsIgnoreCase("")) {

                                     //   Toast.makeText(getActivity(), "E-card not available", Toast.LENGTH_SHORT).show();
                                    } else if (ecardResponse.getMessage().getECard().contains("http")) {
                                        if (tpa_code.equalsIgnoreCase("HITS") ||
                                                tpa_code.equalsIgnoreCase("PHS") ||
                                                tpa_code.equalsIgnoreCase("ERICT") ||
                                                tpa_code.equalsIgnoreCase("VMCT") ||
                                                tpa_code.equalsIgnoreCase("MDH")
                                        ) {


                                            File file = new File(requireActivity().getFilesDir(), "E-card.pdf");
                                            if (Build.VERSION.SDK_INT > 32) {
                                                Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                                                try {
                                                    FileDownloaderAll.downloadFileWithoutPermission(ecardResponse.getMessage().getECard().replace("\"", ""), file, requireActivity(), getActivity());
                                                } catch (ActivityNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {

                                                StrictMode.ThreadPolicy gfgPolicy =
                                                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                                StrictMode.setThreadPolicy(gfgPolicy);
                                                try {
                                                    FileDownloaderAll.downloadFile(ecardResponse.getMessage().getECard().replace("\"", ""), file, requireActivity(), requireActivity());
                                                } catch (ActivityNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        } else {
                                            openCustomTab(ecardResponse.getMessage().getECard().replace("\"", ""));
                                        }
                                    }


                                }
                            }
                        } else {

                           // Toast.makeText(getActivity(), "E-card not available", Toast.LENGTH_SHORT).show();

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getActivity(), "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();

                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        });
    }

    public void getEcardDownload() {
        String empID = "";
        String GrpCode = "";
        String oe_grp_bas_inf_sr_no = "";
        String URL = BuildConfig.BASE_URL + "/mybenefits/documents/mybenefitsdata/02_PROCESSED_DATA/";
        String Params = GrpCode + "/05_E-CARDS/" + oe_grp_bas_inf_sr_no + "/" + empID + ".pdf";
        requireActivity().startActivity(new DownloadFile(getContext(), getActivity()).downloadFilePDF("ecard.pdf", URL + Params));


    }

    public static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;
        String extension;
        EcardDownloadHelper ecradDownloadHelper;
        int position;


        public DownloadFile(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;

        }

        public Intent downloadFilePDF(String fileName, String fileUrl) {
            //we can show the loading animation here
            //showLoading()
            ExecutorService executors = Executors.newSingleThreadExecutor();

            Future<Intent> future = executors.submit(() -> {
                //runnable thread
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                if (Build.VERSION.SDK_INT > 32) {

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), fileName);
                    Log.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                    try {
                        ecradDownloadHelper.onFinishDownload(position);
                        return PolicyFeatureDownloader.downloadFileWithoutPermission(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        ecradDownloadHelper.onFinishDownload(position);
                        return PolicyFeatureDownloader.downloadFile(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }


            });


            try {
                return future.get();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

    }

    private void openCustomTab(String url) {


        // Uses the established session to build a PCCT intent.
        CustomTabsSession session = mCustomTabActivityHelper.getSession();
        CustomTabsIntent.Builder intentBuilder =
                new CustomTabsIntent.Builder(session)
                        .setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.gradient_start))
                        .setUrlBarHidingEnabled(true)
                        .setShowTitle(true)
                        .setStartAnimations(requireActivity(), R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        int resizeBehavior = false
                ? CustomTabsIntent.ACTIVITY_HEIGHT_FIXED
                : CustomTabsIntent.ACTIVITY_HEIGHT_DEFAULT;

        intentBuilder.setInitialActivityHeightPx(INITIAL_HEIGHT_DEFAULT_PX, resizeBehavior);
        int toolbarCornerRadiusDp = CORNER_RADIUS_DEFAULT_DP;
        intentBuilder.setToolbarCornerRadiusDp(toolbarCornerRadiusDp);

        CustomTabsIntent customTabsIntent = intentBuilder.build();


        customTabsIntent.intent.putExtra(
                "androidx.browser.customtabs.extra.INITIAL_ACTIVITY_HEIGHT_IN_PIXEL",
                INITIAL_HEIGHT_DEFAULT_PX);
        int toolbarCornerRadiusPx =
                Math.round(toolbarCornerRadiusDp * getResources().getDisplayMetrics().density);
        customTabsIntent.intent.putExtra(
                "androidx.browser.customtabs.extra.TOOLBAR_CORNER_RADIUS_IN_PIXEL",
                toolbarCornerRadiusPx);
        if (resizeBehavior != CustomTabsIntent.ACTIVITY_HEIGHT_DEFAULT) {
            customTabsIntent.intent.putExtra(
                    CustomTabsIntent.EXTRA_ACTIVITY_RESIZE_BEHAVIOR, resizeBehavior);
        }

        customTabsIntent.intent.putExtra(
                "androix.browser.customtabs.extra.ENABLE_BACKGROUND_INTERACTION",
                BACKGROUND_INTERACT_OFF_VALUE);


        CustomTabActivityHelper.openCustomTab(
                requireActivity(), customTabsIntent, Uri.parse(url), new WebviewFallback());
    }

    private void getEcard() {

        selectedPolicyViewModel.getSelectedPolicy().observe(getViewLifecycleOwner(), groupPolicyData -> {

            /*      String dataRequest = "<DataRequest>" +
                    "<tpacode>HITS</tpacode>" +
                    "<employeeno>MCXL01139</employeeno>" +
                    "<policynumber>33180034210400000010</policynumber>" +
                    "<policycommencementdt>01~03~2022</policycommencementdt>" +
                    "<policyvaliduptodt>28~02~2023</policyvaliduptodt>" +
                    "<groupcode>MCX1</groupcode>" +
                    "</DataRequest>";*/


            //  ecardViewModel.getEcard(dataRequest);
        });


       /* ecardViewModel.getEcardData().observe(getViewLifecycleOwner(), ecardResponse -> {
            if (ecardResponse != null) {
                LogMyBenefits.d(LogTags.E_CARD_ACTIVITY, ecardResponse.toString());
                if (ecardResponse.getStatus().equals(BuildConfig.SUCCESS)) {


                    binding.ecardWebview.setWebViewClient(new WebViewClient() {
                        @Override

                        public void onPageFinished(WebView view, String url) {

                            super.onPageFinished(view, url);

                            // initializing the printWeb Object

                            binding.progressBar.setVisibility(View.GONE);
                            printWeb = view;

                        }
                    });

                    binding.ecardWebview.loadUrl(ecardResponse.getEcardInformation());
                }
            }

        });*/
    }


    private void PrintTheWebPage(WebView webView) {

        // set printBtnPressed true
        printBtnPressed = true;

        // Creating  PrintManager instance
        PrintManager printManager = (PrintManager) requireContext()
                .getSystemService(Context.PRINT_SERVICE);

        // setting the name of job
        String jobName = getString(R.string.app_name) + " webpage" + webView.getUrl();

        // Creating  PrintDocumentAdapter instance
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);


        // Create a print job with name and adapter instance
        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    @Override
    public void onResume() {
        super.onResume();
        printBtnPressed = false;
    }
}