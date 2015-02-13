package com.maker.contenttools.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Daniil on 13.02.2015.
 */
public class ApiParser {

    public static Gson gson = null;

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return gson;
    }
}
