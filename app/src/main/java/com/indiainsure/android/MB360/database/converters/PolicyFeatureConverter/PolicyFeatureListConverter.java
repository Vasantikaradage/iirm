package com.indiainsure.android.MB360.database.converters.PolicyFeatureConverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponse;

import java.lang.reflect.Type;
import java.util.List;


public class PolicyFeatureListConverter {


    @TypeConverter
    public static List<PolicyFeaturesResponse> tolist(String value) {
        Gson gson = new Gson();

        Type listType = new TypeToken<List<PolicyFeaturesResponse>>() {
        }.getType();

        return new Gson().fromJson(value, listType);


    }

    @TypeConverter
    public static String toString(List<PolicyFeaturesResponse> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}


