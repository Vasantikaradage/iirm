package com.indiainsure.android.MB360.insurance.profile.response;

import android.graphics.drawable.Drawable;

public class ProfileServiceModel {
    Drawable menuIcon;
    String menuName;

    public ProfileServiceModel(Drawable menuIcon, String menuName) {
        this.menuIcon = menuIcon;
        this.menuName = menuName;
    }

    public Drawable getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(Drawable menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


    @Override
    public String toString() {
        return "ProfileServiceModel{" +
                "menuIcon=" + menuIcon +
                ", menuName='" + menuName + '\'' +
                '}';
    }
}
