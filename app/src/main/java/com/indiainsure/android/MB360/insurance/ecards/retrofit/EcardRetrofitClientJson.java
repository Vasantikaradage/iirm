package com.indiainsure.android.MB360.insurance.ecards.retrofit;

import android.content.Context;
import android.util.Log;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EcardRetrofitClientJson {
    private static EcardRetrofitClientJson instance;
    EcardApi ecardApi;
    private EncryptionPreference encryptionPreference;

    public EcardRetrofitClientJson(Context context) {//constructor


        //getting the token
        encryptionPreference = new EncryptionPreference(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));


                    Request newRequest = builder.build();
                    Log.d("E_CARD_ACTIVITY", newRequest.url().toString());

                    return chain.proceed(newRequest);
                })
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.E_CARD_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ecardApi = retrofit.create(EcardApi.class);
    }

    public static synchronized EcardRetrofitClientJson getInstance(Context context) {
        if (instance == null) {
            instance = new EcardRetrofitClientJson(context);
        }
        return instance;
    }


    public EcardApi getEcardApi() {
        return ecardApi;
    }

}
