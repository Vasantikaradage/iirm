package com.indiainsure.android.MB360.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.indiainsure.android.MB360.insurance.queries.responseclass.ListOfQuery;
import com.indiainsure.android.MB360.insurance.queries.responseclass.Message;

import java.lang.reflect.Type;
import java.util.List;

public class ListOfQueriesConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static List<ListOfQuery> toList(String value) {
        Type listType = new TypeToken<List<ListOfQuery>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String toString(List<ListOfQuery> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
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
