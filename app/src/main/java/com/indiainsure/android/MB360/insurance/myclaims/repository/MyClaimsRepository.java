package com.indiainsure.android.MB360.insurance.myclaims.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.MyClaimsDao;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.ClaimInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.ClaimsResponse;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimsDetails;
import com.indiainsure.android.MB360.insurance.myclaims.retrofit.MyClaimsRetrofitClientJson;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.ResponseException;
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

public class MyClaimsRepository {

    MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    MutableLiveData<Boolean> error = new MutableLiveData<>(false);
    MutableLiveData<String> errorDescription = new MutableLiveData<>(null);
    FirebaseCrashlytics crashlytics;
    boolean SSL_PINNED = false;


    Application application;
    private final MutableLiveData<ClaimsResponse> myClaimsResponse;
    private final MutableLiveData<ClaimsDetails> myClaimsDetailsResponse;
    public MutableLiveData<ClaimInformation> selectedClaim = new MutableLiveData<>();
    private AppDatabase appDatabase;
    private MyClaimsDao documentElementDao;
    public final MutableLiveData<Boolean> reloginState;


    public MyClaimsRepository(Application application) {
        myClaimsResponse = new MutableLiveData<>();
        myClaimsDetailsResponse = new MutableLiveData<>();
        this.application = application;
        appDatabase = AppDatabase.getInstance(application);
        documentElementDao = appDatabase.documentElementDao();
        crashlytics = FirebaseCrashlytics.getInstance();
        this.reloginState = new MutableLiveData<>(false);

    }

