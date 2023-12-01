package com.indiainsure.android.MB360.insurance.escalations.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.EscalationsDao;
import com.indiainsure.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;
import com.indiainsure.android.MB360.insurance.escalations.repository.retrofit.EscalationsRetrofitClient;
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
 * business logic for Escalations {@link EscalationRepository }
 * in users
 **/
public class EscalationRepository {

    private  MutableLiveData<EscalationsResponse> escalationsMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    private AppDatabase appDatabase;
    private EscalationsDao escalationsDao;

    boolean SSL_PINNED = false;
    public final MutableLiveData<Boolean> reloginState;

    public EscalationRepository(Application application) {
        this.escalationsMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(false);
        this.errorState = new MutableLiveData<>(false);
        this.application = application;
        appDatabase = AppDatabase.getInstance(application);
        escalationsDao = appDatabase.escalationsDao();
        this.reloginState = new MutableLiveData<>(false);

    }

    /**
     * this function  has the business logic for calling
     * and parsing the @PolicyFeatures {@link EscalationsResponse } response
     **/

    public MutableLiveData<EscalationsResponse> getEscalations(String groupChildSrNo, String oeGrpBasInfSrNo) {
        loadingState.setValue(true);
        errorState.setValue(false);
        try {
            Call<EscalationsResponse> escalationsCall = EscalationsRetrofitClient.getInstance(application.getApplicationContext()).getEscalationApi().getEscalations(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(groupChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)));

            Callback<EscalationsResponse> callback = new Callback<EscalationsResponse>() {
                @Override
                public void onResponse(Call<EscalationsResponse> call, Response<EscalationsResponse> response) {
                    LogMyBenefits.d(LogTags.ESCALATION_ACTIVITY, "onResponse: " + response.body().toString());
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.ESCALATION_ACTIVITY, "onResponse: " + response.body().toString());
                            escalationsMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                            escalationsDao.insertEscalations(response.body());

                            if (response.body().getGroupEscalationInfo().isEmpty()){
                                errorState.setValue(true);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.ESCALATION_ACTIVITY, "Error: ", e);
                            escalationsMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                            Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        escalationsMutableLiveData.setValue(null);
                        Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<EscalationsResponse> call, Throwable t) {
                    call.cancel();

                    try {

                        LogMyBenefits.d(LogTags.FAQ_ACTIVITY, "onFailure: " + escalationsDao.getEscalations(oeGrpBasInfSrNo).toString());
                        escalationsMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        escalationsMutableLiveData.setValue(null);

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
                            escalationsCall.enqueue(new Callback<EscalationsResponse>() {

                                @Override
                                public void onResponse(Call<EscalationsResponse> call, Response<EscalationsResponse> response) {
                                    errorState.setValue(false);
                                    loadingState.setValue(true);
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.ESCALATION_ACTIVITY, "onResponse: " + response.body().toString());
                                            escalationsMutableLiveData.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                            escalationsDao.insertEscalations(response.body());

                                            if (response.body().getGroupEscalationInfo().isEmpty()){
                                                errorState.setValue(true);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.ESCALATION_ACTIVITY, "Error: ", e);
                                            escalationsMutableLiveData.setValue(null);
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
                                            Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (response.code() == 401 || response.code() == 400) {


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
                                                            if (!response.body().getAuthToken().isEmpty()) {
                                                                encryptionPreference.setEncryptedDataString(BuildConfig.BEARER_TOKEN, response.body().getAuthToken());
                                                                escalationsCall.clone().enqueue(callback);
                                                            }else {
                                                                reloginState.setValue(true);
                                                            }
                                                        }else {
                                                            reloginState.setValue(true);
                                                        }

                                                    }else {
                                                        reloginState.setValue(true);
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
                                        escalationsMutableLiveData.setValue(null);
                                        Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<EscalationsResponse> call, Throwable t) {
                                    call.cancel();
                                    try {

                                        LogMyBenefits.d(LogTags.FAQ_ACTIVITY, "onFailure: " + escalationsDao.getEscalations(oeGrpBasInfSrNo).toString());
                                        escalationsMutableLiveData.setValue(escalationsDao.getEscalations(oeGrpBasInfSrNo));
                                        loadingState.setValue(false);
                                        errorState.setValue(false);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        escalationsMutableLiveData.setValue(null);

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


        } catch (Exception e) {
            e.printStackTrace();
            loadingState.setValue(false);
            errorState.setValue(false);
        }


        return escalationsMutableLiveData;
    }

    public MutableLiveData<EscalationsResponse> getEscalationsData() {
        return escalationsMutableLiveData;

    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
