package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimChargesInformation {


    @SerializedName("DEDUCTION_REASONS")
    @Expose
    private String deductionReasons;
    @SerializedName("FINAL_BILL_AMOUNT")
    @Expose
    private String finalBillAmount;
    @SerializedName("CO_PAYMENT_DEDUCTION")
    @Expose
    private String coPaymentDeduction;
    @SerializedName("ROOM_NURSING_CHARGES")
    @Expose
    private String roomNursingCharges;
    @SerializedName("SURGERY_CHARGES")
    @Expose
    private String surgeryCharges;
    @SerializedName("CONSULTANT_CHARGES")
    @Expose
    private String consultantCharges;
    @SerializedName("INVESTIGATION_CHARGES")
    @Expose
    private String investigationCharges;
    @SerializedName("MISCELLANEOUS_CHARGES")
    @Expose
    private String miscellaneousCharges;
    @SerializedName("NonPayableExpenses")
    @Expose
    private String nonPayableExpenses;
    @SerializedName("MedicineCharges")
    @Expose
    private String medicineCharges;

    public String getDeductionReasons() {
        return deductionReasons;
    }

    public void setDeductionReasons(String deductionReasons) {
        this.deductionReasons = deductionReasons;
    }

    public String getFinalBillAmount() {
        return finalBillAmount;
    }

    public void setFinalBillAmount(String finalBillAmount) {
        this.finalBillAmount = finalBillAmount;
    }

    public String getCoPaymentDeduction() {
        return coPaymentDeduction;
    }

    public void setCoPaymentDeduction(String coPaymentDeduction) {
        this.coPaymentDeduction = coPaymentDeduction;
    }

    public String getRoomNursingCharges() {
        return roomNursingCharges;
    }

    public void setRoomNursingCharges(String roomNursingCharges) {
        this.roomNursingCharges = roomNursingCharges;
    }

    public String getSurgeryCharges() {
        return surgeryCharges;
    }

    public void setSurgeryCharges(String surgeryCharges) {
        this.surgeryCharges = surgeryCharges;
    }

    public String getConsultantCharges() {
        return consultantCharges;
    }

    public void setConsultantCharges(String consultantCharges) {
        this.consultantCharges = consultantCharges;
    }

    public String getInvestigationCharges() {
        return investigationCharges;
    }

    public void setInvestigationCharges(String investigationCharges) {
        this.investigationCharges = investigationCharges;
    }

    public String getMiscellaneousCharges() {
        return miscellaneousCharges;
    }

    public void setMiscellaneousCharges(String miscellaneousCharges) {
        this.miscellaneousCharges = miscellaneousCharges;
    }

    public String getNonPayableExpenses() {
        return nonPayableExpenses;
    }

    public void setNonPayableExpenses(String nonPayableExpenses) {
        this.nonPayableExpenses = nonPayableExpenses;
    }

    public String getMedicineCharges() {
        return medicineCharges;
    }

    public void setMedicineCharges(String medicineCharges) {
        this.medicineCharges = medicineCharges;
    }

    @Override
    public String toString() {
        return "ClaimChargesInformation{" +
                "deductionReasons='" + deductionReasons + '\'' +
                ", finalBillAmount='" + finalBillAmount + '\'' +
                ", coPaymentDeduction='" + coPaymentDeduction + '\'' +
                ", roomNursingCharges='" + roomNursingCharges + '\'' +
                ", surgeryCharges='" + surgeryCharges + '\'' +
                ", consultantCharges='" + consultantCharges + '\'' +
                ", investigationCharges='" + investigationCharges + '\'' +
                ", miscellaneousCharges='" + miscellaneousCharges + '\'' +
                ", nonPayableExpenses='" + nonPayableExpenses + '\'' +
                ", medicineCharges='" + medicineCharges + '\'' +
                '}';
    }
}
