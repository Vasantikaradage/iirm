package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.retrofit;

import static com.indiainsure.android.MB360.BuildConfig.BASE_URL;
import static com.indiainsure.android.MB360.BuildConfig.DOWNLOAD_BASE_URL;

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
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ClaimProcedureRetrofitClient {

    private static ClaimProcedureRetrofitClient instance;
    private ClaimProcedureApi claimProcedureApi;
    EncryptionPreference encryptionPreference;

    private ClaimProcedureRetrofitClient(Context context) {
        //constructor

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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();


        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        claimProcedureApi = retrofit.create(ClaimProcedureApi.class);
    }

    private ClaimProcedureRetrofitClient(Boolean Download, Context context) {
        //constructor
        encryptionPreference = new EncryptionPreference(context);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = null;
                    try {
                        builder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .header("Authorization", "Bearer " + encryptionPreference.getEncryptedDataToken(BuildConfig.BEARER_TOKEN));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();


        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DOWNLOAD_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        claimProcedureApi = retrofit.create(ClaimProcedureApi.class);
    }

    public static synchronized ClaimProcedureRetrofitClient getInstance(Boolean download, Context context) {

        if (!download) {
            instance = new ClaimProcedureRetrofitClient(context);
        } else {
            instance = new ClaimProcedureRetrofitClient(true, context);
        }

        return instance;

    }

    public ClaimProcedureApi getClaimProcedureClient() {
        return claimProcedureApi;
    }
}


