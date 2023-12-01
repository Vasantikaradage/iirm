package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimCashlessInformation {
    @SerializedName("CashlessStatus")
    @Expose
    private String cashlessStatus;
    @SerializedName("CashlessRequestedOn")
    @Expose
    private String cashlessRequestedOn;
    @SerializedName("CashlessSentDate")
    @Expose
    private String cashlessSentDate;
    @SerializedName("CashlessAmount")
    @Expose
    private String cashlessAmount;

    public String getCashlessStatus() {
        return cashlessStatus;
    }

    public void setCashlessStatus(String cashlessStatus) {
        this.cashlessStatus = cashlessStatus;
    }

    public String getCashlessRequestedOn() {
        return cashlessRequestedOn;
    }

    public void setCashlessRequestedOn(String cashlessRequestedOn) {
        this.cashlessRequestedOn = cashlessRequestedOn;
    }

    public String getCashlessSentDate() {
        return cashlessSentDate;
    }

    public void setCashlessSentDate(String cashlessSentDate) {
        this.cashlessSentDate = cashlessSentDate;
    }

    public String getCashlessAmount() {
        return cashlessAmount;
    }

    public void setCashlessAmount(String cashlessAmount) {
        this.cashlessAmount = cashlessAmount;
    }

    @Override
    public String toString() {
        return "ClaimCashlessInformation{" +
                "cashlessStatus='" + cashlessStatus + '\'' +
                ", cashlessRequestedOn='" + cashlessRequestedOn + '\'' +
                ", cashlessSentDate='" + cashlessSentDate + '\'' +
                ", cashlessAmount='" + cashlessAmount + '\'' +
                '}';
    }
}
