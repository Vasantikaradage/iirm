package com.indiainsure.android.MB360.insurance.queries.repository.retrofit;

import static com.indiainsure.android.MB360.BuildConfig.BASE_URL;

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

public class QueryRetrofitClient {

    private static QueryRetrofitClient instance;
    QueryApi queryApi;
    private EncryptionPreference encryptionPreference;


    public QueryRetrofitClient(Context context) {//constructor
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

                              /*  .header("Authorization",
                                        Credentials.basic(AesEncryption.encrypt(BASIC_AUTH_USERNAME), AesEncryption.encrypt(BASIC_AUTH_PASSWORD)));
                */    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.QUERY_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();
        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        queryApi = retrofit.create(QueryApi.class);
    }

    public static synchronized QueryRetrofitClient getInstance(Context context
    ) {
        if (instance == null) {
            instance = new QueryRetrofitClient(context);
        }
        return instance;
    }

    public QueryApi getQueryApi() {
        return queryApi;
    }
}
