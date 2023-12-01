package com.indiainsure.android.MB360.insurance.enrollmentstatus.retrofit;

import android.content.Context;
import android.util.Log;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnrollmentStatusRetrofitClient {
    private static EnrollmentStatusRetrofitClient instance;
    EnrollmentStatusApi enrollmentStatusApi;
    private EncryptionPreference encryptionPreference;

    public EnrollmentStatusRetrofitClient(Context context) {//constructor


        //getting the token
        encryptionPreference = new EncryptionPreference(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));


                    Request newRequest = builder.build();
                    LogMyBenefits.d("ENROLLMENT-STATUS", newRequest.url().toString());

                    return chain.proceed(newRequest);
                })
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.E_CARD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        enrollmentStatusApi = retrofit.create(EnrollmentStatusApi.class);
    }

    public static synchronized EnrollmentStatusRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new EnrollmentStatusRetrofitClient(context);
        }
        return instance;
    }


    public EnrollmentStatusApi getEnrollmentStatusApi() {
        return enrollmentStatusApi;
    }

}
