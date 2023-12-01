package com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("height")
    @Expose
    public Integer height;
    @SerializedName("photo_reference")
    @Expose
    public String photoReference;
    @SerializedName("width")
    @Expose
    public Integer width;

    public Photo() {
    }




    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "height=" + height +
                ", photoReference='" + photoReference + '\'' +
                ", width=" + width +
                '}';
    }
}
