package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimProcessInformation {

    @SerializedName("TPA_ID")
    @Expose
    private Object tpaId;
    @SerializedName("REPORTED_AMOUNT")
    @Expose
    private String reportedAmount;
    @SerializedName("AMOUNT")
    @Expose
    private String amount;
    @SerializedName("PAID_AMOUNT")
    @Expose
    private String paidAmount;
    @SerializedName("REJECTED_CLOSE_DATE")
    @Expose
    private String rejectedCloseDate;
    @SerializedName("TypeOfClaim")
    @Expose
    private String typeOfClaim;
    @SerializedName("OUTSTANDING_CLAIM_STATUS")
    @Expose
    private String outstandingClaimStatus;
    @SerializedName("DENIAL_REASONS")
    @Expose
    private String denialReasons;
    @SerializedName("CLOSE_REASONS")
    @Expose
    private String closeReasons;
    @SerializedName("ClaimStatus")
    @Expose
    private String claimStatus;
    @SerializedName("ClaimPaidDate")
    @Expose
    private String claimPaidDate;
    @SerializedName("ClaimRejectedDate")
    @Expose
    private String claimRejectedDate;
    @SerializedName("ClaimClosedDate")
    @Expose
    private String claimClosedDate;

    public Object getTpaId() {
        return tpaId;
    }

    public void setTpaId(Object tpaId) {
        this.tpaId = tpaId;
    }

    public String getReportedAmount() {
        return reportedAmount;
    }

    public void setReportedAmount(String reportedAmount) {
        this.reportedAmount = reportedAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getRejectedCloseDate() {
        return rejectedCloseDate;
    }

    public void setRejectedCloseDate(String rejectedCloseDate) {
        this.rejectedCloseDate = rejectedCloseDate;
    }

    public String getTypeOfClaim() {
        return typeOfClaim;
    }

    public void setTypeOfClaim(String typeOfClaim) {
        this.typeOfClaim = typeOfClaim;
    }

    public String getOutstandingClaimStatus() {
        return outstandingClaimStatus;
    }

    public void setOutstandingClaimStatus(String outstandingClaimStatus) {
        this.outstandingClaimStatus = outstandingClaimStatus;
    }

    public String getDenialReasons() {
        return denialReasons;
    }

    public void setDenialReasons(String denialReasons) {
        this.denialReasons = denialReasons;
    }

    public String getCloseReasons() {
        return closeReasons;
    }

    public void setCloseReasons(String closeReasons) {
        this.closeReasons = closeReasons;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getClaimPaidDate() {
        return claimPaidDate;
    }

    public void setClaimPaidDate(String claimPaidDate) {
        this.claimPaidDate = claimPaidDate;
    }

    public String getClaimRejectedDate() {
        return claimRejectedDate;
    }

    public void setClaimRejectedDate(String claimRejectedDate) {
        this.claimRejectedDate = claimRejectedDate;
    }

    public String getClaimClosedDate() {
        return claimClosedDate;
    }

    public void setClaimClosedDate(String claimClosedDate) {
        this.claimClosedDate = claimClosedDate;
    }

    @Override
    public String toString() {
        return "ClaimProcessInformation{" +
                "tpaId=" + tpaId +
                ", reportedAmount='" + reportedAmount + '\'' +
                ", amount='" + amount + '\'' +
                ", paidAmount='" + paidAmount + '\'' +
                ", rejectedCloseDate='" + rejectedCloseDate + '\'' +
                ", typeOfClaim='" + typeOfClaim + '\'' +
                ", outstandingClaimStatus='" + outstandingClaimStatus + '\'' +
                ", denialReasons='" + denialReasons + '\'' +
                ", closeReasons='" + closeReasons + '\'' +
                ", claimStatus='" + claimStatus + '\'' +
                ", claimPaidDate='" + claimPaidDate + '\'' +
                ", claimRejectedDate='" + claimRejectedDate + '\'' +
                ", claimClosedDate='" + claimClosedDate + '\'' +
                '}';
    }
}
