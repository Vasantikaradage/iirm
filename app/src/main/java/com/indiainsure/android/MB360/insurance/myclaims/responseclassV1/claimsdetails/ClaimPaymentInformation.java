package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimPaymentInformation {

    @SerializedName("DATE_OF_SETTLEMENT")
    @Expose
    private String dateOfSettlement;
    @SerializedName("BANK_CHEQUE_NO")
    @Expose
    private String bankChequeNo;
    @SerializedName("DATE_OF_PAYMENT_TO_MEMBER")
    @Expose
    private String dateOfPaymentToMember;
    @SerializedName("AMOUNT_PAID_TO_MEMBER")
    @Expose
    private String amountPaidToMember;
    @SerializedName("CHEQUE_NO_TO_MEMBER")
    @Expose
    private String chequeNoToMember;
    @SerializedName("DateOfPaymentToHospital")
    @Expose
    private String dateOfPaymentToHospital;
    @SerializedName("AmountPaidToHospital")
    @Expose
    private String amountPaidToHospital;

    public String getDateOfSettlement() {
        return dateOfSettlement;
    }

    public void setDateOfSettlement(String dateOfSettlement) {
        this.dateOfSettlement = dateOfSettlement;
    }

    public String getBankChequeNo() {
        return bankChequeNo;
    }

    public void setBankChequeNo(String bankChequeNo) {
        this.bankChequeNo = bankChequeNo;
    }

    public String getDateOfPaymentToMember() {
        return dateOfPaymentToMember;
    }

    public void setDateOfPaymentToMember(String dateOfPaymentToMember) {
        this.dateOfPaymentToMember = dateOfPaymentToMember;
    }

    public String getAmountPaidToMember() {
        return amountPaidToMember;
    }

    public void setAmountPaidToMember(String amountPaidToMember) {
        this.amountPaidToMember = amountPaidToMember;
    }

    public String getChequeNoToMember() {
        return chequeNoToMember;
    }

    public void setChequeNoToMember(String chequeNoToMember) {
        this.chequeNoToMember = chequeNoToMember;
    }

    public String getDateOfPaymentToHospital() {
        return dateOfPaymentToHospital;
    }

    public void setDateOfPaymentToHospital(String dateOfPaymentToHospital) {
        this.dateOfPaymentToHospital = dateOfPaymentToHospital;
    }

    public String getAmountPaidToHospital() {
        return amountPaidToHospital;
    }

    public void setAmountPaidToHospital(String amountPaidToHospital) {
        this.amountPaidToHospital = amountPaidToHospital;
    }

    @Override
    public String toString() {
        return "ClaimPaymentInformation{" +
                "dateOfSettlement='" + dateOfSettlement + '\'' +
                ", bankChequeNo='" + bankChequeNo + '\'' +
                ", dateOfPaymentToMember='" + dateOfPaymentToMember + '\'' +
                ", amountPaidToMember='" + amountPaidToMember + '\'' +
                ", chequeNoToMember='" + chequeNoToMember + '\'' +
                ", dateOfPaymentToHospital='" + dateOfPaymentToHospital + '\'' +
                ", amountPaidToHospital='" + amountPaidToHospital + '\'' +
                '}';
    }
}
