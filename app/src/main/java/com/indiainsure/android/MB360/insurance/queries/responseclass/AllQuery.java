package com.indiainsure.android.MB360.insurance.queries.responseclass;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllQuery implements Parcelable {
    @SerializedName("EQ_CUST_QRY_SR_NO")
    @Expose
    private String eqCustQrySrNo;
    @SerializedName("NO_OF_REPLIES")
    @Expose
    private String noOfReplies;
    @SerializedName("EQ_CUST_QRY_SOLVED")
    @Expose
    private String eqCustQrySolved;
    @SerializedName("EQ_CUST_QRY_ENDED")
    @Expose
    private String eqCustQryEnded;
    @SerializedName("GROUPNAME")
    @Expose
    private String groupname;
    @SerializedName("GROUPCODE")
    @Expose
    private String groupcode;
    @SerializedName("TICKET_NUMBER")
    @Expose
    private String ticketNumber;
    @SerializedName("COMPLETE_QUERY_TEXT")
    @Expose
    private String completeQueryText;
    @SerializedName("PARTIAL_QUERY_TEXT")
    @Expose
    private String partialQueryText;
    @SerializedName("POSTED_DATE")
    @Expose
    private String postedDate;
    @SerializedName("PERSON_NAME")
    @Expose
    private String personName;
    @SerializedName("EMPLOYEE_SR_NO")
    @Expose
    private String employeeSrNo;
    @SerializedName("LAST_REPLIED_ON")
    @Expose
    private String lastRepliedOn;
    @SerializedName("LAST_REPLY")
    @Expose
    private String lastReply;
    @SerializedName("LAST_REPLY1")
    @Expose
    private String lastReply1;
    @SerializedName("LAST_All_TEXT")
    @Expose
    private String lASTAllTEXT;
    @SerializedName("PARTIAL_REPLY_QUERY_TEXT")
    @Expose
    private String partialReplyQueryText;
    @SerializedName("LAST_REPLY_SR_NO")
    @Expose
    private String lastReplySrNo;
    @SerializedName("SOLVED_DATE")
    @Expose
    private String solvedDate;
    @SerializedName("ENDED_DATE")
    @Expose
    private String endedDate;
    @SerializedName("QUERY_SOLVED_FLAG")
    @Expose
    private String querySolvedFlag;
    @SerializedName("QUERY_ENDED_FLAG")
    @Expose
    private String queryEndedFlag;
    @SerializedName("ROW_NUMBER")
    @Expose
    private String rowNumber;

    protected AllQuery(Parcel in) {
        eqCustQrySrNo = in.readString();
        noOfReplies = in.readString();
        eqCustQrySolved = in.readString();
        eqCustQryEnded = in.readString();
        groupname = in.readString();
        groupcode = in.readString();
        ticketNumber = in.readString();
        completeQueryText = in.readString();
        partialQueryText = in.readString();
        postedDate = in.readString();
        personName = in.readString();
        employeeSrNo = in.readString();
        lastRepliedOn = in.readString();
        lastReply = in.readString();
        lastReply1 = in.readString();
        lASTAllTEXT = in.readString();
        partialReplyQueryText = in.readString();
        lastReplySrNo = in.readString();
        solvedDate = in.readString();
        endedDate = in.readString();
        querySolvedFlag = in.readString();
        queryEndedFlag = in.readString();
        rowNumber = in.readString();
    }

    public static final Creator<AllQuery> CREATOR = new Creator<AllQuery>() {
        @Override
        public AllQuery createFromParcel(Parcel in) {
            return new AllQuery(in);
        }

        @Override
        public AllQuery[] newArray(int size) {
            return new AllQuery[size];
        }
    };

    public String getEqCustQrySrNo() {
        return eqCustQrySrNo;
    }

    public void setEqCustQrySrNo(String eqCustQrySrNo) {
        this.eqCustQrySrNo = eqCustQrySrNo;
    }

    public String getNoOfReplies() {
        return noOfReplies;
    }

    public void setNoOfReplies(String noOfReplies) {
        this.noOfReplies = noOfReplies;
    }

    public String getEqCustQrySolved() {
        return eqCustQrySolved;
    }

    public void setEqCustQrySolved(String eqCustQrySolved) {
        this.eqCustQrySolved = eqCustQrySolved;
    }

    public String getEqCustQryEnded() {
        return eqCustQryEnded;
    }

    public void setEqCustQryEnded(String eqCustQryEnded) {
        this.eqCustQryEnded = eqCustQryEnded;
    }

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

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getCompleteQueryText() {
        return completeQueryText;
    }

    public void setCompleteQueryText(String completeQueryText) {
        this.completeQueryText = completeQueryText;
    }

    public String getPartialQueryText() {
        return partialQueryText;
    }

    public void setPartialQueryText(String partialQueryText) {
        this.partialQueryText = partialQueryText;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getEmployeeSrNo() {
        return employeeSrNo;
    }

    public void setEmployeeSrNo(String employeeSrNo) {
        this.employeeSrNo = employeeSrNo;
    }

    public String getLastRepliedOn() {
        return lastRepliedOn;
    }

    public void setLastRepliedOn(String lastRepliedOn) {
        this.lastRepliedOn = lastRepliedOn;
    }

    public String getLastReply() {
        return lastReply;
    }

    public void setLastReply(String lastReply) {
        this.lastReply = lastReply;
    }

    public String getLastReply1() {
        return lastReply1;
    }

    public void setLastReply1(String lastReply1) {
        this.lastReply1 = lastReply1;
    }

    public String getLASTAllTEXT() {
        return lASTAllTEXT;
    }

    public void setLASTAllTEXT(String lASTAllTEXT) {
        this.lASTAllTEXT = lASTAllTEXT;
    }

    public String getPartialReplyQueryText() {
        return partialReplyQueryText;
    }

    public void setPartialReplyQueryText(String partialReplyQueryText) {
        this.partialReplyQueryText = partialReplyQueryText;
    }

    public String getLastReplySrNo() {
        return lastReplySrNo;
    }

    public void setLastReplySrNo(String lastReplySrNo) {
        this.lastReplySrNo = lastReplySrNo;
    }

    public String getSolvedDate() {
        return solvedDate;
    }

    public void setSolvedDate(String solvedDate) {
        this.solvedDate = solvedDate;
    }

    public String getEndedDate() {
        return endedDate;
    }

    public void setEndedDate(String endedDate) {
        this.endedDate = endedDate;
    }

    public String getQuerySolvedFlag() {
        return querySolvedFlag;
    }

    public void setQuerySolvedFlag(String querySolvedFlag) {
        this.querySolvedFlag = querySolvedFlag;
    }

    public String getQueryEndedFlag() {
        return queryEndedFlag;
    }

    public void setQueryEndedFlag(String queryEndedFlag) {
        this.queryEndedFlag = queryEndedFlag;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    @Override
    public String toString() {
        return "AllQuery{" +
                "eqCustQrySrNo='" + eqCustQrySrNo + '\'' +
                ", noOfReplies='" + noOfReplies + '\'' +
                ", eqCustQrySolved='" + eqCustQrySolved + '\'' +
                ", eqCustQryEnded='" + eqCustQryEnded + '\'' +
                ", groupname='" + groupname + '\'' +
                ", groupcode='" + groupcode + '\'' +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", completeQueryText='" + completeQueryText + '\'' +
                ", partialQueryText='" + partialQueryText + '\'' +
                ", postedDate='" + postedDate + '\'' +
                ", personName='" + personName + '\'' +
                ", employeeSrNo='" + employeeSrNo + '\'' +
                ", lastRepliedOn='" + lastRepliedOn + '\'' +
                ", lastReply='" + lastReply + '\'' +
                ", lastReply1='" + lastReply1 + '\'' +
                ", lASTAllTEXT='" + lASTAllTEXT + '\'' +
                ", partialReplyQueryText='" + partialReplyQueryText + '\'' +
                ", lastReplySrNo='" + lastReplySrNo + '\'' +
                ", solvedDate='" + solvedDate + '\'' +
                ", endedDate='" + endedDate + '\'' +
                ", querySolvedFlag='" + querySolvedFlag + '\'' +
                ", queryEndedFlag='" + queryEndedFlag + '\'' +
                ", rowNumber='" + rowNumber + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(eqCustQrySrNo);
        parcel.writeString(noOfReplies);
        parcel.writeString(eqCustQrySolved);
        parcel.writeString(eqCustQryEnded);
        parcel.writeString(groupname);
        parcel.writeString(groupcode);
        parcel.writeString(ticketNumber);
        parcel.writeString(completeQueryText);
        parcel.writeString(partialQueryText);
        parcel.writeString(postedDate);
        parcel.writeString(personName);
        parcel.writeString(employeeSrNo);
        parcel.writeString(lastRepliedOn);
        parcel.writeString(lastReply);
        parcel.writeString(lastReply1);
        parcel.writeString(lASTAllTEXT);
        parcel.writeString(partialReplyQueryText);
        parcel.writeString(lastReplySrNo);
        parcel.writeString(solvedDate);
        parcel.writeString(endedDate);
        parcel.writeString(querySolvedFlag);
        parcel.writeString(queryEndedFlag);
        parcel.writeString(rowNumber);
    }
}
