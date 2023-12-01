package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopupApplicabilityData {

    @SerializedName("GMCTopup")
    @Expose
    private String gMCTopup;
    @SerializedName("GPATopup")
    @Expose
    private String gPATopup;
    @SerializedName("GTLTopup")
    @Expose
    private String gTLTopup;

    public String getGMCTopup() {
        return gMCTopup;
    }

    public void setGMCTopup(String gMCTopup) {
        this.gMCTopup = gMCTopup;
    }

    public String getGPATopup() {
        return gPATopup;
    }

    public void setGPATopup(String gPATopup) {
        this.gPATopup = gPATopup;
    }

    public String getGTLTopup() {
        return gTLTopup;
    }

    public void setGTLTopup(String gTLTopup) {
        this.gTLTopup = gTLTopup;
    }

}


