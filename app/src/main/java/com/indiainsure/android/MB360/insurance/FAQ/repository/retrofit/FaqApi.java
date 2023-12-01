package com.indiainsure.android.MB360.insurance.FAQ.repository.retrofit;

import com.indiainsure.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FaqApi {

    @GET("Faqs/Get_FaqsDetails")
    Call<FaqResponse> getFaq(
            @Query("grpchildsrno") String grpChildSrNo,
            @Query("oegrpbasinfosrno") String oeGrpBasInfoSrNo
    );
}
