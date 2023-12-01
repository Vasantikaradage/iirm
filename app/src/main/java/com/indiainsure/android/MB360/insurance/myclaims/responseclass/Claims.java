package com.indiainsure.android.MB360.insurance.myclaims.responseclass;

import org.simpleframework.xml.Element;

@Element(name = "claims",required = false)
public class Claims  {
    public String BeneficiaryName;
    public String ClaimNo;
    public String ClaimDate;
    public int ClaimAmount;
    public String ClaimType;
    public String RelationWithEmployee;
    public int ClaimSrNo;
    public String ClaimStatus;


    public String getBeneficiaryName() {
        return BeneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        BeneficiaryName = beneficiaryName;
    }

    public String getClaimNo() {
        return ClaimNo;
    }

    public void setClaimNo(String claimNo) {
        ClaimNo = claimNo;
    }

    public String getClaimDate() {
        return ClaimDate;
    }

    public void setClaimDate(String claimDate) {
        ClaimDate = claimDate;
    }

    public int getClaimAmount() {
        return ClaimAmount;
    }

    public void setClaimAmount(int claimAmount) {
        ClaimAmount = claimAmount;
    }

    public String getClaimType() {
        return ClaimType;
    }

    public void setClaimType(String claimType) {
        ClaimType = claimType;
    }

    public String getRelationWithEmployee() {
        return RelationWithEmployee;
    }

    public void setRelationWithEmployee(String relationWithEmployee) {
        RelationWithEmployee = relationWithEmployee;
    }

    public int getClaimSrNo() {
        return ClaimSrNo;
    }

    public void setClaimSrNo(int claimSrNo) {
        ClaimSrNo = claimSrNo;
    }

    public String getClaimStatus() {
        return ClaimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        ClaimStatus = claimStatus;
    }

    @Override
    public String toString() {
        return "Claims{" +
                "BeneficiaryName='" + BeneficiaryName + '\'' +
                ", ClaimNo='" + ClaimNo + '\'' +
                ", ClaimDate='" + ClaimDate + '\'' +
                ", ClaimAmount=" + ClaimAmount +
                ", ClaimType='" + ClaimType + '\'' +
                ", RelationWithEmployee='" + RelationWithEmployee + '\'' +
                ", ClaimSrNo=" + ClaimSrNo +
                ", ClaimStatus='" + ClaimStatus + '\'' +
                '}';
    }

}
