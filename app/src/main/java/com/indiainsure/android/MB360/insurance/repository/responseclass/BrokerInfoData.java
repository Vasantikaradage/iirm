package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrokerInfoData {
    @SerializedName("BROKER_NAME")
    @Expose
    private String brokerName;
    @SerializedName("BROKER_CODE")
    @Expose
    private String brokerCode;
    @SerializedName("BROKER_ADDRESS")
    @Expose
    private String brokerAddress;
    @SerializedName("BROKER_EMAIL_ID")
    @Expose
    private String brokerEmailId;

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getBrokerAddress() {
        return brokerAddress;
    }

    public void setBrokerAddress(String brokerAddress) {
        this.brokerAddress = brokerAddress;
    }

    public String getBrokerEmailId() {
        return brokerEmailId;
    }

    public void setBrokerEmailId(String brokerEmailId) {
        this.brokerEmailId = brokerEmailId;
    }

    @Override
    public String toString() {
        return "BrokerInfoData{" +
                "brokerName='" + brokerName + '\'' +
                ", brokerCode='" + brokerCode + '\'' +
                ", brokerAddress='" + brokerAddress + '\'' +
                ", brokerEmailId='" + brokerEmailId + '\'' +
                '}';
    }
}
