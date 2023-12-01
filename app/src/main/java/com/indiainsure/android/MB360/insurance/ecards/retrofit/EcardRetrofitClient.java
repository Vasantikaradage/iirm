package com.indiainsure.android.MB360.insurance.ecards.retrofit;



import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class EcardRetrofitClient {
    private static EcardRetrofitClient instance;
    EcardApi ecardApi;

    public EcardRetrofitClient() {//constructor
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/xml");


                    Request newRequest = builder.build();

                    LogMyBenefits.d(LogTags.E_CARD_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.E_CARD_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
        ecardApi = retrofit.create(EcardApi.class);
    }

    public static synchronized EcardRetrofitClient getInstance() {
        if (instance == null) {
            instance = new EcardRetrofitClient();
        }
        return instance;
    }


    public EcardApi getEcardApi() {
        return ecardApi;
    }

}
