package com.indiainsure.android.MB360.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.EnrollLifeEventInfo;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.EnrollTopupOptions;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.EnrollmentMiscInfo;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.GroupAdminBasicSettings;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.GroupRelation;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.GroupWindowPeriodInfo;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.Message;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.PremiumEmiInfo;

import java.lang.reflect.Type;
import java.util.List;

public class EnrollMentWindowCountConverter {

    private static Gson gson = new Gson();


    //message
    @TypeConverter
    public static Message stringToMessage(String message) {
        return gson.fromJson(message, Message.class);
    }

    @TypeConverter
    public static String messageToString(Message message) {

        return gson.toJson(message);
    }


    //GroupAdminBasicSettings
    @TypeConverter
    public static GroupAdminBasicSettings stringToGroupAdminBasicsSetting(String groupAdminSetting) {
        return gson.fromJson(groupAdminSetting, GroupAdminBasicSettings.class);
    }

    @TypeConverter
    public static String groupAdminBasicSettingToString(GroupAdminBasicSettings groupAdminBasicSettings) {
        return gson.toJson(groupAdminBasicSettings);
    }

    //GroupWindowPeriodInfo
    @TypeConverter
    public static GroupWindowPeriodInfo stringToGroupWindowPeriodInfo(String groupWindowPeriodInfo) {
        return gson.fromJson(groupWindowPeriodInfo, GroupWindowPeriodInfo.class);
    }

    @TypeConverter
    public static String groupWindowPeriodInfoToString(GroupWindowPeriodInfo groupWindowPeriodInfo) {
        return gson.toJson(groupWindowPeriodInfo);
    }

    //EnrollLifeEventInfo
    @TypeConverter
    public static EnrollLifeEventInfo stringToEnrollmentLifeEventnfo(String enrollLifeEventInfo) {
        return gson.fromJson(enrollLifeEventInfo, EnrollLifeEventInfo.class);
    }

    @TypeConverter
    public static String enrollmentLifeEventInfoToString(EnrollLifeEventInfo enrollLifeEventInfo) {
        return gson.toJson(enrollLifeEventInfo);
    }

    //EnrollTopupOptions
    @TypeConverter
    public static EnrollTopupOptions stringToEnrollTopUpOption(String enrollTopupOptions) {
        return gson.fromJson(enrollTopupOptions, EnrollTopupOptions.class);
    }

    @TypeConverter
    public static String enrollTopUpOptionToString(EnrollTopupOptions enrollTopupOptions) {
        return gson.toJson(enrollTopupOptions);
    }

    //EnrollmentMiscInfo
    @TypeConverter
    public static EnrollmentMiscInfo stringToEnrollmentMiscInfo(String enrollmentMiscInfo) {
        return gson.fromJson(enrollmentMiscInfo, EnrollmentMiscInfo.class);
    }

    @TypeConverter
    public static String enrollmentMiscInfoToString(EnrollmentMiscInfo enrollmentMiscInfo) {
        return gson.toJson(enrollmentMiscInfo);
    }


    //GroupRelation list
    @TypeConverter
    public static List<GroupRelation> toList(String value) {
        Type listType = new TypeToken<List<GroupRelation>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<GroupRelation> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    // PremiumEmiInfo list
    @TypeConverter
    public static List<PremiumEmiInfo> toPremiumList(String value) {
        Type listType = new TypeToken<List<PremiumEmiInfo>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String preminumToString(List<PremiumEmiInfo> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
