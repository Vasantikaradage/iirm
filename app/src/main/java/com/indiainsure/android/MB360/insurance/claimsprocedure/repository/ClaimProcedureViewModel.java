package com.indiainsure.android.MB360.insurance.claimsprocedure.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureEmergencyContactResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureImageResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureLayoutInstructionInfo;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureTextResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimsProcedureLayoutInfoResponse;

public class ClaimProcedureViewModel extends AndroidViewModel {

    ClaimProcedureRepository claimProcedureRepository;

    public ClaimProcedureViewModel(@NonNull Application application) {
        super(application);
        claimProcedureRepository = new ClaimProcedureRepository(application);
    }


    public LiveData<ClaimsProcedureLayoutInfoResponse> getClaimsProcedureLayoutInfo(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {
        return claimProcedureRepository.getClaimsProcedureLayoutInfo(grpChildSrNo, oeGrpBasInfSrNo, productCode, layoutOfClaim);
    }

    public LiveData<ClaimProcedureImageResponse> getClaimProcedureImage(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {
        return claimProcedureRepository.getClaimProcedureImage(grpChildSrNo, oeGrpBasInfSrNo, productCode, layoutOfClaim);
    }


    public LiveData<ClaimProcedureLayoutInstructionInfo> getClaimProcedureInformation(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim)  {
        return claimProcedureRepository.getClaimProcedureInformation(grpChildSrNo, oeGrpBasInfSrNo, productCode, layoutOfClaim);
    }

    public LiveData<ClaimProcedureTextResponse> getClaimProcedureText(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {
        return claimProcedureRepository.getClaimProcedureText(grpChildSrNo, oeGrpBasInfSrNo, productCode, layoutOfClaim);
    }

    public LiveData<ClaimProcedureEmergencyContactResponse> getEmergencyContact(String tpaCode) {
        return claimProcedureRepository.getEmergencyContacts(tpaCode);
    }

    //for observing the data


    public MutableLiveData<ClaimsProcedureLayoutInfoResponse> getClaimsProcedureLayoutInfoData() {
        return claimProcedureRepository.getClaimsProcedureLayoutInfoData();
    }


    public MutableLiveData<ClaimProcedureImageResponse> getClaimProcedureImageData() {
        return claimProcedureRepository.getClaimProcedureImageData();
    }

    public MutableLiveData<ClaimProcedureLayoutInstructionInfo> getClaimProcedureLayoutInstructionInfoData() {
        return claimProcedureRepository.getClaimProcedureLayoutInstructionInfoData();
    }

    public MutableLiveData<ClaimProcedureTextResponse> getClaimProcedureTextData() {
        return claimProcedureRepository.getClaimProcedureTextData();
    }

    public MutableLiveData<ClaimProcedureEmergencyContactResponse> getClaimProcedureEmergencyContactData() {
        return claimProcedureRepository.getClaimProcedureEmergencyContactData();
    }


    public LiveData<Boolean> getLoadingState() {
        return claimProcedureRepository.loadingState;
    }

    public LiveData<Boolean> getErrorState() {
        return claimProcedureRepository.errorState;
    }

    public LiveData<String> getClaimProcedureTextFileData() {
        return claimProcedureRepository.getClaimProcedureTextFileData();
    }


    public LiveData<String> getClaimStepsHtmlData(String groupChildSrno, String group_oegrpbasInfoSrNo, String file_name) {
        return claimProcedureRepository.getClaimStepsHtmlData(groupChildSrno, group_oegrpbasInfoSrNo, file_name);
    }

    public LiveData<String> getClaimStepsHtmlDataObserver() {
        return claimProcedureRepository.getClaimStepsHtmlDataObserver();
    }

    public LiveData<String> getClaimStepsHtmlAdditionalData(String groupChildSrno, String group_oegrpbasInfoSrNo, String file_name) throws Exception {
        return claimProcedureRepository.getClaimStepsHtmlAdditionalData(groupChildSrno, group_oegrpbasInfoSrNo, file_name);
    }
    public LiveData<String> getClaimStepsHtmlAdditionalDataObserver()  {
        return claimProcedureRepository.getClaimStepsHtmlAdditionalDataObserver();
    }

    public MutableLiveData<Boolean> getReloginState() {
        return claimProcedureRepository.reloginState;
    }

}
