package com.indiainsure.android.MB360.insurance.myclaims.retrofit;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MyClaimsRetrofitClient {
    private static MyClaimsRetrofitClient instance;
    MyClaimsApi myClaimsApi;


    public MyClaimsRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/xml");


                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.CLAIM_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
        myClaimsApi = retrofit.create(MyClaimsApi.class);

    }

    public static synchronized MyClaimsRetrofitClient getInstance() {
        if (instance == null) {
            instance = new MyClaimsRetrofitClient();
        }
        return instance;
    }

    public MyClaimsApi getClaimsApi() {
        return myClaimsApi;
    }
}
