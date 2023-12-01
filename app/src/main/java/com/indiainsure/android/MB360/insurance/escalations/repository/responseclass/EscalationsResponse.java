package com.indiainsure.android.MB360.insurance.escalations.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.EscalationsResponseConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "ESCALATION", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))

@TypeConverters(EscalationsResponseConverter.class)

public class EscalationsResponse {
    @SerializedName("GroupEscalationInfo")
    @Expose
    private List<GroupEscalationInfo> groupEscalationInfo  = new ArrayList<>();
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

    public List<GroupEscalationInfo> getGroupEscalationInfo() {
        return groupEscalationInfo;
    }

    public void setGroupEscalationInfo(List<GroupEscalationInfo> groupEscalationInfo) {
        this.groupEscalationInfo = groupEscalationInfo;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EscalationsResponse{" +
                "groupEscalationInfo=" + groupEscalationInfo +
                ", message=" + message +
                '}';
    }
}
