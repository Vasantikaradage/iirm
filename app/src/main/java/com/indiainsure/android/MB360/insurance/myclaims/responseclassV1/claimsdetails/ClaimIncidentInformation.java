package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimIncidentInformation {
    @SerializedName("ClaimNo")
    @Expose
    private String claimNo;
    @SerializedName("ClaimUniqueNo")
    @Expose
    private String claimUniqueNo;
    @SerializedName("ClaimExtension")
    @Expose
    private String claimExtension;
    @SerializedName("ClaimPartialPaymentSequence")
    @Expose
    private String claimPartialPaymentSequence;
    @SerializedName("ClaimDate")
    @Expose
    private String claimDate;

    public String getClaimNo() {
        return claimNo;
    }

    public void setClaimNo(String claimNo) {
        this.claimNo = claimNo;
    }

    public String getClaimUniqueNo() {
        return claimUniqueNo;
    }

    public void setClaimUniqueNo(String claimUniqueNo) {
        this.claimUniqueNo = claimUniqueNo;
    }

    public String getClaimExtension() {
        return claimExtension;
    }

    public void setClaimExtension(String claimExtension) {
        this.claimExtension = claimExtension;
    }

    public String getClaimPartialPaymentSequence() {
        return claimPartialPaymentSequence;
    }

    public void setClaimPartialPaymentSequence(String claimPartialPaymentSequence) {
        this.claimPartialPaymentSequence = claimPartialPaymentSequence;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    @Override
    public String toString() {
        return "ClaimIncidentInformation{" +
                "claimNo='" + claimNo + '\'' +
                ", claimUniqueNo='" + claimUniqueNo + '\'' +
                ", claimExtension='" + claimExtension + '\'' +
                ", claimPartialPaymentSequence='" + claimPartialPaymentSequence + '\'' +
                ", claimDate='" + claimDate + '\'' +
                '}';
    }
}
