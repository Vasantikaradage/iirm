package com.indiainsure.android.MB360.insurance.escalations.repository.retrofit;

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

public class EscalationsRetrofitClient {

    private static EscalationsRetrofitClient instance;
    EscalationApi escalationApi;

    public EscalationsRetrofitClient(Context context) {
        // constructor
        EncryptionPreference encryptionPreference = new EncryptionPreference(context);
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
                                .header("Authorization",
                                        "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.ESCALATION_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        escalationApi = retrofit.create(EscalationApi.class);

    }

    public static synchronized EscalationsRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new EscalationsRetrofitClient(context);
        }
        return instance;
    }

    public EscalationApi getEscalationApi() {
        return escalationApi;
    }
}
