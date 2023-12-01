package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimPaymentInformation {
    public String DateOfSettlement;
    public String DateOfPaymentToHospital;
    public String AmountPaidToHospital;
    public String BankOrChequeNo;
    public String DateOfPaymentToMember;
    public String AmountPaidToMember;
    public String ChequeNoToMember;


    public String getDateOfSettlement() {
        return DateOfSettlement;
    }

    public void setDateOfSettlement(String dateOfSettlement) {
        DateOfSettlement = dateOfSettlement;
    }

    public String getDateOfPaymentToHospital() {
        return DateOfPaymentToHospital;
    }

    public void setDateOfPaymentToHospital(String dateOfPaymentToHospital) {
        DateOfPaymentToHospital = dateOfPaymentToHospital;
    }

    public String getAmountPaidToHospital() {
        return AmountPaidToHospital;
    }

    public void setAmountPaidToHospital(String amountPaidToHospital) {
        AmountPaidToHospital = amountPaidToHospital;
    }

    public String getBankOrChequeNo() {
        return BankOrChequeNo;
    }

    public void setBankOrChequeNo(String bankOrChequeNo) {
        BankOrChequeNo = bankOrChequeNo;
    }

    public String getDateOfPaymentToMember() {
        return DateOfPaymentToMember;
    }

    public void setDateOfPaymentToMember(String dateOfPaymentToMember) {
        DateOfPaymentToMember = dateOfPaymentToMember;
    }

    public String getAmountPaidToMember() {
        return AmountPaidToMember;
    }

    public void setAmountPaidToMember(String amountPaidToMember) {
        AmountPaidToMember = amountPaidToMember;
    }

    public String getChequeNoToMember() {
        return ChequeNoToMember;
    }

    public void setChequeNoToMember(String chequeNoToMember) {
        ChequeNoToMember = chequeNoToMember;
    }

    @Override
    public String toString() {
        return "ClaimPaymentInformation{" +
                "DateOfSettlement='" + DateOfSettlement + '\'' +
                ", DateOfPaymentToHospital='" + DateOfPaymentToHospital + '\'' +
                ", AmountPaidToHospital=" + AmountPaidToHospital +
                ", BankOrChequeNo='" + BankOrChequeNo + '\'' +
                ", DateOfPaymentToMember='" + DateOfPaymentToMember + '\'' +
                ", AmountPaidToMember=" + AmountPaidToMember +
                ", ChequeNoToMember=" + ChequeNoToMember +
                '}';
    }
}
