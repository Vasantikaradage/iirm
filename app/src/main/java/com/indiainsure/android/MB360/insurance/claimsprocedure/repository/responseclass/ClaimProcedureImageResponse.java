package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.claimProcedureConverters.ClaimProcedureImageConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "CLAIM_PROCEDURE_IMAGE", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(ClaimProcedureImageConverter.class)
public class ClaimProcedureImageResponse {
    @SerializedName("ClaimProcPart1ImagePath")
    @Expose
    private List<ClaimProcImagePath> claimProcImagePath = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String oeGrpBasInfoSrNo;

    private  String layoutOfClaim;

    public String getLayoutOfClaim() {
        return layoutOfClaim;
    }

    public void setLayoutOfClaim(String layoutOfClaim) {
        this.layoutOfClaim = layoutOfClaim;
    }

    public void setClaimProcImagePath(List<ClaimProcImagePath> claimProcImagePath) {
        this.claimProcImagePath = claimProcImagePath;
    }

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public List<ClaimProcImagePath> getClaimProcImagePath() {
        return claimProcImagePath;
    }

    public void setClaimProcPart1ImagePath(List<ClaimProcImagePath> claimProcPart1ImagePath) {
        this.claimProcImagePath = claimProcPart1ImagePath;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClaimProcedureImageResponse{" +
                "claimProcImagePath=" + claimProcImagePath +
                ", message=" + message +
                '}';
    }
}
