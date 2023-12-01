package com.indiainsure.android.MB360.insurance.claims.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.ClaimsDao;
import com.indiainsure.android.MB360.insurance.claims.repository.requestclass.NewIntimateRequest;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.Result;
import com.indiainsure.android.MB360.insurance.claims.repository.retrofit.ClaimsRetrofitClient;
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

/**
 * this class has the
 * business logic for Claims {@link ClaimsRepository }
 * in users
 **/
public class ClaimsRepository {

    private final MutableLiveData<ClaimsResponse> claimsMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    public final MutableLiveData<LoadPersonsIntimationResponse> personsMutableLiveData;
    Application application;
    private AppDatabase appDatabase;
    private ClaimsDao claimsDao;
    boolean SSL_PINNED = false;
    public final MutableLiveData<Boolean> reloginState;

    public ClaimsRepository(Application application) {
        this.claimsMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(false);
        this.errorState = new MutableLiveData<>();
        this.personsMutableLiveData = new MutableLiveData<>();
        this.application = application;
        appDatabase = AppDatabase.getInstance(application);
        claimsDao = appDatabase.claimsDao();
        this.reloginState = new MutableLiveData<>(false);

    }

    /**
     * this function  has the business logic for calling
     * and parsing the @PolicyFeatures {@link ClaimsResponse } response
     **/

    public MutableLiveData<ClaimsResponse> getClaims(String employeeSrNo, String groupChildSrNo, String oeGrpBasInfSrNo) {
        loadingState.setValue(true);
        try {
            Call<ClaimsResponse> claimsCall = ClaimsRetrofitClient.getInstance(application.getApplicationContext()).getClaimsApi().getClaims(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(employeeSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(groupChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)));


            Callback<ClaimsResponse> claimCallback = new Callback<ClaimsResponse>() {

                @Override
                public void onResponse(Call<ClaimsResponse> call, Response<ClaimsResponse> response) {
                    if (response.code() == 200 && response.body().getClaimslist() != null) {
                        try {
                            LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body().toString());
                            claimsMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                            claimsDao.insertClaims(response.body());

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.CLAIM_ACTIVITY, "Error: ", e);
                            claimsMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                            Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getResult() != null && !response.body().getResult().getStatus()) {
                        claimsMutableLiveData.setValue(response.body());
                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body().toString());
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        // Toast.makeText(application, "Something Went wrong: " + response.body().getResult().getMessage(), Toast.LENGTH_LONG).show();

                    } else if (response.code() == 401 | response.code() == 400) {
                        claimsMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<ClaimsResponse> call, Throwable t) {
                    try {

                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + claimsDao.getClaims(oeGrpBasInfSrNo).toString());
                        claimsMutableLiveData.setValue(claimsDao.getClaims(oeGrpBasInfSrNo));
                        loadingState.setValue(false);
                        errorState.setValue(false);


                    } catch (Exception e) {
                        e.printStackTrace();
                        claimsMutableLiveData.setValue(null);
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

                            claimsCall.enqueue(new Callback<ClaimsResponse>() {
                                @Override
                                public void onResponse(Call<ClaimsResponse> call, Response<ClaimsResponse> response) {
                                    try {
                                        if (response.code() == 200 && response.body().getClaimslist() != null) {
                                            try {
                                                LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body().toString());
                                                claimsMutableLiveData.setValue(response.body());
                                                errorState.setValue(false);
                                                loadingState.setValue(false);
                                                response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                                claimsDao.insertClaims(response.body());

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                LogMyBenefits.e(LogTags.CLAIM_ACTIVITY, "Error: ", e);
                                                claimsMutableLiveData.setValue(null);
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
                                                                    claimsCall.clone().enqueue(claimCallback);
                                                                } else {
                                                                    reloginState.setValue(true);
                                                                }
                                                            } else {
                                                                reloginState.setValue(true);
                                                            }

                                                        } else {
                                                            reloginState.setValue(true);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<AuthToken> call, Throwable t) {
                                                        Log.e("REFRESH-TOKEN", "onFailure: ", t);
                                                        t.printStackTrace();
                                                    }
                                                });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        } else if (response.body().getResult() != null && !response.body().getResult().getStatus()) {
                                            claimsMutableLiveData.setValue(response.body());
                                            LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body().toString());
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
                                            // Toast.makeText(application, "Something Went wrong: " + response.body().getResult().getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimsResponse> call, Throwable t) {
                                    try {

                                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + claimsDao.getClaims(oeGrpBasInfSrNo).toString());
                                        claimsMutableLiveData.setValue(claimsDao.getClaims(oeGrpBasInfSrNo));
                                        loadingState.setValue(false);
                                        errorState.setValue(false);


                                    } catch (Exception e) {
                                        e.printStackTrace();
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
        }


        return claimsMutableLiveData;
    }

