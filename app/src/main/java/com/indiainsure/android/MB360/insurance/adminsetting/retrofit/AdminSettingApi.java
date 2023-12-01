package com.indiainsure.android.MB360.insurance.adminsetting.retrofit;

import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdminSettingApi {

    @GET("EnrollmentDetails/GetAdminSettings")
    Call<AdminSettingResponse> getAdminSettingData(@Query("grpchildsrno") String grpChildSrNo,
                                                   @Query("oegrpbasinfosrno") String oeGrpBasInfSrNo);
}
