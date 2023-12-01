package com.indiainsure.android.MB360.onboarding.validation.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidationEmailRequest {
    @SerializedName("officialemailId")
    @Expose
    private String officialemailId;
    @SerializedName("enteredotp")
    @Expose
    private Integer enteredotp;

    public String getOfficialemailId() {
        return officialemailId;
    }

    public void setOfficialemailId(String officialemailId) {
        this.officialemailId = officialemailId;
    }

    public Integer getEnteredotp() {
        return enteredotp;
    }

    public void setEnteredotp(Integer enteredotp) {
        this.enteredotp = enteredotp;
    }
}
