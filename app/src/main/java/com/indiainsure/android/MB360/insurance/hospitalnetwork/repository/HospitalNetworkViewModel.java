package com.indiainsure.android.MB360.insurance.hospitalnetwork.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalCountResponse;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalResponse;

public class HospitalNetworkViewModel extends AndroidViewModel {

    HospitalNetworkRepository hospitalNetworkRepository;

    public HospitalNetworkViewModel(@NonNull Application application) {
        super(application);
        hospitalNetworkRepository = new HospitalNetworkRepository(application);
    }


    public LiveData<HospitalResponse> getHospitals(String groupChildSrNo, String oeGrpBasInfoSrNo,String searchText) {
        return hospitalNetworkRepository.getHospitals(groupChildSrNo, oeGrpBasInfoSrNo,searchText);
    }

    public LiveData<HospitalResponse> getHospitalsData() {
        return hospitalNetworkRepository.getHospitalsData();
    }

    public LiveData<HospitalCountResponse> getHospitalsCount(String groupChildSrNo, String OeGrpBasInfoSrnNo,String searchText) {
        return hospitalNetworkRepository.getHospitalCount(groupChildSrNo, OeGrpBasInfoSrnNo,searchText);
    }

    public LiveData<HospitalCountResponse> getHospitalsCountData() {
        return hospitalNetworkRepository.getHospitalsCountData();
    }

    public LiveData<Boolean> getLoading() {
        return hospitalNetworkRepository.getLoading();
    }

    public LiveData<Boolean> getError() {
        return hospitalNetworkRepository.getError();
    }

    public MutableLiveData<Boolean> getReloginState() {
        return hospitalNetworkRepository.reloginState;
    }


}
