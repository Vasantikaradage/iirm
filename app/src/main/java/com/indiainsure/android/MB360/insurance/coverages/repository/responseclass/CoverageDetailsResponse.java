package com.indiainsure.android.MB360.insurance.coverages.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.coverageConverters.CoverageDetailConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "COVERAGE_DETAILS", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(CoverageDetailConverter.class)
public class CoverageDetailsResponse {
    @SerializedName("Coverages_Data")
    @Expose
    private List<CoveragesDatum> coveragesData = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private  String  oeGrpBasInfoSrNo;

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public List<CoveragesDatum> getCoveragesData() {
        return coveragesData;
    }

    public void setCoveragesData(List<CoveragesDatum> coveragesData) {
        this.coveragesData = coveragesData;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CoverageDetailsResponse{" +
                "coveragesData=" + coveragesData +
                ", message=" + message +
                '}';
    }
}

