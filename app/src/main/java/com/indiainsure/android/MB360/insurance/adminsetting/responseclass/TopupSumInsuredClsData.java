package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopupSumInsuredClsData {

    @SerializedName("GMCTopupOptions_data")
    @Expose
    private List<TopupOptionsData> gMCTopupOptionsData = null;
    @SerializedName("GPATopupOptions_data")
    @Expose
    private List<TopupOptionsData> gPATopupOptionsData = null;
    @SerializedName("GTLTopupOptions_data")
    @Expose
    private List<TopupOptionsData> gTLTopupOptionsData = null;

    public List<TopupOptionsData> getGMCTopupOptionsData() {
        return gMCTopupOptionsData;
    }

    public void setGMCTopupOptionsData(List<TopupOptionsData> gMCTopupOptionsData) {
        this.gMCTopupOptionsData = gMCTopupOptionsData;
    }

    public List<TopupOptionsData> getGPATopupOptionsData() {
        return gPATopupOptionsData;
    }

    public void setGPATopupOptionsData(List<TopupOptionsData> gPATopupOptionsData) {
        this.gPATopupOptionsData = gPATopupOptionsData;
    }

    public List<TopupOptionsData> getGTLTopupOptionsData() {
        return gTLTopupOptionsData;
    }

    public void setGTLTopupOptionsData(List<TopupOptionsData> gTLTopupOptionsData) {
        this.gTLTopupOptionsData = gTLTopupOptionsData;
    }

}


