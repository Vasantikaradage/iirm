package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.claimProcedureConverters.ClaimProcedureLayoutInfoConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "CLAIM_PROCEDURE_LAYOUT_INFO", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(ClaimProcedureLayoutInfoConverter.class)
public class ClaimsProcedureLayoutInfoResponse {
    public String getLayoutOfClaim() {
        return layoutOfClaim;
    }
    public void setLayoutOfClaim(String layoutOfClaim) {
        this.layoutOfClaim = layoutOfClaim;
    }

    @SerializedName("ClaimProcLayoutInfo")
    @Expose

    private List<ClaimProcLayoutInfo> claimProcLayoutInfo = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String oeGrpBasInfoSrNo;

    private  String layoutOfClaim;



    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public List<ClaimProcLayoutInfo> getClaimProcLayoutInfo() {
        return claimProcLayoutInfo;
    }

    public void setClaimProcLayoutInfo(List<ClaimProcLayoutInfo> claimProcLayoutInfo) {
        this.claimProcLayoutInfo = claimProcLayoutInfo;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClaimsProcedureLayoutInfoResponse{" +
                "claimProcLayoutInfo=" + claimProcLayoutInfo +
                ", message=" + message +
                '}';
    }
}
