package com.indiainsure.android.MB360.onboarding.authentication.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("OTPStatusInformation")
    @Expose
    private String oTPStatusInformation;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOTPStatusInformation() {
        return oTPStatusInformation;
    }

    public void setOTPStatusInformation(String oTPStatusInformation) {
        this.oTPStatusInformation = oTPStatusInformation;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "status='" + status + '\'' +
                ", oTPStatusInformation='" + oTPStatusInformation + '\'' +
                '}';
    }
}
