package com.indiainsure.android.MB360.insurance.ecards.reponseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EcardResponseJson {
    @SerializedName("message")
    @Expose
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EcardResponseJson{" +
                "message=" + message +
                '}';
    }
}
