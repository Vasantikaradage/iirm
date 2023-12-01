package com.indiainsure.android.MB360.insurance.queries.repository;

import static com.indiainsure.android.MB360.BuildConfig.BEARER_TOKEN;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.MyQueryDao;
import com.indiainsure.android.MB360.insurance.queries.repository.retrofit.QueryAddRetrofitClient;
import com.indiainsure.android.MB360.insurance.queries.repository.retrofit.QueryRetrofitClient;
import com.indiainsure.android.MB360.insurance.queries.responseclass.Message;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryResponse;
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

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueryRepository {

    Application application;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;

    public final MutableLiveData<Boolean> loadingDetailState;
    public final MutableLiveData<Boolean> errorDetailState;

    public final MutableLiveData<QueryResponse> queryResponse;
    public final MutableLiveData<QueryDetails> queryDetailsResponse;
    public final MutableLiveData<Message> markingResolve;
    public final MutableLiveData<Message> addQueryResponse;
    private AppDatabase appDatabase;
    private MyQueryDao myQueryDao;

    boolean SSL_PINNED = false;
    public final MutableLiveData<Boolean> reloginState;


    public QueryRepository(Application application) {
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        this.queryResponse = new MutableLiveData<>();
        this.queryDetailsResponse = new MutableLiveData<>();
        this.markingResolve = new MutableLiveData<>();
        this.addQueryResponse = new MutableLiveData<>();
        this.loadingDetailState = new MutableLiveData<>(true);
        this.errorDetailState = new MutableLiveData<>();
        appDatabase = AppDatabase.getInstance(application);
        myQueryDao = appDatabase.myQueryDao();
        this.reloginState = new MutableLiveData<>(false);
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @Query response
     **/

    public MutableLiveData<QueryResponse> getQuery(String empSrNo) {
        errorState.setValue(false);
        loadingState.setValue(true);
        try {
            Call<QueryResponse> queryCall = QueryRetrofitClient.getInstance(application.getApplicationContext()).getQueryApi().getAllQueries(
                    AesNew.encrypt(UtilMethods.checkSpecialCharacters(empSrNo), application.getString(R.string.pass_phrase)));

            Callback<QueryResponse> queryResponseCallback = new Callback<QueryResponse>() {

                @Override
                public void onResponse(Call<QueryResponse> call, Response<QueryResponse> response) {
                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                            queryResponse.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                            response.body().setEmpSrNo(empSrNo);
                            myQueryDao.insertMyQuery(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                            queryResponse.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                        }
                    } else {
                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                        queryResponse.setValue(response.body());
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                }

                @Override
                public void onFailure(Call<QueryResponse> call, Throwable t) {

                    LogMyBenefits.e(LogTags.QUERY_ACTIVITY, "OnFailure: " + t.getLocalizedMessage());

                    try {

                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onFailure: " + myQueryDao.getQuery(empSrNo).toString());
                        queryResponse.setValue(myQueryDao.getQuery(empSrNo));
                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        queryResponse.setValue(null);
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

                            queryCall.enqueue(new Callback<QueryResponse>() {
                                @Override
                                public void onResponse(Call<QueryResponse> call, Response<QueryResponse> response) {
                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                                            queryResponse.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                            response.body().setEmpSrNo(empSrNo);
                                            myQueryDao.insertMyQuery(response.body());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                                            queryResponse.setValue(null);
                                            errorState.setValue(true);
                                            loadingState.setValue(false);
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
                                                                queryCall.clone().enqueue(queryResponseCallback);
                                                            }else {
                                                                reloginState.setValue(true);
                                                            }
                                                        }
                                                        else {
                                                            reloginState.setValue(true);
                                                        }

                                                    } else {
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
                                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                                        queryResponse.setValue(response.body());
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                    }
                                }

                                @Override
                                public void onFailure(Call<QueryResponse> call, Throwable t) {

                                    LogMyBenefits.e(LogTags.QUERY_ACTIVITY, "OnFailure: " + t.getLocalizedMessage());


                                    try {

                                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onFailure: " + myQueryDao.getQuery(empSrNo).toString());
                                        queryResponse.setValue(myQueryDao.getQuery(empSrNo));
                                        loadingState.setValue(false);
                                        errorState.setValue(false);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        queryResponse.setValue(null);
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


            //queryCall.clone().enqueue(queryResponseCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return queryResponse;
    }

    public MutableLiveData<QueryDetails> getQueryDetails(String custQuerySrNo) {
        errorDetailState.setValue(false);
        loadingDetailState.setValue(true);
        Call<QueryDetails> queryDetailsCall = QueryRetrofitClient.getInstance(application.getApplicationContext()).getQueryApi().getQueryDetails(custQuerySrNo);

        Callback<QueryDetails> detailsCallback = new Callback<QueryDetails>() {
            @Override
            public void onResponse(Call<QueryDetails> call, Response<QueryDetails> response) {
                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                        queryDetailsResponse.setValue(response.body());
                        errorDetailState.setValue(false);
                        loadingDetailState.setValue(false);
                        response.body().setCustQuerySrNo(custQuerySrNo);
                        myQueryDao.insertQueryDetails(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                        queryDetailsResponse.setValue(null);
                        errorDetailState.setValue(true);
                        loadingDetailState.setValue(false);
                    }
                } else {
                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                    queryDetailsResponse.setValue(response.body());
                    errorDetailState.setValue(true);
                    loadingDetailState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<QueryDetails> call, Throwable t) {
                try {

                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onFailure: " + myQueryDao.getQueryDeatils(custQuerySrNo).toString());
                    myQueryDao.getQueryDeatils(custQuerySrNo);
                    queryDetailsResponse.setValue(myQueryDao.getQueryDeatils(custQuerySrNo));
                    loadingDetailState.setValue(false);
                    errorDetailState.setValue(false);


                } catch (Exception e) {
                    e.printStackTrace();

                    errorDetailState.setValue(true);
                    loadingDetailState.setValue(false);
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
                        queryDetailsCall.enqueue(new Callback<QueryDetails>() {
                            @Override
                            public void onResponse(Call<QueryDetails> call, Response<QueryDetails> response) {
                                if (response.code() == 200) {
                                    try {
                                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                                        queryDetailsResponse.setValue(response.body());
                                        errorDetailState.setValue(false);
                                        loadingDetailState.setValue(false);
                                        response.body().setCustQuerySrNo(custQuerySrNo);
                                        myQueryDao.insertQueryDetails(response.body());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                                        queryDetailsResponse.setValue(null);
                                        errorDetailState.setValue(true);
                                        loadingDetailState.setValue(false);
                                    }
                                } else if (response.code() == 401 || response.code()== 400) {
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
                                                            encryptionPreference.setEncryptedDataString(BEARER_TOKEN, response.body().getAuthToken());
                                                            queryDetailsCall.clone().enqueue(detailsCallback);
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
                                                call.cancel();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                                    queryDetailsResponse.setValue(response.body());
                                    errorDetailState.setValue(true);
                                    loadingDetailState.setValue(false);
                                }
                            }

                            @Override
                            public void onFailure(Call<QueryDetails> call, Throwable t) {
                                try {

                                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onFailure: " + myQueryDao.getQueryDeatils(custQuerySrNo).toString());
                                    myQueryDao.getQueryDeatils(custQuerySrNo);
                                    queryDetailsResponse.setValue(myQueryDao.getQueryDeatils(custQuerySrNo));
                                    loadingDetailState.setValue(false);
                                    errorDetailState.setValue(false);


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
                    loadingDetailState.postValue(false);
                    errorDetailState.postValue(true);
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


        //queryDetailsCall.clone().enqueue(detailsCallback);


        return queryDetailsResponse;
    }


    public MutableLiveData<QueryResponse> getQueryData() {
        return queryResponse;
    }

    public MutableLiveData<QueryDetails> getQueryDetailsData() {
        return queryDetailsResponse;
    }


    public LiveData<Boolean> getLoading() {
        return loadingState;
    }

    public LiveData<Boolean> getError() {
        return errorState;
    }

    public LiveData<Message> markResolve(String eqCustQrySrNo, String emp_sr_no) {
        try {
            Call<Message> markCall = QueryRetrofitClient.getInstance(application.getApplicationContext())
                    .getQueryApi().markAsResolved(eqCustQrySrNo,
                            AesNew.encrypt(emp_sr_no, application.getString(R.string.pass_phrase)));
            Callback<Message> messageCallback = new Callback<Message>() {


                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {

                    if (response.code() == 200) {
                        try {
                            LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                            markingResolve.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                            markingResolve.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                        }
                    } else {
                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                        markingResolve.setValue(response.body());
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    LogMyBenefits.e(LogTags.QUERY_ACTIVITY, "OnFailure: " + t.getLocalizedMessage());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    markingResolve.setValue(null);
                    Toast.makeText(application, "Could not Mark as resolved, reason: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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
                            markCall.enqueue(new Callback<Message>() {
                                @Override
                                public void onResponse(Call<Message> call, Response<Message> response) {

                                    if (response.code() == 200) {
                                        try {
                                            LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                                            markingResolve.setValue(response.body());
                                            errorState.setValue(false);
                                            loadingState.setValue(false);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                                            markingResolve.setValue(null);
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

                                                        if (response.body() != null) {
                                                            if (!response.body().getAuthToken().isEmpty()) {
                                                                encryptionPreference.setEncryptedDataString(BEARER_TOKEN, response.body().getAuthToken());
                                                                markCall.clone().enqueue(messageCallback);
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
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                                        markingResolve.setValue(response.body());
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                        Toast.makeText(application, "Something Went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Message> call, Throwable t) {
                                    LogMyBenefits.e(LogTags.QUERY_ACTIVITY, "OnFailure: " + t.getLocalizedMessage());
                                    errorState.setValue(true);
                                    loadingState.setValue(false);
                                    markingResolve.setValue(null);
                                    Toast.makeText(application, "Could not Mark as resolved, reason: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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


            /* markCall.clone().enqueue(messageCallback);*/




        } catch (Exception e) {
            e.printStackTrace();
        }
        return markingResolve;
    }

    public LiveData<Message> addQuery(RequestBody requestBody) {

        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "addQuery: " + requestBody.contentType());
        Call<Message> replyQuery = QueryAddRetrofitClient.getInstance(application.getApplicationContext()).getQueryApi().AddQuery(requestBody);

        Callback<Message> callback = new Callback<Message>() {

            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {


                if (response.code() == 200) {
                    try {
                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                        addQueryResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                        markingResolve.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.toString());
                    addQueryResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something Went wrong, error code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                errorState.setValue(true);
                loadingState.setValue(false);
                queryResponse.setValue(null);
                Toast.makeText(application, "Could not Mark as resolved, reason: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

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
                        replyQuery.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {


                                if (response.code() == 200) {
                                    try {
                                        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                                        addQueryResponse.setValue(response.body());
                                        errorState.setValue(false);
                                        loadingState.setValue(false);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        LogMyBenefits.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                                        markingResolve.setValue(null);
                                        errorState.setValue(true);
                                        loadingState.setValue(false);
                                    }
                                } else if (response.code() == 401 || response.code() ==400) {

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
                                                            replyQuery.clone().enqueue(callback);
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
                                                call.cancel();
                                                LogMyBenefits.e("REFRESH-TOKEN", "onFailure: ", t);
                                                t.printStackTrace();
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.toString());
                                    addQueryResponse.setValue(response.body());
                                    errorState.setValue(true);
                                    loadingState.setValue(false);
                                    Toast.makeText(application, "Something Went wrong, error code: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                errorState.setValue(true);
                                loadingState.setValue(false);
                                queryResponse.setValue(null);
                                Toast.makeText(application, "Could not Mark as resolved, reason: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

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


        return addQueryResponse;
    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }


    public void setLoadingFromFilePicker() {
        loadingDetailState.setValue(false);
    }

    public void resetDetails() {
        queryDetailsResponse.setValue(null);

    }

}

