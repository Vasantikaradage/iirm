package com.indiainsure.android.MB360.insurance.enrollmentstatus.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrollmentStatus {
    @SerializedName("$id")
    @Expose
    private String $id;
    @SerializedName("IsEnrollmentSaved")
    @Expose
    private Integer isEnrollmentSaved;
    @SerializedName("IsWindowPeriodOpen")
    @Expose
    private Integer isWindowPeriodOpen;
    @SerializedName("message")
    @Expose
    private Message message;

    public String get$id() {
        return $id;
    }

    public void set$id(String $id) {
        this.$id = $id;
    }

    public Integer getIsEnrollmentSaved() {
        return isEnrollmentSaved;
    }

    public void setIsEnrollmentSaved(Integer isEnrollmentSaved) {
        this.isEnrollmentSaved = isEnrollmentSaved;
    }

    public Integer getIsWindowPeriodOpen() {
        return isWindowPeriodOpen;
    }

    public void setIsWindowPeriodOpen(Integer isWindowPeriodOpen) {
        this.isWindowPeriodOpen = isWindowPeriodOpen;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EnrollmentStatus{" +
                "$id='" + $id + '\'' +
                ", isEnrollmentSaved=" + isEnrollmentSaved +
                ", isWindowPeriodOpen=" + isWindowPeriodOpen +
                ", message=" + message +
                '}';
    }
}
