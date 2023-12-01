package com.indiainsure.android.MB360.insurance.utilities.repository.retrofit;

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

public class UtilitiesRetrofitClient {

    private static UtilitiesRetrofitClient instance;
    UtilitiesApi utilitiesApi;
    private EncryptionPreference encryptionPreference;




    public UtilitiesRetrofitClient(Context context) {//constructor
        //getting the token
        encryptionPreference = new EncryptionPreference(context);
        //ssl pinning
        CertificatePinner certPinner = new CertificatePinner.Builder()
                .add(BuildConfig.DOMAIN_STAR,
                        BuildConfig.CERT_256).build();



      /*  CertificatePinner certPinner = new CertificatePinner.Builder()
                .add("*.mybenefits360.com",
                        "sha256/hmASn+xDTVHu9w0zvEm4/YnQlS6sxscouLLJNctgRJY=").build();*/
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
                                        Credentials.basic(AesEncryption.encrypt(BASIC_AUTH_USERNAME), AesEncryption.encrypt(BASIC_AUTH_PASSWORD)));*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.UTILITIES_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        utilitiesApi = retrofit.create(UtilitiesApi.class);

    }

    public static synchronized UtilitiesRetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new UtilitiesRetrofitClient(context);
        }
        return instance;

    }

    public UtilitiesApi getUtilitiesApi() {
        return utilitiesApi;
    }

}
