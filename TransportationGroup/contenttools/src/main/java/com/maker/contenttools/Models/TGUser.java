package com.maker.contenttools.Models;

import com.buddy.sdk.models.User;
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

    public static TGUser convertUser(User user) {
        TGUser tgUser = new TGUser();

        tgUser.email = user.userName;
        tgUser.userId = user.id;

        return tgUser;
    }
}
