package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WPForNewJoineeData {

    @SerializedName("STARTS_FROM_DATE_OF_JOINING")
    @Expose
    private String STARTS_FROM_DATE_OF_JOINING;
    @SerializedName("STARTS_FROM_DATE_OF_DATAINSERT")
    @Expose
    private String STARTS_FROM_DATE_OF_DATAINSERT;
    @SerializedName("DATE_DURATION")
    @Expose
    private String DATE_DURATION;

    public String getSTARTS_FROM_DATE_OF_JOINING() {
        return STARTS_FROM_DATE_OF_JOINING;
    }

    public void setSTARTS_FROM_DATE_OF_JOINING(String STARTS_FROM_DATE_OF_JOINING) {
        this.STARTS_FROM_DATE_OF_JOINING = STARTS_FROM_DATE_OF_JOINING;
    }

    public String getSTARTS_FROM_DATE_OF_DATAINSERT() {
        return STARTS_FROM_DATE_OF_DATAINSERT;
    }

    public void setSTARTS_FROM_DATE_OF_DATAINSERT(String STARTS_FROM_DATE_OF_DATAINSERT) {
        this.STARTS_FROM_DATE_OF_DATAINSERT = STARTS_FROM_DATE_OF_DATAINSERT;
    }

    public String getDATE_DURATION() {
        return DATE_DURATION;
    }

    public void setDATE_DURATION(String DATE_DURATION) {
        this.DATE_DURATION = DATE_DURATION;
    }

    @Override
    public String toString() {
        return "WP_ForNewJoinee_data{" +
                "STARTS_FROM_DATE_OF_JOINING='" + STARTS_FROM_DATE_OF_JOINING + '\'' +
                ", STARTS_FROM_DATE_OF_DATAINSERT='" + STARTS_FROM_DATE_OF_DATAINSERT + '\'' +
                ", DATE_DURATION='" + DATE_DURATION + '\'' +
                '}';
    }
}
