package com.maker.transportationgroup.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Interfaces.SignInUpCallbacks;
import com.maker.contenttools.Models.ApiResponse;
import com.maker.contenttools.Models.SignInUp;
import com.maker.contenttools.Tools;
import com.maker.transportationgroup.R;
import com.maker.transportationgroup.AddRoomsActivity;
import com.maker.transportationgroup.RoomsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    public static final String TAG = "SignInFragment";

    public SignInFragment() {}

    private SignInUpCallbacks fragmentCallbacks;

    public void setFragmentCallbacks(SignInUpCallbacks fragmentCallbacks) {
        this.fragmentCallbacks = fragmentCallbacks;
    }

    private static SignInFragment instance;

    public static SignInFragment getInstance() {
        if (instance == null) {
            instance = new SignInFragment();
        }
        return instance;
    }

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private AppCompatActivity activity;
    private Api api;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (AppCompatActivity) activity;
        if(this.activity.getSupportActionBar() != null)
            this.activity.getSupportActionBar().setTitle(R.string.logged);
        api = new Api(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnRegister = (Button) view.findViewById(R.id.btn_register);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnLogin.setOnClickListener(clickLoginListener);
        //FOR TEST
        btnLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openHome();
                return false;
            }
        });
        //$$$$$$$$$
        btnRegister.setOnClickListener(clickRegisterListener);
        etPassword.setOnKeyListener(keyPasswordListener);
    }

    private View.OnKeyListener keyPasswordListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                btnLogin.performClick();
                return true;
            }
            return false;
        }
    };

    private View.OnClickListener clickLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Tools.isCorrectEmail(etEmail.getText().toString())
                    && Tools.isCorrectPassword(etPassword.getText().toString())) {
                logging();
            } else {
                Tools.showToastCenter(activity, activity.getString(R.string.field_no_correct));
            }
        }
    };

    private View.OnClickListener clickRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fragmentCallbacks != null) {
                fragmentCallbacks.switchToSignUp();
            }
        }
    };

    private void logging() {
        if (fragmentCallbacks != null) {
            enableControls(false);
            fragmentCallbacks.enableProgressBar(true);
        }

        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        SignInUp signInUp = new SignInUp();
        signInUp.email = email;
        signInUp.password = password;

        api.requestSignIn(signInUp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.d(TAG, "RESPONSE: " + response);
                    ApiResponse apiResponse = ApiParser.getGson()
                            .fromJson(response,
                                    ApiResponse.getTypeToken());
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        openHome();
                        Tools.setUserIsRegistered(activity, apiResponse.isSuccess());
                    }
                }

                if (fragmentCallbacks != null) {
                    fragmentCallbacks.enableProgressBar(false);
                }
                enableControls(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String response = new String(error.networkResponse.data);
                    Log.e(TAG, "DATA: " + response);
                    ApiResponse apiResponse = ApiParser.getGson()
                            .fromJson(response,
                                    ApiResponse.getTypeToken());
                    if (apiResponse != null) {
                        String msg = String.valueOf(apiResponse.errors);
                        if (msg != null) {
                            if (msg.contains("[") && msg.contains("]")) {
                                msg = msg.replace("[", "").intern();
                                msg = msg.replace("]", "").intern();
                            }
                            Tools.showToastCenter(activity, msg);
                        }
                    }
                } catch (Exception e) {

                }
                enableControls(true);
                if (fragmentCallbacks != null) {
                    fragmentCallbacks.enableProgressBar(false);
                }
            }
        });
    }

    private void openHome() {
        Intent openHome = new Intent(activity, RoomsActivity.class);
        startActivity(openHome);
        activity.finish();
    }

    private void enableControls(boolean enable) {
        btnLogin.setEnabled(enable);
        btnRegister.setEnabled(enable);
        etEmail.setEnabled(enable);
        etPassword.setEnabled(enable);
    }
}
