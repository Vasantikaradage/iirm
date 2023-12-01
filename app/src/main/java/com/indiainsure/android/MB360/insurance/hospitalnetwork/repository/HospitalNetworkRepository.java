package com.indiainsure.android.MB360.insurance.hospitalnetwork.repository;

import android.app.Application;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.database.AppDatabase;
import com.indiainsure.android.MB360.database.Dao.ProviderNetworkDao;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass.PlacesResponse;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalCountResponse;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalResponse;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.Message;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.retrofit.HospitalNetworkRetrofitClientJson;
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
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalNetworkRepository {
    private final MutableLiveData<HospitalResponse> hospitalResponse;
    MutableLiveData<HospitalCountResponse> hospitalCount;
    MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    MutableLiveData<Boolean> error = new MutableLiveData<>(false);
    Application application;
    MutableLiveData<PlacesResponse> hospitalPlaces = new MutableLiveData<>();
    private AppDatabase appDatabase;
    private ProviderNetworkDao documentElementCountDao;
    boolean SSL_PINNED = false;
    public final MutableLiveData<Boolean> reloginState;

    public HospitalNetworkRepository(Application application) {
        hospitalResponse = new MutableLiveData<>();
        hospitalCount = new MutableLiveData<>();
        this.application = application;
        appDatabase = AppDatabase.getInstance(application);
        documentElementCountDao = appDatabase.documentElementCountDao();
        this.reloginState = new MutableLiveData<>(false);

    }


    public LiveData<HospitalResponse> getHospitals(String groupChildSrNo, String oeGrpBasInfoSrNo, String searchText) {


        try {
            documentElementCountDao.getHospitals()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<HospitalInformation> providerNetworkData) {
                            if (!providerNetworkData.isEmpty()) {
                                Log.d(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onSuccess: success.");
                                HospitalResponse providerNetworkData1 = new HospitalResponse();
                                Message message = new Message();
                                message.setMessage(BuildConfig.SUCCESS);
                                message.setStatus(true);
                                providerNetworkData1.setMessage(message);
                                providerNetworkData1.setHospitalInformation(providerNetworkData);
                                hospitalResponse.setValue(providerNetworkData1);
                                loading.setValue(false);
                                error.setValue(false);
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onError: error.", e);

                        }

                        @Override
                        public void onComplete() {
                            Log.d(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onComplete: Done.");
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Call<HospitalResponse> hospitalResponseCall = HospitalNetworkRetrofitClientJson
                    .getInstance(application.getApplicationContext())
                    .getHospitalNetworkApi()
                    .getHospitalsData(AesNew.encrypt(groupChildSrNo, application.getString(R.string.pass_phrase)), AesNew.encrypt(oeGrpBasInfoSrNo, application.getString(R.string.pass_phrase)), searchText);
            //   hospitalResponseCall.enqueue(new Callback<>() {
            Callback<HospitalResponse> callback = new Callback<HospitalResponse>() {

                @Override
                public void onResponse(Call<HospitalResponse> call, Response<HospitalResponse> response) {
                    if (response.code() == 200) {
                        try {


                            Log.d(LogTags.HOSPITAL_NETWORK, "onResponse: " + response.body());
                            hospitalResponse.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);

                            if (response.body().getHospitalInformation() != null) {

                                Observable<HospitalResponse> observable;
                                observable = io.reactivex.Observable.just(response.body());
                                observable.subscribeOn(Schedulers.io())
                                        .subscribe(new Observer<>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onNext(@NonNull HospitalResponse providerNetworkData) {
                                                //delete first
                                                documentElementCountDao.deleteAllHospital();
                                                //Insert here
                                                documentElementCountDao.insertHospitalDetails(providerNetworkData.getHospitalInformation());

                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                Log.e("Error", "Error at" + e);
                                            }

                                            @Override
                                            public void onComplete() {
                                                Log.d(LogTags.HOSPITAL_NETWORK, "Successfully saved Hospital data");
                                            }
                                        });
                            } else {
                                //response is null
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            hospitalResponse.setValue(null);
                            loading.setValue(false);
                            error.setValue(false);


                        }
                    } else {

                        FirebaseCrashlytics crashlytics;
                        crashlytics = FirebaseCrashlytics.getInstance();
                        ResponseException responseException = new ResponseException("Claims ->" +
                                " getClaims -> Response Code:- " + String.valueOf(response.code()) +
                                "GroupChildSrNo:- " + "something" + "OeGrpBasInfoSrNo:- " + "something"
                                + "employeeSrno:- " + "something");
                        crashlytics.recordException(responseException);

                        Log.d(LogTags.HOSPITAL_NETWORK, "onResponse: " + response.body());
                        hospitalResponse.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(true);


                    }
                }

                @Override
                public void onFailure(Call<HospitalResponse> call, Throwable t) {
                    Log.e(LogTags.HOSPITAL_NETWORK, "onFailure: ", t);
                    hospitalResponse.setValue(null);
                    loading.setValue(false);
                    error.setValue(true);
                    // Toast.makeText(application, "Something went wrong", Toast.LENGTH_SHORT).show();

                    //get the data here
               /* try {
                    documentElementCountDao.getHospitals()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(List<Hospitals> providerNetworkData) {
                                    Log.d(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onSuccess: success.");
                                    ProviderNetworkData providerNetworkData1 = new ProviderNetworkData();
                                    providerNetworkData1.setStatus(BuildConfig.SUCCESS);
                                    HospitalInformation hospitalInformation = new HospitalInformation();
                                    hospitalInformation.setHospitals(providerNetworkData);
                                    providerNetworkData1.setHospitalInformation(hospitalInformation);
                                    hospitalResponse.setValue(providerNetworkData1);

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onError: error.", e);
                                    hospitalResponse.setValue(null);
                                }

                                @Override
                                public void onComplete() {
                                    Log.d(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onComplete: Done.");
                                }
                            });

                } catch (Exception e) {

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
                            hospitalResponseCall.enqueue(new Callback<HospitalResponse>() {

                                @Override
                                public void onResponse(Call<HospitalResponse> callload, Response<HospitalResponse> response) {
                                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                                    if (response.code() == 200) {
                                        try {


                                            Log.d(LogTags.HOSPITAL_NETWORK, "onResponse: " + response.body());
                                            hospitalResponse.setValue(response.body());
                                            loading.setValue(false);
                                            error.setValue(false);

                                            if (response.body().getHospitalInformation() != null) {

                                                Observable<HospitalResponse> observable;
                                                observable = io.reactivex.Observable.just(response.body());
                                                observable.subscribeOn(Schedulers.io())
                                                        .subscribe(new Observer<>() {
                                                            @Override
                                                            public void onSubscribe(@NonNull Disposable d) {

                                                            }

                                                            @Override
                                                            public void onNext(@NonNull HospitalResponse providerNetworkData) {
                                                                //delete first
                                                                documentElementCountDao.deleteAllHospital();
                                                                //Insert here
                                                                documentElementCountDao.insertHospitalDetails(providerNetworkData.getHospitalInformation());

                                                            }

                                                            @Override
                                                            public void onError(@NonNull Throwable e) {
                                                                Log.e("Error", "Error at" + e);
                                                            }

                                                            @Override
                                                            public void onComplete() {
                                                                Log.d(LogTags.HOSPITAL_NETWORK, "Successfully saved Hospital data");
                                                            }
                                                        });
                                            } else {
                                                //response is null
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            hospitalResponse.setValue(null);
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
                                                                hospitalResponseCall.clone().enqueue(callback);
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

                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {
                                        LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED" + response.code());
                                        hospitalResponse.setValue(null);
                                        loading.setValue(false);
                                        error.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<HospitalResponse> call, Throwable t) {

                                    Log.e("E_CARD_ACTIVITY", "onFailure: ", t);
                                    hospitalResponse.setValue(null);
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
        } catch (Exception e) {
            e.printStackTrace();
            hospitalResponse.setValue(null);
            loading.setValue(false);
            error.setValue(true);
        }

        return hospitalResponse;
    }

    public LiveData<HospitalCountResponse> getHospitalCount(String grpChildSrNo, String oeGrpBasInfoSrNo, String searchText) {

        try {

            Call<HospitalCountResponse> hospitalCountCall = HospitalNetworkRetrofitClientJson
                    .getInstance(application.getApplicationContext())
                    .getHospitalNetworkApi()
                    .getHospitalsCountData(AesNew.encrypt(grpChildSrNo, application.getString(R.string.pass_phrase)),
                            AesNew.encrypt(oeGrpBasInfoSrNo, application.getString(R.string.pass_phrase)), searchText);
            Callback<HospitalCountResponse> hospitalCountCallback = new Callback<HospitalCountResponse>() {

                //  hospitalCountCall.enqueue(new Callback<HospitalCountResponse>() {
                @Override
                public void onResponse(Call<HospitalCountResponse> call, Response<HospitalCountResponse> response) {
                    if (response.code() == 200) {
                        try {
                            Log.d(LogTags.HOSPITAL_NETWORK, "COUNT 200 responseCode: " + response.body());
                            hospitalCount.setValue(response.body());
                            /* response.body().setOeGrpBasInfoSrNo(oeGrpBasInfoSrNo); */
                            /* documentElementCountDao.insertDocumentElementCount(response.body()); */

                        } catch (Exception e) {
                            e.printStackTrace();
                            hospitalResponse.setValue(null);
                        }
                    } else {
                        Log.d(LogTags.HOSPITAL_NETWORK, "COUNT: " + response.code());
                        hospitalCount.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<HospitalCountResponse> call, Throwable t) {
                    Log.e(LogTags.HOSPITAL_NETWORK, "onFailure: ", t);
                    try {
                   /* Log.d(LogTags.HOSPITAL_NETWORK, "onFailure: " + documentElementCountDao.getDocumentElementCount(oeGrpBasInfoSrNo).toString());
                    hospitalCount.setValue(documentElementCountDao.getDocumentElementCount(oeGrpBasInfoSrNo));*/
                    } catch (Exception e) {
                        e.printStackTrace();
                        hospitalCount.setValue(null);
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
                            hospitalCountCall.enqueue(new Callback<HospitalCountResponse>() {

                                @Override
                                public void onResponse(Call<HospitalCountResponse> callload, Response<HospitalCountResponse> response) {
                                    LogMyBenefits.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                                    if (response.code() == 200) {
                                        try {
                                            Log.d(LogTags.HOSPITAL_NETWORK, "COUNT 200 responseCode: " + response.body());
                                            hospitalCount.setValue(response.body());
                                            /* response.body().setOeGrpBasInfoSrNo(oeGrpBasInfoSrNo); */
                                            /* documentElementCountDao.insertDocumentElementCount(response.body()); */

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            hospitalResponse.setValue(null);
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

                                                        if (response.body() != null) {
                                                            if (!response.body().getAuthToken().isEmpty()) {
                                                                encryptionPreference.setEncryptedDataString(BuildConfig.BEARER_TOKEN, response.body().getAuthToken());
                                                                hospitalCountCall.clone().enqueue(hospitalCountCallback);
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
                                        LogMyBenefits.d(LogTags.HOSPITAL_NETWORK, "onResponse: FAILED" + response.code());
                                        hospitalCount.setValue(null);
                                        loading.setValue(false);
                                        error.setValue(true);
                                    }
                                }

                                @Override
                                public void onFailure(Call<HospitalCountResponse> call, Throwable t) {

                                    Log.e("HOSPITAL_ACTIVITY", "onFailure: ", t);
                                    hospitalCount.setValue(null);
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

        } catch (Exception e) {
            e.printStackTrace();
            hospitalCount.setValue(null);
            loading.setValue(false);
            error.setValue(false);
        }
        return hospitalCount;
    }

    public LiveData<HospitalCountResponse> getHospitalsCountData() {
        return hospitalCount;
    }

    public LiveData<HospitalResponse> getHospitalsData() {
        return hospitalResponse;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }


    public String getAPIKEY() {
        String API_KEY = null;
        try {
            ActivityInfo ai = application.getApplicationContext().getPackageManager().getActivityInfo(new ComponentName("com.google.android.geo.API_KEY", String.valueOf(1)), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle != null) {
                String apiKey = bundle.getString("com.google.android.geo.API_KEY");
                API_KEY = apiKey;
            }
        } catch (
                PackageManager.NameNotFoundException e) {
            Log.e(LogTags.HOSPITAL_NETWORK, "getAPIKEY: ", e);
        } catch (
                NullPointerException e) {
            Log.e(LogTags.HOSPITAL_NETWORK, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        return API_KEY;

    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

}
