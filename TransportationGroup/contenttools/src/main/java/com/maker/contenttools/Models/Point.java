package com.maker.contenttools.Models;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Daniil on 13.02.2015.
 */
public class Point implements Serializable {

    public String location;

    @SerializedName("date_trip")
    public String dateTrip;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTrip() {
        return dateTrip;
    }

    public void setDateTrip(String dateTrip) {
        this.dateTrip = dateTrip;
    }

    public static Type getItemSerializable() {
        Type type = new TypeToken<Point>() {
        }.getType();
        return type;
    }

    public static Type getArraySerializable() {
        Type type = new TypeToken<ArrayList<Point>>() {
        }.getType();
        return type;
    }
}
