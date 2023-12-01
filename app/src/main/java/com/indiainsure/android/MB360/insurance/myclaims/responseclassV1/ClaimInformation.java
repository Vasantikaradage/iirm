package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimInformation {


    @SerializedName("BENEFICIARY")
    @Expose
    private String beneficiary;
    @SerializedName("CLAIM_NO")
    @Expose
    private String claimNo;
    @SerializedName("CLAIM_DATE")
    @Expose
    private String claimDate;
    @SerializedName("CLAIM_AMT")
    @Expose
    private String claimAmt;
    @SerializedName("CLAIM_TYPE")
    @Expose
    private String claimType;
    @SerializedName("CLAIM_SR_NO")
    @Expose
    private String claimSrNo;
    @SerializedName("RELATION_WITH_EMPLOYEE")
    @Expose
    private String relationWithEmployee;
    @SerializedName("CLAIM_STATUS")
    @Expose
    private String claimStatus;

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getClaimAmt() {
        return claimAmt;
    }

    public void setClaimAmt(String claimAmt) {
        this.claimAmt = claimAmt;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

    public String getClaimSrNo() {
        return claimSrNo;
    }

    public void setClaimSrNo(String claimSrNo) {
        this.claimSrNo = claimSrNo;
    }

    public String getRelationWithEmployee() {
        return relationWithEmployee;
    }

    public void setRelationWithEmployee(String relationWithEmployee) {
        this.relationWithEmployee = relationWithEmployee;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }


    @Override
    public String toString() {
        return "ClaimInformation{" +
                "beneficiary='" + beneficiary + '\'' +
                ", claimNo='" + claimNo + '\'' +
                ", claimDate='" + claimDate + '\'' +
                ", claimAmt='" + claimAmt + '\'' +
                ", claimType='" + claimType + '\'' +
                ", claimSrNo='" + claimSrNo + '\'' +
                ", relationWithEmployee='" + relationWithEmployee + '\'' +
                ", claimStatus='" + claimStatus + '\'' +
                '}';
    }
}
