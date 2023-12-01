package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimIncidentInformation {
    public String ClaimNo;
    public String ClaimUniqueNo;
    public String ClaimExtension;
    public String ClaimPartialPaymentSequence;
    public String ClaimDate;

    public String getClaimNo() {
        return ClaimNo;
    }

    public void setClaimNo(String claimNo) {
        ClaimNo = claimNo;
    }

    public String getClaimUniqueNo() {
        return ClaimUniqueNo;
    }

    public void setClaimUniqueNo(String claimUniqueNo) {
        ClaimUniqueNo = claimUniqueNo;
    }

    public String getClaimExtension() {
        return ClaimExtension;
    }

    public void setClaimExtension(String claimExtension) {
        ClaimExtension = claimExtension;
    }

    public String getClaimPartialPaymentSequence() {
        return ClaimPartialPaymentSequence;
    }

    public void setClaimPartialPaymentSequence(String claimPartialPaymentSequence) {
        ClaimPartialPaymentSequence = claimPartialPaymentSequence;
    }

    public String getClaimDate() {
        return ClaimDate;
    }

    public void setClaimDate(String claimDate) {
        ClaimDate = claimDate;
    }

    @Override
    public String toString() {
        return "ClaimIncidentInformation{" +
                "ClaimNo='" + ClaimNo + '\'' +
                ", ClaimUniqueNo=" + ClaimUniqueNo +
                ", ClaimExtension='" + ClaimExtension + '\'' +
                ", ClaimPartialPaymentSequence=" + ClaimPartialPaymentSequence +
                ", ClaimDate='" + ClaimDate + '\'' +
                '}';
    }
}
