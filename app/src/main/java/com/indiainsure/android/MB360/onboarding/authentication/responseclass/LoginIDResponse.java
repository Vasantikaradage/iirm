package com.indiainsure.android.MB360.onboarding.authentication.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginIDResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("UniqueID")
    @Expose
    private String uniqueID;

    @SerializedName("AuthToken")
    @Expose
    private String authToken = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return "LoginIDResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", uniqueID='" + uniqueID + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
