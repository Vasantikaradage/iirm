package com.indiainsure.android.MB360.insurance.coverages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoveragePolicyDatum {
    @SerializedName("GROUPNAME")
    @Expose
    private String groupname;
    @SerializedName("POLICY_NUMBER")
    @Expose
    private String policyNumber;
    @SerializedName("POLICY_COMMENCEMENT_DATE")
    @Expose
    private String policyCommencementDate;
    @SerializedName("POLICY_VALID_UPTO")
    @Expose
    private String policyValidUpto;
    @SerializedName("PRODUCT_CODE")
    @Expose
    private String productCode;
    @SerializedName("TPA_NAME")
    @Expose
    private String tpaName;
    @SerializedName("BROKER_NAME")
    @Expose
    private String brokerName;
    @SerializedName("INSURANCE_CO_NAME")
    @Expose
    private String insuranceCoName;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyCommencementDate() {
        return policyCommencementDate;
    }

    public void setPolicyCommencementDate(String policyCommencementDate) {
        this.policyCommencementDate = policyCommencementDate;
    }

    public String getPolicyValidUpto() {
        return policyValidUpto;
    }

    public void setPolicyValidUpto(String policyValidUpto) {
        this.policyValidUpto = policyValidUpto;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTpaName() {
        return tpaName;
    }

    public void setTpaName(String tpaName) {
        this.tpaName = tpaName;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getInsuranceCoName() {
        return insuranceCoName;
    }

    public void setInsuranceCoName(String insuranceCoName) {
        this.insuranceCoName = insuranceCoName;
    }

    @Override
    public String toString() {
        return "CoveragePolicyDatum{" +
                "groupname='" + groupname + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", policyCommencementDate='" + policyCommencementDate + '\'' +
                ", policyValidUpto='" + policyValidUpto + '\'' +
                ", productCode='" + productCode + '\'' +
                ", tpaName='" + tpaName + '\'' +
                ", brokerName='" + brokerName + '\'' +
                ", insuranceCoName='" + insuranceCoName + '\'' +
                '}';
    }
}
