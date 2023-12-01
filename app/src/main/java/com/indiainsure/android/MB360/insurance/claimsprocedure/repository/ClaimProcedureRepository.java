package com.indiainsure.android.MB360.insurance.claimsprocedure.repository;


import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.ClaimProcedureDao;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureEmergencyContactResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureImageResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureLayoutInstructionInfo;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureTextResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimsProcedureLayoutInfoResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.retrofit.ClaimProcedureRetrofitClient;
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
 * business logic for Claim-Procedures
 * in users
 **/

public class ClaimProcedureRepository {

    private final MutableLiveData<ClaimsProcedureLayoutInfoResponse> claimsProcedureLayoutInfoResponseMutableLiveData;
    private final MutableLiveData<ClaimProcedureImageResponse> claimProcedureImageResponseMutableLiveData;
    private final MutableLiveData<ClaimProcedureLayoutInstructionInfo> claimProcedureLayoutInstructionInfoMutableLiveData;
    private final MutableLiveData<ClaimProcedureTextResponse> claimProcedureTextResponseMutableLiveData;
    private final MutableLiveData<ClaimProcedureEmergencyContactResponse> claimProcedureEmergencyContactResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    public final MutableLiveData<String> claimProcedureTextFileData;
    public final MutableLiveData<String> claimStepsHtmlData;
    public final MutableLiveData<String> claimStepsHtmlAdditionalData;
    Application application;
    private AppDatabase appDatabase;
    private ClaimProcedureDao claimProcedureLayoutDao;

    boolean SSL_PINNED = false;
    public final MutableLiveData<Boolean> reloginState;


    public ClaimProcedureRepository(Application application) {
        this.claimProcedureEmergencyContactResponseMutableLiveData = new MutableLiveData<>();
        this.claimProcedureImageResponseMutableLiveData = new MutableLiveData<>();
        this.claimProcedureTextResponseMutableLiveData = new MutableLiveData<>();
        this.claimsProcedureLayoutInfoResponseMutableLiveData = new MutableLiveData<>();
        this.claimProcedureLayoutInstructionInfoMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        this.claimProcedureTextFileData = new MutableLiveData<>("");
        this.claimStepsHtmlData = new MutableLiveData<>("");
        this.claimStepsHtmlAdditionalData = new MutableLiveData<>("");
        appDatabase = AppDatabase.getInstance(application);
        claimProcedureLayoutDao = appDatabase.claimProcedureLayoutDao();
        this.reloginState = new MutableLiveData<>(false);

    }


    /**
     * this function  has the business logic for calling
     * and parsing the @Claim-Procedure response
     **/

