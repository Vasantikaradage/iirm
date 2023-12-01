package com.indiainsure.android.MB360.insurance.adminsetting;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.SummaryFileResponse;
import com.indiainsure.android.MB360.insurance.adminsetting.retrofit.SummeryRetrofitClient;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
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

public class SummaryReository {
    private final MutableLiveData<SummaryFileResponse> responseBodyMutableLiveData;
    Application application;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    boolean SSL_PINNED = false;


    public SummaryReository(Application application) {
        this.responseBodyMutableLiveData = new MutableLiveData<>();
        this.application = application;
        this.loadingState = new MutableLiveData<>(false);
        this.errorState = new MutableLiveData<>();

    }

    public MutableLiveData<SummaryFileResponse> getSummeryDetailsData() {
        return responseBodyMutableLiveData;
    }


    public MutableLiveData<SummaryFileResponse> getSummaryData(String empSRNo) {
        loadingState.setValue(true);
        MutableLiveData<SummaryFileResponse> responseBodyMutableLiveData = new MutableLiveData<>();

        try {
            Call<SummaryFileResponse> summaryDetails = SummeryRetrofitClient.getInstance(application.getApplicationContext()).getMyApi().getSummaryDetails(
                    UtilMethods.checkSpecialCharacters(empSRNo));

            Callback<SummaryFileResponse> summeryCallback = new Callback<SummaryFileResponse>() {
                @Override
                public void onResponse(Call<SummaryFileResponse> call, Response<SummaryFileResponse> summeryFileResponse) {

                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + summeryFileResponse);
                    if (summeryFileResponse.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + summeryFileResponse.body());

                            responseBodyMutableLiveData.setValue(summeryFileResponse.body());
                            loadingState.setValue(false);
                            errorState.setValue(false);
                            /* loadSessionDao.insertLoadSession(response.body());*/
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                            responseBodyMutableLiveData.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    } else {
                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "LoadSessionWithPhoneNumber : ERROR CODE " + summeryFileResponse.code());
                        responseBodyMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<SummaryFileResponse> call, Throwable t) {
                    call.cancel();
                    LogMyBenefits.e(LogTags.LOAD_SESSIONS, "Error: " + t);

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


                        try {
                            InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.cer");
                            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                            X509Certificate leafCertificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);

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

                            summaryDetails.enqueue(new Callback<SummaryFileResponse>() {
                                @Override
                                public void onResponse(Call<SummaryFileResponse> call, Response<SummaryFileResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.ESCALATION_ACTIVITY, "onResponse: " + response.body().toString());
                                            responseBodyMutableLiveData.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.ESCALATION_ACTIVITY, "Error: ", e);
                                            responseBodyMutableLiveData.setValue(null);
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
                                            Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (response.code() == 401) {


                                        //refresh the token
                                        EncryptionPreference encryptionPreference = new EncryptionPreference(application.getApplicationContext());
                                        try {

                                            Call<AuthToken> authTokenCall = BearerRetrofitClient.getInstance(application.getApplicationContext()).getBearerApi().
                                                    refreshToken(AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_EMP_SR_NO), application.getString(R.string.pass_phrase)),
                                                            AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_PERSON_SR_NO), application.getString(R.string.pass_phrase)),
                                                            AesNew.encrypt(encryptionPreference.getEncryptedDataString(BuildConfig.TOKEN_EMP_ID_NO), application.getString(R.string.pass_phrase)));

                                            authTokenCall.enqueue(new Callback<AuthToken>() {
                                                @Override
                                                public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                                                    if (response.code() == 200) {
                                                        LogMyBenefits.d("REFRESH-TOKEN", "TOKEN: " + response.body().getAuthToken());
                                                        if (response.body() != null) {
                                                            encryptionPreference.setEncryptedDataString(BuildConfig.BEARER_TOKEN, response.body().getAuthToken());
                                                            summaryDetails.clone().enqueue(summeryCallback);
                                                        } else {
                                                            Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();

                                                        }

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<AuthToken> call, Throwable t) {
                                                    call.cancel();
                                                    LogMyBenefits.e("REFRESH-TOKEN", "onFailure: ", t);
                                                    t.printStackTrace();
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        responseBodyMutableLiveData.setValue(null);
                                        //Toast.makeText(application, "" + "Data is  not available", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<SummaryFileResponse> call, Throwable t) {
                                    call.cancel();

                                    loadingState.setValue(false);
                                    errorState.setValue(true);

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

        } catch (Exception e) {
            e.printStackTrace();
            loadingState.setValue(false);
            errorState.setValue(false);
        }


        return responseBodyMutableLiveData;

    }


    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }




}
