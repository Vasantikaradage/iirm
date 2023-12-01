package com.indiainsure.android.MB360.insurance.profile.repository;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.profile.response.Message;
import com.indiainsure.android.MB360.insurance.profile.response.ProfileResponse;

public class ProfileViewModel extends AndroidViewModel {
    ProfileRepository profileRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
    }

    public LiveData<ProfileResponse> getProfile(String groupChildSrNo, String oegrBasInfoSrNo, String employeeSrNo) {
        return profileRepository.getProfile(groupChildSrNo, oegrBasInfoSrNo, employeeSrNo);
    }


    public LiveData<ProfileResponse> getProfileData() {
        return profileRepository.getProfileData();
    }


    public LiveData<Message> uploadDocuments(Uri fileUri, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo, String docType, String docNo) {
        return profileRepository.uploadDocuments(fileUri, groupChildSrNo, oeGrpBasInfoSrNo, employeeSrNo, docType, docNo);

    }

    public MutableLiveData<Boolean> getReloginState() {
        return profileRepository.reloginState;
    }

}
