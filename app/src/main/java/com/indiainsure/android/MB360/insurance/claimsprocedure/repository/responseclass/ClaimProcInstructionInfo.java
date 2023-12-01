package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ClaimProcInstructionInfo {
    @SerializedName("INSTRUCTIONS_TEXT")
    @Expose
    private String instructionsText;
    @SerializedName("IS_DEF_INST_TEXT_FROM_FILE")
    @Expose
    private String isDefInstTextFromFile;
    @SerializedName("DEF_INST_TEXT_FROM_FILE_PATH")
    @Expose
    private String defInstTextFromFilePath;
    @SerializedName("INST_MAPP_ORDER")
    @Expose
    private String instMappOrder;

    public String getInstructionsText() {
        return instructionsText;
    }

    public void setInstructionsText(String instructionsText) {
        this.instructionsText = instructionsText;
    }

    public String getIsDefInstTextFromFile() {
        return isDefInstTextFromFile;
    }

    public void setIsDefInstTextFromFile(String isDefInstTextFromFile) {
        this.isDefInstTextFromFile = isDefInstTextFromFile;
    }

    public String getDefInstTextFromFilePath() {
        return defInstTextFromFilePath;
    }

    public void setDefInstTextFromFilePath(String defInstTextFromFilePath) {
        this.defInstTextFromFilePath = defInstTextFromFilePath;
    }

    public String getInstMappOrder() {
        return instMappOrder;
    }

    public void setInstMappOrder(String instMappOrder) {
        this.instMappOrder = instMappOrder;
    }

    @Override
    public String toString() {
        return "ClaimProcInstructionInfo{" +
                "instructionsText='" + instructionsText + '\'' +
                ", isDefInstTextFromFile='" + isDefInstTextFromFile + '\'' +
                ", defInstTextFromFilePath='" + defInstTextFromFilePath + '\'' +
                ", instMappOrder='" + instMappOrder + '\'' +
                '}';
    }
}
