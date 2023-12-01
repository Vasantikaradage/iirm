package com.indiainsure.android.MB360.onboarding;

import static com.indiainsure.android.MB360.BuildConfig.AUTH_EMAIL;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_FIRST_TIME_USER;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_LOGIN_ID;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_PHONE_NUMBER;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.insurance.DashBoardActivity;
import com.indiainsure.android.MB360.onboarding.authentication.LoginActivity;
import com.indiainsure.android.MB360.onboarding.walkthrough.WalkThroughActivity;
import com.indiainsure.android.MB360.utilities.AppLocalConstant;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.FridaListener;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.ResponseException;
import com.indiainsure.android.MB360.utilities.rootdetection.RootDetection;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SuppressLint("CustomSplashScreen")

/** @splash-screen google has new custom api for splash screen
we can update this thing later **/

public class SplashScreenActivity extends AppCompatActivity implements FridaListener {

    SharedPreferences sharedPreferences;
    String LOGIN_TYPE = AUTH_PHONE_NUMBER;

    //update related variables
    Task<AppUpdateInfo> appUpdateInfoTask;
    AppUpdateManager appUpdateManager;
    EncryptionPreference encryptionPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /**check for new updates for the app from play store **/
        checkForUpdates();

        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);


        //check for root
        if (RootDetection.isRooted(this, this, false, this)) {
            finishAffinity();
        } else {

            try {
                encryptionPreference = new EncryptionPreference(this);
                sharedPreferences = getEncryptedSharedPreferences();
            } catch (Exception e) {
                LogMyBenefits.e("SPLASHSCREEN", "ENCRYPTION: ", e);
                e.printStackTrace();
            }


            /** we must check for account authentication available locally
             *  so that we can call @loadSession api for downloading updated content
             * **/

            /** if number is present in @getExistingAccount() then we have to navigate to homepage after load sessions are being called
             if number is not present we have to navigate to login page **/
            //if user comes for the first time
            if (firstTimeUser()) {
                Intent walkThroughIntent = new Intent(this, WalkThroughActivity.class);
                startActivity(walkThroughIntent);
                finish();
            } else if (getExistingAccount() != null) {
                splashScreen.setKeepOnScreenCondition(() -> true);
                Intent dashboardIntent = new Intent(this, DashBoardActivity.class);
                startActivity(dashboardIntent);
                finish();
            } else {
                //Navigate to login page (LOGIN-PAGE)
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }


        }
    }

    private boolean firstTimeUser() {
        try {
            return encryptionPreference.getEncryptedDataBool(AUTH_FIRST_TIME_USER);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }

    private void checkForUpdates() {
        //this function check the updates and throws the download activity
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            LogMyBenefits.d(LogTags.UPDATE, "CHECKING FOR UPDATE.....");

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE// This applies an immediate update
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            AppLocalConstant.UPDATE_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    FirebaseCrashlytics crashlytics;
                    crashlytics = FirebaseCrashlytics.getInstance();
                    ResponseException exception = new ResponseException(LogTags.UPDATE + " : " + " | Message " + e.getLocalizedMessage());
                    Throwable throwable = new Throwable(exception);
                    crashlytics.recordException(throwable);

                }
            }
        }).addOnFailureListener(e -> {
            getExistingAccount();
        });


    }

    //this will return the number if number is already logged-in once!
    //since we don't have any authentication locally.
    private String getExistingAccount() {
        try {

            String phone_number = encryptionPreference.getEncryptedDataString(AUTH_PHONE_NUMBER);
            String email = encryptionPreference.getEncryptedDataString(AUTH_EMAIL);
            String loginid = encryptionPreference.getEncryptedDataString(AUTH_LOGIN_ID);

            if (phone_number != null) {
                LOGIN_TYPE = AUTH_PHONE_NUMBER;
                return phone_number;
            } else if (email != null) {
                LOGIN_TYPE = AUTH_EMAIL;
                return email;
            } else if (loginid != null) {
                LOGIN_TYPE = AUTH_LOGIN_ID;
                return loginid;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onStop() {
        if (appUpdateManager != null) {
            //   appUpdateManager.unregisterListener(installStateUpdatedListener);
            super.onStop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            AppLocalConstant.UPDATE_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppLocalConstant.UPDATE_CODE) {
            if (resultCode != RESULT_OK) {
                LogMyBenefits.d(LogTags.UPDATE, "onActivityResult: Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                finish();
            }
        }
    }

    public SharedPreferences getEncryptedSharedPreferences() throws GeneralSecurityException, IOException {
        KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                BuildConfig.MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(Integer.parseInt(BuildConfig.KEY_SIZE))
                .build();

        MasterKey masterKey = new MasterKey.Builder(this)
                .setKeyGenParameterSpec(spec)
                .build();

        sharedPreferences = EncryptedSharedPreferences.create(
                this,
                BuildConfig.SECRET_FILE_KEY,
                masterKey, // masterKey created above
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        return sharedPreferences;
    }

    @Override
    public void onFridaDetection(boolean result) {
        if (result) {
            Toast.makeText(this, "inappropriate environment found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}