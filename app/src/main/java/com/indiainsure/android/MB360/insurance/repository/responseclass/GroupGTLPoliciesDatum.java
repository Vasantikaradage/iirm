package com.indiainsure.android.MB360.insurance.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupGTLPoliciesDatum {
    @SerializedName("OE_GRP_BAS_INF_SR_NO")
    @Expose
    private String oeGrpBasInfSrNo;
    @SerializedName("POLICY_NUMBER")
    @Expose
    private String policyNumber;
    @SerializedName("INS_CO_NAME")
    @Expose
    private String insCoName;
    @SerializedName("INS_CODE")
    @Expose
    private String insCode;
    @SerializedName("TPA_NAME")
    @Expose
    private String tpaName;
    @SerializedName("TPA_CODE")
    @Expose
    private String tpaCode;
    @SerializedName("POLICY_COMMENCEMENT_DATE")
    @Expose
    private String policyCommencementDate;
    @SerializedName("POLICY_VALID_UPTO")
    @Expose
    private String policyValidUpto;
    @SerializedName("ACTIVE")
    @Expose
    private String active;
    @SerializedName("PRODUCT_CODE")
    @Expose
    private String productCode;
    @SerializedName("TYPE_OF_POL_SR_NO")
    @Expose
    private String typeOfPolSrNo;
    @SerializedName("POLICY_TYPE")
    @Expose
    private String policyType;

    public String getOeGrpBasInfSrNo() {
        return oeGrpBasInfSrNo;
    }

    public void setOeGrpBasInfSrNo(String oeGrpBasInfSrNo) {
        this.oeGrpBasInfSrNo = oeGrpBasInfSrNo;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getInsCoName() {
        return insCoName;
    }

    public void setInsCoName(String insCoName) {
        this.insCoName = insCoName;
    }

    public String getInsCode() {
        return insCode;
    }

    public void setInsCode(String insCode) {
        this.insCode = insCode;
    }

    public String getTpaName() {
        return tpaName;
    }

    public void setTpaName(String tpaName) {
        this.tpaName = tpaName;
    }

    public String getTpaCode() {
        return tpaCode;
    }

    public void setTpaCode(String tpaCode) {
        this.tpaCode = tpaCode;
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

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getTypeOfPolSrNo() {
        return typeOfPolSrNo;
    }

    public void setTypeOfPolSrNo(String typeOfPolSrNo) {
        this.typeOfPolSrNo = typeOfPolSrNo;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    @Override
    public String toString() {
        return "GroupGTLPoliciesDatum{" +
                "oeGrpBasInfSrNo='" + oeGrpBasInfSrNo + '\'' +
                ", policyNumber='" + policyNumber + '\'' +
                ", insCoName='" + insCoName + '\'' +
                ", insCode='" + insCode + '\'' +
                ", tpaName='" + tpaName + '\'' +
                ", tpaCode='" + tpaCode + '\'' +
                ", policyCommencementDate='" + policyCommencementDate + '\'' +
                ", policyValidUpto='" + policyValidUpto + '\'' +
                ", active='" + active + '\'' +
                ", productCode='" + productCode + '\'' +
                ", typeOfPolSrNo='" + typeOfPolSrNo + '\'' +
                ", policyType='" + policyType + '\'' +
                '}';
    }
}
