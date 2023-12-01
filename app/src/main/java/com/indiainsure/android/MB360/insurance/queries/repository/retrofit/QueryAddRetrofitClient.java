package com.indiainsure.android.MB360.insurance.queries.repository.retrofit;

import static com.indiainsure.android.MB360.BuildConfig.BASE_URL;

import android.content.Context;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QueryAddRetrofitClient {
    private static QueryAddRetrofitClient instance;
    QueryApi queryApi;

    public QueryAddRetrofitClient(Context context) {
        EncryptionPreference encryptionPreference = new EncryptionPreference(context);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = null;
                    try {
                        builder = originalRequest.newBuilder()
                                // .addHeader("Content-Type", "multipart/form-data")
                                .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                    } catch (Exception e) {
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

    public static synchronized QueryAddRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new QueryAddRetrofitClient(context);
        }
        return instance;
    }

    public QueryApi getQueryApi() {
        return queryApi;
    }
}