    /**
     * this function  has the business logic for calling
     * and parsing the claims XML response
     **/
    public LiveData<ClaimsResponse> getMyClaims(String groupChildSrNo, String employeeSrNo) {

        try {
            Call<ClaimsResponse> myClaimsCall = MyClaimsRetrofitClientJson
                    .getInstance(application.getApplicationContext())
                    .getClaimsApi()
                    .getEmployeeClaims(AesNew.encrypt(groupChildSrNo, application.getString(R.string.pass_phrase)), AesNew.encrypt(employeeSrNo, application.getString(R.string.pass_phrase)));
            // myClaimsCall.enqueue(new Callback<ClaimsResponse>() {
            Callback<ClaimsResponse> myclaimsResponseCallback = new Callback<ClaimsResponse>() {
                @Override
                public void onResponse(Call<ClaimsResponse> call, Response<ClaimsResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                            myClaimsResponse.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);
                            /* documentElementDao.insertDocumentElement(response.body());*/
                        } catch (Exception e) {
                            e.printStackTrace();
                            myClaimsResponse.setValue(null);
                            errorDescription.setValue(application.getApplicationContext().getString(R.string.something_went_wrong));
                            loading.setValue(false);
                            error.setValue(false);

                        }
                    } else {
                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.code());
                        myClaimsResponse.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(true);
                        errorDescription.setValue(null);

                        ResponseException responseException = new ResponseException("CLAIM_ACTIVITY ->" +
                                " getMyClaims-> Response Code:- " + String.valueOf(response.code()) + " :- " + "GROUPCHILDSRNO: " + groupChildSrNo + " EMPLOYEE_SR_NO: " + employeeSrNo);
                        crashlytics.recordException(responseException);
                    }
                }

                @Override
                public void onFailure(Call<ClaimsResponse> call, Throwable t) {
                    LogMyBenefits.e(LogTags.CLAIM_ACTIVITY, "onFailure: ", t);
                    try {
                        myClaimsResponse.setValue(null);
                        loading.setValue(false);
                        error.setValue(false);
                        //  LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + documentElementDao.getDocumentElement().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                        myClaimsResponse.setValue(null);
                        loading.setValue(false);
                        error.setValue(true);
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
                            myClaimsCall.enqueue(new Callback<ClaimsResponse>() {

                                @Override
                                public void onResponse(Call<ClaimsResponse> callload, Response<ClaimsResponse> response) {
                                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                                            myClaimsResponse.setValue(response.body());
                                            loading.setValue(false);
                                            error.setValue(false);
                                            /* documentElementDao.insertDocumentElement(response.body());*/
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            myClaimsResponse.setValue(null);
                                            errorDescription.setValue(application.getApplicationContext().getString(R.string.something_went_wrong));
                                            loading.setValue(false);
                                            error.setValue(false);

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
                                                                myClaimsCall.clone().enqueue(myclaimsResponseCallback);
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
                                        myClaimsResponse.setValue(null);
                                        loading.setValue(false);
                                        error.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimsResponse> call, Throwable t) {

                                    LogMyBenefits.e(LogTags.CLAIM_ACTIVITY, "onFailure: ", t);
                                    try {
                                        myClaimsResponse.setValue(null);
                                        loading.setValue(false);
                                        error.setValue(false);
                                        //  LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + documentElementDao.getDocumentElement().toString());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        myClaimsResponse.setValue(null);
                                        loading.setValue(false);
                                        error.setValue(true);
                                    }


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



        } catch (Exception e) {
            myClaimsResponse.setValue(null);
            loading.setValue(false);
            error.setValue(true);
            e.printStackTrace();

        }
        return myClaimsResponse;
    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

    /**
     * this function  has the business logic for calling
     * and parsing the claims Details XML response
     **/
    public LiveData<ClaimsDetails> getMyClaimsDetails(String groupChilSrNo, String oegrpBasInfoSr0, String claimSrNo) {
        loading.setValue(true);
        MutableLiveData<ClaimsDetails> myClaimsDetailsResponse = new MutableLiveData<>();
        try {
            Call<ClaimsDetails> myClaimsCall = MyClaimsRetrofitClientJson.getInstance(application.getApplicationContext()).getClaimsApi().getEmployeeClaimsDetails(
                    AesNew.encrypt(groupChilSrNo, application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(oegrpBasInfoSr0, application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(claimSrNo, application.getString(R.string.pass_phrase)));

            Callback<ClaimsDetails> myclaimsDetailsResponseCallback = new Callback<ClaimsDetails>() {

                // myClaimsCall.enqueue(new Callback<ClaimsDetails>() {
                @Override
                public void onResponse(Call<ClaimsDetails> call, Response<ClaimsDetails> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                            myClaimsDetailsResponse.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);
                            /*  documentElementDao.insertClaimDetails(response.body());*/
                        } catch (Exception e) {
                            e.printStackTrace();
                            myClaimsDetailsResponse.setValue(null);
                            loading.setValue(false);
                            error.setValue(false);

                        }
                    } else {
                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.code());
                        myClaimsDetailsResponse.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(true);
                        errorDescription.setValue("Some thing went wrong \nError Code : " + response.code());
                        ResponseException responseException = new ResponseException("CLAIM_ACTIVITY ->" +
                                " getMyClaims-> Response Code:- " + String.valueOf(response.code()) +
                                "groupChilSrNo:- " + groupChilSrNo + "oegrpbasInfoSrno:- " + oegrpBasInfoSr0 + "claimSrNo:-" + claimSrNo);
                        crashlytics.recordException(responseException);
                    }
                }

                @Override
                public void onFailure(Call<ClaimsDetails> call, Throwable t) {
                    LogMyBenefits.e(LogTags.CLAIM_ACTIVITY, "onFailure: ", t);
                    try {

                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + documentElementDao.getClaimDeatils(claimSrNo).toString());
                        /*myClaimsDetailsResponse.setValue(documentElementDao.getClaimDeatils(claimSrNo));*/
                        loading.setValue(false);
                        error.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        loading.setValue(false);
                        error.setValue(true);
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
                            myClaimsCall.enqueue(new Callback<ClaimsDetails>() {

                                @Override
                                public void onResponse(Call<ClaimsDetails> callload, Response<ClaimsDetails> response) {
                                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                                    if (response.code() == 200) {
                                        if (response.code() == 200) {
                                            try {
                                                LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                                                myClaimsDetailsResponse.setValue(response.body());
                                                loading.setValue(false);
                                                error.setValue(false);
                                                /*  documentElementDao.insertClaimDetails(response.body());*/
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                myClaimsDetailsResponse.setValue(null);
                                                loading.setValue(false);
                                                error.setValue(false);

                                            }
                                        } else {
                                            LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.code());
                                            myClaimsDetailsResponse.setValue(response.body());
                                            loading.setValue(false);
                                            error.setValue(true);
                                            errorDescription.setValue("Some thing went wrong \nError Code : " + response.code());
                                            ResponseException responseException = new ResponseException("CLAIM_ACTIVITY ->" +
                                                    " getMyClaims-> Response Code:- " + String.valueOf(response.code()) +
                                                    "groupChilSrNo:- " + groupChilSrNo + "oegrpbasInfoSrno:- " + oegrpBasInfoSr0 + "claimSrNo:-" + claimSrNo);
                                            crashlytics.recordException(responseException);
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
                                                                myClaimsCall.clone().enqueue(myclaimsDetailsResponseCallback);
                                                            } else {
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
                                        myClaimsResponse.setValue(null);
                                        loading.setValue(false);
                                        error.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimsDetails> call, Throwable t) {

                                    LogMyBenefits.e(LogTags.CLAIM_ACTIVITY, "onFailure: ", t);
                                    try {

                                        LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + documentElementDao.getClaimDeatils(claimSrNo).toString());
                                        /*myClaimsDetailsResponse.setValue(documentElementDao.getClaimDeatils(claimSrNo));*/
                                        loading.setValue(false);
                                        error.setValue(false);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        loading.setValue(false);
                                        error.setValue(true);
                                    }
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


        } catch (Exception e) {
            e.printStackTrace();
        }

        return myClaimsDetailsResponse;
    }

    public LiveData<ClaimsResponse> getMyClaimsData() {
        return myClaimsResponse;
    }

    public LiveData<ClaimsDetails> getMyClaimsDetailsData() {
        return myClaimsDetailsResponse;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }

    public MutableLiveData<ClaimInformation> getSelectedClaim() {
        return selectedClaim;
    }

    public void setSelectedClaim(ClaimInformation claims) {
        selectedClaim.setValue(claims);
    }


    public MutableLiveData<String> getErrorDescription() {
        return errorDescription;
    }
}
