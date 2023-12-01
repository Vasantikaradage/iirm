package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopupOptionsData {

    @SerializedName("benifits_id")
    @Expose
    private String benifitsId;
    @SerializedName("BASE_SI")
    @Expose
    private String baseSi;
    @SerializedName("TopSumInsureds_values")
    @Expose
    private List<TopSumInsuredsValue> topSumInsuredsValues = null;

    public String getBenifitsId() {
        return benifitsId;
    }

    public void setBenifitsId(String benifitsId) {
        this.benifitsId = benifitsId;
    }

    public String getBaseSi() {
        return baseSi;
    }

    public void setBaseSi(String baseSi) {
        this.baseSi = baseSi;
    }

    public List<TopSumInsuredsValue> getTopSumInsuredsValues() {
        return topSumInsuredsValues;
    }

    public void setTopSumInsuredsValues(List<TopSumInsuredsValue> topSumInsuredsValues) {
        this.topSumInsuredsValues = topSumInsuredsValues;
    }



}
