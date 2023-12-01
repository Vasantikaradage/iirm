package com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity(tableName = "PROVIDER_NETWORK", indices = @Index(value = {"index"}))
public class HospitalInformation {

    @SerializedName("HOSP_NAME")
    @Expose
    private String hospName;
    @SerializedName("HOSP_ADDRESS")
    @Expose
    private String hospAddress;
    @SerializedName("HOSP_PHONE_NO")
    @Expose
    private String hospPhoneNo;
    @SerializedName("HOSP_LEVEL_OF_CARE")
    @Expose
    private String hospLevelOfCare;
    @SerializedName("LONGITUDE")
    @Expose
    private String longitude;
    @SerializedName("LATITUDE")
    @Expose
    private String latitude;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHospName() {
        return hospName;
    }

    public void setHospName(String hospName) {
        this.hospName = hospName;
    }

    public String getHospAddress() {
        return hospAddress;
    }

    public void setHospAddress(String hospAddress) {
        this.hospAddress = hospAddress;
    }

    public String getHospPhoneNo() {
        return hospPhoneNo;
    }

    public void setHospPhoneNo(String hospPhoneNo) {
        this.hospPhoneNo = hospPhoneNo;
    }

    public String getHospLevelOfCare() {
        return hospLevelOfCare;
    }

    public void setHospLevelOfCare(String hospLevelOfCare) {
        this.hospLevelOfCare = hospLevelOfCare;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "HospitalInformation{" +
                "hospName='" + hospName + '\'' +
                ", hospAddress='" + hospAddress + '\'' +
                ", hospPhoneNo='" + hospPhoneNo + '\'' +
                ", hospLevelOfCare='" + hospLevelOfCare + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
