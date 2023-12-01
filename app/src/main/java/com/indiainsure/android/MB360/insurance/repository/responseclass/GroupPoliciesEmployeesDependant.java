package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupPoliciesEmployeesDependant {
    @SerializedName("GroupGMCPolicyEmpDependants_data")
    @Expose
    private List<GroupGMCPolicyEmpDependantsDatum> groupGMCPolicyEmpDependantsData = null;
    @SerializedName("GroupGPAPolicyEmpDependants_data")
    @Expose
    private List<GroupGPAPolicyEmpDependantsDatum> groupGPAPolicyEmpDependantsData = null;
    @SerializedName("GroupGTLPolicyEmpDependants_data")
    @Expose
    private List<GroupGTLPolicyEmpDependantsDatum> groupGTLPolicyEmpDependantsData = null;

    public List<GroupGMCPolicyEmpDependantsDatum> getGroupGMCPolicyEmpDependantsData() {
        return groupGMCPolicyEmpDependantsData;
    }

    public void setGroupGMCPolicyEmpDependantsData(List<GroupGMCPolicyEmpDependantsDatum> groupGMCPolicyEmpDependantsData) {
        this.groupGMCPolicyEmpDependantsData = groupGMCPolicyEmpDependantsData;
    }

    public List<GroupGPAPolicyEmpDependantsDatum> getGroupGPAPolicyEmpDependantsData() {
        return groupGPAPolicyEmpDependantsData;
    }

    public void setGroupGPAPolicyEmpDependantsData(List<GroupGPAPolicyEmpDependantsDatum> groupGPAPolicyEmpDependantsData) {
        this.groupGPAPolicyEmpDependantsData = groupGPAPolicyEmpDependantsData;
    }

    public List<GroupGTLPolicyEmpDependantsDatum> getGroupGTLPolicyEmpDependantsData() {
        return groupGTLPolicyEmpDependantsData;
    }

    public void setGroupGTLPolicyEmpDependantsData(List<GroupGTLPolicyEmpDependantsDatum> groupGTLPolicyEmpDependantsData) {
        this.groupGTLPolicyEmpDependantsData = groupGTLPolicyEmpDependantsData;
    }

    @Override
    public String toString() {
        return "GroupPoliciesEmployeesDependant{" +
                "groupGMCPolicyEmpDependantsData=" + groupGMCPolicyEmpDependantsData +
                ", groupGPAPolicyEmpDependantsData=" + groupGPAPolicyEmpDependantsData +
                ", groupGTLPolicyEmpDependantsData=" + groupGTLPolicyEmpDependantsData +
                '}';
    }
}
