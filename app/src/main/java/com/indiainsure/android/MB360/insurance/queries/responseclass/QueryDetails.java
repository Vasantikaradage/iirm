package com.indiainsure.android.MB360.insurance.queries.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.ListOfQueriesConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "QUERY_DETAIL_LIST", indices = @Index(value = {"custQuerySrNo"}, unique = true))
@TypeConverters(ListOfQueriesConverter.class)

public class QueryDetails {

    @SerializedName("ListOfQueries")
    @Expose
    private List<ListOfQuery> listOfQueries = new ArrayList<>();
    @SerializedName("message")
    @Expose
    private Message message;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String custQuerySrNo;

    @NonNull
    public String getCustQuerySrNo() {
        return custQuerySrNo;
    }

    public void setCustQuerySrNo(@NonNull String custQuerySrNo) {
        this.custQuerySrNo = custQuerySrNo;
    }

    public List<ListOfQuery> getListOfQueries() {
        return listOfQueries;
    }

    public void setListOfQueries(List<ListOfQuery> listOfQueries) {
        this.listOfQueries = listOfQueries;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "QueryDetails{" +
                "listOfQueries=" + listOfQueries +
                ", message=" + message +
                '}';
    }
}
