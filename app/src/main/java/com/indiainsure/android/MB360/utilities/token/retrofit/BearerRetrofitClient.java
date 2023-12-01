package com.indiainsure.android.MB360.utilities.token.retrofit;

import static com.indiainsure.android.MB360.BuildConfig.BASE_URL;

import android.content.Context;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BearerRetrofitClient {

    private static BearerRetrofitClient instance;
    private BearerApi bearerApi;
    private EncryptionPreference encryptionPreference;


    public BearerRetrofitClient(Context context) {
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
                        /* .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));*/
                               /* .header("Authorization",
                                        Credentials.basic(BASIC_AUTH_USERNAME_OLD, BASIC_AUTH_PASSWORD_OLD));*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Request newRequest = builder.build();
                    LogMyBenefits.d("REFRESH TOKEN", newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).readTimeout(12, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        bearerApi = retrofit.create(BearerApi.class);


    }

    public static synchronized BearerRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new BearerRetrofitClient(context);
        }
        return instance;
    }

    public BearerApi getBearerApi() {
        return bearerApi;
    }

}

