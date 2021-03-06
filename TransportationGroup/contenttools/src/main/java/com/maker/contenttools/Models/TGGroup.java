package com.maker.contenttools.Models;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Daniil on 30-Mar-15.
 */
public class TGGroup implements Serializable {

    public int id;
    public String name;
    public String key;
    public String description;
    public int numberOfMembers = 0;
    public int numberOfNews = 0;
    public String created_at;
    public String updated_at;

    public static Type getArrayTypeToken(){
        return new TypeToken<ArrayList<TGGroup>>(){}.getType();
    }
}
