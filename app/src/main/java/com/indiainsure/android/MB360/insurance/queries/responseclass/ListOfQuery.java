package com.indiainsure.android.MB360.insurance.queries.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListOfQuery {
    @SerializedName("Query")
    @Expose
    private String query;
    @SerializedName("PostedBy")
    @Expose
    private String postedBy;
    @SerializedName("PostedByimage")
    @Expose
    private String postedByimage;
    @SerializedName("Role")
    @Expose
    private String role;
    @SerializedName("PostedOn")
    @Expose
    private String postedOn;
    @SerializedName("Attachments")
    @Expose
    private List<Attachment> attachments = null;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostedByimage() {
        return postedByimage;
    }

    public void setPostedByimage(String postedByimage) {
        this.postedByimage = postedByimage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "ListOfQuery{" +
                "query='" + query + '\'' +
                ", postedBy='" + postedBy + '\'' +
                ", postedByimage='" + postedByimage + '\'' +
                ", role='" + role + '\'' +
                ", postedOn='" + postedOn + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
