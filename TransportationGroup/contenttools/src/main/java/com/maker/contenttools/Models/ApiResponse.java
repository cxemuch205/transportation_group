package com.maker.contenttools.Models;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by Daniil on 30-Mar-15.
 */
public class ApiResponse implements Serializable {

    public static final String SUCCESS = "success";

    public String status;
    public Object data;
    public Object errors;
    public boolean success;

    public boolean isSuccess() {
        return (status != null && status.equals(SUCCESS)) || errors == null || success;
    }

    public static Type getTypeToken() {
        return new TypeToken<ApiResponse>() {
        }.getType();
    }
}
