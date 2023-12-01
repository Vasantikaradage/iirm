package com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpeningHours {
    @SerializedName("open_now")
    @Expose
    public Boolean openNow;

    public OpeningHours() {
    }




    public Boolean getOpenNow() {
        return openNow;
    }

    public void setOpenNow(Boolean openNow) {
        this.openNow = openNow;
    }


    @Override
    public String toString() {
        return "OpeningHours{" +
                "openNow=" + openNow +
                '}';
    }

}
