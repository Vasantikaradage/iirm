package com.indiainsure.android.MB360.insurance.profile.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDocumentsDetail {
    @SerializedName("DOCUMENT_TYPE")
    @Expose
    private String documentType;
    @SerializedName("DOCUMENT_NO")
    @Expose
    private String documentNo;
    @SerializedName("DOCUMENT_NAME")
    @Expose
    private String documentName;
    @SerializedName("DOCUMENT_PATH")
    @Expose
    private String documentPath;

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    @Override
    public String toString() {
        return "UserDocumentsDetail{" +
                "documentType='" + documentType + '\'' +
                ", documentNo='" + documentNo + '\'' +
                ", documentName='" + documentName + '\'' +
                ", documentPath='" + documentPath + '\'' +
                '}';
    }
}
