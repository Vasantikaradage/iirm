package com.indiainsure.android.MB360.insurance.servicenames.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.ServiceNamesConverter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "SERVICE_NAMES",indices = @Index(value = {"strGroupChildSrno"}, unique = true))
@TypeConverters(ServiceNamesConverter.class)
public class ServiceNamesResponse {
    @SerializedName("showButtons")
    @Expose
    List<ShowButtons> showButtons = new ArrayList<>();
    @SerializedName("Message")
    @Expose
    Message Message;

    @NonNull
    public String getStrGroupChildSrno() {
        return strGroupChildSrno;
    }

    public void setStrGroupChildSrno(@NonNull String strGroupChildSrno) {
        this.strGroupChildSrno = strGroupChildSrno;
    }

    @PrimaryKey
    @NonNull
    private  String strGroupChildSrno;

    public List<ShowButtons> getShowButtons() {
        return showButtons;
    }

    public void setShowButtons(List<ShowButtons> showButtons) {
        this.showButtons = showButtons;
    }

    public com.indiainsure.android.MB360.insurance.servicenames.responseclass.Message getMessage() {
        return Message;
    }

    public void setMessage(com.indiainsure.android.MB360.insurance.servicenames.responseclass.Message message) {
        Message = message;
    }

    @Override
    public String toString() {
        return "ServiceNamesResponse{" +
                "showButtons=" + showButtons +
                ", Message=" + Message +
                '}';
    }
}
