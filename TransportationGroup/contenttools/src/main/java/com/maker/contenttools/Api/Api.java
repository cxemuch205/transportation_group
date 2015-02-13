package com.maker.contenttools.Api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Daniil on 13.02.2015.
 */
public class Api {

    public static final String TAG = "Api";

    private Context context;
    private RequestQueue requestQueue;

    public Api(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }
}
