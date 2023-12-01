package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimAilmentInformation {
    @SerializedName("AILMENT")
    @Expose
    private String ailment;
    @SerializedName("FINAL_DIAGNOSIS")
    @Expose
    private String finalDiagnosis;
    @SerializedName("DISEASE_CATEGORY")
    @Expose
    private String diseaseCategory;
    @SerializedName("ICD_CODE")
    @Expose
    private String icdCode;

    public String getAilment() {
        return ailment;
    }

    public void setAilment(String ailment) {
        this.ailment = ailment;
    }

    public String getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public void setFinalDiagnosis(String finalDiagnosis) {
        this.finalDiagnosis = finalDiagnosis;
    }

    public String getDiseaseCategory() {
        return diseaseCategory;
    }

    public void setDiseaseCategory(String diseaseCategory) {
        this.diseaseCategory = diseaseCategory;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    @Override
    public String toString() {
        return "ClaimAilmentInformation{" +
                "ailment='" + ailment + '\'' +
                ", finalDiagnosis='" + finalDiagnosis + '\'' +
                ", diseaseCategory='" + diseaseCategory + '\'' +
                ", icdCode='" + icdCode + '\'' +
                '}';
    }
}
