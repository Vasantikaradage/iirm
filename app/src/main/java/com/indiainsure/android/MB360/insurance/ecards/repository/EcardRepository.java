package com.indiainsure.android.MB360.insurance.ecards.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.insurance.ecards.reponseclass.EcardResponse;
import com.indiainsure.android.MB360.insurance.ecards.reponseclass.EcardResponseJson;
import com.indiainsure.android.MB360.insurance.ecards.retrofit.EcardRetrofitClientJson;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.rootdetection.CertChecker;
import com.indiainsure.android.MB360.utilities.token.responseclasses.AuthToken;
import com.indiainsure.android.MB360.utilities.token.retrofit.BearerRetrofitClient;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
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


public class EcardRepository {
    Application application;
    private final MutableLiveData<EcardResponse> eCardResponseMutableLiveData;
    MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    MutableLiveData<Boolean> error = new MutableLiveData<>(false);
    boolean SSL_PINNED = false;

    private final MutableLiveData<EcardResponseJson> eCardResponseMutableLiveDataJSON;
    public final MutableLiveData<Boolean> reloginState;



    public EcardRepository(Application application) {
        this.application = application;
        this.eCardResponseMutableLiveData = new MutableLiveData<>();
        this.eCardResponseMutableLiveDataJSON = new MutableLiveData<>();
        this.reloginState = new MutableLiveData<>(false);

    }


   /* public LiveData<EcardResponse> getEcard(String dataRequest) {
        LogMyBenefits.d(LogTags.E_CARD_ACTIVITY, "data request: " + dataRequest);
        loading.setValue(true);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), dataRequest);
        Call<EcardResponse> ecardResponseCall = EcardRetrofitClient.getInstance().getEcardApi().getEcard(requestBody);
        ecardResponseCall.enqueue(new Callback<EcardResponse>() {
            @Override
            public void onResponse(Call<EcardResponse> call, Response<EcardResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.E_CARD_ACTIVITY, "onResponse: " + response.body());
                        eCardResponseMutableLiveData.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        eCardResponseMutableLiveData.setValue(null);
                        loading.setValue(false);
                        error.setValue(false);

                    }
                } else {
                    LogMyBenefits.d(LogTags.E_CARD_ACTIVITY, "onResponse: " + response.body());
                    eCardResponseMutableLiveData.setValue(response.body());
                    loading.setValue(false);
                    error.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<EcardResponse> call, Throwable t) {
                LogMyBenefits.e(LogTags.E_CARD_ACTIVITY, "onFailure: ", t);
                eCardResponseMutableLiveData.setValue(null);
                loading.setValue(false);
                error.setValue(true);
                //  Toast.makeText(application, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
        return eCardResponseMutableLiveData;
    }


    public LiveData<EcardResponse> getEcardData() {
        return eCardResponseMutableLiveData;
    }*/

    public LiveData<EcardResponseJson> getEcard(Map<String, String> ecardMapOptions) {
        MutableLiveData<EcardResponseJson> eCardResponseMutableLiveDataJSON = new MutableLiveData<>();
        loading.setValue(true);

        Call<EcardResponseJson> ecardResponseCall = EcardRetrofitClientJson.getInstance(application.getApplicationContext()).getEcardApi().getEcardJSON(ecardMapOptions);
        //   ecardResponseCall.enqueue(new Callback<EcardResponseJson>() {
        Callback<EcardResponseJson> ecardResponseCallback = new Callback<EcardResponseJson>() {
            @Override
            public void onResponse(Call<EcardResponseJson> call, Response<EcardResponseJson> response) {
                if (response.code() == 200) {
                    try {
                        Log.d("E_CARD_ACTIVITY", "onResponse: " + response.body());
                        eCardResponseMutableLiveDataJSON.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        eCardResponseMutableLiveDataJSON.setValue(null);
                        loading.setValue(false);
                        error.setValue(false);

                    }
                } else {
                    Log.d("E_CARD_ACTIVITY", "onResponse: " + response.body());
                    eCardResponseMutableLiveDataJSON.setValue(response.body());
                    loading.setValue(false);
                    error.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<EcardResponseJson> call, Throwable t) {
                Log.e("E_CARD_ACTIVITY", "onFailure: ", t);
                eCardResponseMutableLiveDataJSON.setValue(null);
                loading.setValue(false);
                error.setValue(true);
                //  Toast.makeText(application, "Something went wrong", Toast.LENGTH_SHORT).show();

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
                        ecardResponseCall.enqueue(new Callback<EcardResponseJson>() {

                            @Override
                            public void onResponse(Call<EcardResponseJson> callload, Response<EcardResponseJson> response) {
                                LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                                if (response.code() == 200) {
                                    try {
                                        Log.d("E_CARD_ACTIVITY", "onResponse: " + response.body());
                                        eCardResponseMutableLiveDataJSON.setValue(response.body());
                                        loading.setValue(false);
                                        error.setValue(false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        eCardResponseMutableLiveDataJSON.setValue(null);
                                        loading.setValue(false);
                                        error.setValue(false);

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

                                                    if (response.body() != null) {
                                                        if (!response.body().getAuthToken().isEmpty()) {
                                                            encryptionPreference.setEncryptedDataString(BuildConfig.BEARER_TOKEN, response.body().getAuthToken());
                                                            ecardResponseCall.clone().enqueue(ecardResponseCallback);
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

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED" + response.code());
                                    eCardResponseMutableLiveDataJSON.setValue(null);
                                    loading.setValue(false);
                                    error.setValue(true);
                                }
                            }

                            @Override
                            public void onFailure(Call<EcardResponseJson> call, Throwable t) {

                                Log.e("E_CARD_ACTIVITY", "onFailure: ", t);
                                eCardResponseMutableLiveDataJSON.setValue(null);
                                loading.setValue(false);
                                error.setValue(true);
                                //  Toast.makeText(application, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }


                        });


                    }

                    socket.close();
                } catch (Exception e) {

                    LogMyBenefits.e("SSL", "Error", e);
                    e.printStackTrace();
                    loading.postValue(false);
                    error.postValue(true);
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
        return eCardResponseMutableLiveDataJSON;
    }

    public LiveData<EcardResponseJson> getEcardData() {
        return eCardResponseMutableLiveDataJSON;
    }

    public LiveData<EcardResponseJson> getEcardJSONData() {
        return eCardResponseMutableLiveDataJSON;
    }


    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }
    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
