package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimProcessInformation {
    public String TypeOfClaim;
    public String ClaimStatus;
    public String ClaimOutstandingStatus;
    public String ClaimReportedAmount;
    public String ClaimAmount;
    public String ClaimPaidAmount;
    public String ClaimPaidDate;
    public String ClaimRejectedDate;
    public String ClaimClosedDate;
    public String ClaimDenialReasons;
    public String ClaimClosureReasons;

    public String getTypeOfClaim() {
        return TypeOfClaim;
    }

    public void setTypeOfClaim(String typeOfClaim) {
        TypeOfClaim = typeOfClaim;
    }

    public String getClaimStatus() {
        return ClaimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        ClaimStatus = claimStatus;
    }

    public String getClaimOutstandingStatus() {
        return ClaimOutstandingStatus;
    }

    public void setClaimOutstandingStatus(String claimOutstandingStatus) {
        ClaimOutstandingStatus = claimOutstandingStatus;
    }

    public String getClaimReportedAmount() {
        return ClaimReportedAmount;
    }

    public void setClaimReportedAmount(String claimReportedAmount) {
        ClaimReportedAmount = claimReportedAmount;
    }

    public String getClaimAmount() {
        return ClaimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        ClaimAmount = claimAmount;
    }

    public String getClaimPaidAmount() {
        return ClaimPaidAmount;
    }

    public void setClaimPaidAmount(String claimPaidAmount) {
        ClaimPaidAmount = claimPaidAmount;
    }

    public String getClaimPaidDate() {
        return ClaimPaidDate;
    }

    public void setClaimPaidDate(String claimPaidDate) {
        ClaimPaidDate = claimPaidDate;
    }

    public String getClaimRejectedDate() {
        return ClaimRejectedDate;
    }

    public void setClaimRejectedDate(String claimRejectedDate) {
        ClaimRejectedDate = claimRejectedDate;
    }

    public String getClaimClosedDate() {
        return ClaimClosedDate;
    }

    public void setClaimClosedDate(String claimClosedDate) {
        ClaimClosedDate = claimClosedDate;
    }

    public String getClaimDenialReasons() {
        return ClaimDenialReasons;
    }

    public void setClaimDenialReasons(String claimDenialReasons) {
        ClaimDenialReasons = claimDenialReasons;
    }

    public String getClaimClosureReasons() {
        return ClaimClosureReasons;
    }

    public void setClaimClosureReasons(String claimClosureReasons) {
        ClaimClosureReasons = claimClosureReasons;
    }

    @Override
    public String toString() {
        return "ClaimProcessInformation{" +
                "TypeOfClaim='" + TypeOfClaim + '\'' +
                ", ClaimStatus='" + ClaimStatus + '\'' +
                ", ClaimOutstandingStatus='" + ClaimOutstandingStatus + '\'' +
                ", ClaimReportedAmount=" + ClaimReportedAmount +
                ", ClaimAmount=" + ClaimAmount +
                ", ClaimPaidAmount=" + ClaimPaidAmount +
                ", ClaimPaidDate='" + ClaimPaidDate + '\'' +
                ", ClaimRejectedDate='" + ClaimRejectedDate + '\'' +
                ", ClaimClosedDate='" + ClaimClosedDate + '\'' +
                ", ClaimDenialReasons='" + ClaimDenialReasons + '\'' +
                ", ClaimClosureReasons='" + ClaimClosureReasons + '\'' +
                '}';
    }
}
