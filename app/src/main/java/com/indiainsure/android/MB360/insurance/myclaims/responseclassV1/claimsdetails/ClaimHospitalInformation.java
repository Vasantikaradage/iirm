package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimHospitalInformation {

    @SerializedName("HOSPITAL_NO")
    @Expose
    private String hospitalNo;
    @SerializedName("HOSPITAL_NAME")
    @Expose
    private String hospitalName;
    @SerializedName("IS_IN_NETWORK")
    @Expose
    private String isInNetwork;
    @SerializedName("NETWORK_CITY")
    @Expose
    private String networkCity;
    @SerializedName("NetworkState")
    @Expose
    private String networkState;
    @SerializedName("LevelOfCare")
    @Expose
    private String levelOfCare;
    @SerializedName("DateOfAdmission")
    @Expose
    private String dateOfAdmission;
    @SerializedName("DateOfDischarge")
    @Expose
    private String dateOfDischarge;
    @SerializedName("LengthOfStay")
    @Expose
    private String lengthOfStay;

    public String getHospitalNo() {
        return hospitalNo;
    }

    public void setHospitalNo(String hospitalNo) {
        this.hospitalNo = hospitalNo;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getIsInNetwork() {
        return isInNetwork;
    }

    public void setIsInNetwork(String isInNetwork) {
        this.isInNetwork = isInNetwork;
    }

    public String getNetworkCity() {
        return networkCity;
    }

    public void setNetworkCity(String networkCity) {
        this.networkCity = networkCity;
    }

    public String getNetworkState() {
        return networkState;
    }

    public void setNetworkState(String networkState) {
        this.networkState = networkState;
    }

    public String getLevelOfCare() {
        return levelOfCare;
    }

    public void setLevelOfCare(String levelOfCare) {
        this.levelOfCare = levelOfCare;
    }

    public String getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(String dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public String getDateOfDischarge() {
        return dateOfDischarge;
    }

    public void setDateOfDischarge(String dateOfDischarge) {
        this.dateOfDischarge = dateOfDischarge;
    }

    public String getLengthOfStay() {
        return lengthOfStay;
    }

    public void setLengthOfStay(String lengthOfStay) {
        this.lengthOfStay = lengthOfStay;
    }

    @Override
    public String toString() {
        return "ClaimHospitalInformation{" +
                "hospitalNo='" + hospitalNo + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", isInNetwork='" + isInNetwork + '\'' +
                ", networkCity='" + networkCity + '\'' +
                ", networkState='" + networkState + '\'' +
                ", levelOfCare='" + levelOfCare + '\'' +
                ", dateOfAdmission='" + dateOfAdmission + '\'' +
                ", dateOfDischarge='" + dateOfDischarge + '\'' +
                ", lengthOfStay='" + lengthOfStay + '\'' +
                '}';
    }
}
