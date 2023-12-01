package com.indiainsure.android.MB360.insurance.servicenames.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowButtons {
    @SerializedName("GROUPNAME")
    @Expose
    private String groupName;

    @SerializedName("GROUPCHILDSRNO")
    @Expose
    private String groupChildSrNo;

    @SerializedName("SER_OFFR_SR_NO")
    @Expose
    private String serOfferSRNo;


    @SerializedName("SERVICE_NAME")
    @Expose
    private String serviceName;


    @SerializedName("SERVICE_NAME_TODISP")
    @Expose
    private String serviceNameToDisp;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupChildSrNo() {
        return groupChildSrNo;
    }

    public void setGroupChildSrNo(String groupChildSrNo) {
        this.groupChildSrNo = groupChildSrNo;
    }

    public String getSerOfferSRNo() {
        return serOfferSRNo;
    }

    public void setSerOfferSRNo(String serOfferSRNo) {
        this.serOfferSRNo = serOfferSRNo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceNameToDisp() {
        return serviceNameToDisp;
    }

    public void setServiceNameToDisp(String serviceNameToDisp) {
        this.serviceNameToDisp = serviceNameToDisp;
    }

    @Override
    public String toString() {
        return "ShowButtons{" +
                "groupName='" + groupName + '\'' +
                ", groupChildSrNo='" + groupChildSrNo + '\'' +
                ", serOfferSRNo='" + serOfferSRNo + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceNameToDisp='" + serviceNameToDisp + '\'' +
                '}';
    }
}
