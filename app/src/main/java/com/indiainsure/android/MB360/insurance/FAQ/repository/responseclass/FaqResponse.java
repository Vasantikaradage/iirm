package com.indiainsure.android.MB360.insurance.FAQ.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.FaqDataConverter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "FAQ", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(FaqDataConverter.class)
public class FaqResponse {
    @SerializedName("FAQ_DATA")
    @Expose

    private List<FaqData> faqData = new ArrayList<>();
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String oeGrpBasInfoSrNo;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<FaqData> getFaqData() {
        return faqData;
    }

    public void setFaqData(List<FaqData> faqData) {
        this.faqData = faqData;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    @Override
    public String toString() {
        return "FaqResponse{" +
                "faqData=" + faqData +
                ", message=" + message +
                '}';
    }
}
