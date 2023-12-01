package com.indiainsure.android.MB360.insurance.coverages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoveragesDatum {
    @SerializedName("EMPLOYEE_IDENTIFICATION_NO")
    @Expose
    private String employeeIdentificationNo;
    @SerializedName("PERSON_NAME")
    @Expose
    private String personName;
    @SerializedName("GENDER")
    @Expose
    private String gender;
    @SerializedName("DATE_OF_BIRTH")
    @Expose
    private String dateOfBirth;
    @SerializedName("AGE")
    @Expose
    private String age;
    @SerializedName("SORT_ORDER")
    @Expose
    private String sortOrder;
    @SerializedName("RELATION")
    @Expose
    private String relation;
    @SerializedName("BASE_SUM_INSURED")
    @Expose
    private String baseSumInsured;
    @SerializedName("TOP_UP_FLAG")
    @Expose
    private Integer topUpFlag;
    @SerializedName("TOP_UP_BASE_SUM_INSURED")
    @Expose
    private String topUpBaseSumInsured;

    public String getEmployeeIdentificationNo() {
        return employeeIdentificationNo;
    }

    public void setEmployeeIdentificationNo(String employeeIdentificationNo) {
        this.employeeIdentificationNo = employeeIdentificationNo;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getBaseSumInsured() {
        return baseSumInsured;
    }

    public void setBaseSumInsured(String baseSumInsured) {
        this.baseSumInsured = baseSumInsured;
    }

    public Integer getTopUpFlag() {
        return topUpFlag;
    }

    public void setTopUpFlag(Integer topUpFlag) {
        this.topUpFlag = topUpFlag;
    }

    public String getTopUpBaseSumInsured() {
        return topUpBaseSumInsured;
    }

    public void setTopUpBaseSumInsured(String topUpBaseSumInsured) {
        this.topUpBaseSumInsured = topUpBaseSumInsured;
    }

    @Override
    public String toString() {
        return "CoveragesDatum{" +
                "employeeIdentificationNo='" + employeeIdentificationNo + '\'' +
                ", personName='" + personName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", age='" + age + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", relation='" + relation + '\'' +
                ", baseSumInsured='" + baseSumInsured + '\'' +
                ", topUpFlag=" + topUpFlag +
                ", topUpBaseSumInsured='" + topUpBaseSumInsured + '\'' +
                '}';
    }
}
