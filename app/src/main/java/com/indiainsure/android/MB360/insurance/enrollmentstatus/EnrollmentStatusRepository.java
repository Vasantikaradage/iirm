package com.indiainsure.android.MB360.insurance.enrollmentstatus;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.insurance.enrollmentstatus.responseclass.EnrollmentStatus;
import com.indiainsure.android.MB360.insurance.enrollmentstatus.responseclass.EnrollmentStatusResponse;
import com.indiainsure.android.MB360.insurance.enrollmentstatus.retrofit.EnrollmentStatusRetrofitClient;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnrollmentStatusRepository {

    Application application;

    MutableLiveData<EnrollmentStatusResponse> statusResponse = new MutableLiveData<>();
    EnrollmentStatusResponse enrollmentStatusResponse = new EnrollmentStatusResponse();


    public EnrollmentStatusRepository(Application application) {
        this.application = application;
    }


    public MutableLiveData<EnrollmentStatusResponse> getEnrollmentStatus(String employeeSrNo, String groupChildSrNo, String oegrpBasInfoSrNo) {

        enrollmentStatusResponse.setLoadingState(true);
        enrollmentStatusResponse.setErrorState(false);

        Call<EnrollmentStatus> enrollmentStatusCall = null;
        try {
            LogMyBenefits.d(LogTags.ENROLLMENT, "STATUS PARAMETERS" + " Emp: " + employeeSrNo + " GrpChildSrno: " + groupChildSrNo + " OeGrpBasInfo: " + oegrpBasInfoSrNo);
            enrollmentStatusCall = EnrollmentStatusRetrofitClient.getInstance(application)
                    .getEnrollmentStatusApi().getEnrollmentStatus(
                            AesNew.encrypt(UtilMethods.checkSpecialCharacters(employeeSrNo), application.getString(R.string.pass_phrase)),
                            AesNew.encrypt(UtilMethods.checkSpecialCharacters(groupChildSrNo), application.getString(R.string.pass_phrase)),
                            AesNew.encrypt(UtilMethods.checkSpecialCharacters(oegrpBasInfoSrNo), application.getString(R.string.pass_phrase)));


            enrollmentStatusCall.enqueue(new Callback<EnrollmentStatus>() {
                @Override
                public void onResponse(Call<EnrollmentStatus> call, Response<EnrollmentStatus> response) {
                    try {
                        LogMyBenefits.d("ENROLLMENT-STATUS", response.body().toString());
                        if (response.code() == 200) {
                            enrollmentStatusResponse.setEnrollmentStatus(response.body());
                            enrollmentStatusResponse.setErrorState(false);
                            enrollmentStatusResponse.setLoadingState(false);
                            statusResponse.setValue(enrollmentStatusResponse);

                        } else {
                            enrollmentStatusResponse.setErrorState(true);
                            enrollmentStatusResponse.setLoadingState(false);
                            enrollmentStatusResponse.setEnrollmentStatus(null);
                            statusResponse.setValue(enrollmentStatusResponse);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        enrollmentStatusResponse.setErrorState(true);
                        enrollmentStatusResponse.setLoadingState(false);
                        enrollmentStatusResponse.setEnrollmentStatus(null);
                        statusResponse.setValue(enrollmentStatusResponse);
                    }

                }

                @Override
                public void onFailure(Call<EnrollmentStatus> call, Throwable t) {
                    LogMyBenefits.e("ENROLLMENT-STATUS", t.getLocalizedMessage());
                    enrollmentStatusResponse.setErrorState(true);
                    enrollmentStatusResponse.setLoadingState(false);
                    enrollmentStatusResponse.setEnrollmentStatus(null);
                    statusResponse.setValue(enrollmentStatusResponse);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            enrollmentStatusResponse.setErrorState(false);
            enrollmentStatusResponse.setLoadingState(false);
        }

        return statusResponse;
    }


    public MutableLiveData<EnrollmentStatusResponse> getEnrollmentStatusData() {
        return statusResponse;
    }
}
