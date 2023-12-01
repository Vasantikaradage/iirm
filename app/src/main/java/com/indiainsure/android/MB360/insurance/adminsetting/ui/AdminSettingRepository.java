package com.indiainsure.android.MB360.insurance.adminsetting.ui;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.EnrollmentWindowCountDao;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;
import com.indiainsure.android.MB360.insurance.adminsetting.retrofit.AdminSettingRetrofitClient;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.UtilMethods;
import com.indiainsure.android.MB360.utilities.rootdetection.CertChecker;
import com.indiainsure.android.MB360.utilities.token.responseclasses.AuthToken;
import com.indiainsure.android.MB360.utilities.token.retrofit.BearerRetrofitClient;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSettingRepository {
    private final MutableLiveData<AdminSettingResponse> adminSettingViewModelMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    FirebaseCrashlytics crashlytics;
    Application application;
    private AppDatabase appDatabase;
    private EnrollmentWindowCountDao enrollmentWindowCountDao;

    Boolean SSL_PINNED = false;

    public AdminSettingRepository(Application application) {
        this.adminSettingViewModelMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        crashlytics = FirebaseCrashlytics.getInstance();
        this.application = application;
        appDatabase = AppDatabase.getInstance(application);
        enrollmentWindowCountDao = appDatabase.enrollmentWindowCountDao();


    }

    public MutableLiveData<AdminSettingResponse> getAdminSettingDetailsData() {
        return adminSettingViewModelMutableLiveData;
    }

    public MutableLiveData<AdminSettingResponse> getAdminSettingData(String groupChildSrNo, String oeGrpBasInfSrNo) {

        Call<AdminSettingResponse> adminSetting = AdminSettingRetrofitClient.getInstance(application.getApplicationContext()).getAdminSettingApi().getAdminSettingData(
                UtilMethods.checkSpecialCharacters(groupChildSrNo),
                UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo));
        Callback<AdminSettingResponse> adminSettingResponseCallback = new Callback<AdminSettingResponse>() {
            @Override
            public void onResponse(Call<AdminSettingResponse> call, Response<AdminSettingResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d("", "onResponse: " + response.body().toString());
                        adminSettingViewModelMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        enrollmentWindowCountDao.insertEnrollmentWindowCount(response.body());


                    } catch (Exception e) {
                        e.printStackTrace();
                        adminSettingViewModelMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        LogMyBenefits.e("ADMIN-SETTINGS", e.getLocalizedMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AdminSettingResponse> call, Throwable t) {
                t.printStackTrace();
                try {
                    LogMyBenefits.d("", "onFailure: " + enrollmentWindowCountDao.getEnrollmentCount(oeGrpBasInfSrNo).toString());
                    adminSettingViewModelMutableLiveData.setValue(enrollmentWindowCountDao.getEnrollmentCount(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    adminSettingViewModelMutableLiveData.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }


            }

        };


        try {
            // Disable trust manager checks - we'll check the certificate manually ourselves later
            TrustManager[] trustManager = {new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                try {

                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, trustManager, null);

                    SSLSocket socket = (SSLSocket) context.getSocketFactory().createSocket(BuildConfig.DOMAIN, Integer.parseInt(BuildConfig.PORT));

                    X509Certificate[] certs = (X509Certificate[]) socket.getSession().getPeerCertificates();
                    X509Certificate leafCertificateserver = (X509Certificate) certs[0];
                    LogMyBenefits.d("CERT", "SERVER CERT: " + leafCertificateserver);


                    try {
                        InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.crt");
                        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                        X509Certificate leafCertificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);
                        LogMyBenefits.d("CERT", "CLIENT CERT: " + leafCertificate);

                        if (leafCertificate.equals(leafCertificateserver)) {
                            LogMyBenefits.d("CERT", "VALID CERT");
                            SSL_PINNED = true;
                        } else {
                            LogMyBenefits.d("CERT", "ERROR LEAF: NOT MATCH" + leafCertificate);
                            throw new RuntimeException();
                        }

                        // Use the leafCertificate object to pin the certificate
                    } catch (Exception e) {
                        LogMyBenefits.e("CERT", "ERROR: ", e);
                        e.printStackTrace();
                        if (e instanceof RuntimeException) {
                            throw new RuntimeException("SSL ERROR!");
                        }
                    }


                    Predicate<X509Certificate> certMatch = cert -> CertChecker.Companion.doesCertMatchPin(BuildConfig.CERT, cert);
                    if (!Arrays.stream(certs).anyMatch(certMatch)) {
                        socket.close();
                        // Close the socket immediately without sending a request
                        SSL_PINNED = false;
                        throw new RuntimeException("Unrecognized cert hash.");

                    } else {
                        SSL_PINNED = true;
                        //regular code here
                        adminSetting.enqueue(new Callback<AdminSettingResponse>() {
                            @Override
                            public void onResponse(Call<AdminSettingResponse> call, Response<AdminSettingResponse> response) {
                                if (response.code() == 200) {
                                    try {
                                        LogMyBenefits.d("", "onResponse: " + response.body().toString());
                                        adminSettingViewModelMutableLiveData.setValue(response.body());
                                        errorState.setValue(false);
                                        loadingState.setValue(false);
                                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                        enrollmentWindowCountDao.insertEnrollmentWindowCount(response.body());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        adminSettingViewModelMutableLiveData.setValue(null);
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        LogMyBenefits.e("ADMIN-SETTINGS", e.getLocalizedMessage());

                                    }
                                } else if (response.code() == 401) {

                                    //refresh the token
                                    EncryptionPreference encryptionPreference = new EncryptionPreference(application.getApplicationContext());
                                    try {


                                        Call<AuthToken> authTokenCall = BearerRetrofitClient.getInstance(application.getApplicationContext()).getBearerApi().
                                                refreshToken(AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_EMP_SR_NO),application.getString(R.string.pass_phrase)),
                                                        AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_PERSON_SR_NO),application.getString(R.string.pass_phrase)),
                                                        AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_EMP_ID_NO),application.getString(R.string.pass_phrase)));

                                        authTokenCall.enqueue(new Callback<AuthToken>() {
                                            @Override
                                            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                                                if (response.code() == 200) {

                                                    if (response.body() != null) {
                                                        encryptionPreference.setEncryptedDataString(BuildConfig.BEARER_TOKEN, response.body().getAuthToken());
                                                        adminSetting.clone().enqueue(adminSettingResponseCallback);
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<AuthToken> call, Throwable t) {
                                                call.cancel();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            }

                            @Override
                            public void onFailure(Call<AdminSettingResponse> call, Throwable t) {
                                t.printStackTrace();
                                try {

                                    LogMyBenefits.d("", "onFailure: " + enrollmentWindowCountDao.getEnrollmentCount(oeGrpBasInfSrNo).toString());
                                    adminSettingViewModelMutableLiveData.setValue(enrollmentWindowCountDao.getEnrollmentCount(oeGrpBasInfSrNo));
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    adminSettingViewModelMutableLiveData.setValue(null);

                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                }


                            }

                        });

                    }

                    socket.close();
                } catch (Exception e) {

                    LogMyBenefits.e("SSL", "Error", e);
                    e.printStackTrace();
                    loadingState.postValue(false);
                    errorState.postValue(true);
                    if (e instanceof RuntimeException) {
                        showToast(() -> Toast.makeText(application, "SSL ERROR!", Toast.LENGTH_SHORT).show());
                        throw new RuntimeException("SSL ERROR!");
                    }

                }

            });


        } catch (Throwable e) {
            SSL_PINNED = false;
            e.printStackTrace();
            LogMyBenefits.e("SSL", "Error", e);
        }



        return adminSettingViewModelMutableLiveData;
    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
