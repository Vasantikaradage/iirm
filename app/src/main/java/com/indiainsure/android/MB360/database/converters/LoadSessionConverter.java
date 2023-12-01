package com.indiainsure.android.MB360.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indiainsure.android.MB360.insurance.repository.responseclass.BrokerInfoData;
import com.indiainsure.android.MB360.insurance.repository.responseclass.EnrollmentParentalOption;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupInfoData;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupPoliciesEmployee;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupPoliciesEmployeesDependant;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupPolicy;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupProduct;
import com.indiainsure.android.MB360.insurance.repository.responseclass.Message;

import java.lang.reflect.Type;
import java.util.List;

public class LoadSessionConverter {
    private static Gson gson = new Gson();

    //group info
    @TypeConverter
    public static GroupInfoData stringToGroupInfoData(String groupInfoData) {
        return gson.fromJson(groupInfoData, GroupInfoData.class);
    }

    @TypeConverter
    public static String groupInfoToString(GroupInfoData groupInfoData) {
        return gson.toJson(groupInfoData);
    }


    //broker info
    @TypeConverter
    public static BrokerInfoData stringToBrokerInfodata(String brokerInfodata) {
        return gson.fromJson(brokerInfodata, BrokerInfoData.class);
    }

    @TypeConverter
    public static String brokerInfoDataToString(BrokerInfoData brokerInfoData) {
        return gson.toJson(brokerInfoData);
    }


    //Message

    @TypeConverter
    public static Message stringToMessage(String message) {
        return gson.fromJson(message, Message.class);
    }

    @TypeConverter
    public static String messageToString(Message message) {
        return gson.toJson(message);
    }

    //GroupProducts
    @TypeConverter
    public static List<GroupProduct> toList(String value) {
        Type listType = new TypeToken<List<GroupProduct>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<GroupProduct> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    //GroupPolicy
    @TypeConverter
    public static List<GroupPolicy> toGroupPolicyList(String value) {
        Type listType = new TypeToken<List<GroupPolicy>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toGroupPolicyString(List<GroupPolicy> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    //GroupPolicy Employee
    @TypeConverter
    public static List<GroupPoliciesEmployee> toGroupPolicyEmployeeList(String value) {
        Type listType = new TypeToken<List<GroupPoliciesEmployee>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toGroupPolicyEmployeeString(List<GroupPoliciesEmployee> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    // GroupPoliciesEmployeesDependant
    @TypeConverter
    public static List<GroupPoliciesEmployeesDependant> toGroupPolicyEmployeeDependantList(String value) {
        Type listType = new TypeToken<List<GroupPoliciesEmployeesDependant>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toGroupPolicyEmployeeDependentString(List<GroupPoliciesEmployeesDependant> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    // EnrollmentParentalOption
    @TypeConverter
    public static List<EnrollmentParentalOption> toEnrollmentParentalList(String value) {
        Type listType = new TypeToken<List<EnrollmentParentalOption>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toEnrollmentParentalString(List<EnrollmentParentalOption> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }












}
