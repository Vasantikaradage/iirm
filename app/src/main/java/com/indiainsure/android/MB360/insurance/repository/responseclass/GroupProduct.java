package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupProduct {
    @SerializedName("PRODUCT_CODE")
    @Expose
    private String productCode;
    @SerializedName("ACTIVE")
    @Expose
    private String active;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "GroupProduct{" +
                "productCode='" + productCode + '\'' +
                ", active='" + active + '\'' +
                '}';
    }
}
