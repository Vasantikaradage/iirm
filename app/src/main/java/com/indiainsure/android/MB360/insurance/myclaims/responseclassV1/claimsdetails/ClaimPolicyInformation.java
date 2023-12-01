package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimPolicyInformation {
    @SerializedName("CLAIM_SR_NO")
    @Expose
    private String claimSrNo;

    public String getClaimSrNo() {
        return claimSrNo;
    }

    public void setClaimSrNo(String claimSrNo) {
        this.claimSrNo = claimSrNo;
    }

    @Override
    public String toString() {
        return "ClaimPolicyInformation{" +
                "claimSrNo='" + claimSrNo + '\'' +
                '}';
    }
}
