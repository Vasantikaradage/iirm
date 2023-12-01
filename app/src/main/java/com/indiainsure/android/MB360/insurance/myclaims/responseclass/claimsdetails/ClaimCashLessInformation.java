package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimCashLessInformation {

    public String CashlessStatus;
    public String CashlessRequestedOn;
    public String CashlessSentDate;
    public String CashlessAmount;

    public String getCashlessStatus() {
        return CashlessStatus;
    }

    public void setCashlessStatus(String cashlessStatus) {
        CashlessStatus = cashlessStatus;
    }

    public String getCashlessRequestedOn() {
        return CashlessRequestedOn;
    }

    public void setCashlessRequestedOn(String cashlessRequestedOn) {
        CashlessRequestedOn = cashlessRequestedOn;
    }

    public String getCashlessSentDate() {
        return CashlessSentDate;
    }

    public void setCashlessSentDate(String cashlessSentDate) {
        CashlessSentDate = cashlessSentDate;
    }

    public String getCashlessAmount() {
        return CashlessAmount;
    }

    public void setCashlessAmount(String cashlessAmount) {
        CashlessAmount = cashlessAmount;
    }

    @Override
    public String toString() {
        return "ClaimCashLessInformation{" +
                "CashlessStatus=" + CashlessStatus +
                ", CashlessRequestedOn='" + CashlessRequestedOn + '\'' +
                ", CashlessSentDate='" + CashlessSentDate + '\'' +
                ", CashlessAmount=" + CashlessAmount +
                '}';
    }
}
