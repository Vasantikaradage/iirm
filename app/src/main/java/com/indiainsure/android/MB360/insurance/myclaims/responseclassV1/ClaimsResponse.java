package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ClaimsResponse {

    @SerializedName("EmployeeClaimsValues")
    @Expose
    private EmployeeClaimsValues employeeClaimsValues;
    @SerializedName("ClaimInformation")
    @Expose
    private List<ClaimInformation> claimInformation;
    @SerializedName("message")
    @Expose
    private Message message;

    public EmployeeClaimsValues getEmployeeClaimsValues() {
        return employeeClaimsValues;
    }

    public void setEmployeeClaimsValues(EmployeeClaimsValues employeeClaimsValues) {
        this.employeeClaimsValues = employeeClaimsValues;
    }

    public List<ClaimInformation> getClaimInformation() {
        return claimInformation;
    }

    public void setClaimInformation(List<ClaimInformation> claimInformation) {
        this.claimInformation = claimInformation;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClaimsResponse{" +
                "employeeClaimsValues=" + employeeClaimsValues +
                ", claimInformation=" + claimInformation +
                ", message=" + message +
                '}';
    }
}
