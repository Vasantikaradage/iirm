package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrollmentMiscInfo {

    @SerializedName("WP_ForNewJoinee_data")
    @Expose
    private WPForNewJoineeData WP_ForNewJoinee_data;
    @SerializedName("WP_DurationForOpting_TopupCoverage_data")
    @Expose
    private WPDurationForOptingTopupCoverageData WP_DurationForOpting_TopupCoverage_data;
    @SerializedName("OpenEnroll_WP_Information_data")
    @Expose
    private OpenEnrollWPInformationData OpenEnroll_WP_Information_data;
    @SerializedName("WP_DurationForOpting_ParentalCoverage_data")
    @Expose
    private WPDurationForOptingParentalCoverageData WP_DurationForOpting_ParentalCoverage_data;

    public WPForNewJoineeData getWP_ForNewJoinee_data() {
        return WP_ForNewJoinee_data;
    }

    public void setWP_ForNewJoinee_data(WPForNewJoineeData WP_ForNewJoinee_data) {
        this.WP_ForNewJoinee_data = WP_ForNewJoinee_data;
    }

    public WPDurationForOptingTopupCoverageData getWP_DurationForOpting_TopupCoverage_data() {
        return WP_DurationForOpting_TopupCoverage_data;
    }

    public void setWP_DurationForOpting_TopupCoverage_data(WPDurationForOptingTopupCoverageData WP_DurationForOpting_TopupCoverage_data) {
        this.WP_DurationForOpting_TopupCoverage_data = WP_DurationForOpting_TopupCoverage_data;
    }

    public OpenEnrollWPInformationData getOpenEnroll_WP_Information_data() {
        return OpenEnroll_WP_Information_data;
    }

    public void setOpenEnroll_WP_Information_data(OpenEnrollWPInformationData openEnroll_WP_Information_data) {
        OpenEnroll_WP_Information_data = openEnroll_WP_Information_data;
    }

    public WPDurationForOptingParentalCoverageData getWP_DurationForOpting_ParentalCoverage_data() {
        return WP_DurationForOpting_ParentalCoverage_data;
    }

    public void setWP_DurationForOpting_ParentalCoverage_data(WPDurationForOptingParentalCoverageData WP_DurationForOpting_ParentalCoverage_data) {
        this.WP_DurationForOpting_ParentalCoverage_data = WP_DurationForOpting_ParentalCoverage_data;
    }

    @Override
    public String toString() {
        return "EnrollmentMiscInfo{" +
                "WP_ForNewJoinee_data=" + WP_ForNewJoinee_data +
                ", WP_DurationForOpting_TopupCoverage_data=" + WP_DurationForOpting_TopupCoverage_data +
                ", OpenEnroll_WP_Information_data=" + OpenEnroll_WP_Information_data +
                ", WP_DurationForOpting_ParentalCoverage_data=" + WP_DurationForOpting_ParentalCoverage_data +
                '}';
    }
}
