package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupRelation {


    @SerializedName("RELATION")
    @Expose
    private String RELATION;
    @SerializedName("MIN_AGE")
    @Expose
    private String MIN_AGE;
    @SerializedName("RELATION_ID")
    @Expose
    private String RELATION_ID;
    @SerializedName("MAX_AGE")
    @Expose
    private String MAX_AGE;

    public String getRELATION() {
        return RELATION;
    }

    public void setRELATION(String RELATION) {
        this.RELATION = RELATION;
    }

    public String getMIN_AGE() {
        return MIN_AGE;
    }

    public void setMIN_AGE(String MIN_AGE) {
        this.MIN_AGE = MIN_AGE;
    }

    public String getRELATION_ID() {
        return RELATION_ID;
    }

    public void setRELATION_ID(String RELATION_ID) {
        this.RELATION_ID = RELATION_ID;
    }

    public String getMAX_AGE() {
        return MAX_AGE;
    }

    public void setMAX_AGE(String MAX_AGE) {
        this.MAX_AGE = MAX_AGE;
    }

    @Override
    public String toString() {
        return "GroupRelation{" +
                "RELATION='" + RELATION + '\'' +
                ", MIN_AGE='" + MIN_AGE + '\'' +
                ", RELATION_ID='" + RELATION_ID + '\'' +
                ", MAX_AGE='" + MAX_AGE + '\'' +
                '}';
    }
}
