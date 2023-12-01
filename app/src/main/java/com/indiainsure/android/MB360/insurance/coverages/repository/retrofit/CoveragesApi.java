package com.indiainsure.android.MB360.insurance.coverages.repository.retrofit;

import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoveragesApi {

    @GET("PolicyCoverages/GetCoveragePolicyData")
    Call<CoverageResponse> getCoveragesPolicyData(
            @Query("GroupChildSrNo") String groupChildSrNo,
            @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo
    );


    @GET("PolicyCoverages/GetPolicyCoveragesDetails")
    Call<CoverageDetailsResponse> getPolicyCoveragesDetails(
            @Query("GroupChildSrNo") String groupChildSrNo,
            @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo,
            @Query("ProductType") String productType,
            @Query("EmployeeSrNo") String employeeSrNo ,
            @Query("EmployeeSrNoGMC") String EmployeeSrNoGMC
    );
}
