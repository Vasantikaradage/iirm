package com.indiainsure.android.MB360.insurance;

import android.graphics.drawable.Drawable;

public class DashBoardModel {
    String dashBoardHeader;
    Drawable dashBoardImage;
    String dashBoardTextDescription;

    public DashBoardModel(String dashBoardHeader, Drawable dashBoardImage, String dashBoardTextDescription) {
        this.dashBoardHeader = dashBoardHeader;
        this.dashBoardImage = dashBoardImage;
        this.dashBoardTextDescription = dashBoardTextDescription;
    }

    public DashBoardModel() {
    }

    public String getDashBoardHeader() {
        return dashBoardHeader;
    }

    public void setDashBoardHeader(String dashBoardHeader) {
        this.dashBoardHeader = dashBoardHeader;
    }

    public Drawable getDashBoardImage() {
        return dashBoardImage;
    }

    public void setDashBoardImage(Drawable dashBoardImage) {
        this.dashBoardImage = dashBoardImage;
    }

    public String getDashBoardTextDescription() {
        return dashBoardTextDescription;
    }

    public void setDashBoardTextDescription(String dashBoardTextDescription) {
        this.dashBoardTextDescription = dashBoardTextDescription;
    }

}
