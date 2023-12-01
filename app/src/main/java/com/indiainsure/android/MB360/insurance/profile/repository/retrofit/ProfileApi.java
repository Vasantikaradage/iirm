package com.indiainsure.android.MB360.insurance.profile.repository.retrofit;

import com.indiainsure.android.MB360.insurance.profile.response.Message;
import com.indiainsure.android.MB360.insurance.profile.response.ProfileResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProfileApi {
    @GET("UserProfile/loaduserprofiledetails")
    Call<ProfileResponse> getProfile(@Query("grpchildsrno") String grpChildSrNo,
                                     @Query("oegrpbasinfosrno") String oeGrpBasInfoSrNo,
                                     @Query("empsrno") String empSrNo);


    @POST("UserProfile/AddUpdateSocialNetwork")
    Call<Message> uploadSocialNetwork(
            @Query("empsrno") String EmpSrno, @Query("oebrno") String strOeGrpBasInfSrNo,
            @Query("groupchildsrno") String grpchildsrno, @Query("strFacebook") String strFacebook,
            @Query("strTwitter") String strTwitter, @Query("strLinkedin") String strLinkedin,
            @Query("strInstagram") String strInstagram);

    // used in form-data
   /* @Multipart
    @POST("UserProfile/AddEmployeeDocuments")
    Call<Message> uploadDocuments(
            @Part MultipartBody.Part file,
            @Body DocumentUploadBeans description);*/

    //alternative for form data
    @POST("UserProfile/AddEmployeeDocuments")
    Call<Message> uploadDocuments(
            @Body RequestBody description);

    @POST("UserProfile/UploadProfileImage")
    Call<Message> uploadProfile(
            @Body RequestBody description);

    @POST("UserProfile/AddNomineeDetails")
    Call<Message> uploadNomineeDetails(
            @Query("strNomineeRelation") String strRelation,
            @Query("strNomineeName") String strName,
            @Query("strNomineeDob") String strDOB,
            @Query("strNomineeShare") String strNomineeShare,
            @Query("strEmpSrno") String strEmpSrNo);

    @POST("UserProfile/UploadFeedBack")
    Call<Message> uploadFeedBack(
            @Query("intFeedback") String strFeedbackNo,
            @Query("intFeedbackCategory") String strFeedbackCategory,
            @Query("strFeedbackRemark") String strFeedbackRemark,
            @Query("intFeedbackBy") String empsrno,
            @Query("strSessionId") String strSessionId,
            @Query("strFeedbackPage") String strFeedbackPage
    );
}
