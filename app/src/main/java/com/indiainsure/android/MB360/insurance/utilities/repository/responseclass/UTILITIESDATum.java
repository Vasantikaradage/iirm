package com.indiainsure.android.MB360.insurance.utilities.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UTILITIESDATum {
    @SerializedName("DOWNLOAD_SR_NO")
    @Expose
    private String downloadSrNo;
    @SerializedName("DOWNLOAD_NAME")
    @Expose
    private String downloadName;
    @SerializedName("DOWNLOAD_DISPLAY_NAME")
    @Expose
    private String downloadDisplayName;
    @SerializedName("PRODUCT_NAME")
    @Expose
    private String productName;
    @SerializedName("PRODUCT_CODE")
    @Expose
    private String productCode;
    @SerializedName("DOWNLOAD_VISIBILITY")
    @Expose
    private String downloadVisibility;
    @SerializedName("SYS_GEN_FILE_NAME")
    @Expose
    private String sysGenFileName;
    @SerializedName("FILE_TYPE")
    @Expose
    private String fileType;
    @SerializedName("GROUPCHILDSRNO")
    @Expose
    private String groupchildsrno;
    @SerializedName("OE_GRP_BAS_INF_SR_NO")
    @Expose
    private String oeGrpBasInfSrNo;
    @SerializedName("FILE_PATH")
    @Expose
    private String filePath;

    public String getDownloadSrNo() {
        return downloadSrNo;
    }

    public void setDownloadSrNo(String downloadSrNo) {
        this.downloadSrNo = downloadSrNo;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }

    public String getDownloadDisplayName() {
        return downloadDisplayName;
    }

    public void setDownloadDisplayName(String downloadDisplayName) {
        this.downloadDisplayName = downloadDisplayName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDownloadVisibility() {
        return downloadVisibility;
    }

    public void setDownloadVisibility(String downloadVisibility) {
        this.downloadVisibility = downloadVisibility;
    }

    public String getSysGenFileName() {
        return sysGenFileName;
    }

    public void setSysGenFileName(String sysGenFileName) {
        this.sysGenFileName = sysGenFileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getGroupchildsrno() {
        return groupchildsrno;
    }

    public void setGroupchildsrno(String groupchildsrno) {
        this.groupchildsrno = groupchildsrno;
    }

    public String getOeGrpBasInfSrNo() {
        return oeGrpBasInfSrNo;
    }

    public void setOeGrpBasInfSrNo(String oeGrpBasInfSrNo) {
        this.oeGrpBasInfSrNo = oeGrpBasInfSrNo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "UTILITIESDATum{" +
                "downloadSrNo='" + downloadSrNo + '\'' +
                ", downloadName='" + downloadName + '\'' +
                ", downloadDisplayName='" + downloadDisplayName + '\'' +
                ", productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", downloadVisibility='" + downloadVisibility + '\'' +
                ", sysGenFileName='" + sysGenFileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", groupchildsrno='" + groupchildsrno + '\'' +
                ", oeGrpBasInfSrNo='" + oeGrpBasInfSrNo + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
