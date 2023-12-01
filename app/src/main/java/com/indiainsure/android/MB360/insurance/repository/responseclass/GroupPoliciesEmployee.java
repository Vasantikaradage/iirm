package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupPoliciesEmployee {
    @SerializedName("GroupGMCPolicyEmployee_data")
    @Expose
    private List<GroupGMCPolicyEmployeeDatum> groupGMCPolicyEmployeeData = null;
    @SerializedName("GroupGPAPolicyEmployee_data")
    @Expose
    private List<GroupGPAPolicyEmployeeDatum> groupGPAPolicyEmployeeData = null;
    @SerializedName("GroupGTLPolicyEmployee_data")
    @Expose
    private List<GroupGTLPolicyEmployeeDatum> groupGTLPolicyEmployeeData = null;

    public List<GroupGMCPolicyEmployeeDatum> getGroupGMCPolicyEmployeeData() {
        return groupGMCPolicyEmployeeData;
    }

    public void setGroupGMCPolicyEmployeeData(List<GroupGMCPolicyEmployeeDatum> groupGMCPolicyEmployeeData) {
        this.groupGMCPolicyEmployeeData = groupGMCPolicyEmployeeData;
    }

    public List<GroupGPAPolicyEmployeeDatum> getGroupGPAPolicyEmployeeData() {
        return groupGPAPolicyEmployeeData;
    }

    public void setGroupGPAPolicyEmployeeData(List<GroupGPAPolicyEmployeeDatum> groupGPAPolicyEmployeeData) {
        this.groupGPAPolicyEmployeeData = groupGPAPolicyEmployeeData;
    }

    public List<GroupGTLPolicyEmployeeDatum> getGroupGTLPolicyEmployeeData() {
        return groupGTLPolicyEmployeeData;
    }

    public void setGroupGTLPolicyEmployeeData(List<GroupGTLPolicyEmployeeDatum> groupGTLPolicyEmployeeData) {
        this.groupGTLPolicyEmployeeData = groupGTLPolicyEmployeeData;
    }

    @Override
    public String toString() {
        return "GroupPoliciesEmployee{" +
                "groupGMCPolicyEmployeeData=" + groupGMCPolicyEmployeeData +
                ", groupGPAPolicyEmployeeData=" + groupGPAPolicyEmployeeData +
                ", groupGTLPolicyEmployeeData=" + groupGTLPolicyEmployeeData +
                '}';
    }
}
