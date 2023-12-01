package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoadDetailedClaimsValues {

    @SerializedName("CLAIM_SR_NO")
    @Expose
    private String claimSrNo;
    @SerializedName("FIR")
    @Expose
    private String fir;
    @SerializedName("UNIQUE_FIR")
    @Expose
    private String uniqueFir;
    @SerializedName("GROUP_CODE")
    @Expose
    private String groupCode;
    @SerializedName("EMPLOYEE_NO")
    @Expose
    private String employeeNo;
    @SerializedName("EMPLOYEE_SR_NO")
    @Expose
    private String employeeSrNo;

    public String getClaimSrNo() {
        return claimSrNo;
    }

    public void setClaimSrNo(String claimSrNo) {
        this.claimSrNo = claimSrNo;
    }

    public String getFir() {
        return fir;
    }

    public void setFir(String fir) {
        this.fir = fir;
    }

    public String getUniqueFir() {
        return uniqueFir;
    }

    public void setUniqueFir(String uniqueFir) {
        this.uniqueFir = uniqueFir;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getEmployeeSrNo() {
        return employeeSrNo;
    }

    public void setEmployeeSrNo(String employeeSrNo) {
        this.employeeSrNo = employeeSrNo;
    }

    @Override
    public String toString() {
        return "LoadDetailedClaimsValues{" +
                "claimSrNo='" + claimSrNo + '\'' +
                ", fir='" + fir + '\'' +
                ", uniqueFir='" + uniqueFir + '\'' +
                ", groupCode='" + groupCode + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", employeeSrNo='" + employeeSrNo + '\'' +
                '}';
    }
}
