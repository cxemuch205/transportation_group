package com.maker.contenttools.Models;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by Daniil on 30-Mar-15.
 */
public class TGUser implements Serializable {

    public String email;
    public String accessToken;
    public String client;
    public String userId;

    public static Type getTypeToken() {
        return new TypeToken<TGUser>(){}.getType();
    }
}
