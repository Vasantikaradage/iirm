package com.indiainsure.android.MB360.insurance.myclaims.retrofit;

import android.content.Context;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyClaimsRetrofitClientJson {
    private static MyClaimsRetrofitClientJson instance;
    MyClaimsApi myClaimsApi;

    private EncryptionPreference encryptionPreference;
    public MyClaimsRetrofitClientJson(Context context) {
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

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));

                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        myClaimsApi = retrofit.create(MyClaimsApi.class);

    }

    public static synchronized MyClaimsRetrofitClientJson getInstance(Context context) {
        if (instance == null) {
            instance = new MyClaimsRetrofitClientJson(context);
        }
        return instance;
    }

    public MyClaimsApi getClaimsApi() {
        return myClaimsApi;
    }
}
