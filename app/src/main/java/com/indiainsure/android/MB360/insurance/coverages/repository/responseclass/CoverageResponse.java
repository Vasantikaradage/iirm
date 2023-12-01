package com.indiainsure.android.MB360.insurance.coverages.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.coverageConverters.CoverageResponseConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "COVERAGE", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(CoverageResponseConverter.class)

public class CoverageResponse {

    @SerializedName("CoveragePolicy_Data")
    @Expose
    private List<CoveragePolicyDatum> coveragePolicyData = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;


    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String oeGrpBasInfoSrNo;

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public List<CoveragePolicyDatum> getCoveragePolicyData() {
        return coveragePolicyData;
    }

    public void setCoveragePolicyData(List<CoveragePolicyDatum> coveragePolicyData) {
        this.coveragePolicyData = coveragePolicyData;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CoverageResponse{" +
                "coveragePolicyData=" + coveragePolicyData +
                ", message=" + message +
                '}';
    }
}
