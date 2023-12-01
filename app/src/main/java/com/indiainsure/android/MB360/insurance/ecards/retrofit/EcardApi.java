package com.indiainsure.android.MB360.insurance.ecards.retrofit;



import com.indiainsure.android.MB360.insurance.ecards.reponseclass.EcardResponse;
import com.indiainsure.android.MB360.insurance.ecards.reponseclass.EcardResponseJson;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface EcardApi {
    @POST("GetEcard")
    Call<EcardResponse> getEcard(@Body RequestBody requestBody);

    @GET("EnrollmentDetails/GetEcard")
    Call<EcardResponseJson> getEcardJSON(@QueryMap(encoded = false) Map<String, String> ecardOptions);

}
