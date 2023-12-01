package com.indiainsure.android.MB360.insurance.policyfeatures.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponse;

import java.util.List;

public class PolicyFeaturesViewModel extends AndroidViewModel {

    PolicyFeaturesRepository policyFeaturesRepository;

    public PolicyFeaturesViewModel(@NonNull Application application) {
        super(application);
        policyFeaturesRepository = new PolicyFeaturesRepository(application);
    }

    public MutableLiveData<List<PolicyFeaturesResponse>> getPolicyFeatures(String grpChildSrNo, String oeGrpBasInfSrNo, String productType) {
        return policyFeaturesRepository.getPolicyFeatures(grpChildSrNo, oeGrpBasInfSrNo, productType);
    }

    public MutableLiveData<List<PolicyFeaturesResponse>> getPolicyFeaturesData() {
        return policyFeaturesRepository.getPolicyFeaturesData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return policyFeaturesRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return policyFeaturesRepository.errorState;
    }

    public MutableLiveData<Boolean> getReloginState() {
        return policyFeaturesRepository.reloginState;
    }


}
