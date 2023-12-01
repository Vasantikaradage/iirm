package com.indiainsure.android.MB360.insurance.adminsetting.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;

public class AdminSettingViewModel extends AndroidViewModel {
    AdminSettingRepository adminSettingRepository;

    public AdminSettingViewModel(@NonNull Application application) {
        super(application);
        adminSettingRepository = new AdminSettingRepository(application);

    }

    public MutableLiveData<AdminSettingResponse> getAdminSetting(String groupChildSrNo, String oeGrpBasInfSrNo) {
        return adminSettingRepository.getAdminSettingData(groupChildSrNo, oeGrpBasInfSrNo);
    }

    public MutableLiveData<AdminSettingResponse> getAdminSettingData() {
        return adminSettingRepository.getAdminSettingDetailsData();
    }
    public MutableLiveData<Boolean> getLoadingState() {
        return adminSettingRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return adminSettingRepository.errorState;
    }

}


