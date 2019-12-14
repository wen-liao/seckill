package com.ds.seckill.util;

import com.google.gson.JsonObject;

import java.math.BigDecimal;

public class JsonUtil {

    public static String getFromJsonObjectAsString(JsonObject jsonObject, String key){
        return jsonObject.get(key).getAsString();
    }

    public static Integer getFromJsonObjectAsInt(JsonObject jsonObject, String key){
        return jsonObject.get(key).getAsInt();
    }

    public static BigDecimal getFromJsonObjectAsDouble(JsonObject jsonObject, String key){
        return jsonObject.get(key).getAsBigDecimal();
    }
}
