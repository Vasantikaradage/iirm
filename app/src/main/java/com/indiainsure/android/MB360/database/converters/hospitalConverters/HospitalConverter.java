package com.indiainsure.android.MB360.database.converters.hospitalConverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass.Hospitals;

import java.lang.reflect.Type;
import java.util.List;

public class HospitalConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Hospitals> stringToHospitalInformation(String hospitalInformation) {
        Type listType = new TypeToken<List<Hospitals>>() {
        }.getType();
        return new Gson().fromJson(hospitalInformation, listType);
    }

    @TypeConverter
    public static String hospitalnformatiionToString(List<Hospitals> hospitalInformation) {

        return gson.toJson(hospitalInformation);
    }
}
