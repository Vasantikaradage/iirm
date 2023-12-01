package com.indiainsure.android.MB360.insurance.enrollmentstatus;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.indiainsure.android.MB360.insurance.enrollmentstatus.responseclass.EnrollmentStatusResponse;

public class EnrollmentStatusViewModel extends AndroidViewModel {
    EnrollmentStatusRepository enrollmentStatusRepository;

    public EnrollmentStatusViewModel(@NonNull Application application) {
        super(application);
        enrollmentStatusRepository = new EnrollmentStatusRepository(application);
    }

    public LiveData<EnrollmentStatusResponse> getEnrollmentStatus(String employeeSrno, String groupChildSrNo, String oegrpBasInfoSrNo) {
        return enrollmentStatusRepository.getEnrollmentStatus(employeeSrno, groupChildSrNo, oegrpBasInfoSrNo);
    }

    public LiveData<EnrollmentStatusResponse> getEnrollmentStatusData() {
        return enrollmentStatusRepository.getEnrollmentStatusData();
    }
}