    public MutableLiveData<ClaimsProcedureLayoutInfoResponse> getClaimsProcedureLayoutInfo(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {

        try {

            Call<ClaimsProcedureLayoutInfoResponse> claimsProcedureLayoutInfoResponseCall = ClaimProcedureRetrofitClient.getInstance(false, application.getApplicationContext()).getClaimProcedureClient().getClaimsProcedureLayoutInfo(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(grpChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)),
                    productCode,
                    layoutOfClaim);

            Callback<ClaimsProcedureLayoutInfoResponse> callback = new Callback<ClaimsProcedureLayoutInfoResponse>() {

                @Override
                public void onResponse(Call<ClaimsProcedureLayoutInfoResponse> call, Response<ClaimsProcedureLayoutInfoResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                            claimsProcedureLayoutInfoResponseMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                            response.body().setLayoutOfClaim(layoutOfClaim);
                            claimProcedureLayoutDao.insertClaimsProcedureLayoutInfo(response.body());

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error getClaimsProcedureLayoutInfo : ", e);
                            claimsProcedureLayoutInfoResponseMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                        }
                    } else {
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                }

                @Override
                public void onFailure(Call<ClaimsProcedureLayoutInfoResponse> call, Throwable t) {
                    call.cancel();
                    LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimsProcedureLayoutInfo" + t.getLocalizedMessage());
                    t.printStackTrace();

                    try {
                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureLayoutInfo(oeGrpBasInfSrNo, layoutOfClaim).toString());
                        claimsProcedureLayoutInfoResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureLayoutInfo(oeGrpBasInfSrNo, layoutOfClaim));
                        loadingState.setValue(false);
                        errorState.setValue(false);


                    } catch (Exception e) {
                        e.printStackTrace();
                        claimsProcedureLayoutInfoResponseMutableLiveData.setValue(null);

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

                            claimsProcedureLayoutInfoResponseCall.enqueue(new Callback<ClaimsProcedureLayoutInfoResponse>() {
                                @Override
                                public void onResponse(Call<ClaimsProcedureLayoutInfoResponse> call, Response<ClaimsProcedureLayoutInfoResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                                            claimsProcedureLayoutInfoResponseMutableLiveData.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                            response.body().setLayoutOfClaim(layoutOfClaim);
                                            claimProcedureLayoutDao.insertClaimsProcedureLayoutInfo(response.body());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error getClaimsProcedureLayoutInfo : ", e);
                                            claimsProcedureLayoutInfoResponseMutableLiveData.setValue(null);
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
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
                                                                claimsProcedureLayoutInfoResponseCall.clone().enqueue(callback);
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
                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimsProcedureLayoutInfoResponse> call, Throwable t) {
                                    call.cancel();
                                    LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimsProcedureLayoutInfo" + t.getLocalizedMessage());
                                    t.printStackTrace();

                                    try {
                                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureLayoutInfo(oeGrpBasInfSrNo, layoutOfClaim).toString());
                                        claimsProcedureLayoutInfoResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureLayoutInfo(oeGrpBasInfSrNo, layoutOfClaim));
                                        loadingState.setValue(false);
                                        errorState.setValue(false);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        claimsProcedureLayoutInfoResponseMutableLiveData.setValue(null);

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


        return claimsProcedureLayoutInfoResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureImageResponse> getClaimProcedureImage(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {
        try {
            Call<ClaimProcedureImageResponse> callClaimProcedureImageResponseCall = ClaimProcedureRetrofitClient.getInstance(false, application.getApplicationContext()).getClaimProcedureClient().getClaimsProcedureImage(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(grpChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)),
                    productCode,
                    layoutOfClaim);

            Callback<ClaimProcedureImageResponse> callback = new Callback<ClaimProcedureImageResponse>() {
                @Override
                public void onResponse(Call<ClaimProcedureImageResponse> call, Response<ClaimProcedureImageResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                            claimProcedureImageResponseMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                            response.body().setLayoutOfClaim(layoutOfClaim);
                            claimProcedureLayoutDao.insertClaimProcedureImage(response.body());

                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureImage", e);
                            claimProcedureImageResponseMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                        }
                    } else {
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }

                }

                @Override
                public void onFailure(Call<ClaimProcedureImageResponse> call, Throwable t) {
                    call.cancel();

                    LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureImage" + t.getLocalizedMessage());
                    try {
                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureImage(oeGrpBasInfSrNo, layoutOfClaim).toString());
                        claimProcedureImageResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureImage(oeGrpBasInfSrNo, layoutOfClaim));
                        loadingState.setValue(false);
                        errorState.setValue(false);


                    } catch (Exception e) {
                        e.printStackTrace();
                        claimProcedureImageResponseMutableLiveData.setValue(null);

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

                            callClaimProcedureImageResponseCall.enqueue(new Callback<ClaimProcedureImageResponse>() {
                                @Override
                                public void onResponse(Call<ClaimProcedureImageResponse> call, Response<ClaimProcedureImageResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                                            claimProcedureImageResponseMutableLiveData.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                            response.body().setLayoutOfClaim(layoutOfClaim);
                                            claimProcedureLayoutDao.insertClaimProcedureImage(response.body());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureImage", e);
                                            claimProcedureImageResponseMutableLiveData.setValue(null);
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
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
                                                                callClaimProcedureImageResponseCall.clone().enqueue(callback);
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
                                    }

                                }

                                @Override
                                public void onFailure(Call<ClaimProcedureImageResponse> call, Throwable t) {
                                    call.cancel();
                                    LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureImage" + t.getLocalizedMessage());
                                    try {
                                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureImage(oeGrpBasInfSrNo, layoutOfClaim).toString());
                                        claimProcedureImageResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureImage(oeGrpBasInfSrNo, layoutOfClaim));
                                        loadingState.setValue(false);
                                        errorState.setValue(false);


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        claimProcedureImageResponseMutableLiveData.setValue(null);

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
        return claimProcedureImageResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureLayoutInstructionInfo> getClaimProcedureInformation(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {

        try {
            Call<ClaimProcedureLayoutInstructionInfo> callClaimsProcedureInstruction = ClaimProcedureRetrofitClient.getInstance(false, application.getApplicationContext()).getClaimProcedureClient().getClaimProcedureLayoutInstructionInfo(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(grpChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)),
                    productCode,
                    layoutOfClaim);

            Callback<ClaimProcedureLayoutInstructionInfo> callback = new Callback<ClaimProcedureLayoutInstructionInfo>() {

                @Override
                public void onResponse(Call<ClaimProcedureLayoutInstructionInfo> call, Response<ClaimProcedureLayoutInstructionInfo> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                            claimProcedureLayoutInstructionInfoMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                            response.body().setLayoutOfClaim(layoutOfClaim);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                            claimProcedureLayoutDao.insertClaimProcedureLayoutInstruction(response.body());


                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureInformation ", e);
                            claimProcedureLayoutInstructionInfoMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                        }
                    } else {
                        errorState.setValue(true);
                        loadingState.setValue(false);

                    }
                }

                @Override
                public void onFailure(Call<ClaimProcedureLayoutInstructionInfo> call, Throwable t) {
                    call.cancel();
                    t.printStackTrace();
                    LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureInformation " + t.getLocalizedMessage());

                    try {
                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimProcedureLayoutInstruction(oeGrpBasInfSrNo, layoutOfClaim).toString());
                        claimProcedureLayoutInstructionInfoMutableLiveData.setValue(claimProcedureLayoutDao.getClaimProcedureLayoutInstruction(oeGrpBasInfSrNo, layoutOfClaim));
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
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

                            callClaimsProcedureInstruction.enqueue(new Callback<ClaimProcedureLayoutInstructionInfo>() {
                                @Override
                                public void onResponse(Call<ClaimProcedureLayoutInstructionInfo> call, Response<ClaimProcedureLayoutInstructionInfo> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                                            claimProcedureLayoutInstructionInfoMutableLiveData.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                            response.body().setLayoutOfClaim(layoutOfClaim);
                                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                            claimProcedureLayoutDao.insertClaimProcedureLayoutInstruction(response.body());


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureInformation ", e);
                                            claimProcedureLayoutInstructionInfoMutableLiveData.setValue(null);
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
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
                                                                callClaimsProcedureInstruction.clone().enqueue(callback);
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

                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimProcedureLayoutInstructionInfo> call, Throwable t) {
                                    call.cancel();
                                    t.printStackTrace();
                                    LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureInformation " + t.getLocalizedMessage());

                                    try {
                                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimProcedureLayoutInstruction(oeGrpBasInfSrNo, layoutOfClaim).toString());
                                        claimProcedureLayoutInstructionInfoMutableLiveData.setValue(claimProcedureLayoutDao.getClaimProcedureLayoutInstruction(oeGrpBasInfSrNo, layoutOfClaim));
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


        return claimProcedureLayoutInstructionInfoMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureTextResponse> getClaimProcedureText(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {

        try {
            Call<ClaimProcedureTextResponse> callClaimProcedureText = ClaimProcedureRetrofitClient.getInstance(false, application.getApplicationContext()).getClaimProcedureClient().getClaimProcedureText(

                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(grpChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)),
                    productCode,
                    layoutOfClaim);

            Callback<ClaimProcedureTextResponse> callback = new Callback<ClaimProcedureTextResponse>() {

                @Override
                public void onResponse(Call<ClaimProcedureTextResponse> call, Response<ClaimProcedureTextResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                            claimProcedureTextResponseMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                            claimProcedureLayoutDao.insertClaimProcedureTextPath(response.body());


                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureText ", e);
                            claimProcedureTextResponseMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                        }
                    } else {
                        claimProcedureTextResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);

                    }
                }

                @Override
                public void onFailure(Call<ClaimProcedureTextResponse> call, Throwable t) {
                    call.cancel();
                    t.printStackTrace();

                    LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureText " + t.getLocalizedMessage());
                    try {
                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimProcedureTextPath(oeGrpBasInfSrNo).toString());
                        claimProcedureTextResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimProcedureTextPath(oeGrpBasInfSrNo));
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
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

                            callClaimProcedureText.enqueue(new Callback<ClaimProcedureTextResponse>() {
                                @Override
                                public void onResponse(Call<ClaimProcedureTextResponse> call, Response<ClaimProcedureTextResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                                            claimProcedureTextResponseMutableLiveData.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                            claimProcedureLayoutDao.insertClaimProcedureTextPath(response.body());


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureText ", e);
                                            claimProcedureTextResponseMutableLiveData.setValue(null);
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
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
                                                                callClaimProcedureText.clone().enqueue(callback);
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
                                                    call.cancel();
                                                    LogMyBenefits.e("REFRESH-TOKEN", "onFailure: ", t);
                                                    t.printStackTrace();
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        claimProcedureTextResponseMutableLiveData.setValue(null);
                                        errorState.setValue(true);
                                        loadingState.setValue(false);

                                    }
                                }

                                @Override
                                public void onFailure(Call<ClaimProcedureTextResponse> call, Throwable t) {
                                    call.cancel();
                                    t.printStackTrace();

                                    LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureText " + t.getLocalizedMessage());
                                    try {
                                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimProcedureTextPath(oeGrpBasInfSrNo).toString());
                                        claimProcedureTextResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimProcedureTextPath(oeGrpBasInfSrNo));
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

        return claimProcedureTextResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureEmergencyContactResponse> getEmergencyContacts(String tpaCode) {

        Call<ClaimProcedureEmergencyContactResponse> callClaimProcedureEmergencyContact = ClaimProcedureRetrofitClient.getInstance(false, application.getApplicationContext()).getClaimProcedureClient().getClaimProcedureEmergencyResponse(tpaCode);

        Callback<ClaimProcedureEmergencyContactResponse> callback = new Callback<ClaimProcedureEmergencyContactResponse>() {

            @Override
            public void onResponse(Call<ClaimProcedureEmergencyContactResponse> call, Response<ClaimProcedureEmergencyContactResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                        claimProcedureEmergencyContactResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setTpa_code(tpaCode);
                        claimProcedureLayoutDao.insertClaimProcedureEmergencyContact(response.body());


                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getEmergencyContacts ", e);
                        claimProcedureEmergencyContactResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);

                }
            }

            @Override
            public void onFailure(Call<ClaimProcedureEmergencyContactResponse> call, Throwable t) {
                call.cancel();
                LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getEmergencyContacts " + t.getLocalizedMessage());
                t.printStackTrace();

                try {
                    LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureEmergencyContact(tpaCode).toString());
                    claimProcedureEmergencyContactResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureEmergencyContact(tpaCode));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
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

                        callClaimProcedureEmergencyContact.enqueue(new Callback<ClaimProcedureEmergencyContactResponse>() {
                            @Override
                            public void onResponse(Call<ClaimProcedureEmergencyContactResponse> call, Response<ClaimProcedureEmergencyContactResponse> response) {
                                if (response.code() == 200) {
                                    try {
                                        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                                        claimProcedureEmergencyContactResponseMutableLiveData.setValue(response.body());
                                        errorState.setValue(false);
                                        loadingState.setValue(false);
                                        response.body().setTpa_code(tpaCode);
                                        claimProcedureLayoutDao.insertClaimProcedureEmergencyContact(response.body());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getEmergencyContacts ", e);
                                        claimProcedureEmergencyContactResponseMutableLiveData.setValue(null);
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
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
                                                            callClaimProcedureEmergencyContact.clone().enqueue(callback);
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

                                }
                            }

                            @Override
                            public void onFailure
                                    (Call<ClaimProcedureEmergencyContactResponse> call, Throwable t) {
                                call.cancel();
                                LogMyBenefits.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getEmergencyContacts " + t.getLocalizedMessage());
                                t.printStackTrace();

                                try {
                                    LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureEmergencyContact(tpaCode).toString());
                                    claimProcedureEmergencyContactResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureEmergencyContact(tpaCode));
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


        return claimProcedureEmergencyContactResponseMutableLiveData;
    }


    public MutableLiveData<ClaimsProcedureLayoutInfoResponse> getClaimsProcedureLayoutInfoData
            () {
        return claimsProcedureLayoutInfoResponseMutableLiveData;
    }


    public MutableLiveData<ClaimProcedureImageResponse> getClaimProcedureImageData() {
        return claimProcedureImageResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureLayoutInstructionInfo> getClaimProcedureLayoutInstructionInfoData
            () {
        return claimProcedureLayoutInstructionInfoMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureTextResponse> getClaimProcedureTextData() {
        return claimProcedureTextResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureEmergencyContactResponse> getClaimProcedureEmergencyContactData
            () {
        return claimProcedureEmergencyContactResponseMutableLiveData;
    }


    public LiveData<String> getClaimProcedureTextFileData() {
        return claimProcedureTextFileData;
    }


    public MutableLiveData<String> getClaimStepsHtmlData(String groupChildSrno, String
            group_oegrpbasInfoSrNo, String file_name) {

        Call<String> claimProceduretxtCall = ClaimProcedureRetrofitClient.getInstance(true, application.getApplicationContext()).getClaimProcedureClient().getClaimProcedureTxtFileResponse(
                groupChildSrno,
                group_oegrpbasInfoSrNo,
                file_name);


        claimProceduretxtCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "STEPS HTML DATA : " + response.body());
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            claimStepsHtmlData.setValue(response.body().toString());
                        } else {
                            claimStepsHtmlData.setValue("");
                        }
                    } else {
                        claimStepsHtmlData.setValue("");
                    }

                } catch (Exception e) {
                    //something went wrong
                    claimStepsHtmlData.setValue("");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                claimStepsHtmlData.setValue("");
                t.printStackTrace();
            }
        });

        return claimStepsHtmlData;
    }

    public MutableLiveData<String> getClaimStepsHtmlDataObserver() {
        return claimStepsHtmlData;
    }

    public MutableLiveData<String> getClaimStepsHtmlAdditionalData(String
                                                                           groupChildSrno, String group_oegrpbasInfoSrNo, String file_name) throws Exception {


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

                        Call<String> claimProceduretxtCall = ClaimProcedureRetrofitClient.getInstance(true, application.getApplicationContext()).getClaimProcedureClient().getClaimProcedureAdditionalTxtFileResponse(
                                (groupChildSrno),
                                (group_oegrpbasInfoSrNo),
                                file_name);


                        claimProceduretxtCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                                try {
                                    LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "STEPS HTML DATA : " + response.body());
                                    if (response.code() == 200) {
                                        if (response.body() != null) {
                                            claimStepsHtmlAdditionalData.setValue(response.body().toString());
                                        } else {
                                            claimStepsHtmlAdditionalData.setValue("");
                                        }
                                    } else {
                                        claimStepsHtmlAdditionalData.setValue("");
                                    }

                                } catch (Exception e) {
                                    //something went wrong
                                    claimStepsHtmlAdditionalData.setValue("");
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                call.cancel();
                                claimStepsHtmlAdditionalData.setValue("");
                                t.printStackTrace();
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


        return claimStepsHtmlAdditionalData;
    }

    public MutableLiveData<String> getClaimStepsHtmlAdditionalDataObserver() {
        return claimStepsHtmlAdditionalData;
    }

    public void setClaimsSteps(String htmlData) {
        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "ClaimsStepsHTML: " + htmlData);
        claimStepsHtmlData.postValue(htmlData);
    }

    public void setClaimsAdditionalSteps(String htmlData) {
        LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "ClaimsAdditionalStepsHTML: " + htmlData);
        claimStepsHtmlAdditionalData.postValue(htmlData);
    }


    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }
}

