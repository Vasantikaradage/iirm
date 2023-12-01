package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimAilmentInformation {
    public String Ailment;
    public String FinalDiagnosis;
    public String DiseaseCategory;
    public String ICDCode;

    public String getAilment() {
        return Ailment;
    }

    public void setAilment(String ailment) {
        Ailment = ailment;
    }

    public String getFinalDiagnosis() {
        return FinalDiagnosis;
    }

    public void setFinalDiagnosis(String finalDiagnosis) {
        FinalDiagnosis = finalDiagnosis;
    }

    public String getDiseaseCategory() {
        return DiseaseCategory;
    }

    public void setDiseaseCategory(String diseaseCategory) {
        DiseaseCategory = diseaseCategory;
    }

    public String getICDCode() {
        return ICDCode;
    }

    public void setICDCode(String ICDCode) {
        this.ICDCode = ICDCode;
    }

    @Override
    public String toString() {
        return "ClaimAilmentInformation{" +
                "Ailment='" + Ailment + '\'' +
                ", FinalDiagnosis='" + FinalDiagnosis + '\'' +
                ", DiseaseCategory='" + DiseaseCategory + '\'' +
                ", ICDCode='" + ICDCode + '\'' +
                '}';
    }
}
