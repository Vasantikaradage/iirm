package com.indiainsure.android.MB360.insurance.hospitalnetwork.retrofit;

import android.content.Context;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HospitalNetworkRetrofitClientJson {
    private static HospitalNetworkRetrofitClientJson instance;
    HospitalNetworkApi hospitalNetworkApi;
    private EncryptionPreference encryptionPreference;


    public HospitalNetworkRetrofitClientJson(Context context) {
        // constructor
        //getting the token
        encryptionPreference = new EncryptionPreference(context);

        //ssl pinning
        CertificatePinner certPinner = new CertificatePinner.Builder()
                .add(BuildConfig.DOMAIN_STAR,
                        BuildConfig.CERT_256).build();


        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certPinner)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = null;
                    try {
                        builder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                        LogMyBenefits.d("==TOKEN==", encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.HOSPITAL_NETWORK, newRequest.url().toString());


                    return chain.proceed(newRequest);
                }).readTimeout(15, TimeUnit.MINUTES)
                .build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_NETWORK_HOSPITAL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        hospitalNetworkApi = retrofit.create(HospitalNetworkApi.class);

    }

    public static synchronized HospitalNetworkRetrofitClientJson getInstance(Context context) {
        if (instance == null) {
            instance = new HospitalNetworkRetrofitClientJson(context);
        }
        return instance;
    }

    public HospitalNetworkApi getHospitalNetworkApi() {
        return hospitalNetworkApi;
    }
}


