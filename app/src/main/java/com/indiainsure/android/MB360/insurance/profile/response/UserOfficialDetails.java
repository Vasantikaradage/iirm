package com.indiainsure.android.MB360.insurance.profile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOfficialDetails {
    @SerializedName("OFFICIAL_E_MAIL_ID")
    @Expose
    private String officialEMailId;
    @SerializedName("DATE_OF_JOINING")
    @Expose
    private String dateOfJoining;
    @SerializedName("DEPARTMENT")
    @Expose
    private String department;
    @SerializedName("DESIGNATION")
    @Expose
    private String designation;
    @SerializedName("GRADE")
    @Expose
    private String grade;

    public String getOfficialEMailId() {
        return officialEMailId;
    }

    public void setOfficialEMailId(String officialEMailId) {
        this.officialEMailId = officialEMailId;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "UserOfficialDetails{" +
                "officialEMailId='" + officialEMailId + '\'' +
                ", dateOfJoining='" + dateOfJoining + '\'' +
                ", department='" + department + '\'' +
                ", designation='" + designation + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
