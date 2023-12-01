package com.indiainsure.android.MB360.insurance.hospitalnetwork.retrofit;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class HospitalNetworkRetrofitClient {
    private static HospitalNetworkRetrofitClient instance;
    HospitalNetworkApi hospitalNetworkApi;


    public HospitalNetworkRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/xml");


                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.HOSPITAL_NETWORK, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).readTimeout(3, TimeUnit.MINUTES)
                .build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
        hospitalNetworkApi = retrofit.create(HospitalNetworkApi.class);

    }

    public static synchronized HospitalNetworkRetrofitClient getInstance() {
        if (instance == null) {
            instance = new HospitalNetworkRetrofitClient();
        }
        return instance;
    }

    public HospitalNetworkApi getHospitalNetworkApi() {
        return hospitalNetworkApi;
    }
}


