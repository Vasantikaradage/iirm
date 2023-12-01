package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EnrollLifeEventInfo {


    @SerializedName("Child_Birth")
    @Expose
    private String Child_Birth;
    @SerializedName("Marriage")
    @Expose
    private String Marriage;

    public String getChild_Birth() {
        return Child_Birth;
    }

    public void setChild_Birth(String child_Birth) {
        Child_Birth = child_Birth;
    }

    public String getMarriage() {
        return Marriage;
    }

    @Override
    public String toString() {
        return "EnrollLifeEventInfo{" +
                "Child_Birth='" + Child_Birth + '\'' +
                ", Marriage='" + Marriage + '\'' +
                '}';
    }

    public void setMarriage(String marriage) {
        Marriage = marriage;
    }
}
