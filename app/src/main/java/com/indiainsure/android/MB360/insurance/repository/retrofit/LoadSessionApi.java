package com.indiainsure.android.MB360.insurance.repository.retrofit;

import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoadSessionApi {

    @POST("Login/LoadSessionValues")
    Call<LoadSessionResponse> loadSessionWithPhoneNumber(
            @Query("mobileno") String phoneNumber
    );
    @POST("Login/LoadSessionValueswithOTP")
    Call<LoadSessionResponse> loadSessionWithPhoneNumber(
            @Query("mobileno") String phoneNumber,
            @Query("OTP") String OTP
    );

  /*  @POST("Login/LoadSessionValues")
    Call<LoadSessionResponse> loadSessionWithEmail(
            @Body String emailId
    );*/
  @POST("Login/LoadSessionValuesWithEmailD")
  Call<LoadSessionResponse> loadSessionWithEmail(
          @Query ("EmailId")String emailId
  );


    @POST("Login/LoadSessionValueswithOTP")
    Call<LoadSessionResponse> loadSessionWithEmail(
            @Body String emailId,
            @Query("OTP") String OTP
    );

    @POST("Login/LoadSessionByUniqueID")
    Call<LoadSessionResponse> loadSessionWithID(
            @Query("LoginId") String loginId
    );

}
