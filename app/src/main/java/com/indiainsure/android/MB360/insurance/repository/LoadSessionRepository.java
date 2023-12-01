package com.indiainsure.android.MB360.insurance.repository;


import static com.indiainsure.android.MB360.BuildConfig.BEARER_TOKEN;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.LoadSessionDao;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.retrofit.LoadSessionRetrofitClient;
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

/**
 * this class has the
 * business logic for Loading sessions
 * in users
 **/
public class LoadSessionRepository {
    private final MutableLiveData<LoadSessionResponse> loadSessionResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    public final MutableLiveData<Boolean> reloginState;
    private final int MAX_RETRY_CALL = 3;
    private int RETRIES = 1;
    Application application;
    AppDatabase appDatabase;
    LoadSessionDao loadSessionDao;
    Boolean TOKEN = false;

    Boolean SSL_PINNED = false;

    public LoadSessionRepository(Application application) {
        this.loadSessionResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        this.reloginState = new MutableLiveData<>(false);
        appDatabase = AppDatabase.getInstance(application);
        loadSessionDao = appDatabase.loadSessionDao();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @load-sessions response
     **/
    //with phone number
    public MutableLiveData<LoadSessionResponse> loadSessionWithPhoneNumber(String mobileNumber) {
        try {
            Call<LoadSessionResponse> loadSessionCall = LoadSessionRetrofitClient.getInstance(application.getApplicationContext()).getLoadSessionApi().loadSessionWithPhoneNumber(AesNew.encrypt(mobileNumber, application.getString(R.string.pass_phrase)));
            loadingState.setValue(true);
            Callback<LoadSessionResponse> loadSessionCallBack = new Callback<LoadSessionResponse>() {
                @Override
                public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {

                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());

                            loadSessionResponseMutableLiveData.setValue(response.body());
                            loadingState.setValue(false);
                            errorState.setValue(false);
                            loadSessionDao.insertLoadSession(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                            loadSessionResponseMutableLiveData.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                            reloginState.setValue(true);
                        }
                    } else {
                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "LoadSessionWithPhoneNumber : ERROR CODE " + response.code());
                        loadSessionResponseMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                        reloginState.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<LoadSessionResponse> call, Throwable t) {
                    call.cancel();
                    LogMyBenefits.e(LogTags.LOAD_SESSIONS, "Error: " + t);
                    if (RETRIES < MAX_RETRY_CALL) {
                        loadingState.setValue(true);
                        errorState.setValue(false);
                        RETRIES++;
                        call.clone().enqueue(this);
                    } else {
                        Toast.makeText(application, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        reloginState.setValue(true);
                    }

                  /*  try {

                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onFailure: " + loadSessionDao.getLoadSession().toString());
                        loadSessionResponseMutableLiveData.setValue(loadSessionDao.getLoadSession());
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        loadSessionResponseMutableLiveData.setValue(null);
                        reloginState.setValue(true);
                        loadingState.setValue(false);
                        errorState.setValue(true);


                    }*/
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
                            InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.crt");
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
                            loadSessionCall.enqueue(new Callback<LoadSessionResponse>() {

                                @Override
                                public void onResponse(Call<LoadSessionResponse> callload, Response<LoadSessionResponse> response) {
                                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());

                                            loadSessionResponseMutableLiveData.setValue(response.body());
                                            loadingState.setValue(false);
                                            errorState.setValue(false);
                                            loadSessionDao.insertLoadSession(response.body());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                                            loadSessionResponseMutableLiveData.setValue(null);
                                            loadingState.setValue(false);
                                            errorState.setValue(true);
                                            reloginState.setValue(true);
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
                                                    LogMyBenefits.d("REFRESH-TOKEN", "TOKEN: " + response);
                                                    if (response.code() == 200) {
                                                        if (response.body() != null) {
                                                            if (!response.body().getAuthToken().isEmpty()) {
                                                                encryptionPreference.setEncryptedDataString(BEARER_TOKEN, response.body().getAuthToken());
                                                                loadSessionCall.clone().enqueue(loadSessionCallBack);
                                                            }else {
                                                                reloginState.setValue(true);
                                                            }
                                                        }else {
                                                            reloginState.setValue(true);
                                                        }

                                                    } else  {
                                                        reloginState.setValue(true);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<AuthToken> call, Throwable t) {
                                                    LogMyBenefits.d("REFRESH-TOKEN", "TOKEN: " + t.getLocalizedMessage());
                                                    call.cancel();
                                                    t.printStackTrace();
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "LoadSessionWithPhoneNumber : ERROR CODE " + response.code());
                                        loadSessionResponseMutableLiveData.setValue(null);
                                        loadingState.setValue(false);
                                        errorState.setValue(true);
                                        reloginState.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoadSessionResponse> call, Throwable t) {

                                    LogMyBenefits.e(LogTags.LOAD_SESSIONS, "Error: " + t);
                                    if (RETRIES < MAX_RETRY_CALL) {
                                        loadingState.setValue(true);
                                        errorState.setValue(false);
                                        RETRIES++;
                                        call.clone().enqueue(this);
                                    } else {
                                        call.cancel();
                                        Toast.makeText(application, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        reloginState.setValue(true);
                                    }

                                   /* try {

                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onFailure: " + loadSessionDao.getLoadSession().toString());
                                        loadSessionResponseMutableLiveData.setValue(loadSessionDao.getLoadSession());
                                        loadingState.setValue(false);
                                        errorState.setValue(false);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        loadSessionResponseMutableLiveData.setValue(null);
                                        loadingState.setValue(false);
                                        errorState.setValue(true);
                                        reloginState.setValue(true);


                                    }*/
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


            //loadSessionCall.clone().enqueue(loadSessionCallBack);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return loadSessionResponseMutableLiveData;
    }

    //@Overloading
    public MutableLiveData<LoadSessionResponse> loadSessionWithPhoneNumber(String mobileNumber, String OTP) {

        try {
            Call<LoadSessionResponse> loadSessionCall = LoadSessionRetrofitClient.getInstance(application.getApplicationContext()).getLoadSessionApi().loadSessionWithPhoneNumber(AesNew.encrypt(mobileNumber, application.getString(R.string.pass_phrase)), AesNew.encrypt(OTP, application.getString(R.string.pass_phrase)));
            loadingState.setValue(true);


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
                            InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.crt");
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


                            loadSessionCall.enqueue(new Callback<LoadSessionResponse>() {
                                @Override
                                public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponseWithOTP: " + response.body());
                                            loadSessionResponseMutableLiveData.postValue(response.body());
                                            loadingState.setValue(false);
                                            errorState.setValue(false);
                                            loadSessionDao.insertLoadSession(response.body());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                                            loadSessionResponseMutableLiveData.postValue(null);
                                            loadingState.setValue(false);
                                            errorState.setValue(true);
                                        }
                                    }else {
                                        reloginState.setValue(true);
                                        loadingState.setValue(false);

                                    }
                                   /* if (response.code() == 401 || response.code()==400) {
                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponseWithOTP : ERROR CODE " + response.code());
                                        loadSessionResponseMutableLiveData.setValue(null);
                                        loadingState.setValue(false);
                                        errorState.setValue(true);
                                        reloginState.setValue(true);
                                    } else {
                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponseWithOTP : ERROR CODE " + response.code());
                                        //  loadSessionResponseMutableLiveData.setValue(null);
                                        loadingState.setValue(false);
                                        errorState.setValue(true);
                                    }*/
                                }

                                @Override
                                public void onFailure(Call<LoadSessionResponse> call, Throwable t) {
                                    LogMyBenefits.e(LogTags.LOAD_SESSIONS, "Error: " + t.getLocalizedMessage());
                                    if (RETRIES <= MAX_RETRY_CALL) {
                                        loadingState.setValue(true);
                                        errorState.setValue(false);
                                        RETRIES++;
                                        call.clone().enqueue(this);
                                    } else {
                                        Toast.makeText(application, "Something went wrong.Please try again with correct credentials", Toast.LENGTH_SHORT).show();
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        reloginState.setValue(true);
                                    }

                                   /* try {

                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onFailure: " + loadSessionDao.getLoadSession().toString());
                                        loadSessionResponseMutableLiveData.setValue(loadSessionDao.getLoadSession());
                                        loadingState.setValue(false);
                                        errorState.setValue(false);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        //  loadSessionResponseMutableLiveData.setValue(null);
                                        loadingState.setValue(false);
                                        errorState.setValue(true);


                                    }*/
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

        }


        return loadSessionResponseMutableLiveData;
    }

    //with email
    public MutableLiveData<LoadSessionResponse> loadSessionWithEmail(String email) {

        try {
            Call<LoadSessionResponse> loadSessionCall = LoadSessionRetrofitClient.getInstance(application.getApplicationContext()).getLoadSessionApi().loadSessionWithEmail(AesNew.encrypt(email, application.getString(R.string.pass_phrase)));
            loadingState.setValue(true);
            Callback<LoadSessionResponse> loadSessionCallback = new Callback<LoadSessionResponse>() {
                @Override
                public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                            loadSessionResponseMutableLiveData.setValue(response.body());

                            loadingState.setValue(false);
                            errorState.setValue(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                            loadingState.setValue(false);
                            errorState.setValue(true);
                        }
                    } else {
                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED " + response.code());
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        reloginState.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<LoadSessionResponse> call, Throwable t) {
                    LogMyBenefits.e(LogTags.LOAD_SESSIONS, "Error: " + t.getLocalizedMessage());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    if (RETRIES < MAX_RETRY_CALL) {
                        RETRIES++;
                        call.clone().enqueue(this);
                    }else {
                        call.cancel();
                        Toast.makeText(application, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        reloginState.setValue(true);
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


                        try {
                            InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.crt");
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

                            loadSessionCall.enqueue(new Callback<LoadSessionResponse>() {
                                @Override
                                public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                                            loadSessionResponseMutableLiveData.setValue(response.body());

                                            loadingState.setValue(false);
                                            errorState.setValue(false);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
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
                                                        LogMyBenefits.d("REFRESH-TOKEN", "TOKEN: " + response.body().getAuthToken());
                                                        if (response.body() != null) {
                                                            if (!response.body().getAuthToken().isEmpty()) {
                                                                encryptionPreference.setEncryptedDataString(BEARER_TOKEN, response.body().getAuthToken());
                                                                loadSessionCall.clone().enqueue(loadSessionCallback);
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
                                                    t.printStackTrace();
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED " + response.code());
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoadSessionResponse> call, Throwable t) {
                                    LogMyBenefits.e(LogTags.LOAD_SESSIONS, "Error: " + t.getLocalizedMessage());
                                    errorState.setValue(true);
                                    loadingState.setValue(false);
                                    Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    if (RETRIES < MAX_RETRY_CALL) {
                                        RETRIES++;
                                        call.clone().enqueue(this);
                                    }else {
                                        call.cancel();
                                        Toast.makeText(application, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        reloginState.setValue(true);
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
        }


        return loadSessionResponseMutableLiveData;
    }

    public MutableLiveData<LoadSessionResponse> loadSessionWithEmail(String email, String OTP) {

        try {
            Call<LoadSessionResponse> loadSessionCall = LoadSessionRetrofitClient.getInstance(application.getApplicationContext()).getLoadSessionApi().loadSessionWithEmail(AesNew.encrypt(email, application.getString(R.string.pass_phrase)), AesNew.encrypt(OTP, application.getString(R.string.pass_phrase)));
            loadingState.setValue(true);


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
                            InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.crt");
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

                            loadSessionCall.enqueue(new Callback<LoadSessionResponse>() {
                                @Override
                                public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                                            loadSessionResponseMutableLiveData.setValue(response.body());

                                            loadingState.setValue(false);
                                            errorState.setValue(false);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                                            loadingState.setValue(false);
                                            errorState.setValue(true);
                                        }
                                    } else {
                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED " + response.code());
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoadSessionResponse> call, Throwable t) {
                                    LogMyBenefits.e(LogTags.LOAD_SESSIONS, "Error: " + t.getLocalizedMessage());
                                    errorState.setValue(true);
                                    loadingState.setValue(false);
                                    Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    if (RETRIES < MAX_RETRY_CALL) {
                                        RETRIES++;
                                        call.clone().enqueue(this);
                                    }else {
                                        call.cancel();
                                        Toast.makeText(application, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        reloginState.setValue(true);
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
        }

        return loadSessionResponseMutableLiveData;
    }

    public MutableLiveData<LoadSessionResponse> loadSessionID(String loginID) {

        try {
            Call<LoadSessionResponse> loadSessionCall = LoadSessionRetrofitClient.getInstance(application.getApplicationContext()).getLoadSessionApi().loadSessionWithID(AesNew.encrypt(loginID, application.getString(R.string.pass_phrase)));
            loadingState.setValue(true);


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
                            InputStream certInputStream = application.getApplicationContext().getAssets().open("cer.crt");
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
                            loadSessionCall.enqueue(new Callback<LoadSessionResponse>() {
                                @Override
                                public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                                            loadSessionResponseMutableLiveData.setValue(response.body());

                                            loadingState.setValue(false);
                                            errorState.setValue(false);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                                            loadingState.setValue(false);
                                            errorState.setValue(true);
                                        }
                                    } else {
                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED " + response.code());
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        //if status code is 500 try to send the user for new login credentials.
                                        if (response.code() == 500) {
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                            reloginState.setValue(true);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<LoadSessionResponse> call, Throwable t) {
                                    LogMyBenefits.e(LogTags.LOAD_SESSIONS, "Error: " + t.getLocalizedMessage());
                                    errorState.setValue(true);
                                    loadingState.setValue(false);
                                    Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    if (RETRIES < MAX_RETRY_CALL) {
                                        RETRIES++;
                                        call.clone().enqueue(this);
                                        loadingState.setValue(true);
                                    }else {
                                        call.cancel();
                                        Toast.makeText(application, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        reloginState.setValue(true);
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
        }


        return loadSessionResponseMutableLiveData;
    }

    public MutableLiveData<LoadSessionResponse> getLoadSessionResponseMutableLiveData() {
        return loadSessionResponseMutableLiveData;
    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
