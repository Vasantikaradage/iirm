package com.indiainsure.android.MB360.insurance.policyfeatures.repository.retrofit;

import com.indiainsure.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PolicyFeaturesApi {

    @GET("PolicyFeatures/GetPolicyFeatures")
    Call<List<PolicyFeaturesResponse>> getPolicyFeatures(
            @Query("grpchildsrno") String grpChildSrNo,
            @Query("oegrpbasinfosrno") String oeGrpBasInfoSrNo,
            @Query("productype") String productType
    );
}
