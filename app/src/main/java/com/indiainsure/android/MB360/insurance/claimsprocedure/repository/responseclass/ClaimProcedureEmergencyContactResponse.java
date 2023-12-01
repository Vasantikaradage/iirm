package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.claimProcedureConverters.EmergencyContactNoConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "CLAIM_PROCEDURE_EMERGENCY_CONTACT", indices = @Index(value = {"tpa_code"}, unique = true))
@TypeConverters(EmergencyContactNoConverter.class)
public class ClaimProcedureEmergencyContactResponse {
    @SerializedName("EmergencyContactNo")
    @Expose

    private List<EmergencyContactNo> emergencyContactNo = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String tpa_code;

    @NonNull
    public String getTpa_code() {
        return tpa_code;
    }

    public void setTpa_code(@NonNull String tpa_code) {
        this.tpa_code = tpa_code;
    }

    public List<EmergencyContactNo> getEmergencyContactNo() {
        return emergencyContactNo;
    }

    public void setEmergencyContactNo(List<EmergencyContactNo> emergencyContactNo) {
        this.emergencyContactNo = emergencyContactNo;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClaimProcedureEmergencyContactResponse{" +
                "emergencyContactNo=" + emergencyContactNo +
                ", message=" + message +
                '}';
    }
}
