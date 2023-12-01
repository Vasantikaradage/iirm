package com.indiainsure.android.MB360.insurance.repository.responseclass;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.indiainsure.android.MB360.database.converters.LoadSessionConverter;

import java.util.List;
@Entity(tableName = "LOAD_SESSION")
@TypeConverters(LoadSessionConverter.class)
public class LoadSessionResponse {
    @PrimaryKey
    public int oeGrpBasInfoSrNo;

    @SerializedName("Server_Date")
    @Expose
    private String serverDate;
    @SerializedName("Group_Info_data")
    @Expose
    private GroupInfoData groupInfoData;
    @SerializedName("Broker_Info_data")
    @Expose
    private BrokerInfoData brokerInfoData;
    @SerializedName("GroupProducts")
    @Expose
    private List<GroupProduct> groupProducts = null;
    @SerializedName("Group_Policies")
    @Expose
    private List<GroupPolicy> groupPolicies = null;
    @SerializedName("Group_Policies_Employees")
    @Expose
    private List<GroupPoliciesEmployee> groupPoliciesEmployees = null;
    @SerializedName("Group_Policies_Employees_Dependants")
    @Expose
    private List<GroupPoliciesEmployeesDependant> groupPoliciesEmployeesDependants = null;
    @SerializedName("Enrollment_Parental_Options")
    @Expose
    private List<EnrollmentParentalOption> enrollmentParentalOptions = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public GroupInfoData getGroupInfoData() {
        return groupInfoData;
    }

    public void setGroupInfoData(GroupInfoData groupInfoData) {
        this.groupInfoData = groupInfoData;
    }

    public BrokerInfoData getBrokerInfoData() {
        return brokerInfoData;
    }

    public void setBrokerInfoData(BrokerInfoData brokerInfoData) {
        this.brokerInfoData = brokerInfoData;
    }

    public List<GroupProduct> getGroupProducts() {
        return groupProducts;
    }

    public void setGroupProducts(List<GroupProduct> groupProducts) {
        this.groupProducts = groupProducts;
    }

    public List<GroupPolicy> getGroupPolicies() {
        return groupPolicies;
    }

    public void setGroupPolicies(List<GroupPolicy> groupPolicies) {
        this.groupPolicies = groupPolicies;
    }

    public List<GroupPoliciesEmployee> getGroupPoliciesEmployees() {
        return groupPoliciesEmployees;
    }

    public void setGroupPoliciesEmployees(List<GroupPoliciesEmployee> groupPoliciesEmployees) {
        this.groupPoliciesEmployees = groupPoliciesEmployees;
    }

    public List<GroupPoliciesEmployeesDependant> getGroupPoliciesEmployeesDependants() {
        return groupPoliciesEmployeesDependants;
    }

    public void setGroupPoliciesEmployeesDependants(List<GroupPoliciesEmployeesDependant> groupPoliciesEmployeesDependants) {
        this.groupPoliciesEmployeesDependants = groupPoliciesEmployeesDependants;
    }

    public List<EnrollmentParentalOption> getEnrollmentParentalOptions() {
        return enrollmentParentalOptions;
    }

    public void setEnrollmentParentalOptions(List<EnrollmentParentalOption> enrollmentParentalOptions) {
        this.enrollmentParentalOptions = enrollmentParentalOptions;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LoadSessionResponse{" +
                "serverDate='" + serverDate + '\'' +
                ", groupInfoData=" + groupInfoData +
                ", brokerInfoData=" + brokerInfoData +
                ", groupProducts=" + groupProducts +
                ", groupPolicies=" + groupPolicies +
                ", groupPoliciesEmployees=" + groupPoliciesEmployees +
                ", groupPoliciesEmployeesDependants=" + groupPoliciesEmployeesDependants +
                ", enrollmentParentalOptions=" + enrollmentParentalOptions +
                ", message=" + message +
                '}';
    }
}
