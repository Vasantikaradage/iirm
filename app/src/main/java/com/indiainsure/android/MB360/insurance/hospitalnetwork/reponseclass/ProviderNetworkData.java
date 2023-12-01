package com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass;

import androidx.room.PrimaryKey;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name = "DocumentElement")

public class ProviderNetworkData {

    @Element(name = "status")
    public String status;
    @Element(name = "HospitalInformation",required = false)
    public HospitalInformation HospitalInformation;

    public String getStatus() {
        return status;
    }

    @PrimaryKey
    public int oeGrpBasInfoSrNo;


    public void setStatus(String status) {
        this.status = status;
    }

    public HospitalInformation getHospitalInformation() {
        return HospitalInformation;
    }

    public void setHospitalInformation(HospitalInformation hospitalInformation) {
        HospitalInformation = hospitalInformation;
    }

    @Override
    public String toString() {
        return "DocumentElement{" +
                "status='" + status + '\'' +
                ", HospitalInformation=" + HospitalInformation +
                '}';
    }
}
