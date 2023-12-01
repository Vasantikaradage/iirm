package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenEnrollWPInformationData {


    @SerializedName("EXTENSION_DAYS")
    @Expose
    private String EXTENSION_DAYS;
    @SerializedName("WINDOW_PERIOD_END_DATE")
    @Expose
    private String WINDOW_PERIOD_END_DATE;
    @SerializedName("WINDOW_PERIOD_START_DATE")
    @Expose
    private String WINDOW_PERIOD_START_DATE;
    @SerializedName("DATE_DURATION")
    @Expose
    private String DATE_DURATION;

    public String getEXTENSION_DAYS() {
        return EXTENSION_DAYS;
    }

    public void setEXTENSION_DAYS(String EXTENSION_DAYS) {
        this.EXTENSION_DAYS = EXTENSION_DAYS;
    }

    public String getWINDOW_PERIOD_END_DATE() {
        return WINDOW_PERIOD_END_DATE;
    }

    public void setWINDOW_PERIOD_END_DATE(String WINDOW_PERIOD_END_DATE) {
        this.WINDOW_PERIOD_END_DATE = WINDOW_PERIOD_END_DATE;
    }

    public String getWINDOW_PERIOD_START_DATE() {
        return WINDOW_PERIOD_START_DATE;
    }

    public void setWINDOW_PERIOD_START_DATE(String WINDOW_PERIOD_START_DATE) {
        this.WINDOW_PERIOD_START_DATE = WINDOW_PERIOD_START_DATE;
    }

    public String getDATE_DURATION() {
        return DATE_DURATION;
    }

    public void setDATE_DURATION(String DATE_DURATION) {
        this.DATE_DURATION = DATE_DURATION;
    }

    @Override
    public String toString() {
        return "OpenEnroll_WP_Information_data{" +
                "EXTENSION_DAYS='" + EXTENSION_DAYS + '\'' +
                ", WINDOW_PERIOD_END_DATE='" + WINDOW_PERIOD_END_DATE + '\'' +
                ", WINDOW_PERIOD_START_DATE='" + WINDOW_PERIOD_START_DATE + '\'' +
                ", DATE_DURATION='" + DATE_DURATION + '\'' +
                '}';
    }
}
