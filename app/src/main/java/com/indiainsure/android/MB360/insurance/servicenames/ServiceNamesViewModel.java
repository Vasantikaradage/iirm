package com.indiainsure.android.MB360.insurance.servicenames;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.servicenames.responseclass.ServiceNamesResponse;

public class ServiceNamesViewModel extends AndroidViewModel {
    ServiceNamesRepository serviceNamesRepository;

    public ServiceNamesViewModel(@NonNull Application application) {
        super(application);
        serviceNamesRepository = new ServiceNamesRepository(application);

    }

    public MutableLiveData<ServiceNamesResponse> getServiceNameData(String groupChildSrNo) {
        return serviceNamesRepository.getServiceNameData(groupChildSrNo);
    }

    public MutableLiveData<ServiceNamesResponse> getAdminSettingData() {
        return serviceNamesRepository.getServiceNamesDetailsData();
    }
    public MutableLiveData<Boolean> getLoadingState() {
        return serviceNamesRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return serviceNamesRepository.errorState;
    }

}


