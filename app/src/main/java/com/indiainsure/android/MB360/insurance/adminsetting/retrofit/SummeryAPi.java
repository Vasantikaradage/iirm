package com.indiainsure.android.MB360.insurance.adminsetting.retrofit;

import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.SummaryFileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SummeryAPi { @GET("EmployeeEnrollement/EnrollmentSummaryFile?")
Call<SummaryFileResponse> getSummaryDetails(@Query("EmpSrNo") String empSrNo);

}

