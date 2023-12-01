package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WPDurationForOptingTopupCoverageData {

    @SerializedName("DATE_DURATION")
    @Expose
    private String DATE_DURATION;

    public String getDATE_DURATION() {
        return DATE_DURATION;
    }

    public void setDATE_DURATION(String DATE_DURATION) {
        this.DATE_DURATION = DATE_DURATION;
    }

    @Override
    public String toString() {
        return "WP_DurationForOpting_TopupCoverage_data{" +
                "DATE_DURATION='" + DATE_DURATION + '\'' +
                '}';
    }
}
