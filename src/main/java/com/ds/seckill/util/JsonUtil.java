package com.ds.seckill.util;

import com.google.gson.JsonObject;

public class JsonUtil {

    public static String getFromJsonObjectAsString(JsonObject jsonObject, String key){
        return jsonObject.get(key).getAsString();
    }

    public static Integer getFromJsonObjectAsInt(JsonObject jsonObject, String key){
        return jsonObject.get(key).getAsInt();
    }

    public static Double getFromJsonObjectAsDouble(JsonObject jsonObject, String key){
        return jsonObject.get(key).getAsDouble();
    }
}
