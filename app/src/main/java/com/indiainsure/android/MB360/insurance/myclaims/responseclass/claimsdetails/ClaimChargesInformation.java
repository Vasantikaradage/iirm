package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimChargesInformation {
    public String FinalBillAmount;
    public String NonPayableExpenses;
    public String CoPaymentDeductions;
    public String DeductionReasons;
    public String RoomNursingCharges;
    public String SurgeryCharges;
    public String ConsultationCharges;
    public String InvestigationCharges;
    public String MedicineCharges;
    public String MiscCharges;

    public String getFinalBillAmount() {
        return FinalBillAmount;
    }

    public void setFinalBillAmount(String finalBillAmount) {
        FinalBillAmount = finalBillAmount;
    }

    public String getNonPayableExpenses() {
        return NonPayableExpenses;
    }

    public void setNonPayableExpenses(String nonPayableExpenses) {
        NonPayableExpenses = nonPayableExpenses;
    }

    public String getCoPaymentDeductions() {
        return CoPaymentDeductions;
    }

    public void setCoPaymentDeductions(String coPaymentDeductions) {
        CoPaymentDeductions = coPaymentDeductions;
    }

    public String getDeductionReasons() {
        return DeductionReasons;
    }

    public void setDeductionReasons(String deductionReasons) {
        DeductionReasons = deductionReasons;
    }

    public String getRoomNursingCharges() {
        return RoomNursingCharges;
    }

    public void setRoomNursingCharges(String roomNursingCharges) {
        RoomNursingCharges = roomNursingCharges;
    }

    public String getSurgeryCharges() {
        return SurgeryCharges;
    }

    public void setSurgeryCharges(String surgeryCharges) {
        SurgeryCharges = surgeryCharges;
    }

    public String getConsultationCharges() {
        return ConsultationCharges;
    }

    public void setConsultationCharges(String consultationCharges) {
        ConsultationCharges = consultationCharges;
    }

    public String getInvestigationCharges() {
        return InvestigationCharges;
    }

    public void setInvestigationCharges(String investigationCharges) {
        InvestigationCharges = investigationCharges;
    }

    public String getMedicineCharges() {
        return MedicineCharges;
    }

    public void setMedicineCharges(String medicineCharges) {
        MedicineCharges = medicineCharges;
    }

    public String getMiscCharges() {
        return MiscCharges;
    }

    public void setMiscCharges(String miscCharges) {
        MiscCharges = miscCharges;
    }

    @Override
    public String toString() {
        return "ClaimChargesInformation{" +
                "FinalBillAmount=" + FinalBillAmount +
                ", NonPayableExpenses=" + NonPayableExpenses +
                ", CoPaymentDeductions=" + CoPaymentDeductions +
                ", DeductionReasons='" + DeductionReasons + '\'' +
                ", RoomNursingCharges=" + RoomNursingCharges +
                ", SurgeryCharges=" + SurgeryCharges +
                ", ConsultationCharges=" + ConsultationCharges +
                ", InvestigationCharges=" + InvestigationCharges +
                ", MedicineCharges=" + MedicineCharges +
                ", MiscCharges=" + MiscCharges +
                '}';
    }
}
