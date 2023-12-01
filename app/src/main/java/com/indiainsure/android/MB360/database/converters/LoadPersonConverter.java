package com.indiainsure.android.MB360.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.ClaimBeneficiary;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.Result;

import java.lang.reflect.Type;
import java.util.List;

public class LoadPersonConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<ClaimBeneficiary> toList(String value) {
        Type listType = new TypeToken<List<ClaimBeneficiary>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<ClaimBeneficiary> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static Result stringToResult(String message) {
        return gson.fromJson(message, Result.class);
    }

    @TypeConverter
    public static String resultToString(Result result) {

        return gson.toJson(result);
    }
}
