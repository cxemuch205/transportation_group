package com.maker.contenttools.Models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Daniil on 30-Mar-15.
 */
public class ApiError implements Serializable {

    public ArrayList<String> full_messages;
    public String password;
}
