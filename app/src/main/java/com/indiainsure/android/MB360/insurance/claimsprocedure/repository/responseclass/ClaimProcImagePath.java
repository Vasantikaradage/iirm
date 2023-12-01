package com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimProcImagePath {
    @SerializedName("CLM_PROC_P1_IMG_PATH")
    @Expose
    private String clmProcP1ImgPath;

    public String getClmProcP1ImgPath() {
        return clmProcP1ImgPath;
    }

    public void setClmProcP1ImgPath(String clmProcP1ImgPath) {
        this.clmProcP1ImgPath = clmProcP1ImgPath;
    }

    @Override
    public String toString() {
        return "ClaimProcImagePath{" +
                "clmProcP1ImgPath='" + clmProcP1ImgPath + '\'' +
                '}';
    }
}
