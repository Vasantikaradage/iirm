package com.indiainsure.android.MB360.insurance.escalations.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;

public class EscalationsViewModel extends AndroidViewModel {

    EscalationRepository escalationRepository;

    public EscalationsViewModel(@NonNull Application application) {
        super(application);
        escalationRepository = new EscalationRepository(application);
    }

    public MutableLiveData<EscalationsResponse> getEscalations(String grpChildSrNo, String oeGrpBasInfSrNo) {
        return escalationRepository.getEscalations(grpChildSrNo, oeGrpBasInfSrNo);
    }

    public MutableLiveData<EscalationsResponse> getEscalationsData() {
        return escalationRepository.getEscalationsData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return escalationRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return escalationRepository.errorState;
    }

    public MutableLiveData<Boolean> getReloginState() {
        return escalationRepository.reloginState;
    }

}
