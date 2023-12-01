package com.indiainsure.android.MB360.insurance.escalations.repository.retrofit;

import com.indiainsure.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EscalationApi {
    @GET("Escalation/GetGroupEscalationInfo")
    Call<EscalationsResponse> getEscalations(@Query("GroupChildSrNo") String grpChildSrNo,
                                             @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo);
}
