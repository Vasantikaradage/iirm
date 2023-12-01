package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupInfoData {
    @SerializedName("GROUPNAME")
    @Expose
    private String groupname;
    @SerializedName("GROUPCODE")
    @Expose
    private String groupcode;
    @SerializedName("GROUPCHILDSRNO")
    @Expose
    private String groupchildsrno;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupcode() {
        return groupcode;
    }

    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }

    public String getGroupchildsrno() {
        return groupchildsrno;
    }

    public void setGroupchildsrno(String groupchildsrno) {
        this.groupchildsrno = groupchildsrno;
    }

    @Override
    public String toString() {
        return "GroupInfoData{" +
                "groupname='" + groupname + '\'' +
                ", groupcode='" + groupcode + '\'' +
                ", groupchildsrno='" + groupchildsrno + '\'' +
                '}';
    }
}
