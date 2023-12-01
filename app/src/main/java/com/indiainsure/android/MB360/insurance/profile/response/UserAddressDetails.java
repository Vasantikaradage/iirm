package com.indiainsure.android.MB360.insurance.profile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAddressDetails {

    @SerializedName("EMP_PER_ADDR_LINE1")
    @Expose
    private String empPerAddrLine1;
    @SerializedName("EMP_PER_ADDR_LINE2")
    @Expose
    private String empPerAddrLine2;
    @SerializedName("EMP_PER_ADDR_LANDMARK")
    @Expose
    private String empPerAddrLandmark;
    @SerializedName("EMP_CITY")
    @Expose
    private String empCity;
    @SerializedName("EMP_STATE")
    @Expose
    private String empState;
    @SerializedName("EMP_PINCODE")
    @Expose
    private String empPincode;

    public String getEmpPerAddrLine1() {
        return empPerAddrLine1;
    }

    public void setEmpPerAddrLine1(String empPerAddrLine1) {
        this.empPerAddrLine1 = empPerAddrLine1;
    }

    public String getEmpPerAddrLine2() {
        return empPerAddrLine2;
    }

    public void setEmpPerAddrLine2(String empPerAddrLine2) {
        this.empPerAddrLine2 = empPerAddrLine2;
    }

    public String getEmpPerAddrLandmark() {
        return empPerAddrLandmark;
    }

    public void setEmpPerAddrLandmark(String empPerAddrLandmark) {
        this.empPerAddrLandmark = empPerAddrLandmark;
    }

    public String getEmpCity() {
        return empCity;
    }

    public void setEmpCity(String empCity) {
        this.empCity = empCity;
    }

    public String getEmpState() {
        return empState;
    }

    public void setEmpState(String empState) {
        this.empState = empState;
    }

    public String getEmpPincode() {
        return empPincode;
    }

    public void setEmpPincode(String empPincode) {
        this.empPincode = empPincode;
    }

    @Override
    public String toString() {
        return "UserAddressDetails{" +
                "empPerAddrLine1='" + empPerAddrLine1 + '\'' +
                ", empPerAddrLine2='" + empPerAddrLine2 + '\'' +
                ", empPerAddrLandmark='" + empPerAddrLandmark + '\'' +
                ", empCity='" + empCity + '\'' +
                ", empState='" + empState + '\'' +
                ", empPincode='" + empPincode + '\'' +
                '}';
    }
}
