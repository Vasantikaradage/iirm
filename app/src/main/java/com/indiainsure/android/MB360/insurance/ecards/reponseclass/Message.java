package com.indiainsure.android.MB360.insurance.ecards.reponseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("ECard")
    @Expose
    private String eCard;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getECard() {
        return eCard;
    }

    public void setECard(String eCard) {
        this.eCard = eCard;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", eCard='" + eCard + '\'' +
                '}';
    }
}
