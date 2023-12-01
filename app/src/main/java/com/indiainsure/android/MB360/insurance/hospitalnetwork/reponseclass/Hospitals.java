package com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.simpleframework.xml.Element;

@Entity(tableName = "PROVIDER_NETWORK_v1", indices = @Index(value = {"index"}))
public class Hospitals {
    @Element(name = "HospitalName",required = false)
    public String HospitalName;
    @Element(name = "HospitalAddress",required = false)
    public String HospitalAddress;
    @Element(name = "HospitalContactNo",required = false)
    public String HospitalContactNo;
    @Element(name = "HospitalLevelOfCare",required = false)
    public String HospitalLevelOfCare;
    @Element(name = "Latitude",required = false)
    public String Latitude;
    @Element(name = "Longitude",required = false)
    public String Longitude;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String hospitalName) {
        HospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return HospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        HospitalAddress = hospitalAddress;
    }

    public String getHospitalContactNo() {
        return HospitalContactNo;
    }

    public void setHospitalContactNo(String hospitalContactNo) {
        HospitalContactNo = hospitalContactNo;
    }

    public String getHospitalLevelOfCare() {
        return HospitalLevelOfCare;
    }

    public void setHospitalLevelOfCare(String hospitalLevelOfCare) {
        HospitalLevelOfCare = hospitalLevelOfCare;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    @Override
    public String toString() {
        return "Hospitals{" +
                "HospitalName='" + HospitalName + '\'' +
                ", HospitalAddress='" + HospitalAddress + '\'' +
                ", HospitalContactNo='" + HospitalContactNo + '\'' +
                ", HospitalLevelOfCare='" + HospitalLevelOfCare + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                '}';
    }
}
