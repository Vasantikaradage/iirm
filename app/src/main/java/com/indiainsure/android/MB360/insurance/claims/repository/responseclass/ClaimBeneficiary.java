package com.indiainsure.android.MB360.insurance.claims.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimBeneficiary {
    @SerializedName("PERSON_NAME")
    @Expose
    private String personName;
    @SerializedName("PERSON_SR_NO")
    @Expose
    private String personSrNo;
    @SerializedName("RELATION_NAME")
    @Expose
    private String relationName;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public String toString() {
        return "ClaimBeneficiary{" +
                "personName='" + personName + '\'' +
                ", personSrNo='" + personSrNo + '\'' +
                ", relationName='" + relationName + '\'' +
                '}';
    }
}
