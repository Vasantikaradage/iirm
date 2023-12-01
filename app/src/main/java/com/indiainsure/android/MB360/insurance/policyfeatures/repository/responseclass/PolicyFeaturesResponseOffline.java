package com.indiainsure.android.MB360.insurance.policyfeatures.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.indiainsure.android.MB360.database.converters.PolicyFeatureConverter.PolicyFeatureListConverter;
import com.indiainsure.android.MB360.database.converters.PolicyFeatureConverter.PolicyFeatureResponseConverter;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "POLICY_FEATURE", indices = @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(PolicyFeatureResponseConverter.class)
public class PolicyFeaturesResponseOffline extends PolicyFeaturesResponse {


    @TypeConverters(PolicyFeatureListConverter.class)
    List<PolicyFeaturesResponse> policyFeaturesResponseList = new ArrayList<>();


    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String oeGrpBasInfoSrNo;

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }


    public List<PolicyFeaturesResponse> getPolicyFeaturesResponseList() {
        return policyFeaturesResponseList;
    }

    public void setPolicyFeaturesResponseList(List<PolicyFeaturesResponse> policyFeaturesResponseList) {
        this.policyFeaturesResponseList = policyFeaturesResponseList;
    }
}
