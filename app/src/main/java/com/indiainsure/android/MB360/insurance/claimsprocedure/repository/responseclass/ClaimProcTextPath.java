package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimProcTextPath {
    @SerializedName("CLM_PROC_P1_WYSIWYG_TEXT_PATH")
    @Expose
    private String clmProcP1WysiwygTextPath;

    public String getClmProcP1WysiwygTextPath() {
        return clmProcP1WysiwygTextPath;
    }

    public void setClmProcP1WysiwygTextPath(String clmProcP1WysiwygTextPath) {
        this.clmProcP1WysiwygTextPath = clmProcP1WysiwygTextPath;
    }

    @Override
    public String toString() {
        return "ClaimProcTextPath{" +
                "clmProcP1WysiwygTextPath='" + clmProcP1WysiwygTextPath + '\'' +
                '}';
    }
}
