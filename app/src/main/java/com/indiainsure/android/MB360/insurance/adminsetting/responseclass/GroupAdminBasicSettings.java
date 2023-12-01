package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupAdminBasicSettings {
    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("Server_Date")
    @Expose
    private String serverDate;
    @SerializedName("ENROLLMENT_THROUGH_MB")
    @Expose
    private String enrollmentThroughMb;
    @SerializedName("ENROLMENT_TYPE")
    @Expose
    private String enrolmentType;
    @SerializedName("APP_ENROLLMENT_TYPE")
    @Expose
    private String appEnrollmentType;
    @SerializedName("ENROLLMENT_USER_ID")
    @Expose
    private String enrollmentUserId;
    @SerializedName("PolicyDefinition_data")
    @Expose
    private PolicyDefinitionData policyDefinitionData;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getEnrollmentThroughMb() {
        return enrollmentThroughMb;
    }

    public void setEnrollmentThroughMb(String enrollmentThroughMb) {
        this.enrollmentThroughMb = enrollmentThroughMb;
    }

    public String getEnrolmentType() {
        return enrolmentType;
    }

    public void setEnrolmentType(String enrolmentType) {
        this.enrolmentType = enrolmentType;
    }

    public String getAppEnrollmentType() {
        return appEnrollmentType;
    }

    public void setAppEnrollmentType(String appEnrollmentType) {
        this.appEnrollmentType = appEnrollmentType;
    }

    public String getEnrollmentUserId() {
        return enrollmentUserId;
    }

    public void setEnrollmentUserId(String enrollmentUserId) {
        this.enrollmentUserId = enrollmentUserId;
    }

    public PolicyDefinitionData getPolicyDefinitionData() {
        return policyDefinitionData;
    }

    public void setPolicyDefinitionData(PolicyDefinitionData policyDefinitionData) {
        this.policyDefinitionData = policyDefinitionData;
    }

    @Override
    public String toString() {
        return "GroupAdminBasicSettings{" +
                "$id='" + $id + '\'' +
                ", serverDate='" + serverDate + '\'' +
                ", enrollmentThroughMb='" + enrollmentThroughMb + '\'' +
                ", enrolmentType='" + enrolmentType + '\'' +
                ", appEnrollmentType=" + appEnrollmentType +
                ", enrollmentUserId='" + enrollmentUserId + '\'' +
                ", policyDefinitionData=" + policyDefinitionData +
                '}';
    }
}