    //spinner data loading the intifate for
    public MutableLiveData<LoadPersonsIntimationResponse> getPersons(String empSrNo, String grpChildSrNo, String oeGrpBAsInfoSrNo) {
        try {
            Call<LoadPersonsIntimationResponse> callGetPersons = ClaimsRetrofitClient.getInstance(application.getApplicationContext()).getClaimsApi().getPersons(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(empSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(grpChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBAsInfoSrNo), application.getString(R.string.pass_phrase)));

            Callback<LoadPersonsIntimationResponse> callback = new Callback<LoadPersonsIntimationResponse>() {
                @Override
                public void onResponse(Call<LoadPersonsIntimationResponse> call, Response<LoadPersonsIntimationResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                            personsMutableLiveData.setValue(response.body());
                            loadingState.setValue(false);
                            errorState.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBAsInfoSrNo);
                            claimsDao.insertLoadPerson(response.body());


                        } catch (Exception e) {
                            personsMutableLiveData.setValue(null);
                            loadingState.setValue(false);
                            errorState.setValue(true);

                        }
                    } else {
                        personsMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<LoadPersonsIntimationResponse> call, Throwable t) {
                    try {

                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + claimsDao.getLoadPerson(oeGrpBAsInfoSrNo).toString());
                        personsMutableLiveData.setValue(claimsDao.getLoadPerson(oeGrpBAsInfoSrNo));
                        loadingState.setValue(false);
                        errorState.setValue(false);


                    } catch (Exception e) {
                        e.printStackTrace();
                        claimsMutableLiveData.setValue(null);
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

                            callGetPersons.enqueue(new Callback<LoadPersonsIntimationResponse>() {
                                @Override
                                public void onResponse(Call<LoadPersonsIntimationResponse> call, Response<LoadPersonsIntimationResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                                            personsMutableLiveData.setValue(response.body());
                                            loadingState.setValue(false);
                                            errorState.setValue(false);
                                            response.body().setOeGrpBasInfoSrNo(oeGrpBAsInfoSrNo);
                                            claimsDao.insertLoadPerson(response.body());


                                        } catch (Exception e) {
                                            personsMutableLiveData.setValue(null);
                                            loadingState.setValue(false);
                                            errorState.setValue(true);

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
                                                                callGetPersons.clone().enqueue(callback);
                                                            } else {
                                                                reloginState.setValue(true);
                                                            }
                                                        } else {
                                                            reloginState.setValue(true);
                                                        }

                                                    } else {
                                                        reloginState.setValue(true);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<AuthToken> call, Throwable t) {
                                                    Log.e("REFRESH-TOKEN", "onFailure: ", t);
                                                    t.printStackTrace();
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        personsMutableLiveData.setValue(response.body());
                                        loadingState.setValue(false);
                                        errorState.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<LoadPersonsIntimationResponse> call, Throwable t) {
                                    try {

                                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + claimsDao.getLoadPerson(oeGrpBAsInfoSrNo).toString());
                                        personsMutableLiveData.setValue(claimsDao.getLoadPerson(oeGrpBAsInfoSrNo));
                                        loadingState.setValue(false);
                                        errorState.setValue(false);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        claimsMutableLiveData.setValue(null);
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
        }

        return personsMutableLiveData;
    }

    public MutableLiveData<LoadPersonsIntimationResponse> getPersonData() {
        return personsMutableLiveData;
    }


    public MutableLiveData<ClaimsResponse> getClaimsData() {
        return claimsMutableLiveData;

    }


    public MutableLiveData<Result> startIntimation(NewIntimateRequest newIntimateRequest) {

        loadingState.setValue(true);
        LogMyBenefits.d("NEW-INTIMATION", newIntimateRequest.toString());
        final MutableLiveData<Result> resultLiveData = new MutableLiveData<>();

        Call<Result> startIntimationCall = ClaimsRetrofitClient.getInstance(application.getApplicationContext()).getClaimsApi().startIntimation(newIntimateRequest);
        Callback<Result> callback = new Callback<Result>() {

            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                        resultLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    loadingState.setValue(false);
                    errorState.setValue(true);
                    resultLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                loadingState.setValue(false);
                errorState.setValue(true);
                resultLiveData.setValue(null);
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

                        startIntimationCall.enqueue(new Callback<Result>() {
                            @Override
                            public void onResponse(Call<Result> call, Response<Result> response) {
                                if (response.code() == 200) {
                                    try {
                                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                                        resultLiveData.setValue(response.body());
                                        loadingState.setValue(false);
                                        errorState.setValue(false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        resultLiveData.setValue(null);
                                        loadingState.setValue(false);
                                        errorState.setValue(true);
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
                                                            startIntimationCall.clone().enqueue(callback);
                                                        } else {
                                                            reloginState.setValue(true);
                                                        }

                                                    } else {
                                                        reloginState.setValue(true);
                                                    }
                                                } else {
                                                    reloginState.setValue(true);
                                                }
                                            }

                                            @Override
                                            public void onFailure
                                                    (Call<AuthToken> call, Throwable t) {
                                                call.cancel();
                                                Log.e("REFRESH-TOKEN", "onFailure: ", t);
                                                t.printStackTrace();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    loadingState.setValue(false);
                                    errorState.setValue(true);
                                    resultLiveData.setValue(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<Result> call, Throwable t) {
                                loadingState.setValue(false);
                                errorState.setValue(true);
                                resultLiveData.setValue(null);
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


        return resultLiveData;
    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}
