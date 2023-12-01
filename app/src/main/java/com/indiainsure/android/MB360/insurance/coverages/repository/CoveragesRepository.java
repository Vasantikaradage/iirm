package com.indiainsure.android.MB360.insurance.coverages.repository;

import static com.indiainsure.android.MB360.BuildConfig.BEARER_TOKEN;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.CoverageDao;
import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;
import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoveragesDatum;
import com.indiainsure.android.MB360.insurance.coverages.repository.retrofit.CoveragesRetrofitClient;
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
import java.util.List;
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
 * business logic for Coverages
 * in users
 **/
public class CoveragesRepository {

    private final MutableLiveData<CoverageResponse> coverageResponseMutableLiveData;
    public final MutableLiveData<CoverageDetailsResponse> coverageDetailsResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    public final MutableLiveData<String> relationGroupLiveData;
    private AppDatabase appDatabase;
    private CoverageDao coverageDao;

    boolean SSL_PINNED = false;
    public final MutableLiveData<Boolean> reloginState;

    //relation group count count

    public final MutableLiveData<Integer> Son = new MutableLiveData<>(0);
    public final MutableLiveData<Integer> Dt = new MutableLiveData<>(0);

    public CoveragesRepository(Application application) {
        this.coverageResponseMutableLiveData = new MutableLiveData<>();
        this.coverageDetailsResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.relationGroupLiveData = new MutableLiveData<>();
        this.application = application;
        appDatabase = AppDatabase.getInstance(application);
        coverageDao = appDatabase.coverageDao();
        this.reloginState = new MutableLiveData<>(false);

    }

