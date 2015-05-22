package com.maker.contenttools.Models;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by den4ik on 5/22/15.
 */
public class RegDevice implements Serializable {

    public String serviceRoot;
    public String accessToken;
    public String accessTokenExpires;

    public static Type getTypeToken() {
        return new TypeToken<RegDevice>() {
        }.getType();
    }
}
