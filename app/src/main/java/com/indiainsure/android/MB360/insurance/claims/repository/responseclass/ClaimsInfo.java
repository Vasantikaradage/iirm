
package com.indiainsure.android.MB360.insurance.claims.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimsInfo {

    @SerializedName("INTIMATION_SR_NO")
    @Expose
    private String intimationSrNo;
    @SerializedName("PERSON_NAME")
    @Expose
    private String personName;
    @SerializedName("INTIMATIONS")
    @Expose
    private String intimations;
    @SerializedName("EMPLOYEE_NAME")
    @Expose
    private String employeeName;
    @SerializedName("EMPLOYEE_NO")
    @Expose
    private String employeeNo;
    @SerializedName("NAME_OF_HOSPITAL")
    @Expose
    private String nameOfHospital;
    @SerializedName("DOA_LIKELYDOA")
    @Expose
    private String doaLikelydoa;
    @SerializedName("DIAGNOSIS_OR_AILMENT")
    @Expose
    private String diagnosisOrAilment;
    @SerializedName("CLAIM_AMOUNT")
    @Expose
    private String claimAmount;

    private boolean isExpanded;

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getIntimationSrNo() {
        return intimationSrNo;
    }

    public void setIntimationSrNo(String intimationSrNo) {
        this.intimationSrNo = intimationSrNo;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getIntimations() {
        return intimations;
    }

    public void setIntimations(String intimations) {
        this.intimations = intimations;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getNameOfHospital() {
        return nameOfHospital;
    }

    public void setNameOfHospital(String nameOfHospital) {
        this.nameOfHospital = nameOfHospital;
    }

    public String getDoaLikelydoa() {
        return doaLikelydoa;
    }

    public void setDoaLikelydoa(String doaLikelydoa) {
        this.doaLikelydoa = doaLikelydoa;
    }

    public String getDiagnosisOrAilment() {
        return diagnosisOrAilment;
    }

    public void setDiagnosisOrAilment(String diagnosisOrAilment) {
        this.diagnosisOrAilment = diagnosisOrAilment;
    }

    public String getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        this.claimAmount = claimAmount;
    }

    @Override
    public String toString() {
        return "ClaimsInfo{" +
                "intimationSrNo='" + intimationSrNo + '\'' +
                ", personName='" + personName + '\'' +
                ", intimations='" + intimations + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", nameOfHospital='" + nameOfHospital + '\'' +
                ", doaLikelydoa='" + doaLikelydoa + '\'' +
                ", diagnosisOrAilment='" + diagnosisOrAilment + '\'' +
                ", claimAmount='" + claimAmount + '\'' +
                '}';
    }
}
