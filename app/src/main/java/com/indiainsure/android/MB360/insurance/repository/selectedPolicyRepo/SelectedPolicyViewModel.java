package com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;

import java.util.ArrayList;
import java.util.List;

public class SelectedPolicyViewModel extends AndroidViewModel {


    private final MutableLiveData<List<GroupPolicyData>> policyData;
    private final MutableLiveData<GroupPolicyData> selectedPolicyData;
    private final MutableLiveData<Integer> selectedIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> policyCount = new MutableLiveData<>(0);

    List<GroupPolicyData> groupGMCPoliciesData = new ArrayList<>();
    List<GroupPolicyData> groupGPAPoliciesData = new ArrayList<>();
    List<GroupPolicyData> groupGTLPoliciesData = new ArrayList<>();


    public SelectedPolicyViewModel(@NonNull Application application) {
        super(application);
        this.policyData = new MutableLiveData<>();
        this.selectedPolicyData = new MutableLiveData<>();
    }

    public LiveData<List<GroupPolicyData>> getAllPoliciesData() {
        ArrayList<GroupPolicyData> subPolicyData = new ArrayList<>();


        //we must get all the policy form the load session from here.
        //here get(0) means the first element in the group_policy array in the load session response

        //get the gmc policies
        subPolicyData.addAll(groupGMCPoliciesData);

        //get the gpa policies
        subPolicyData.addAll(groupGPAPoliciesData);

        //get the gtl policies
        subPolicyData.addAll(groupGTLPoliciesData);

        policyCount.setValue(subPolicyData.size());

        policyData.setValue(subPolicyData);
        //the functions help to select the first policy in the list
        // so that selected policy must always be filled
        try {

            if (selectedIndex.getValue() >= subPolicyData.size()) {
                selectedPolicyData.setValue(subPolicyData.get(0));
            } else {
                selectedPolicyData.setValue(subPolicyData.get(selectedIndex.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SELECT POLICY", "ERROR", e);
        }


        return policyData;
    }

    //global select policy popup
    public LiveData<GroupPolicyData> setSelectedPolicy(int index) {
        if (policyData.getValue() != null) {
            selectedIndex.setValue(index);
            selectedPolicyData.setValue(policyData.getValue().get(index));
            LogMyBenefits.d("SELECTED-POLICY", "setSelectedPolicy: policy selected! : " + policyData.getValue().get(index).toString());
        } else {
            LogMyBenefits.e("SELECTED-POLICY", "setSelectedPolicy: Error, policy List is null");
        }
        return selectedPolicyData;
    }

    //global select policy popup
    public LiveData<GroupPolicyData> setSelectedPolicyFromDropDown(GroupPolicyData policy) {
        if (policyData.getValue() != null) {
            int index = 0;
            for (GroupPolicyData policyItem : policyData.getValue()) {
                if (policyItem.getOeGrpBasInfSrNo().equalsIgnoreCase(policy.getOeGrpBasInfSrNo())) {
                    selectedIndex.setValue(index);
                } else {
                    index++;
                }
            }

            selectedPolicyData.setValue(policy);
            LogMyBenefits.d("SELECTED-POLICY", "setSelectedPolicy: policy selected! : " + policy);
        } else {
            LogMyBenefits.e("SELECTED-POLICY", "setSelectedPolicy: Error, policy List is null");
        }
        return selectedPolicyData;
    }

    public LiveData<GroupPolicyData> getSelectedPolicy() {
        return selectedPolicyData;
    }


    //getters and setters for policies


    public List<GroupPolicyData> getGroupGMCPoliciesData() {
        return groupGMCPoliciesData;
    }

    public void setGroupGMCPoliciesData(List<GroupPolicyData> groupGMCPoliciesData) {
        this.groupGMCPoliciesData = groupGMCPoliciesData;
    }

    public List<GroupPolicyData> getGroupGPAPoliciesData() {
        return groupGPAPoliciesData;
    }

    public void setGroupGPAPoliciesData(List<GroupPolicyData> groupGPAPoliciesData) {
        this.groupGPAPoliciesData = groupGPAPoliciesData;
    }

    public List<GroupPolicyData> getGroupGTLPoliciesData() {
        return groupGTLPoliciesData;
    }

    public void setGroupGTLPoliciesData(List<GroupPolicyData> groupGTLPoliciesData) {
        this.groupGTLPoliciesData = groupGTLPoliciesData;
    }

    public LiveData<List<GroupPolicyData>> getPolicyData() {
        return policyData;
    }

    public MutableLiveData<Integer> getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex.setValue(selectedIndex);
    }

    public LiveData<Integer> totalPolicyCount() {
        return policyCount;
    }
}
