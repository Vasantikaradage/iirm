package com.indiainsure.android.MB360.insurance.hospitalnetwork.retrofit;

import com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsApiClient {

    @GET("maps/api/place/textsearch/json")
    Call<PlacesResponse> getHospital(
            @Query(value = "query", encoded = true) String query,
            @Query("key") String apiKey
    );


}
