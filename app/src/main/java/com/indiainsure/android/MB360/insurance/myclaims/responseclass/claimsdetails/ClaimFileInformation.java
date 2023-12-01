package com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails;

public class ClaimFileInformation {
    public String FileReceivedDate;
    public String Deficiencies;
    public String FirstDeficiencyLetterDate;
    public String SecondDeficiencyLetterDate;
    public String DeficiencyIntimationDate;
    public String DeficiencyRetrivalDate;


    public String getFileReceivedDate() {
        return FileReceivedDate;
    }

    public void setFileReceivedDate(String fileReceivedDate) {
        FileReceivedDate = fileReceivedDate;
    }

    public String getDeficiencies() {
        return Deficiencies;
    }

    public void setDeficiencies(String deficiencies) {
        Deficiencies = deficiencies;
    }

    public String getFirstDeficiencyLetterDate() {
        return FirstDeficiencyLetterDate;
    }

    public void setFirstDeficiencyLetterDate(String firstDeficiencyLetterDate) {
        FirstDeficiencyLetterDate = firstDeficiencyLetterDate;
    }

    public String getSecondDeficiencyLetterDate() {
        return SecondDeficiencyLetterDate;
    }

    public void setSecondDeficiencyLetterDate(String secondDeficiencyLetterDate) {
        SecondDeficiencyLetterDate = secondDeficiencyLetterDate;
    }

    public String getDeficiencyIntimationDate() {
        return DeficiencyIntimationDate;
    }

    public void setDeficiencyIntimationDate(String deficiencyIntimationDate) {
        DeficiencyIntimationDate = deficiencyIntimationDate;
    }

    public String getDeficiencyRetrivalDate() {
        return DeficiencyRetrivalDate;
    }

    public void setDeficiencyRetrivalDate(String deficiencyRetrivalDate) {
        DeficiencyRetrivalDate = deficiencyRetrivalDate;
    }

    @Override
    public String toString() {
        return "ClaimFileInformation{" +
                "FileReceivedDate='" + FileReceivedDate + '\'' +
                ", Deficiencies='" + Deficiencies + '\'' +
                ", FirstDeficiencyLetterDate='" + FirstDeficiencyLetterDate + '\'' +
                ", SecondDeficiencyLetterDate='" + SecondDeficiencyLetterDate + '\'' +
                ", DeficiencyIntimationDate='" + DeficiencyIntimationDate + '\'' +
                ", DeficiencyRetrivalDate='" + DeficiencyRetrivalDate + '\'' +
                '}';
    }
}
