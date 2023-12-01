package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.claimProcedureConverters.ClaimProcedureTextPathConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "CLAIM_PROCEDURE_TEXT_PATH", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(ClaimProcedureTextPathConverter.class)
public class ClaimProcedureTextResponse {
    @SerializedName("ClaimProcTextPath")
    @Expose
    private List<ClaimProcTextPath> claimProcTextPath = new ArrayList<>();
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

    public List<ClaimProcTextPath> getClaimProcTextPath() {
        return claimProcTextPath;
    }

    public void setClaimProcTextPath(List<ClaimProcTextPath> claimProcTextPath) {
        this.claimProcTextPath = claimProcTextPath;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClaimProcedureTextResponse{" +
                "claimProcTextPath=" + claimProcTextPath +
                ", message=" + message +
                '}';
    }
}
