package com.indiainsure.android.MB360.insurance.policyfeatures.repository.ui;

import android.os.Parcel;
import android.os.Parcelable;

public class PolicyInnerRecyclerModel implements Parcelable {
    String mainPolicyHeader;
    String innerHeader;
    String innerDescription;

    public PolicyInnerRecyclerModel(String mainPolicyHeader, String innerHeader, String innerDescription) {
        this.mainPolicyHeader = mainPolicyHeader;
        this.innerHeader = innerHeader;
        this.innerDescription = innerDescription;
    }

    protected PolicyInnerRecyclerModel(Parcel in) {
        mainPolicyHeader = in.readString();
        innerHeader = in.readString();
        innerDescription = in.readString();
    }

    public String getMainPolicyHeader() {
        return mainPolicyHeader;
    }

    public void setMainPolicyHeader(String mainPolicyHeader) {
        this.mainPolicyHeader = mainPolicyHeader;
    }

    public String getInnerHeader() {
        return innerHeader;
    }

    public void setInnerHeader(String innerHeader) {
        this.innerHeader = innerHeader;
    }

    public String getInnerDescription() {
        return innerDescription;
    }

    public void setInnerDescription(String innerDescription) {
        this.innerDescription = innerDescription;
    }

    public static final Creator<PolicyInnerRecyclerModel> CREATOR = new Creator<PolicyInnerRecyclerModel>() {
        @Override
        public PolicyInnerRecyclerModel createFromParcel(Parcel in) {
            return new PolicyInnerRecyclerModel(in);
        }

        @Override
        public PolicyInnerRecyclerModel[] newArray(int size) {
            return new PolicyInnerRecyclerModel[size];
        }
    };

    @Override
    public String toString() {
        return "PolicyInnerRecyclerViewModel{" +
                "mainPolicyHeader='" + mainPolicyHeader + '\'' +
                ", innerHeader='" + innerHeader + '\'' +
                ", innerDescription='" + innerDescription + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mainPolicyHeader);
        parcel.writeString(innerHeader);
        parcel.writeString(innerDescription);
    }
}
