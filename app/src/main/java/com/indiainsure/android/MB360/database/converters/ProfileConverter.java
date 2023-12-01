package com.indiainsure.android.MB360.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indiainsure.android.MB360.insurance.profile.response.Message;
import com.indiainsure.android.MB360.insurance.profile.response.UserAddressDetails;
import com.indiainsure.android.MB360.insurance.profile.response.UserDocumentsDetail;
import com.indiainsure.android.MB360.insurance.profile.response.UserNomineeDetails;
import com.indiainsure.android.MB360.insurance.profile.response.UserOfficialDetails;
import com.indiainsure.android.MB360.insurance.profile.response.UserPersonalDetails;
import com.indiainsure.android.MB360.insurance.profile.response.UserSocialDetails;

import java.lang.reflect.Type;
import java.util.List;

public class ProfileConverter {

    private static Gson gson = new Gson();

    //Address Details
    @TypeConverter
    public static UserAddressDetails stringToUserAddressDetails(String userAddressDetail) {
        return gson.fromJson(userAddressDetail, UserAddressDetails.class);
    }

    @TypeConverter
    public static String userAddressDetailsToString(UserAddressDetails userAddressDetails) {

        return gson.toJson(userAddressDetails);
    }

    //Document Details
    @TypeConverter
    public static List<UserDocumentsDetail> toList(String value) {
        Type listType = new TypeToken<List<UserDocumentsDetail>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<UserDocumentsDetail> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    //Nominee Details
    @TypeConverter
    public static List<UserNomineeDetails> toNomineeList(String value) {
        Type listType = new TypeToken<List<UserNomineeDetails>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toNomineeString(List<UserNomineeDetails> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    //personal Details
    @TypeConverter
    public static UserPersonalDetails stringToUserPersonalDetails(String userPersonalDetail) {
        return gson.fromJson(userPersonalDetail, UserPersonalDetails.class);
    }

    @TypeConverter
    public static String userPersonalDetailToString(UserPersonalDetails userPersonalDetails) {
        return gson.toJson(userPersonalDetails);
    }

    //Social Derails
    @TypeConverter
    public static UserSocialDetails stringToUserSocialDerails(String userSocialDetails) {
        return gson.fromJson(userSocialDetails, UserSocialDetails.class);
    }

    @TypeConverter
    public static String userSocialDetailsToString(UserSocialDetails userSocialDetails) {
        return gson.toJson(userSocialDetails);
    }

    //Official Details
    @TypeConverter
    public static UserOfficialDetails stringToUserOfficialDetails(String userOfficialDetail) {
        return gson.fromJson(userOfficialDetail, UserOfficialDetails.class);
    }

    @TypeConverter
    public static String userOfficialDetailsToString(UserOfficialDetails userOfficialDetails) {
        return gson.toJson(userOfficialDetails);
    }

    @TypeConverter
    public static Message stringToMessage(String message) {
        return gson.fromJson(message, Message.class);
    }

    @TypeConverter
    public static String messageToString(Message message) {

        return gson.toJson(message);
    }
}