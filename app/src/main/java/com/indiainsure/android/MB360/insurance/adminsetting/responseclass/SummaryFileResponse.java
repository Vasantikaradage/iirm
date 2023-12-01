package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummaryFileResponse {
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("ResponseData")
    @Expose
    private SummaryResponseData responseData;

    /**
     * No args constructor for use in serialization
     *
     */
    public SummaryFileResponse() {
    }

    /**
     *
     * @param responseData
     * @param message
     * @param status
     */
    public SummaryFileResponse(String message, Boolean status, SummaryResponseData responseData) {
        super();
        this.message = message;
        this.status = status;
        this.responseData = responseData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public SummaryResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(SummaryResponseData responseData) {
        this.responseData = responseData;
    }
}


