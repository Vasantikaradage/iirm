package com.indiainsure.android.MB360.insurance.claims.repository.requestclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewIntimateRequest {

    @SerializedName("groupchildsrno")
    @Expose
    private String groupchildsrno;
    @SerializedName("oegrpbasinfsrno")
    @Expose
    private String oegrpbasinfsrno;
    @SerializedName("employeesrno")
    @Expose
    private String employeesrno;
    @SerializedName("personsrno")
    @Expose
    private String personsrno;
    @SerializedName("diagnosis")
    @Expose
    private String diagnosis;
    @SerializedName("claimamount")
    @Expose
    private String claimamount;
    @SerializedName("doalikelydoa")
    @Expose
    private String doalikelydoa;
    @SerializedName("hospitalname")
    @Expose
    private String hospitalname;
    @SerializedName("hospitallocation")
    @Expose
    private String hospitallocation;

    public NewIntimateRequest() {
        this.groupchildsrno = "";
        this.oegrpbasinfsrno = "";
        this.employeesrno = "";
        this.personsrno = "";
        this.diagnosis = "";
        this.claimamount = "";
        this.doalikelydoa = "";
        this.hospitalname = "";
        this.hospitallocation = "";
    }

    public String getGroupchildsrno() {
        return groupchildsrno;
    }

    public void setGroupchildsrno(String groupchildsrno) {
        this.groupchildsrno = groupchildsrno;
    }

    public String getOegrpbasinfsrno() {
        return oegrpbasinfsrno;
    }

    public void setOegrpbasinfsrno(String oegrpbasinfsrno) {
        this.oegrpbasinfsrno = oegrpbasinfsrno;
    }

    public String getEmployeesrno() {
        return employeesrno;
    }

    public void setEmployeesrno(String employeesrno) {
        this.employeesrno = employeesrno;
    }

    public String getPersonsrno() {
        return personsrno;
    }

    public void setPersonsrno(String personsrno) {
        this.personsrno = personsrno;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getClaimamount() {
        return claimamount;
    }

    public void setClaimamount(String claimamount) {
        this.claimamount = claimamount;
    }

    public String getDoalikelydoa() {
        return doalikelydoa;
    }

    public void setDoalikelydoa(String doalikelydoa) {
        this.doalikelydoa = doalikelydoa;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getHospitallocation() {
        return hospitallocation;
    }

    public void setHospitallocation(String hospitallocation) {
        this.hospitallocation = hospitallocation;
    }


}
