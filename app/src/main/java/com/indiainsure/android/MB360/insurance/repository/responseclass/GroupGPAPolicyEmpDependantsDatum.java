package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupGPAPolicyEmpDependantsDatum {
    @SerializedName("PERSON_SR_NO")
    @Expose
    private String personSrNo;
    @SerializedName("EMPLOYEE_SR_NO")
    @Expose
    private String employeeSrNo;
    @SerializedName("AGE")
    @Expose
    private String age;
    @SerializedName("DATE_OF_BIRTH")
    @Expose
    private String dateOfBirth;
    @SerializedName("CELLPHONE_NUMBER")
    @Expose
    private String cellphoneNumber;
    @SerializedName("EMERGENCY_CONTACT_NUMBER")
    @Expose
    private String emergencyContactNumber;
    @SerializedName("PERSON_NAME")
    @Expose
    private String personName;
    @SerializedName("GENDER")
    @Expose
    private String gender;
    @SerializedName("RELATION_NAME")
    @Expose
    private String relationName;
    @SerializedName("RELATIONID")
    @Expose
    private String relationid;

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    public String getEmployeeSrNo() {
        return employeeSrNo;
    }

    public void setEmployeeSrNo(String employeeSrNo) {
        this.employeeSrNo = employeeSrNo;
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

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getRelationid() {
        return relationid;
    }

    public void setRelationid(String relationid) {
        this.relationid = relationid;
    }

    @Override
    public String toString() {
        return "GroupGPAPolicyEmpDependantsDatum{" +
                "personSrNo='" + personSrNo + '\'' +
                ", employeeSrNo='" + employeeSrNo + '\'' +
                ", age='" + age + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", cellphoneNumber='" + cellphoneNumber + '\'' +
                ", emergencyContactNumber='" + emergencyContactNumber + '\'' +
                ", personName='" + personName + '\'' +
                ", gender='" + gender + '\'' +
                ", relationName='" + relationName + '\'' +
                ", relationid='" + relationid + '\'' +
                '}';
    }
}

