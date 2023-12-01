package com.indiainsure.android.MB360.insurance.queries.repository.retrofit;

import com.indiainsure.android.MB360.insurance.queries.responseclass.Message;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface QueryApi {

    @GET("EmployeeQueries/GetAllEmployeeQueries")
    Call<QueryResponse> getAllQueries(
            @Query("EmpSrNo") String empSrNo
    );

    @GET("EmployeeQueries/GetSpecificQueryDetails")
    Call<QueryDetails> getQueryDetails(
            @Query("CustQuerySrNo") String custQuerySrNo);

    @POST("query/MarkedSolved")
    Call<Message> markAsResolved(
            @Query("CustQuerySrNo") String custQuerySrNo,
            @Query("usrno1") String emp_sr_no);



    @POST("EmployeeQueries/PostQueries")
    Call<Message> AddQuery(@Body RequestBody requestBody);


    @Multipart()
    @POST("EmployeeQueries/PostQueries")
    Call<Message> AddReply(@Part(value = "QueryData") RequestBody requestBody);

}
