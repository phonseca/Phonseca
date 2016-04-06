package com.phonseca.tanquecheio.helpers;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pedro on 11/02/16.
 */
public class JsonHelper {
    public static <T> List<T> getList(String json, Class<T[]> tClass) {
        return Arrays.asList((new Gson()).fromJson(json, tClass));
    }

    public static <T> T getObject(String json, Class<T> tClass) {
        return (new Gson()).fromJson(json, tClass);
    }
}
