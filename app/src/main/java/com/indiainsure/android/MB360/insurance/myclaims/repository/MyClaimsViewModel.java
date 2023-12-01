package com.indiainsure.android.MB360.insurance.myclaims.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.ClaimInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.ClaimsResponse;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimsDetails;

public class MyClaimsViewModel extends AndroidViewModel {
    MyClaimsRepository myClaimsRepository;


    public MyClaimsViewModel(@NonNull Application application) {
        super(application);
        myClaimsRepository = new MyClaimsRepository(application);
    }


    public LiveData<ClaimsResponse> getMyClaims(String groupChildSrNo, String employee_srNo) {

        return myClaimsRepository.getMyClaims(groupChildSrNo, employee_srNo);
    }

    public LiveData<ClaimsResponse> getMyClaimsData() {
        return myClaimsRepository.getMyClaimsData();
    }


    public LiveData<ClaimsDetails> getMyClaimsDetails(String groupChildSrNo, String OeGrpBasInfoSrNo, String claimSrNo) {

        return myClaimsRepository.getMyClaimsDetails(groupChildSrNo, OeGrpBasInfoSrNo, claimSrNo);
    }

    public LiveData<ClaimsDetails> getMyClaimsDetailsData() {
        return myClaimsRepository.getMyClaimsDetailsData();
    }


    public LiveData<Boolean> getLoading() {
        return myClaimsRepository.getLoading();
    }

    public LiveData<Boolean> getError() {
        return myClaimsRepository.getError();
    }


    public MutableLiveData<ClaimInformation> getSelectedClaim() {
        return myClaimsRepository.getSelectedClaim();
    }

    public void setSelectedClaim(ClaimInformation claims) {
        myClaimsRepository.setSelectedClaim(claims);
    }

    public MutableLiveData<Boolean> getReloginState() {
        return myClaimsRepository.reloginState;
    }



    public MutableLiveData<String> getErrorDescription() {
        return myClaimsRepository.getErrorDescription();
    }

}
