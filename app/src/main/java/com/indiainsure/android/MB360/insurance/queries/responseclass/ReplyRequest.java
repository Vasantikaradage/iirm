package com.indiainsure.android.MB360.insurance.queries.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReplyRequest {
    @SerializedName("EmpSrNo")
    @Expose
    private String EmpSrNo;
    @SerializedName("Query")
    @Expose
    private String Query;
    @SerializedName("CustQuerySrNo")
    @Expose
    private String CustQuerySrNo;
    @SerializedName("IsReply")
    @Expose
    private Boolean IsReply;

    public String getEmpSrNo() {
        return EmpSrNo;
    }

    public void setEmpSrNo(String EmpSrNo) {
        this.EmpSrNo = EmpSrNo;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String Query) {
        this.Query = Query;
    }

    public String getCustQuerySrNo() {
        return CustQuerySrNo;
    }

    public void setCustQuerySrNo(String CustQuerySrNo) {
        this.CustQuerySrNo = CustQuerySrNo;
    }

    public Boolean getIsReply() {
        return IsReply;
    }

    public void setIsReply(Boolean IsReply) {
        this.IsReply = IsReply;
    }

    @Override
    public String toString() {
        return "ReplyRequest{" +
                "empSrNo='" + EmpSrNo + '\'' +
                ", query='" + Query + '\'' +
                ", custQuerySrNo='" + CustQuerySrNo + '\'' +
                ", isReply=" + IsReply +
                '}';
    }
}
