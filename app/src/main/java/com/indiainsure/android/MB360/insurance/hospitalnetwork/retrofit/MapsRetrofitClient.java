package com.indiainsure.android.MB360.insurance.hospitalnetwork.retrofit;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapsRetrofitClient {
    private static MapsRetrofitClient instance;
    GoogleMapsApiClient googleMapsApiClient;


    public MapsRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder();
                            /*  .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(AppServerConstants.BASIC_AUTH_USERNAME, AppServerConstants.BASIC_AUTH_PASSWORD));
*/
                    Request newRequest = builder.build();
                    LogMyBenefits.d(LogTags.HOSPITAL_NETWORK, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_MAPS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        googleMapsApiClient = retrofit.create(GoogleMapsApiClient.class);

    }

    public static synchronized MapsRetrofitClient getInstance() {
        if (instance == null) {
            instance = new MapsRetrofitClient();
        }
        return instance;
    }

    public GoogleMapsApiClient getGoogleMapsApiClient() {
        return googleMapsApiClient;
    }
}
