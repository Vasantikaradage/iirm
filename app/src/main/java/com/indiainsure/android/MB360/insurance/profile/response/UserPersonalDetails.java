package com.indiainsure.android.MB360.insurance.profile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPersonalDetails {
    @SerializedName("EMPLOYEE_NAME")
    @Expose
    private String employeeName;
    @SerializedName("DATE_OF_BIRTH")
    @Expose
    private String dateOfBirth;
    @SerializedName("AGE")
    @Expose
    private String age;
    @SerializedName("E_MAIL_ID")
    @Expose
    private String eMailId;
    @SerializedName("CELLPHONE_NUMBER")
    @Expose
    private String cellphoneNumber;
    @SerializedName("GENDER")
    @Expose
    private String gender;
    @SerializedName("RELATION")
    @Expose
    private String relation;

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getEMailId() {
        return eMailId;
    }

    public void setEMailId(String eMailId) {
        this.eMailId = eMailId;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "UserPersonalDetails{" +
                "employeeName='" + employeeName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", age='" + age + '\'' +
                ", eMailId='" + eMailId + '\'' +
                ", cellphoneNumber='" + cellphoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
