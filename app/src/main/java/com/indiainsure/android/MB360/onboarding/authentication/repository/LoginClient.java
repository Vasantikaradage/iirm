package com.indiainsure.android.MB360.onboarding.authentication.repository;

import static com.indiainsure.android.MB360.BuildConfig.BASE_URL;

import com.appmattus.certificatetransparency.CTInterceptorBuilder;
import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import java.security.cert.Certificate;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginClient {

    private static LoginClient instance;
    private LoginApi loginApi;
    Certificate ca;
    boolean SSL_PIN = false;

    private LoginClient() { //constructor

        CertificatePinner certPinner = new CertificatePinner.Builder()
                .add(BuildConfig.DOMAIN_STAR,
                        BuildConfig.CERT_256).build();


        CTInterceptorBuilder builderCT = new CTInterceptorBuilder()
                .includeHost("*.mybenefits360.com")
                .setFailOnError(true);

        Interceptor networkInterceptor = builderCT.build();

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(networkInterceptor)
                .certificatePinner(certPinner)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = null;
                    try {
                        builder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json");


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.LOGIN_ACTIVITY, newRequest.url().toString());
                    return chain.proceed(newRequest);

                }).readTimeout(12, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();


        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        loginApi = retrofit.create(LoginApi.class);

    }

    public static synchronized LoginClient getInstance() {
        if (instance == null) {
            instance = new LoginClient();
        }
        return instance;

    }

    public LoginApi getLoginApi() {
        return loginApi;
    }

}
