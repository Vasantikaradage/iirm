package com.indiainsure.android.MB360.insurance.enrollmentstatus.retrofit;

import com.indiainsure.android.MB360.insurance.enrollmentstatus.responseclass.EnrollmentStatus;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EnrollmentStatusApi {

    @POST("EnrollmentStatus/GetEnrollStatus")
    Call<EnrollmentStatus> getEnrollmentStatus(@Query(value = "employeesrno") String employeeSrNo,
                                               @Query(value = "GroupChildSrNo") String groupChildSrNo,
                                               @Query(value = "OeGrpBasInfSrNo") String oeGrpBasInfSrNo);

}
