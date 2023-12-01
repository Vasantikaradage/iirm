package com.indiainsure.android.MB360.insurance.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;

public class LoadSessionViewModel extends AndroidViewModel {

    LoadSessionRepository loadSessionRepository;

    public LoadSessionViewModel(@NonNull Application application) {
        super(application);
        loadSessionRepository = new LoadSessionRepository(application);

    }

    public MutableLiveData<LoadSessionResponse> loadSessionWithNumber(String phoneNumber) {
        return loadSessionRepository.loadSessionWithPhoneNumber(phoneNumber);

    }

    public MutableLiveData<LoadSessionResponse> loadSessionWithNumber(String phoneNumber, String OTP) {
        return loadSessionRepository.loadSessionWithPhoneNumber(phoneNumber,OTP);

    }

    public MutableLiveData<LoadSessionResponse> loadSessionWithEmail(String email) {
        return loadSessionRepository.loadSessionWithEmail(email);

    }
    public MutableLiveData<LoadSessionResponse> loadSessionWithEmail(String email, String OTP) {
        return loadSessionRepository.loadSessionWithEmail(email,OTP);

    }

    public MutableLiveData<LoadSessionResponse> loadSessionWithID(String loginID) {
        return loadSessionRepository.loadSessionID(loginID);

    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadSessionRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return loadSessionRepository.errorState;
    }

    public MutableLiveData<LoadSessionResponse> getLoadSessionData() {
        return loadSessionRepository.getLoadSessionResponseMutableLiveData();
    }

    public MutableLiveData<Boolean> getReloginState() {
        return loadSessionRepository.reloginState;
    }
}
