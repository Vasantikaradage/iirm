package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupGTLPolicyEmployeeDatum {
    @SerializedName("GROUPCHILDSRNO")
    @Expose
    private String groupchildsrno;
    @SerializedName("OE_GRP_BAS_INF_SR_NO")
    @Expose
    private String oeGrpBasInfSrNo;
    @SerializedName("EMPLOYEE_IDENTIFICATION_NO")
    @Expose
    private String employeeIdentificationNo;
    @SerializedName("EMPLOYEE_SR_NO")
    @Expose
    private String employeeSrNo;
    @SerializedName("DATE_OF_JOINING")
    @Expose
    private String dateOfJoining;
    @SerializedName("DATE_OF_DATA_INSERT")
    @Expose
    private String dateOfDataInsert;
    @SerializedName("OFFICIAL_E_MAIL_ID")
    @Expose
    private String officialEMailId;
    @SerializedName("DEPARTMENT")
    @Expose
    private String department;
    @SerializedName("GRADE")
    @Expose
    private String grade;
    @SerializedName("DESIGNATION")
    @Expose
    private String designation;
    @SerializedName("BASE_SUM_INSURED")
    @Expose
    private String baseSumInsured;
    @SerializedName("TOPUP_SUM_INSURED")
    @Expose
    private String topupSumInsured;
    @SerializedName("TOPUP_SI_PK")
    @Expose
    private String topupSiPk;
    @SerializedName("TOPUP_SI_OPTED_FLAG")
    @Expose
    private String topupSiOptedFlag;
    @SerializedName("TOPUP_SI_OPTED")
    @Expose
    private String topupSiOpted;
    @SerializedName("TOPUP_SI_PREMIUM")
    @Expose
    private String topupSiPremium;

    public String getGroupchildsrno() {
        return groupchildsrno;
    }

    public void setGroupchildsrno(String groupchildsrno) {
        this.groupchildsrno = groupchildsrno;
    }

    public String getOeGrpBasInfSrNo() {
        return oeGrpBasInfSrNo;
    }

    public void setOeGrpBasInfSrNo(String oeGrpBasInfSrNo) {
        this.oeGrpBasInfSrNo = oeGrpBasInfSrNo;
    }

    public String getEmployeeIdentificationNo() {
        return employeeIdentificationNo;
    }

    public void setEmployeeIdentificationNo(String employeeIdentificationNo) {
        this.employeeIdentificationNo = employeeIdentificationNo;
    }

    public String getEmployeeSrNo() {
        return employeeSrNo;
    }

    public void setEmployeeSrNo(String employeeSrNo) {
        this.employeeSrNo = employeeSrNo;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDateOfDataInsert() {
        return dateOfDataInsert;
    }

    public void setDateOfDataInsert(String dateOfDataInsert) {
        this.dateOfDataInsert = dateOfDataInsert;
    }

    public String getOfficialEMailId() {
        return officialEMailId;
    }

    public void setOfficialEMailId(String officialEMailId) {
        this.officialEMailId = officialEMailId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBaseSumInsured() {
        return baseSumInsured;
    }

    public void setBaseSumInsured(String baseSumInsured) {
        this.baseSumInsured = baseSumInsured;
    }

    public String getTopupSumInsured() {
        return topupSumInsured;
    }

    public void setTopupSumInsured(String topupSumInsured) {
        this.topupSumInsured = topupSumInsured;
    }

    public String getTopupSiPk() {
        return topupSiPk;
    }

    public void setTopupSiPk(String topupSiPk) {
        this.topupSiPk = topupSiPk;
    }

    public String getTopupSiOptedFlag() {
        return topupSiOptedFlag;
    }

    public void setTopupSiOptedFlag(String topupSiOptedFlag) {
        this.topupSiOptedFlag = topupSiOptedFlag;
    }

    public String getTopupSiOpted() {
        return topupSiOpted;
    }

    public void setTopupSiOpted(String topupSiOpted) {
        this.topupSiOpted = topupSiOpted;
    }

    public String getTopupSiPremium() {
        return topupSiPremium;
    }

    public void setTopupSiPremium(String topupSiPremium) {
        this.topupSiPremium = topupSiPremium;
    }

    @Override
    public String toString() {
        return "GroupGTLPolicyEmployeeDatum{" +
                "groupchildsrno='" + groupchildsrno + '\'' +
                ", oeGrpBasInfSrNo='" + oeGrpBasInfSrNo + '\'' +
                ", employeeIdentificationNo='" + employeeIdentificationNo + '\'' +
                ", employeeSrNo='" + employeeSrNo + '\'' +
                ", dateOfJoining='" + dateOfJoining + '\'' +
                ", dateOfDataInsert='" + dateOfDataInsert + '\'' +
                ", officialEMailId='" + officialEMailId + '\'' +
                ", department='" + department + '\'' +
                ", grade='" + grade + '\'' +
                ", designation='" + designation + '\'' +
                ", baseSumInsured='" + baseSumInsured + '\'' +
                ", topupSumInsured='" + topupSumInsured + '\'' +
                ", topupSiPk='" + topupSiPk + '\'' +
                ", topupSiOptedFlag='" + topupSiOptedFlag + '\'' +
                ", topupSiOpted='" + topupSiOpted + '\'' +
                ", topupSiPremium='" + topupSiPremium + '\'' +
                '}';
    }
}
