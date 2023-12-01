package com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class PolicyFeaturesOuterModel implements Parcelable  {
    String policyType;
    boolean isExpanded;
    ArrayList<PolicyInnerRecyclerModel> innerList;

    public PolicyFeaturesOuterModel(String policyType, ArrayList<PolicyInnerRecyclerModel> innerList) {
        this.policyType = policyType;
        this.innerList = innerList;
        this.isExpanded = false;
    }

    protected PolicyFeaturesOuterModel(Parcel in) {
        policyType = in.readString();
        innerList = in.createTypedArrayList(PolicyInnerRecyclerModel.CREATOR);
    }


    public static final Creator<PolicyFeaturesOuterModel> CREATOR = new Creator<PolicyFeaturesOuterModel>() {
        @Override
        public PolicyFeaturesOuterModel createFromParcel(Parcel in) {
            return new PolicyFeaturesOuterModel(in);
        }

        @Override
        public PolicyFeaturesOuterModel[] newArray(int size) {
            return new PolicyFeaturesOuterModel[size];
        }
    };

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public ArrayList<PolicyInnerRecyclerModel> getInnerList() {
        return innerList;
    }

    public void setInnerList(ArrayList<PolicyInnerRecyclerModel> innerList) {
        this.innerList = innerList;
    }


    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public String toString() {
        return "PolicyFeaturesOuterModel{" +
                "policyType='" + policyType + '\'' +
                ", isExpanded=" + isExpanded +
                ", innerList=" + innerList +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(policyType);
        parcel.writeByte((byte) (isExpanded ? 1 : 0));
        parcel.writeTypedList(innerList);
    }
}
