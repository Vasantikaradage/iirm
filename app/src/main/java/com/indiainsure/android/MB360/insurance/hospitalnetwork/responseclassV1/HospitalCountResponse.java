package com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HospitalCountResponse {
    @SerializedName("HospitalCount")
    @Expose
    private Integer hospitalCount;
    @SerializedName("res")
    @Expose
    private Res res;

    public Integer getHospitalCount() {
        return hospitalCount;
    }

    public void setHospitalCount(Integer hospitalCount) {
        this.hospitalCount = hospitalCount;
    }

    public Res getRes() {
        return res;
    }

    public void setRes(Res res) {
        this.res = res;
    }
}