    /**
     * this function  has the business logic for calling
     * and parsing the @Coverage response
     **/
    public MutableLiveData<CoverageResponse> getCoveragesPolicyData(String groupChildSrNo, String oeGrpBasInfSrNo) throws Exception {
        Son.setValue(0);
        Dt.setValue(0);
        loadingState.setValue(true);
        errorState.setValue(false);
        Call<CoverageResponse> coverageResponseCall = CoveragesRetrofitClient.getInstance(application.getApplicationContext()).getCoverageApi().getCoveragesPolicyData(
                AesNew.encrypt(UtilMethods.checkSpecialCharacters(groupChildSrNo), application.getString(R.string.pass_phrase)),
                AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)));

        Callback<CoverageResponse> callback = new Callback<CoverageResponse>() {

            @Override
            public void onResponse(Call<CoverageResponse> call, Response<CoverageResponse> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onResponse: " + response.body());
                        coverageResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        coverageDao.insertCoverage(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                        coverageResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CoverageResponse> call, Throwable t) {
                call.cancel();
                LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: " + t.getLocalizedMessage());
                try {

                    LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onFailure: " + coverageDao.getCoverage(oeGrpBasInfSrNo).toString());
                    coverageResponseMutableLiveData.setValue(coverageDao.getCoverage(oeGrpBasInfSrNo) != null ? coverageDao.getCoverage(oeGrpBasInfSrNo) : null);
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    coverageResponseMutableLiveData.setValue(null);
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

                        coverageResponseCall.enqueue(new Callback<CoverageResponse>() {
                            @Override
                            public void onResponse(Call<CoverageResponse> call, Response<CoverageResponse> response) {
                                loadingState.setValue(true);
                                if (response.code() == 200) {
                                    try {
                                        LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onResponse: " + response.body());
                                        coverageResponseMutableLiveData.setValue(response.body());
                                        errorState.setValue(false);
                                        loadingState.setValue(false);
                                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                        coverageDao.insertCoverage(response.body());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                                        coverageResponseMutableLiveData.setValue(null);
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                    }
                                } else if (response.code() == 401 || response.code() == 400) {

                                    loadingState.setValue(false);
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
                                                            coverageResponseCall.clone().enqueue(callback);
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
                                    Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<CoverageResponse> call, Throwable t) {
                                call.cancel();
                                LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: " + t.getLocalizedMessage());
                                try {

                                    LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onFailure: " + coverageDao.getCoverage(oeGrpBasInfSrNo).toString());
                                    coverageResponseMutableLiveData.setValue(coverageDao.getCoverage(oeGrpBasInfSrNo));
                                    loadingState.setValue(false);
                                    errorState.setValue(false);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    coverageResponseMutableLiveData.setValue(null);
                                    loadingState.setValue(false);
                                    errorState.setValue(true);


                                }

                            }
                        });

                    }

                    socket.close();
                } catch (Exception e) {
                    coverageResponseCall.cancel();
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
            coverageResponseCall.cancel();
            SSL_PINNED = false;
            e.printStackTrace();
            LogMyBenefits.e("SSL", "Error", e);
        }


        return coverageResponseMutableLiveData;
    }

    public MutableLiveData<CoverageDetailsResponse> getCoverageDetails(String groupChildSrNo, String oeGrpBasInfSrNo, String productType, String employeeSrNo, String employeeSrNo_gmc) {

        loadingState.setValue(true);
        errorState.setValue(false);
        try {


            Call<CoverageDetailsResponse> coverageDetailsCall = CoveragesRetrofitClient.getInstance(application.getApplicationContext()).getCoverageApi().getPolicyCoveragesDetails(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(groupChildSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(oeGrpBasInfSrNo), application.getString(R.string.pass_phrase)),
                    productType,
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(employeeSrNo), application.getString(R.string.pass_phrase)),
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(employeeSrNo_gmc), application.getString(R.string.pass_phrase)));

            Callback<CoverageDetailsResponse> callback = new Callback<CoverageDetailsResponse>() {
                @Override
                public void onResponse(Call<CoverageDetailsResponse> call, Response<CoverageDetailsResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onResponse: " + response.body());
                            coverageDetailsResponseMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                            coverageDao.insertCoverageDetails(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                            coverageDetailsResponseMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                        }
                    } else {
                        LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onResponse: " + response);
                        coverageDetailsResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                }

                @Override
                public void onFailure(Call<CoverageDetailsResponse> call, Throwable t) {
                    call.cancel();
                    LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: " + t.getLocalizedMessage());
                    try {

                        LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onFailure: " + coverageDao.getCoverageDetails(oeGrpBasInfSrNo).toString());
                        coverageDetailsResponseMutableLiveData.setValue(coverageDao.getCoverageDetails(oeGrpBasInfSrNo));
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        coverageDetailsResponseMutableLiveData.setValue(null);
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

                            coverageDetailsCall.enqueue(new Callback<CoverageDetailsResponse>() {
                                @Override
                                public void onResponse(Call<CoverageDetailsResponse> call, Response<CoverageDetailsResponse> response) {
                                    loadingState.setValue(true);
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onResponse: " + response.body());
                                            coverageDetailsResponseMutableLiveData.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                            response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                                            coverageDao.insertCoverageDetails(response.body());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                                            coverageDetailsResponseMutableLiveData.setValue(null);
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
                                        }
                                    } else if (response.code() == 401 || response.code() == 400) {
                                        loadingState.setValue(false);
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
                                                                coverageDetailsCall.clone().enqueue(callback);
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
                                        //Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<CoverageDetailsResponse> call, Throwable t) {
                                    call.cancel();
                                    LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: " + t.getLocalizedMessage());
                                    try {

                                        LogMyBenefits.d(LogTags.COVERAGE_ACTIVITY, "onFailure: " + coverageDao.getCoverageDetails(oeGrpBasInfSrNo).toString());
                                        coverageDetailsResponseMutableLiveData.setValue(coverageDao.getCoverageDetails(oeGrpBasInfSrNo));
                                        loadingState.setValue(false);
                                        errorState.setValue(false);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        coverageDetailsResponseMutableLiveData.setValue(null);
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
            loadingState.setValue(false);
            errorState.setValue(true);
            coverageResponseMutableLiveData.setValue(null);
        }

        return coverageDetailsResponseMutableLiveData;
    }

    public MutableLiveData<CoverageResponse> getCoveragesData() {
        return coverageResponseMutableLiveData;
    }

    public MutableLiveData<CoverageDetailsResponse> getCoveragesDetailsData() {
        return coverageDetailsResponseMutableLiveData;
    }

    public String getRelationGroupDependant(String s) {

        switch (s) {
            case "SPOUSE":
                return "Sp";
            case "SON":
                int son = 0;
                if (Son.getValue() != null) {

                    son = +Son.getValue();
                    Son.setValue(Son.getValue() + 1);
                } else {
                    son = 1;
                    Son.setValue(son);
                }

                if (son != 0) {
                    return "Sn" + son;
                } else {
                    return "Sn";
                }


            case "DAUGHTER":
                int dt = 0;
                if (Dt.getValue() != null) {
                    dt = +Dt.getValue();
                    Dt.setValue(Dt.getValue() + 1);
                } else {
                    dt = 1;
                    Dt.setValue(dt);
                }
                if (dt != 0) {
                    return "D" + dt;
                } else {
                    return "D";
                }

            case "MOTHER-IN-LAW":
                return "MIL";
            case "FATHER-IN-LAW":
                return "FIL";
            default:
                return s.substring(0, 1);
        }
    }

    public MutableLiveData<String> getRelationGroupData() {
        try {
            List<CoveragesDatum> coveragesDataList = coverageDetailsResponseMutableLiveData.getValue().getCoveragesData();

            StringBuilder relationGroup = new StringBuilder();
            if (coveragesDataList.size() == 1) {

                relationGroup.append("Employee");
            } else {
                for (CoveragesDatum coverageDetails : coveragesDataList
                ) {
                    if (relationGroup.toString().isEmpty()) {
                        relationGroup.append(getRelationGroupDependant(coverageDetails.getRelation()));
                    } else {
                        relationGroup.append("+").append(getRelationGroupDependant(coverageDetails.getRelation()));
                    }
                }
            }
            relationGroupLiveData.setValue(relationGroup.toString());
        } catch (Exception e) {
            e.printStackTrace();
            relationGroupLiveData.setValue("-");
        }

        return relationGroupLiveData;
    }

    public void setRelationGroupData() {
        relationGroupLiveData.setValue("-");
    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

    public void setError(boolean state) {
        errorState.setValue(state);
    }


}
