package com.indiainsure.android.MB360.onboarding.validation.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidationResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("OTPValidatedInformation")
    @Expose
    private String oTPValidatedInformation;
    @SerializedName("AuthToken")
    @Expose
    private String authToken = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOTPValidatedInformation() {
        return oTPValidatedInformation;
    }

    public void setOTPValidatedInformation(String oTPValidatedInformation) {
        this.oTPValidatedInformation = oTPValidatedInformation;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "status='" + status + '\'' +
                ", oTPValidatedInformation='" + oTPValidatedInformation + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
