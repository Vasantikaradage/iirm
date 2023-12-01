package com.indiainsure.android.MB360.insurance.adminsetting.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PolicyDefinitionData {
    @SerializedName("NO_OF_DEPENDENTS")
    @Expose
    private String NO_OF_DEPENDENTS;
    @SerializedName("FAMILY_DESCRIPTION")
    @Expose
    private String FAMILY_DESCRIPTION;
    @SerializedName("IncludedRelation_data")
    @Expose
    private List<IncludedRelationData> IncludedRelation_data=new ArrayList<>();
    @SerializedName("OE_GRP_POL_REL_INFO_SRNO")
    @Expose
    private String OE_GRP_POL_REL_INFO_SRNO;

    public String getNO_OF_DEPENDENTS() {
        return NO_OF_DEPENDENTS;
    }

    public void setNO_OF_DEPENDENTS(String NO_OF_DEPENDENTS) {
        this.NO_OF_DEPENDENTS = NO_OF_DEPENDENTS;
    }

    public String getFAMILY_DESCRIPTION() {
        return FAMILY_DESCRIPTION;
    }

    public void setFAMILY_DESCRIPTION(String FAMILY_DESCRIPTION) {
        this.FAMILY_DESCRIPTION = FAMILY_DESCRIPTION;
    }

    public List<IncludedRelationData> getIncludedRelation_data() {
        return IncludedRelation_data;
    }

    public void setIncludedRelation_data(List<IncludedRelationData> includedRelation_data) {
        IncludedRelation_data = includedRelation_data;
    }

    public String getOE_GRP_POL_REL_INFO_SRNO() {
        return OE_GRP_POL_REL_INFO_SRNO;
    }

    public void setOE_GRP_POL_REL_INFO_SRNO(String OE_GRP_POL_REL_INFO_SRNO) {
        this.OE_GRP_POL_REL_INFO_SRNO = OE_GRP_POL_REL_INFO_SRNO;
    }

    @Override
    public String toString() {
        return "PolicyDefinitionData{" +
                "NO_OF_DEPENDENTS='" + NO_OF_DEPENDENTS + '\'' +
                ", FAMILY_DESCRIPTION='" + FAMILY_DESCRIPTION + '\'' +
                ", IncludedRelation_data=" + IncludedRelation_data +
                ", OE_GRP_POL_REL_INFO_SRNO='" + OE_GRP_POL_REL_INFO_SRNO + '\'' +
                '}';
    }
}
