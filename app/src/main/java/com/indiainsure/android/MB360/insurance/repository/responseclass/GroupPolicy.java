package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;

import java.util.List;

public class GroupPolicy {
    @SerializedName("GroupGMCPolicies_data")
    @Expose
    private List<GroupPolicyData> groupGMCPoliciesData = null;
    @SerializedName("GroupGPAPolicies_data")
    @Expose
    private List<GroupPolicyData> groupGPAPoliciesData = null;
    @SerializedName("GroupGTLPolicies_data")
    @Expose
    private List<GroupPolicyData> groupGTLPoliciesData = null;

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

    @Override
    public String toString() {
        return "GroupPolicy{" +
                "groupGMCPoliciesData=" + groupGMCPoliciesData +
                ", groupGPAPoliciesData=" + groupGPAPoliciesData +
                ", groupGTLPoliciesData=" + groupGTLPoliciesData +
                '}';
    }
}

