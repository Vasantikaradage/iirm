package com.indiainsure.android.MB360.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.ClaimsInfo;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.Result;

import java.lang.reflect.Type;
import java.util.List;

public class ClaimsInfoConverter {
    private static Gson gson = new Gson();


    @TypeConverter
    public static List<ClaimsInfo> toList(String value) {
        Type listType = new TypeToken<List<ClaimsInfo>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<ClaimsInfo> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static Result stringToResult(String result) {
        return gson.fromJson(result, Result.class);
    }

    @TypeConverter
    public static String resultToString(Result result) {
        return gson.toJson(result);
    }
}
