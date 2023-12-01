package com.indiainsure.android.MB360.insurance.utilities.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.UtilitiesResponseConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "UTILITIES", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(UtilitiesResponseConverter.class)

public class UtilitiesResponse {
    @SerializedName("UTILITIES_DATA")
    @Expose
    private List<UTILITIESDATum> utilitiesData = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private  String oeGrpBasInfoSrNo;

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public List<UTILITIESDATum> getUtilitiesData() {
        return utilitiesData;
    }

    public void setUtilitiesData(List<UTILITIESDATum> utilitiesData) {
        this.utilitiesData = utilitiesData;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UtilitiesResponse{" +
                "utilitiesData=" + utilitiesData +
                ", message=" + message +
                '}';
    }
}
