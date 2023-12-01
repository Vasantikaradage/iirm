package com.indiainsure.android.MB360.utilities.token.retrofit;

import com.indiainsure.android.MB360.utilities.token.responseclasses.AuthToken;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BearerApi {

    @GET("Login/RefreshUserToken")
    Call<AuthToken> refreshToken(@Query("usrno1") String emp_sr_no,
                                 @Query("usrno2") String person_sr_no,
                                 @Query("usrno3") String emp_id);

}
