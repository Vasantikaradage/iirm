package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopSumInsuredsValue {

    @SerializedName("TSumInsured")
    @Expose
    private String tSumInsured;
    @SerializedName("TSumInsured_Premium")
    @Expose
    private String tSumInsuredPremium;
    @SerializedName("Opted")
    @Expose
    private String opted;
    @SerializedName("Premium_to_show")
    @Expose
    private String premiumToShow;
    @SerializedName("Employee_contribution")
    @Expose
    private String employeeContribution;
    @SerializedName("Employer_contribution")
    @Expose
    private String employerContribution;

    public String getTSumInsured() {
        return tSumInsured;
    }

    public void setTSumInsured(String tSumInsured) {
        this.tSumInsured = tSumInsured;
    }

    public String getTSumInsuredPremium() {
        return tSumInsuredPremium;
    }

    public void setTSumInsuredPremium(String tSumInsuredPremium) {
        this.tSumInsuredPremium = tSumInsuredPremium;
    }

    public String getOpted() {
        return opted;
    }

    public void setOpted(String opted) {
        this.opted = opted;
    }

    public String getPremiumToShow() {
        return premiumToShow;
    }

    public void setPremiumToShow(String premiumToShow) {
        this.premiumToShow = premiumToShow;
    }

    public String getEmployeeContribution() {
        return employeeContribution;
    }

    public void setEmployeeContribution(String employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public String getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(String employerContribution) {
        this.employerContribution = employerContribution;
    }

    @Override
    public String toString() {
        return "TopSumInsuredsValue{" +
                "tSumInsured='" + tSumInsured + '\'' +
                ", tSumInsuredPremium='" + tSumInsuredPremium + '\'' +
                ", opted='" + opted + '\'' +
                ", premiumToShow='" + premiumToShow + '\'' +
                ", employeeContribution='" + employeeContribution + '\'' +
                ", employerContribution='" + employerContribution + '\'' +
                '}';
    }
}
