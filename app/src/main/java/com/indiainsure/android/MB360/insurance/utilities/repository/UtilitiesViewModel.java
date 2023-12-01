package com.indiainsure.android.MB360.insurance.utilities.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.utilities.repository.responseclass.UtilitiesResponse;

public class UtilitiesViewModel extends AndroidViewModel {

    UtilitiesRepository utilitiesRepository;

    public UtilitiesViewModel(@NonNull Application application) {
        super(application);
        utilitiesRepository = new UtilitiesRepository(application);
    }


    public MutableLiveData<UtilitiesResponse> getUtilities(String grpChildSrNo, String oeGrpBasInfoSrNo) {
        return utilitiesRepository.getUtilities(grpChildSrNo, oeGrpBasInfoSrNo);
    }

    public MutableLiveData<UtilitiesResponse> getUtilitiesData() {
        return utilitiesRepository.getUtilitiesData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return utilitiesRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return utilitiesRepository.errorState;
    }

    public MutableLiveData<Boolean> getReloginState() {
        return utilitiesRepository.reloginState;
    }


}
