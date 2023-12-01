package com.indiainsure.android.MB360.insurance.queries.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalRecords {
    @SerializedName("Count")
    @Expose
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TotalRecords{" +
                "count=" + count +
                '}';
    }
}
