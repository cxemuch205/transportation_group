package com.maker.contenttools.Api;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Models.SignInUp;
import com.maker.contenttools.Models.TGUser;
import com.maker.contenttools.PreferencesManager;
import com.maker.contenttools.Tools;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniil on 13.02.2015.
 */
public class Api {

    public static final String TAG = "Api";

    public interface Methods {

        String[] SIGN_UP = {"auth"};
        String[] SIGN_IN = {"auth", "sign_in"};
        String[] SIGN_OUT = {"auth", "sign_out"};
        String[] GROUPS = {"api", "groups"};
        String[] JOIN_TO_GROUP = {"api", "groups", ":id", "join"};
        String[] MY_GROUPS = {"api", "groups", "own"};
        String[] SEARCH_GROUPS = {"api", "groups", "search"};
        String[] ALL_TRIPS = {"api", "groups", ":id", "trips"};
    }
    public interface Fields {

        String EMAIL = "email";
        String PASSWORD = "password";
        String PASSWORD_CONFIRMATION = "password_confirmation";
        String ACCESS_TOKEN = "access-token";
        String CLIENT = "client";
        String UID = "uid";
        String QUERY = "q";
        String GROUP_NAME = "group[name]";
        String GROUP_PASSWORD = "group[password]";
        String GROUP_PASSWORD_CONFIRMED = "group[password_confirmation]";
        String DEVICE_DCG_TOKEN = "deviceGCMToken";
    }
    public interface HeaderKeys {

        String UID = "uid";
        String CLIENT = "client";
        String ACCESS_TOKEN = "access-token";
    }
    private Context context;

    private RequestQueue requestQueue;
    public Api(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public static String buildUrl(String[] method, HashMap<String, String> params) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(App.Api.SCHEME)
                .authority(App.Api.AUTHORITY);
        if (method != null && method.length > 0) {
            for (int i = 0; i < method.length; i++) {
                builder.appendPath(method[i]);
            }
        }
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                builder.appendQueryParameter(key, params.get(key));
            }
        }

        return builder.build().toString().replace("%3A", ":");
    }

    public void requestSignUp(final SignInUp data, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = buildUrl(Methods.SIGN_UP, null);
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDeviceGCMToken(headers);
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers != null && !response.headers.isEmpty()) {
                    TGUser user = new TGUser();
                    user.email = data.email;
                    user.userId = response.headers.get(HeaderKeys.UID);
                    user.accessToken = response.headers.get(HeaderKeys.ACCESS_TOKEN);
                    user.client = response.headers.get(HeaderKeys.CLIENT);
                    Tools.setUserData(context, user);
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                if (data != null) {
                    if (data.email != null) {
                        params.put(Fields.EMAIL, data.email);
                    }
                    if (data.password != null) {
                        params.put(Fields.PASSWORD, data.password);
                    }
                    if (data.passwordConfirmed != null) {
                        params.put(Fields.PASSWORD_CONFIRMATION, data.passwordConfirmed);
                    }
                }
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 3, 1f));
        requestQueue.add(request);
    }

    public void requestSignIn(final SignInUp data, final Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = buildUrl(Methods.SIGN_IN, null);
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDeviceGCMToken(headers);
                return headers;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers != null && !response.headers.isEmpty()) {
                    TGUser user = new TGUser();
                    user.email = data.email;
                    user.userId = response.headers.get(HeaderKeys.UID);
                    user.accessToken = response.headers.get(HeaderKeys.ACCESS_TOKEN);
                    user.client = response.headers.get(HeaderKeys.CLIENT);
                    Tools.setUserData(context, user);
                }
                return super.parseNetworkResponse(response);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                if (data != null) {
                    if (data.email != null) {
                        params.put(Fields.EMAIL, data.email);
                    }
                    if (data.password != null) {
                        params.put(Fields.PASSWORD, data.password);
                    }
                }
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 3, 1f));
        requestQueue.add(request);
    }

    public void requestSignOut(Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = buildUrl(Methods.SIGN_OUT, null);
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.DELETE, url, responseListener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDefaultHeaderForLoggedUser(headers);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1f));
        requestQueue.add(request);
    }

    private void putDefaultHeaderForLoggedUser(HashMap<String, String> headers) {
        TGUser user = Tools.getUserData(context);
        if (user != null) {
            if (user.accessToken != null) {
                headers.put(Fields.ACCESS_TOKEN, user.accessToken);
            }
            if (user.accessToken != null) {
                headers.put(Fields.CLIENT, user.client);
            }
            if (user.userId != null) {
                headers.put(Fields.UID, user.userId);
            }
        }
    }

    private void putDeviceGCMToken(HashMap<String, String> headers) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        if (!preferencesManager.getGCMRegID().isEmpty()) {
            headers.put(Fields.DEVICE_DCG_TOKEN, preferencesManager.getGCMRegID());
        }
    }

    public void requestGetGroups(Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = buildUrl(Methods.GROUPS, null);
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDefaultHeaderForLoggedUser(headers);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1f));
        requestQueue.add(request);
    }

    public void requestGetMyGroups(Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = buildUrl(Methods.MY_GROUPS, null);
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDefaultHeaderForLoggedUser(headers);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1f));
        requestQueue.add(request);
    }

    public void requestSearchGroups(String text, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put(Fields.QUERY, text);
        String url = buildUrl(Methods.SEARCH_GROUPS, data);
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDefaultHeaderForLoggedUser(headers);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 2, 1f));
        requestQueue.add(request);
    }

    public void requestAddGroup(Intent data, Response.Listener<String> successListener, Response.ErrorListener errorListener) {
        final String password = data.getStringExtra(App.Keys.PASSWORD);
        int idRoom = data.getIntExtra(App.Keys.ID, -1);

        String url = buildUrl(Methods.JOIN_TO_GROUP, null);
        if (idRoom > -1) {
            url = url.replace(":id", String.valueOf(idRoom));
        } else {
            errorListener.onErrorResponse(null);
            return;
        }
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.POST, url, successListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDefaultHeaderForLoggedUser(headers);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                if (password != null) {
                    params.put(Fields.PASSWORD, password);
                }
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1f));
        requestQueue.add(request);
    }

    public void requestCreateGroup(Intent data, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        final String name = data.getStringExtra(App.Keys.NAME);
        final String password = data.getStringExtra(App.Keys.PASSWORD);
        final String passwordConfirmed = data.getStringExtra(App.Keys.PASSWORD_CONFIGURED);

        String url = buildUrl(Methods.GROUPS, null);
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDefaultHeaderForLoggedUser(headers);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                if (name != null) {
                    params.put(Fields.GROUP_NAME, name);
                }
                if (password != null) {
                    params.put(Fields.GROUP_PASSWORD, password);
                }
                if (passwordConfirmed != null) {
                    params.put(Fields.GROUP_PASSWORD_CONFIRMED, passwordConfirmed);
                }
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1f));
        requestQueue.add(request);
    }

    public void requestGetTripsByGroupId(final String groupId, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        String url = buildUrl(Methods.ALL_TRIPS, null);
        if (groupId != null && !groupId.isEmpty()) {
            url = url.replace(":id", groupId).intern();
        }
        Log.i(TAG, url);

        StringRequest request = new StringRequest(Request.Method.GET, url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                putDefaultHeaderForLoggedUser(headers);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(5000, 1, 1f));
        requestQueue.add(request);
    }
}
