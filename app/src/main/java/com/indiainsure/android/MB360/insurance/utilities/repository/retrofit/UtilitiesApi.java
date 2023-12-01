package com.indiainsure.android.MB360.insurance.utilities.repository.retrofit;

import com.indiainsure.android.MB360.insurance.utilities.repository.responseclass.UtilitiesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UtilitiesApi {

    @GET("Utilities/Get_UtilitiesDetails")
    Call<UtilitiesResponse> getUtilities(
            @Query("grpchildsrno") String grpChildSrNo,
            @Query("oegrpbasinfosrno") String oeGrpBasInfSrNo

    );
}
