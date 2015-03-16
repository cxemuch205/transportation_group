package com.maker.contenttools.Models;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Daniil on 13.02.2015.
 */
public class Transportation implements Serializable {

    public String id;

    @SerializedName("start_point")
    public Point startPoint;

    @SerializedName("end_point")
    public Point endPoint;

    @SerializedName("max_people")
    public int maxPeople;

    @SerializedName("current_available_people")
    public int currentAvailablePeople;

    @SerializedName("max_bagade_weight")
    public float maxWeightBagage;

    @SerializedName("purposes")
    public String purposeTrip;

    @SerializedName("car_type_id")
    public String carTypeId;

    @SerializedName("room_id")
    public String roomId;

    @SerializedName("owner_id")
    public String ownerId;

    public String cost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public int getCurrentAvailablePeople() {
        return currentAvailablePeople;
    }

    public void setCurrentAvailablePeople(int currentAvailablePeople) {
        this.currentAvailablePeople = currentAvailablePeople;
    }

    public float getMaxWeightBagage() {
        return maxWeightBagage;
    }

    public void setMaxWeightBagage(float maxWeightBagage) {
        this.maxWeightBagage = maxWeightBagage;
    }

    public String getPurposeTrip() {
        return purposeTrip;
    }

    public void setPurposeTrip(String purposeTrip) {
        this.purposeTrip = purposeTrip;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public static Type getItemSerializable() {
        Type type = new TypeToken<Transportation>() {
        }.getType();
        return type;
    }

    public static Type getArraySerializable() {
        Type type = new TypeToken<ArrayList<Transportation>>() {
        }.getType();
        return type;
    }
}
