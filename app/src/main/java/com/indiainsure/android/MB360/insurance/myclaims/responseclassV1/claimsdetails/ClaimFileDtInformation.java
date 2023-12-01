package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimFileDtInformation {

    @SerializedName("FILE_RECEIVED_DATE")
    @Expose
    private String fileReceivedDate;
    @SerializedName("DEFICIENCIES")
    @Expose
    private String deficiencies;
    @SerializedName("FIRST_DEFICIENCY_LETTER_DATE")
    @Expose
    private String firstDeficiencyLetterDate;
    @SerializedName("SECOND_DEFICIENCY_LETTER_DATE")
    @Expose
    private String secondDeficiencyLetterDate;
    @SerializedName("DEFICIENCY_INTIMATION_DATE")
    @Expose
    private String deficiencyIntimationDate;
    @SerializedName("DEFICIENCIES_RETRIEVAL_DATE")
    @Expose
    private String deficienciesRetrievalDate;

    public String getFileReceivedDate() {
        return fileReceivedDate;
    }

    public void setFileReceivedDate(String fileReceivedDate) {
        this.fileReceivedDate = fileReceivedDate;
    }

    public String getDeficiencies() {
        return deficiencies;
    }

    public void setDeficiencies(String deficiencies) {
        this.deficiencies = deficiencies;
    }

    public String getFirstDeficiencyLetterDate() {
        return firstDeficiencyLetterDate;
    }

    public void setFirstDeficiencyLetterDate(String firstDeficiencyLetterDate) {
        this.firstDeficiencyLetterDate = firstDeficiencyLetterDate;
    }

    public String getSecondDeficiencyLetterDate() {
        return secondDeficiencyLetterDate;
    }

    public void setSecondDeficiencyLetterDate(String secondDeficiencyLetterDate) {
        this.secondDeficiencyLetterDate = secondDeficiencyLetterDate;
    }

    public String getDeficiencyIntimationDate() {
        return deficiencyIntimationDate;
    }

    public void setDeficiencyIntimationDate(String deficiencyIntimationDate) {
        this.deficiencyIntimationDate = deficiencyIntimationDate;
    }

    public String getDeficienciesRetrievalDate() {
        return deficienciesRetrievalDate;
    }

    public void setDeficienciesRetrievalDate(String deficienciesRetrievalDate) {
        this.deficienciesRetrievalDate = deficienciesRetrievalDate;
    }

    @Override
    public String toString() {
        return "ClaimFileDtInformation{" +
                "fileReceivedDate='" + fileReceivedDate + '\'' +
                ", deficiencies='" + deficiencies + '\'' +
                ", firstDeficiencyLetterDate='" + firstDeficiencyLetterDate + '\'' +
                ", secondDeficiencyLetterDate='" + secondDeficiencyLetterDate + '\'' +
                ", deficiencyIntimationDate='" + deficiencyIntimationDate + '\'' +
                ", deficienciesRetrievalDate='" + deficienciesRetrievalDate + '\'' +
                '}';
    }
}
