package com.indiainsure.android.MB360.insurance.queries.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.AllQueryConverter;

import java.util.List;

@Entity(tableName = "QUERY_TABLE", indices = @Index(value = {"empSrNo"}, unique = true))
@TypeConverters(AllQueryConverter.class)
public class QueryResponse {
    @SerializedName("TotalRecords")
    @Expose
    private TotalRecords totalRecords;
    @SerializedName("AllQueries")
    @Expose

    private List<AllQuery> allQueries = null;
    @SerializedName("message")
    @Expose
    private Message message;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String empSrNo;

    @NonNull
    public String getEmpSrNo() {
        return empSrNo;
    }

    public void setEmpSrNo(@NonNull String empSrNo) {
        this.empSrNo = empSrNo;
    }

    public TotalRecords getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(TotalRecords totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<AllQuery> getAllQueries() {
        return allQueries;
    }

    public void setAllQueries(List<AllQuery> allQueries) {
        this.allQueries = allQueries;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
                "totalRecords=" + totalRecords +
                ", allQueries=" + allQueries +
                ", message=" + message +
                '}';
    }
}
