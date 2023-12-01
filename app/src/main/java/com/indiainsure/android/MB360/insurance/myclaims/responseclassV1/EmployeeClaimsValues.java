package com.indiainsure.android.MB360.insurance.myclaims.responseclassV1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeClaimsValues {

    @SerializedName("EMPLOYEE_SR_NO")
    @Expose
    private String employeeSrNo;
    @SerializedName("OE_GRP_BAS_INF_SR_NO")
    @Expose
    private String oeGrpBasInfSrNo;

    public String getEmployeeSrNo() {
        return employeeSrNo;
    }

    public void setEmployeeSrNo(String employeeSrNo) {
        this.employeeSrNo = employeeSrNo;
    }

    public String getOeGrpBasInfSrNo() {
        return oeGrpBasInfSrNo;
    }

    public void setOeGrpBasInfSrNo(String oeGrpBasInfSrNo) {
        this.oeGrpBasInfSrNo = oeGrpBasInfSrNo;
    }

    @Override
    public String toString() {
        return "EmployeeClaimsValues{" +
                "employeeSrNo='" + employeeSrNo + '\'' +
                ", oeGrpBasInfSrNo='" + oeGrpBasInfSrNo + '\'' +
                '}';
    }
}
