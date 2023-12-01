package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimHospitalInformation {

    public String HospitalNo;
    public String HospitalName;
    public String IsInNetwork;
    public String NetworkCity;
    public String NetworkState;
    public String LevelOfCare;
    public String DateOfAdmission;
    public String DateOfDischarge;
    public String LengthOfStay;

    public String getHospitalNo() {
        return HospitalNo;
    }

    public void setHospitalNo(String hospitalNo) {
        HospitalNo = hospitalNo;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getIsInNetwork() {
        return IsInNetwork;
    }

    public void setIsInNetwork(String isInNetwork) {
        IsInNetwork = isInNetwork;
    }

    public String getNetworkCity() {
        return NetworkCity;
    }

    public void setNetworkCity(String networkCity) {
        NetworkCity = networkCity;
    }

    public String getNetworkState() {
        return NetworkState;
    }

    public void setNetworkState(String networkState) {
        NetworkState = networkState;
    }

    public String getLevelOfCare() {
        return LevelOfCare;
    }

    public void setLevelOfCare(String levelOfCare) {
        LevelOfCare = levelOfCare;
    }

    public String getDateOfAdmission() {
        return DateOfAdmission;
    }

    public void setDateOfAdmission(String dateOfAdmission) {
        DateOfAdmission = dateOfAdmission;
    }

    public String getDateOfDischarge() {
        return DateOfDischarge;
    }

    public void setDateOfDischarge(String dateOfDischarge) {
        DateOfDischarge = dateOfDischarge;
    }

    public String getLengthOfStay() {
        return LengthOfStay;
    }

    public void setLengthOfStay(String lengthOfStay) {
        LengthOfStay = lengthOfStay;
    }

    @Override
    public String toString() {
        return "ClaimHospitalInformation{" +
                "HospitalNo=" + HospitalNo +
                ", HospitalName='" + HospitalName + '\'' +
                ", IsInNetwork='" + IsInNetwork + '\'' +
                ", NetworkCity='" + NetworkCity + '\'' +
                ", NetworkState='" + NetworkState + '\'' +
                ", LevelOfCare='" + LevelOfCare + '\'' +
                ", DateOfAdmission='" + DateOfAdmission + '\'' +
                ", DateOfDischarge='" + DateOfDischarge + '\'' +
                ", LengthOfStay=" + LengthOfStay +
                '}';
    }
}
