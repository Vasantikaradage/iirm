package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmergencyContactNo {
    @SerializedName("CONTACT_NUMBER")
    @Expose
    private String contactNumber;

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "EmergencyContactNo{" +
                "contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
