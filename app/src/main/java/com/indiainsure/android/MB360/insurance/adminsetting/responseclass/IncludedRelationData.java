package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IncludedRelationData {
    @SerializedName("Relation")
    @Expose
    private String Relation;

    @SerializedName("RELATION_ID")
    @Expose
    private String RELATION_ID;

    public String getRelation() {
        return Relation;
    }

    public void setRelation(String relation) {
        Relation = relation;
    }

    public String getRELATION_ID() {
        return RELATION_ID;
    }

    public void setRELATION_ID(String RELATION_ID) {
        this.RELATION_ID = RELATION_ID;
    }

    @Override
    public String toString() {
        return "IncludedRelationData{" +
                "Relation='" + Relation + '\'' +
                ", RELATION_ID='" + RELATION_ID + '\'' +
                '}';
    }
}
