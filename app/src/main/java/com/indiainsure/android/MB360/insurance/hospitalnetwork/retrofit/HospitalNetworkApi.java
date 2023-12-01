package com.indiainsure.android.MB360.insurance.hospitalnetwork.retrofit;

import com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass.DocumentElementCount;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass.ProviderNetworkData;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalCountResponse;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HospitalNetworkApi {
    @POST("LoadProviders")
    Call<ProviderNetworkData> getHospitals(@Body RequestBody dataRequest);

    //count
    @GET("GetProvidersCount/{groupChildSrNo}/{oeGrpBBasInfoSrNo}/All")
    Call<DocumentElementCount> getHospitalsCount(@Path("groupChildSrNo") String groupChildSrNo,
                                                 @Path("oeGrpBBasInfoSrNo") String oeGrpBBasInfoSrNo);


    @GET("EnrollmentDetails/LoadProviders")
    Call<HospitalResponse> getHospitalsData(@Query("groupchildsrno") String groupchildsrno,
                                            @Query("oegrpbasinfsrno") String oegrpbasinfsrno,
                                            @Query("hospitalsearchtext") String searchtecxt);


    //count
    @GET("EnrollmentDetails/GetProvidersCount")
    Call<HospitalCountResponse> getHospitalsCountData(@Query("groupchildsrno") String groupchildsrno,
                                                      @Query("oegrpbasinfsrno") String oegrpbasinfsrno,
                                                      @Query("hospitalsearchtext") String searchtext);


}
