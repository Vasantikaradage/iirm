package com.indiainsure.android.MB360.insurance.FAQ.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;

public class FaqViewModel extends AndroidViewModel {
    FaqRepository faqRepository;

    public FaqViewModel(@NonNull Application application) {
        super(application);
        faqRepository = new FaqRepository(application);
    }


    public MutableLiveData<FaqResponse> getFaq(String groupChildSrNo, String oeGrpBasInfoSrNo) {
        return faqRepository.getFaq(groupChildSrNo, oeGrpBasInfoSrNo);
    }


    public MutableLiveData<FaqResponse> geFaqData() {
        return faqRepository.getFaqData();
    }



    public MutableLiveData<Boolean> getErrorState() {
        return faqRepository.getErrorState();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return faqRepository.getLoadingState();
    }

    public MutableLiveData<Boolean> getReloginState() {
        return faqRepository.reloginState;
    }

}
