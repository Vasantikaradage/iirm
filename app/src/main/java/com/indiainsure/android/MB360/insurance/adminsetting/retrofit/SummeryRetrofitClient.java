package com.indiainsure.android.MB360.insurance.adminsetting.retrofit;

import static com.indiainsure.android.MB360.BuildConfig.BASE_URL;

import android.content.Context;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;

import okhttp3.CertificatePinner;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SummeryRetrofitClient {private static SummeryRetrofitClient instance;
    SummeryAPi summeryAPi;
    private EncryptionPreference encryptionPreference;

    public SummeryRetrofitClient(Context context) {
        // constructor
        encryptionPreference = new EncryptionPreference(context);
        CertificatePinner certPinner = new CertificatePinner.Builder()
                .add(BuildConfig.DOMAIN,
                        BuildConfig.CERT_256).build();

        OkHttpClient client = new OkHttpClient.Builder()
                .certificatePinner(certPinner)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = null;
                    try {
                        builder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                /*.header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));*/
                                .header("Authorization",
                                        Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Request newRequest = builder.build();
                    LogMyBenefits.d("", newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        summeryAPi = retrofit.create(SummeryAPi.class);

    }

    public static synchronized SummeryRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new SummeryRetrofitClient(context);
        }
        return instance;
    }

    public SummeryAPi getMyApi() {
        return summeryAPi;
    }
}

  /*  private static SummeryRetrofitClient instance = null;
    private SummeryAPi myApi;

    private SummeryRetrofitClient(Context context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic("Common", "Password"));

                    Request newRequest = builder.build();

                    Log.d("URL", newRequest.url().toString());
                    return chain.proceed(newRequest);
                }).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        myApi = retrofit.create(SummeryAPi.class);
    }

    public static synchronized SummeryRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new SummeryRetrofitClient(context);
        }
        return instance;
    }

    public SummeryAPi getMyApi() {
        return myApi;
    }
}*/
