package com.indiainsure.android.MB360.database.converters.MyClaimsConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimInformation;

public class MyClaimsDetailsConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static ClaimInformation stringToClaimInformation(String claimInformation) {
        return gson.fromJson(claimInformation, ClaimInformation.class);
    }

    @TypeConverter
    public static String claimInformationToString(ClaimInformation claimInformation) {

        return gson.toJson(claimInformation);
    }
}
