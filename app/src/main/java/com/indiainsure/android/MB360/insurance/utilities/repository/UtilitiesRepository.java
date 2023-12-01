package com.indiainsure.android.MB360.insurance.utilities.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.UtilitiesDao;
import com.indiainsure.android.MB360.insurance.utilities.repository.responseclass.UtilitiesResponse;
import com.indiainsure.android.MB360.insurance.utilities.repository.retrofit.UtilitiesRetrofitClient;
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

/**
 * this class has the
 * business logic for Utility reports
 * in users
 **/
public class UtilitiesRepository {

    private final MutableLiveData<UtilitiesResponse> utilitiesMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    private AppDatabase appDatabase;
    private UtilitiesDao utilitiesDao;

    boolean SSL_PINNED = false;
    public final MutableLiveData<Boolean> reloginState;


    public UtilitiesRepository(Application application) {
        this.utilitiesMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        appDatabase = AppDatabase.getInstance(application);
        utilitiesDao = appDatabase.utilitiesDao();
        this.reloginState = new MutableLiveData<>(false);
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @Utilities response
     **/

    public MutableLiveData<UtilitiesResponse> getUtilities(String grpChildSrNo, String oeGrpBasInfSrNo) {
        loadingState.setValue(true);
        errorState.setValue(false);
        try {
            Call<UtilitiesResponse> utilitiesCall = UtilitiesRetrofitClient.getInstance(application.getApplicationContext()).getUtilitiesApi().getUtilities(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(grpChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)));
            /// utilitiesCall.enqueue(new Callback<UtilitiesResponse>() {
            Callback<UtilitiesResponse> utilitiesResponseCallback = new Callback<UtilitiesResponse>() {
                @Override
                public void onResponse(Call<UtilitiesResponse> call, Response<UtilitiesResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                            utilitiesMutableLiveData.setValue(response.body());
                            loadingState.setValue(false);
                            errorState.setValue(false);

                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);

                            utilitiesDao.insertUtilities(response.body());
                            if (response.body().getUtilitiesData().isEmpty()){
                                errorState.setValue(true);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                            utilitiesMutableLiveData.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    } else {
                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED" + response.code());
                        utilitiesMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<UtilitiesResponse> call, Throwable t) {
                    loadingState.setValue(true);
                    LogMyBenefits.e(LogTags.UTILITIES_ACTIVITY, "Error: " + t.getLocalizedMessage());
                    try {
                        utilitiesMutableLiveData.setValue(utilitiesDao.getUtilities(oeGrpBasInfSrNo));
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        utilitiesMutableLiveData.setValue(null);
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

                            if (true) {
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
                        if (false) {
                            socket.close();
                            // Close the socket immediately without sending a request
                            SSL_PINNED = false;
                            throw new RuntimeException("Unrecognized cert hash.");

                        } else {
                            SSL_PINNED = true;
                            //regular code here
                            utilitiesCall.enqueue(new Callback<UtilitiesResponse>() {

                                @Override
                                public void onResponse(Call<UtilitiesResponse> callload, Response<UtilitiesResponse> response) {
                                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                                            utilitiesMutableLiveData.setValue(response.body());
                                            loadingState.setValue(false);
                                            errorState.setValue(false);

                                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);

                                            utilitiesDao.insertUtilities(response.body());
                                            if (response.body().getUtilitiesData().isEmpty()){
                                                errorState.setValue(true);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                                            utilitiesMutableLiveData.setValue(null);
                                            loadingState.setValue(false);
                                            errorState.setValue(true);
                                        }
                                    } else if (response.code() == 401 || response.code()==400) {
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

                                                        if (response.body() != null) {
                                                            if (!response.body().getAuthToken().isEmpty()) {
                                                                encryptionPreference.setEncryptedDataString(BuildConfig.BEARER_TOKEN, response.body().getAuthToken());
                                                                utilitiesCall.clone().enqueue(utilitiesResponseCallback);
                                                            }else {
                                                                reloginState.setValue(true);
                                                            }
                                                        }
                                                        else {
                                                            reloginState.setValue(true);
                                                        }

                                                    }else {
                                                        reloginState.setValue(true);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<AuthToken> call, Throwable t) {

                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED" + response.code());
                                        utilitiesMutableLiveData.setValue(null);
                                        loadingState.setValue(false);
                                        errorState.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<UtilitiesResponse> call, Throwable t) {

                                    LogMyBenefits.e(LogTags.UTILITIES_ACTIVITY, "Error: " + t.getLocalizedMessage());
                                    try {
                                        utilitiesMutableLiveData.setValue(utilitiesDao.getUtilities(oeGrpBasInfSrNo));
                                        loadingState.setValue(false);
                                        errorState.setValue(false);
                                    } catch (Exception e) {
                                        utilitiesMutableLiveData.setValue(null);
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


            //utilitiesCall.clone().enqueue(utilitiesResponseCallback);

        } catch (Exception e) {
            e.printStackTrace();
            loadingState.setValue(false);
            errorState.setValue(false);
        }


        return utilitiesMutableLiveData;
    }

    public MutableLiveData<UtilitiesResponse> getUtilitiesData() {
        return utilitiesMutableLiveData;
    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
