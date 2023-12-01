package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberInformation {

    @SerializedName("BENEFICIARY_NAME")
    @Expose
    private String beneficiaryName;
    @SerializedName("EMPLOYEE_NAME")
    @Expose
    private String employeeName;
    @SerializedName("EMPLOYEE_NO")
    @Expose
    private String employeeNo;
    @SerializedName("AGE")
    @Expose
    private String age;
    @SerializedName("DATE_OF_BIRTH")
    @Expose
    private String dateOfBirth;
    @SerializedName("GENDER")
    @Expose
    private String gender;
    @SerializedName("GRADE")
    @Expose
    private String grade;
    @SerializedName("PLANT_DEPT")
    @Expose
    private String plantDept;
    @SerializedName("CITY")
    @Expose
    private String city;
    @SerializedName("SUM_INSURED")
    @Expose
    private String sumInsured;
    @SerializedName("TPAId")
    @Expose
    private String tPAId;
    @SerializedName("Relation")
    @Expose
    private String relation;

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPlantDept() {
        return plantDept;
    }

    public void setPlantDept(String plantDept) {
        this.plantDept = plantDept;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(String sumInsured) {
        this.sumInsured = sumInsured;
    }

    public String getTPAId() {
        return tPAId;
    }

    public void setTPAId(String tPAId) {
        this.tPAId = tPAId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "MemberInformation{" +
                "beneficiaryName='" + beneficiaryName + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", employeeNo='" + employeeNo + '\'' +
                ", age='" + age + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", grade='" + grade + '\'' +
                ", plantDept='" + plantDept + '\'' +
                ", city='" + city + '\'' +
                ", sumInsured='" + sumInsured + '\'' +
                ", tPAId='" + tPAId + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
