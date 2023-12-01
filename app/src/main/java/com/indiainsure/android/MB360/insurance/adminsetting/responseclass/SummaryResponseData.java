package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummaryResponseData {
    @SerializedName("SUMARRY_DWLND_FILE_INFO_SR_NO")
    @Expose
    private String sumarryDwlndFileInfoSrNo;
    @SerializedName("EMPLOYEE_SR_NO")
    @Expose
    private String employeeSrNo;
    @SerializedName("FILE_NAME")
    @Expose
    private String fileName;
    @SerializedName("LAST_MODIFIED_ON")
    @Expose
    private String lastModifiedOn;
    @SerializedName("FILE_URL")
    @Expose
    private String fileUrl;

    /**
     * No args constructor for use in serialization
     *
     */
    public SummaryResponseData() {
    }

    /**
     *
     * @param fileName
     * @param sumarryDwlndFileInfoSrNo
     * @param employeeSrNo
     * @param fileUrl
     * @param lastModifiedOn
     */
    public SummaryResponseData(String sumarryDwlndFileInfoSrNo, String employeeSrNo, String fileName, String lastModifiedOn, String fileUrl) {
        super();
        this.sumarryDwlndFileInfoSrNo = sumarryDwlndFileInfoSrNo;
        this.employeeSrNo = employeeSrNo;
        this.fileName = fileName;
        this.lastModifiedOn = lastModifiedOn;
        this.fileUrl = fileUrl;
    }

    public String getSumarryDwlndFileInfoSrNo() {
        return sumarryDwlndFileInfoSrNo;
    }

    public void setSumarryDwlndFileInfoSrNo(String sumarryDwlndFileInfoSrNo) {
        this.sumarryDwlndFileInfoSrNo = sumarryDwlndFileInfoSrNo;
    }

    public String getEmployeeSrNo() {
        return employeeSrNo;
    }

    public void setEmployeeSrNo(String employeeSrNo) {
        this.employeeSrNo = employeeSrNo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }



}
