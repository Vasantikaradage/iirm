package com.indiainsure.android.MB360.insurance;


import static com.indiainsure.android.MB360.BuildConfig.AUTH_EMAIL;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_LOGIN_ID;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_LOGIN_TYPE;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_PHONE_NUMBER;
import static com.indiainsure.android.MB360.BuildConfig.USER_AGREEMENT;
import static com.indiainsure.android.MB360.utilities.AppLocalConstant.askDefaultPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.StrictMode;
import android.system.Os;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.IIsolatedService;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.ActivityDashBoardActivityBinding;
import com.indiainsure.android.MB360.databinding.DeclarationPopupLayoutBinding;
import com.indiainsure.android.MB360.insurance.adminsetting.ui.AdminSettingViewModel;
import com.indiainsure.android.MB360.insurance.coverages.repository.CoveragesViewModel;
import com.indiainsure.android.MB360.insurance.ecards.FileDownloaderAll;
import com.indiainsure.android.MB360.insurance.ecards.repository.EcardViewModel;
import com.indiainsure.android.MB360.insurance.ecards.ui.EcardDownloadHelper;
import com.indiainsure.android.MB360.insurance.ecards.ui.EcardFragment;
import com.indiainsure.android.MB360.insurance.enrollmentstatus.EnrollmentStatusViewModel;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.repository.HospitalNetworkViewModel;
import com.indiainsure.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.indiainsure.android.MB360.insurance.queries.repository.QueryViewModel;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmpDependantsDatum;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.insurance.servicenames.ServiceNamesViewModel;
import com.indiainsure.android.MB360.magisk.IsolatedService;
import com.indiainsure.android.MB360.networkmanager.NetworkStateManager;
import com.indiainsure.android.MB360.onboarding.SplashScreenActivity;
import com.indiainsure.android.MB360.onboarding.authentication.LoginActivity;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.FridaListener;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.UtilMethods;
import com.indiainsure.android.MB360.utilities.rootdetection.RootDetection;
import com.indiainsure.android.MB360.utilities.token.BearerRepository;
import com.indiainsure.android.MB360.utilities.webcustomtab.CustomTabActivityHelper;
import com.indiainsure.android.MB360.utilities.webcustomtab.WebviewFallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DashBoardActivity extends AppCompatActivity implements FridaListener {

    /**
     * we are using view-binding from this activity
     **/
    ActivityDashBoardActivityBinding binding;
    View view;
    NavController navController;
    BottomNavigationView bottomNavigationView;
    private IIsolatedService serviceBinder;
    private boolean bServiceBound;

    String groupChildSrNo = "";
    private AlertDialog.Builder mBuilder;
    private String error;
    EnrollmentStatusViewModel enrollmentStatusViewModel;

    /**
     * load-session view-model
     **/


    LoadSessionViewModel loadSessionViewModel;
    HospitalNetworkViewModel hospitalNetworkViewModel;
    MyClaimsViewModel myClaimsViewModel;
    QueryViewModel queryViewModel;
    CoveragesViewModel coveragesViewModel;
    // EnrollmentViewModel enrollmentViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    EcardViewModel ecardViewModel;
    AdminSettingViewModel adminSettingViewModel;
    ServiceNamesViewModel serviceNamesViewModel;


    Boolean FROM_OTP_SCREEN = false;
    int OTP = 0;

    private CustomTabActivityHelper mCustomTabActivityHelper;
    private static final int INITIAL_HEIGHT_DEFAULT_PX = 1200;
    private static final int CORNER_RADIUS_MAX_DP = 8;
    private static final int CORNER_RADIUS_DEFAULT_DP = CORNER_RADIUS_MAX_DP;
    private static final int BACKGROUND_INTERACT_OFF_VALUE = 2;


    MaterialToolbar toolbar;

    EncryptionPreference encryptionPreference;


    private final Observer<Boolean> activeNetworkStateObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isConnected) {
            //here we get to that if the device is connected to internet or no
            if (isConnected) {
                binding.internetStatus.setVisibility(View.GONE);
            } else {
                binding.internetStatus.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardActivityBinding.inflate(getLayoutInflater());

        if (getIntent().getIntExtra(BuildConfig.AUTH_OTP, 0) > OTP) {
            OTP = getIntent().getIntExtra(BuildConfig.AUTH_OTP, 0);
            FROM_OTP_SCREEN = true;
        } else {
            FROM_OTP_SCREEN = false;
        }
        encryptionPreference = new EncryptionPreference(this);

        if (userAgreementPopup()) {
            showDeclarationDialog();
        } else {
            //to ask the permissions at first
            askDefaultPermissions(this);
        }

        Intent intent = new Intent(getApplicationContext(), IsolatedService.class);
        /*Binding to an isolated service */
        getApplicationContext().bindService(intent, mIsolatedServiceConnection, BIND_AUTO_CREATE);

        view = binding.getRoot();
        setContentView(view);
        toolbar = binding.toolbar.insuranceToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        loadSessionViewModel = new ViewModelProvider(this).get(LoadSessionViewModel.class);
        hospitalNetworkViewModel = new ViewModelProvider(this).get(HospitalNetworkViewModel.class);
        myClaimsViewModel = new ViewModelProvider(this).get(MyClaimsViewModel.class);
        queryViewModel = new ViewModelProvider(this).get(QueryViewModel.class);
        coveragesViewModel = new ViewModelProvider(this).get(CoveragesViewModel.class);
        //  enrollmentViewModel = new ViewModelProvider(this).get(EnrollmentViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(this).get(SelectedPolicyViewModel.class);
        ecardViewModel = new ViewModelProvider(this).get(EcardViewModel.class);
        adminSettingViewModel = new ViewModelProvider(this).get(AdminSettingViewModel.class);
        serviceNamesViewModel = new ViewModelProvider(this).get(ServiceNamesViewModel.class);
        enrollmentStatusViewModel = new ViewModelProvider(this).get(EnrollmentStatusViewModel.class);
        mCustomTabActivityHelper = new CustomTabActivityHelper();

        //encryption
        encryptionPreference = new EncryptionPreference(this);


        NetworkStateManager.getInstance().getNetworkConnectivityStatus().observe(this, activeNetworkStateObserver);


        //this function is responsible for the api call for load session
        //Current issue (api gets called 3 times when a user comes from otp activity)
        loadSessions();


        //this function is to get the enrollment data and instructions for now
        /* getenrollmentInstructions();*/

        bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        //to navigate
        /** to say bottom navigation that we are on home page or-else it will select the first option in menu in this case (Support) **/
        navController.navigate(R.id.homeFragment);
        //we need to remove a page as we are navigating from something in this case (same page is getting stacked)
        navController.popBackStack();


        binding.fab.setOnClickListener(view -> {
            //cause our home page is the first page
            while (navController.getBackQueue().size() > 2) {
                navController.navigateUp();
            }

        });


        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.homeFragment) {

                resetSelection();
                // binding.toolbar.btnBack.setVisibility(View.GONE);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                binding.toolbar.logo.setVisibility(View.VISIBLE);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                showBottomBar();


            } /*else if (*//*navDestination.getId() == R.id.enrollmentHomeFragment || navDestination.getId() == R.id.enrollmentSummaryFragment*//*) {
                //we don't need the toolbar in enrollment home page
                binding.toolbar.getRoot().setVisibility(View.GONE);
                //hide bottomBar
                hideBottomBar();
                Window window = getWindow();

                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                // finally change the color
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.enrollment_bg_start_gradient));


            }*/ else if (navDestination.getId() == R.id.queryDetailsFragment || navDestination.getId()
                    == R.id.myClaimsdetails ||
                    navDestination.getId()
                            == R.id.newQueryFragment || navDestination.getId() == R.id.enrollmentWebView) {
                binding.toolbar.getRoot().setVisibility(View.VISIBLE);
                binding.toolbar.logo.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                hideBottomBar();
            } else {
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                binding.toolbar.logo.setVisibility(View.GONE);
                showBottomBar();
            }

            if (navDestination.getId() == R.id.ecardFragment) {
                navController.navigateUp();

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.smslayout);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.nhborder));

                AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
                final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
                AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
                AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
                LinearLayout llTitle = dialog.findViewById(R.id.llTitle);
                AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
                llTitle.setVisibility(View.VISIBLE);
                lblSMS.setText(getString(R.string.app_name));
                lblSMSHeader.setText("Do you want to download the E-Card?");
                btnSubmit.setText("Yes");
                btnCancel.setText("No");
                smsContact.setVisibility(View.GONE);
                btnSubmit.setOnClickListener(v1 -> {
                    getEcard(dialog);


                });
                btnCancel.setOnClickListener(v -> {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    dialog.dismiss();

                });

                dialog.show();


            }
            for (int i = 0; i < navController.getBackQueue().size(); i++) {
                if (navController.getBackQueue().get(i).getDestination().getLabel() != null && navController.getBackQueue().get(i).getDestination().getLabel().equals("Profile")) {
                    bottomNavigationView.getMenu().getItem(4).setChecked(true);

                }
            }

        });

        NavigationUI.setupActionBarWithNavController(this, navController);

        ecardViewModel.getReloginState().observe(this, relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(this);
            } else {
            }
        });

        //throw  new RuntimeException("test crash");


    }

    @SuppressLint("ResourceAsColor")
    private void showDeclarationDialog() {
        final Dialog dialog = new Dialog(this);

        DeclarationPopupLayoutBinding binding = DeclarationPopupLayoutBinding.inflate(LayoutInflater.from(this));
        dialog.setContentView(binding.getRoot());
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() *
                1f), dialog.getWindow().getAttributes().height);
        dialog.setCancelable(false);
        binding.term.setOnClickListener(v -> {
            String url = "https://www.indiainsure.com/terms_conditions.php";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);
        });
        binding.privacy.setOnClickListener(v -> {
            String url = "https://www.indiainsure.com/privacy_policy.php";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);
        });
        /* binding.disclaimer.setOnClickListener(v -> {
         *//*  String url = "https://mybenefits360.com/disclaimer.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);*//*
        });*/
        /* binding.refund.setOnClickListener(v -> {
         *//*  String url = "https://mybenefits360.com/refundpolicy.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            openCustomTab(url);*//*
        });*/
        binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.background_effect_continue));
        binding.checkBox.setOnClickListener(v -> {
            if (binding.checkBox.isChecked()) {
                binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.continue_button_clicked));
            } else {
                binding.buttonContinue.setBackground(binding.buttonContinue.getContext().getDrawable(R.drawable.background_effect_continue));
            }
        });

        binding.cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

        binding.buttonContinue.setOnClickListener(v -> {
            if (binding.checkBox.isChecked()) {
                SharedPreferences settings = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                // Store false value to say that user already saw the walk-through to SharedPreference
                encryptionPreference.setEncryptedBoolString(USER_AGREEMENT, false);
                editor.putBoolean(USER_AGREEMENT, false);
                editor.commit();
                dialog.dismiss();
                //to ask the permissions at first
                askDefaultPermissions(this);

            } else {
                Toast.makeText(this, "Please select the checkbox", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    private boolean userAgreementPopup() {
        try {
            return encryptionPreference.getEncryptedDataBool(USER_AGREEMENT);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }

    private void checkMagisk() {
        if (bServiceBound) {
            boolean bIsMagisk = false;
            try {
                LogMyBenefits.d("MagiskService", "UID:" + Os.getuid());
                bIsMagisk = serviceBinder.isMagiskPresent();
                if (bIsMagisk) {
                    Toast.makeText(getApplicationContext(), "Magisk Found", Toast.LENGTH_SHORT).show();
                    throw new RuntimeException("Magisk Found");
                } else {
                    // Toast.makeText(getApplicationContext(), "Magisk Not Found", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof RuntimeException) {
                    throw new RuntimeException();
                }
            }
        } else {
            //Toast.makeText(getApplicationContext(), "Isolated service not bound", Toast.LENGTH_SHORT).show();
        }
    }

    private final ServiceConnection mIsolatedServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            serviceBinder = IIsolatedService.Stub.asInterface(iBinder);
            bServiceBound = true;
            LogMyBenefits.d("Service", "Service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bServiceBound = false;
            LogMyBenefits.d("Service", "Service Unbound");
        }
    };


    private void getEcard(Dialog dialog) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        dialog.dismiss();
        GroupPolicyData groupPolicyData = selectedPolicyViewModel.getSelectedPolicy().getValue();

        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        try {
            String employee_sr = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
            String group_code = loadSessionResponse.getGroupInfoData().getGroupcode();
            String dataRequest = "<DataRequest>" +
                    "<tpacode>MEDI</tpacode>" +
                    "<employeeno>IND6025</employeeno>" +
                    "<policynumber>0239736210_00</policynumber>" +
                    "<policycommencementdt>29~10~2022</policycommencementdt>" +
                    "<policyvaliduptodt>28~10~2023</policyvaliduptodt>" +
                    "<groupcode>GSIPL</groupcode>" +
                    "</DataRequest>";
            //  String dataRequest = "<DataRequest>" + "<tpacode>" + groupPolicyData.getTpaCode() + "</tpacode>" + "<employeeno>" + employee_sr + "</employeeno>" + "<policynumber>" + groupPolicyData.getPolicyNumber() + "</policynumber>" + "<policycommencementdt>" + groupPolicyData.getPolicyCommencementDate() + "</policycommencementdt>" + "<policyvaliduptodt>" + groupPolicyData.getPolicyCommencementDate() + "</policyvaliduptodt>" + "<groupcode>" + group_code + "</groupcode>" + "</DataRequest>";

            // comment xml response
         /*   ecardViewModel.getEcard(dataRequest).observe(this, ecardResponse -> {
                if (ecardResponse != null) {
                    Log.d(LogTags.E_CARD_ACTIVITY, ecardResponse.toString());
                    if (ecardResponse.getStatus().equals(BuildConfig.SUCCESS)) {

                        if (ecardResponse.getEcardInformation().contains("E-card under process")) {
                            dialog.dismiss();
                            if (!E_CARD_TOAST) {
                                Toast.makeText(this, "E-card under process", Toast.LENGTH_LONG).show();
                                E_CARD_TOAST = true;
                            }

                        } else {
                            String url = "";

                            if (ecardResponse.getEcardInformation().contains("/mybenefits")) {
                                //if we get this locally from our system (my benefits).
                                url = BuildConfig.DOWNLOAD_BASE_URL + ecardResponse.getEcardInformation();
                            }
                            url = url + ecardResponse.getEcardInformation();

                            openCustomTab(ecardResponse.getEcardInformation());
                        *//*Intent opendata = new Intent();
                        opendata.setAction(Intent.ACTION_VIEW);
                        opendata.setData(Uri.parse(url));

                        try {
                            startActivity(opendata);
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (e instanceof ActivityNotFoundException) {
                                Toast.makeText(this, "No Application available to view this file", Toast.LENGTH_SHORT).show();
                                E_CARD_TOAST = true;
                            } else {
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                E_CARD_TOAST = true;

                            }
                        } *//*

                        }

                    } else {
                        if (!E_CARD_TOAST) {
                            Toast.makeText(this, "E-card not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (!E_CARD_TOAST) {
                        Toast.makeText(this, "E-card not available", Toast.LENGTH_SHORT).show();
                    }
                }

            });*/

            String tpa_code = groupPolicyData.getTpaCode();
            try {
                Map<String, String> ecardOptions = new HashMap<>();

                //  for testing
              /*  ecardOptions.put("tpacode", "VHIPL");
                ecardOptions.put("employeeno", "15");
                ecardOptions.put("policynumber", "441202/48/2021/1618");
                ecardOptions.put("policycommencementdt", "10/07/2023");
                ecardOptions.put("policyvaliduptodt", "09/07/2024");
                ecardOptions.put("groupcode", "TCG1");*/

                ecardOptions.put("tpacode", AesNew.encrypt(groupPolicyData.getTpaCode(), getString(R.string.pass_phrase)));
                ecardOptions.put("employeeno", AesNew.encrypt(employee_sr, getString(R.string.pass_phrase)));
                ecardOptions.put("policynumber", AesNew.encrypt(groupPolicyData.getPolicyNumber(), getString(R.string.pass_phrase)));
                ecardOptions.put("policycommencementdt", AesNew.encrypt(groupPolicyData.getPolicyCommencementDate(), getString(R.string.pass_phrase)));
                ecardOptions.put("policyvaliduptodt", AesNew.encrypt(groupPolicyData.getPolicyValidUpto(), getString(R.string.pass_phrase)));
                ecardOptions.put("groupcode", AesNew.encrypt(group_code, getString(R.string.pass_phrase)));
                ecardOptions.put("OeGrpBasInfSrNo", AesNew.encrypt(groupPolicyData.getOeGrpBasInfSrNo(), getString(R.string.pass_phrase)));
                //  ecardOptions.put("empSrno", AesNew.encrypt(loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo(), getString(R.string.pass_phrase)));


                ecardViewModel.getEcard(ecardOptions).observe(this, ecardResponse -> {
                    if (ecardResponse != null) {

                        if (ecardResponse.getMessage().getECard().startsWith("/mybenefits")) {
                            getEcardDownload();
                        } else {
                            if (ecardResponse.getMessage().getECard().contains("E-card under process")) {
                                runUIThread("E-card under process.");
                            } else if (ecardResponse.getMessage().getECard().contains("No records Found")) {

                                runUIThread("No records Found.");

                            } else if (ecardResponse.getMessage().getECard().equalsIgnoreCase("")) {
                                runUIThread("E-Card not available");

                            } else {

                                if (ecardResponse.getMessage().getECard() == null ||
                                        ecardResponse.getMessage().getECard().equalsIgnoreCase("NA") ||
                                        ecardResponse.getMessage().getECard().equalsIgnoreCase("")) {

                                    runUIThread("E-Card not available");
                                } else if (ecardResponse.getMessage().getECard().contains("http")) {

                                    if (tpa_code.equalsIgnoreCase("HITS") ||
                                            tpa_code.equalsIgnoreCase("PHS") ||
                                            tpa_code.equalsIgnoreCase("ERICT") ||
                                            tpa_code.equalsIgnoreCase("VMCT") ||
                                            tpa_code.equalsIgnoreCase("MDH") ||
                                            tpa_code.equalsIgnoreCase("VHIPL") ||
                                            tpa_code.equalsIgnoreCase("FHPL") ||
                                            tpa_code.equalsIgnoreCase("MEDI")
                                    ) {
                                        try {
                                            StrictMode.ThreadPolicy gfgPolicy =
                                                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(gfgPolicy);
                                            startActivity(new DownloadFile(this, this)
                                                    .downloadFilePDF("Ecard.pdf", ecardResponse.getMessage()
                                                            .getECard().replace("\"", "")));
                                        } catch (ActivityNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        openCustomTab(ecardResponse.getMessage().getECard().replace("\"", ""));
                                    }
                                }


                            }
                        }
                    } else {

                        runUIThread("E-Card not available");

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        ecardViewModel.getLoading().observe(this, loading -> {
            if (loading) {
                binding.progressLayoutEcard.setVisibility(View.VISIBLE);
            } else {
                binding.progressLayoutEcard.setVisibility(View.GONE);
            }
        });


    }


    private void runUIThread(String errorInfo) {
        /*error = errorInfo;
        runOnUiThread(() -> {
            mBuilder = new AlertDialog.Builder(this);

            mBuilder.setMessage(errorInfo);
            mBuilder.setCancelable(true);
            mBuilder.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

            AlertDialog mAlert = mBuilder.create();
            mAlert.show();
        });*/

        error = errorInfo;

        /*E-Card error*/
        final Dialog alertDialog = new Dialog(this);
        alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.nhborder));

        LayoutInflater mLayoutInflater = getLayoutInflater();
        View alertLayout = mLayoutInflater.inflate(R.layout.dialog_internet_error, null);
        alertDialog.setContentView(alertLayout);
        ImageView alertIcon = alertLayout.findViewById(R.id.alertIcon);
        //alertIcon.setImageResource(R.drawable.ic_mark_as_solved);
        Button btnDismiss = alertDialog.findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.setOnDismissListener(dialogInterface -> {

        });
        TextView alertMessage = alertDialog.findViewById(R.id.tvAlertMessage);
        alertMessage.setText("            " + errorInfo + "            ");
        alertDialog.show();


    }

    private void getEcardDownload() {
        String empID = "";
        String GrpCode = "";
        String oe_grp_bas_inf_sr_no = "";
        String URL = BuildConfig.BASE_URL + "/mybenefits/documents/mybenefitsdata/02_PROCESSED_DATA/";
        String Params = GrpCode + "/05_E-CARDS/" + oe_grp_bas_inf_sr_no + "/" + empID + ".pdf";
        this.startActivity(new EcardFragment.DownloadFile(this, this).downloadFilePDF("ecard.pdf", URL + Params));
    }

    private void openCustomTab(String url) {


        // Uses the established session to build a PCCT intent.
        CustomTabsSession session = mCustomTabActivityHelper.getSession();
        CustomTabsIntent.Builder intentBuilder =
                new CustomTabsIntent.Builder(session)
                        .setToolbarColor(ContextCompat.getColor(this, R.color.gradient_start))
                        .setUrlBarHidingEnabled(true)
                        .setShowTitle(true)
                        .setStartAnimations(this, R.anim.slide_in_from_right, R.anim.slide_out_to_left);

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
                this, customTabsIntent, Uri.parse(url), new WebviewFallback());
    }


    private void hideBottomBar() {
        binding.bottomNavHolder.setVisibility(View.GONE);
    }


    private void showBottomBar() {
        binding.bottomNavHolder.setVisibility(View.VISIBLE);
    }


   /* private void getenrollmentInstructions() {
        enrollmentViewModel.getInstructions();
    }*/


    private void loadSessions() {

        LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, "loadSessions: " + getLoginType());
        switch (getLoginType()) {

            case "PHONE_NUMBER":
                //this is the load session with number block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");
                    if (FROM_OTP_SCREEN) {
                        loadSessionViewModel.loadSessionWithNumber(getPhoneNumber(), String.valueOf(OTP));
                    } else {
                        loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                    }

                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    if (FROM_OTP_SCREEN) {
                        loadSessionViewModel.loadSessionWithNumber(getPhoneNumber(), String.valueOf(OTP));
                    } else {
                        loadSessionViewModel.loadSessionWithNumber(getPhoneNumber());
                    }
                }
                break;
            case "EMAIL_ID":
                //this is the load session with email block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    if (FROM_OTP_SCREEN) {
                        loadSessionViewModel.loadSessionWithEmail(getEmail(), String.valueOf(OTP));
                    } else {
                        loadSessionViewModel.loadSessionWithEmail(getEmail());
                    }
                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithEmail(getEmail());
                }
                break;
            case "AUTH_LOGIN_ID":
                //this is the load session with loginId block
                if (Boolean.FALSE.equals(loadSessionViewModel.getLoadingState().getValue()) && Boolean.FALSE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();
                    assert loadSessionResponse != null;
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "FROM ACTIVITY " + loadSessionResponse.toString());

                } else if (Boolean.TRUE.equals(loadSessionViewModel.getErrorState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "Error From Activity");
                } else if (Boolean.TRUE.equals(loadSessionViewModel.getLoadingState().getValue())) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: loading");

                    loadSessionViewModel.loadSessionWithID(getLoginID());
                } else if (loadSessionViewModel.getLoadSessionData() == null) {
                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onCreate: LOADING....");
                    loadSessionViewModel.loadSessionWithID(getLoginID());
                }
                break;
        }


        loadSessionViewModel.getLoadSessionData().observe(this, loadSessionResponse -> {

            try {
                String empSrNo = "";
                String personSrNo = "";
                String empId = "";

                if (encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_SR_NO).isEmpty() ||
                        encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_PERSON_SR_NO).isEmpty() ||
                        encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_ID_NO).isEmpty()
                ) {
                    try {
                        empSrNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    for (GroupGMCPolicyEmpDependantsDatum person : loadSessionResponse.getGroupPoliciesEmployeesDependants().get(0).getGroupGMCPolicyEmpDependantsData()
                    ) {
                        if (person.getRelationid().equalsIgnoreCase("17")) {
                            personSrNo = person.getPersonSrNo();
                        }

                    }
                    empId = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();


                    // Create an instance of BearerRepository
                    BearerRepository bearerRepository = new BearerRepository(getApplication());

                    // Start refreshing the token
                    try {
                        /*bearerRepository.startRefreshingToken(AesEncryption.encrypt(empSrNo),
                                AesEncryption.encrypt(personSrNo),
                                AesEncryption.encrypt(empId));*/
                    } catch (Exception e) {
                        LogMyBenefits.e("REFRESH-TOKEN", "TOKEN : error", e);
                        e.printStackTrace();
                    }


                } else {
                    empSrNo = encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_SR_NO);
                    personSrNo = encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_PERSON_SR_NO);
                    empId = encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_ID_NO);


                    // Create an instance of BearerRepository
                    BearerRepository bearerRepository = new BearerRepository(getApplication());

                    // Start refreshing the token
                    try {
                        /*bearerRepository.startRefreshingToken(AesEncryption.encrypt(encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_SR_NO)),
                                AesEncryption.encrypt(encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_PERSON_SR_NO)),
                                AesEncryption.encrypt(encryptionPreference.getEncryptedDataToken(BuildConfig.TOKEN_EMP_ID_NO)));*/
                    } catch (Exception e) {
                        LogMyBenefits.e("REFRESH-TOKEN", "TOKEN : error", e);
                        e.printStackTrace();
                    }

                }

                encryptionPreference.setEncryptedDataString(BuildConfig.TOKEN_EMP_SR_NO, empSrNo);
                encryptionPreference.setEncryptedDataString(BuildConfig.TOKEN_PERSON_SR_NO, personSrNo);
                encryptionPreference.setEncryptedDataString(BuildConfig.TOKEN_EMP_ID_NO, empId);

                queryViewModel.getQueries(empSrNo);

                try {
                    groupChildSrNo = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String oeGrpBasInfoSrNo = "";


                for (GroupPolicyData policy : loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData()) {
                    if (policy.getPolicyType().equalsIgnoreCase("base")) {
                        oeGrpBasInfoSrNo = policy.getOeGrpBasInfSrNo();
                    }
                }


                //todo change the parameters

                //String dataRequest = "<DataRequest>" + "<groupchildsrno>1224</groupchildsrno>" + "<oegrpbasinfsrno>1356</oegrpbasinfsrno>" + "<hospitalsearchtext>ALL</hospitalsearchtext>" + "</DataRequest>";
                String dataRequest = "<DataRequest>" + "<groupchildsrno>" + groupChildSrNo + "</groupchildsrno>" + "<oegrpbasinfsrno>" + oeGrpBasInfoSrNo + "</oegrpbasinfsrno>" + "<hospitalsearchtext>ALL</hospitalsearchtext>" + "</DataRequest>";

                //TODO call networkProvider
                hospitalNetworkViewModel.getHospitals(groupChildSrNo, oeGrpBasInfoSrNo, "ALL");
                // hospitalNetworkViewModel.getHospitalsCount("1224", "1356");
                hospitalNetworkViewModel.getHospitalsCount(groupChildSrNo, oeGrpBasInfoSrNo, "ALL");

                //call Service Names
                serviceNamesViewModel.getServiceNameData(groupChildSrNo);

                //call AdminSetting API
                adminSettingViewModel.getAdminSetting(groupChildSrNo, oeGrpBasInfoSrNo);


                //myclaims
                String dataRequestMyClaims = "<DataRequest>" + "<groupchildsrno>" + groupChildSrNo + "</groupchildsrno>" + "<employeesrno>" + empSrNo + "</employeesrno>" + "</DataRequest>";
                /*String dataRequestMyClaims = "<DataRequest>" +
                        "<groupchildsrno>1090</groupchildsrno>" +
                        "<employeesrno>109492</employeesrno>" +
                        "</DataRequest>";*/
                myClaimsViewModel.getMyClaims(groupChildSrNo, empSrNo);

                //check the enrollStatus here.
                enrollmentStatusViewModel.getEnrollmentStatus(empSrNo, groupChildSrNo, oeGrpBasInfoSrNo);


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loadSessionViewModel.getReloginState().observe(this, relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(this);
            } else {
            }
        });


    }


    private void logout() {

        // Store null values to SharedPreferences for logging out a user
        encryptionPreference.setEncryptedDataString(AUTH_LOGIN_ID, null);
        encryptionPreference.setEncryptedDataString(AUTH_EMAIL, null);
        encryptionPreference.setEncryptedDataString(AUTH_PHONE_NUMBER, null);
        encryptionPreference.setEncryptedDataString(AUTH_LOGIN_TYPE, null);


        Intent logoutIntent = new Intent(this, SplashScreenActivity.class);
        startActivity(logoutIntent);
        finish();


    }


    private void resetSelection() {
        bottomNavigationView.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0, size = bottomNavigationView.getMenu().size(); i < size; i++) {
            bottomNavigationView.getMenu().getItem(i).setChecked(false);
            bottomNavigationView.getMenu().getItem(i).setVisible(true);
        }
        bottomNavigationView.getMenu().setGroupCheckable(0, true, true);
    }

    private String getPhoneNumber() {


        String phone_number = encryptionPreference.getEncryptedDataString(AUTH_PHONE_NUMBER);

        if (phone_number != null) {
            return phone_number;
        } else {
            return null;
        }
    }

    private String getLoginID() {

        String loginId = encryptionPreference.getEncryptedDataString(AUTH_LOGIN_ID);

        if (loginId != null) {
            return loginId;
        } else {
            return null;
        }
    }

    private String getLoginType() {

        String login_type = encryptionPreference.getEncryptedDataString(AUTH_LOGIN_TYPE);
        LogMyBenefits.d("DEBUG", "getLoginType: " + login_type);
        if (login_type != null) {
            return login_type;
        } else {
            return "PHONE_NUMBER";
        }
    }

    private String getEmail() {

        String email = encryptionPreference.getEncryptedDataString(AUTH_EMAIL);
        LogMyBenefits.d("DEBUG", "getLoginType: " + email);
        if (email != null) {
            return email;
        } else {
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RootDetection.isRooted(this, this, false, this)) {
            // Toast.makeText(this, "Root or in-appropriate app environment detected.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            //nothing
        }

    }

    @Override
    public void onFridaDetection(boolean result) {
        if (result) {
            Toast.makeText(this, "inappropriate environment found", Toast.LENGTH_SHORT).show();
            finish();
        }
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
                        //ecradDownloadHelper.onFinishDownload(position);
                        return FileDownloaderAll.downloadFileWithoutPermission(fileUrl, file, activity, context);

                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    try {
                        //ecradDownloadHelper.onFinishDownload(position);
                        return FileDownloaderAll.downloadFile(fileUrl, file, activity, context);

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
}