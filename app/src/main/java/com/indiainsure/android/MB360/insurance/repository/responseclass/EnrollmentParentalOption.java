package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrollmentParentalOption {
    @SerializedName("Enrollment_Through_MB")
    @Expose
    private String enrollmentThroughMB;
    @SerializedName("PARENTS_COVERED_IN_BP")
    @Expose
    private Object parentsCoveredInBp;
    @SerializedName("CROSS_COMB_ALLOWED")
    @Expose
    private Object crossCombAllowed;
    @SerializedName("IS_PARENTAL_PREMIUM_CHARGED")
    @Expose
    private Object isParentalPremiumCharged;
    @SerializedName("ParentalInformation_data")
    @Expose
    private Object parentalInformationData;

    public String getEnrollmentThroughMB() {
        return enrollmentThroughMB;
    }

    public void setEnrollmentThroughMB(String enrollmentThroughMB) {
        this.enrollmentThroughMB = enrollmentThroughMB;
    }

    public Object getParentsCoveredInBp() {
        return parentsCoveredInBp;
    }

    public void setParentsCoveredInBp(Object parentsCoveredInBp) {
        this.parentsCoveredInBp = parentsCoveredInBp;
    }

    public Object getCrossCombAllowed() {
        return crossCombAllowed;
    }

    public void setCrossCombAllowed(Object crossCombAllowed) {
        this.crossCombAllowed = crossCombAllowed;
    }

    public Object getIsParentalPremiumCharged() {
        return isParentalPremiumCharged;
    }

    public void setIsParentalPremiumCharged(Object isParentalPremiumCharged) {
        this.isParentalPremiumCharged = isParentalPremiumCharged;
    }

    public Object getParentalInformationData() {
        return parentalInformationData;
    }

    public void setParentalInformationData(Object parentalInformationData) {
        this.parentalInformationData = parentalInformationData;
    }

    @Override
    public String toString() {
        return "EnrollmentParentalOption{" +
                "enrollmentThroughMB='" + enrollmentThroughMB + '\'' +
                ", parentsCoveredInBp=" + parentsCoveredInBp +
                ", crossCombAllowed=" + crossCombAllowed +
                ", isParentalPremiumCharged=" + isParentalPremiumCharged +
                ", parentalInformationData=" + parentalInformationData +
                '}';
    }
}
