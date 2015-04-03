package com.maker.transportationgroup.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    public static final String TAG = "SignUpFragment";

    public SignUpFragment() {}

    private SignInUpCallbacks fragmentCallbacks;

    public void setFragmentCallbacks(SignInUpCallbacks fragmentCallbacks) {
        this.fragmentCallbacks = fragmentCallbacks;
    }

    private static SignUpFragment instance;

    public static SignUpFragment getInstance() {
        if (instance == null) {
            instance = new SignUpFragment();
        }
        return instance;
    }

    private EditText etEmail, etPassword, etPasswordConfirmed;
    private Button btnOpenSignIn, btnRegisterOk;
    private ActionBarActivity activity;
    private Api api;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ActionBarActivity) activity;
        ((ActionBarActivity) activity).getSupportActionBar().setTitle(R.string.register);
        api = new Api(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        etPasswordConfirmed = (EditText) view.findViewById(R.id.et_password_confirmed);
        btnRegisterOk = (Button) view.findViewById(R.id.btn_ok_register);
        btnOpenSignIn = (Button) view.findViewById(R.id.btn_sign_in);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnOpenSignIn.setOnClickListener(clickOpenSignInListener);
        btnRegisterOk.setOnClickListener(clickRegisterCompleteListener);
    }

    private View.OnClickListener clickOpenSignInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (fragmentCallbacks != null) {
                fragmentCallbacks.switchToSignIn();
            }
        }
    };

    private View.OnClickListener clickRegisterCompleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Tools.isCorrectEmail(etEmail.getText().toString())
                    && Tools.isCorrectPassword(etPassword.getText().toString())
                    && Tools.isCorrectPassword(etPasswordConfirmed.getText().toString())
                    && etPassword.getText().toString().equals(etPasswordConfirmed.getText().toString())) {
                registered();
            } else {
                Tools.showToastCenter(activity, activity.getString(R.string.field_no_correct));
            }
        }
    };

    private void registered() {
        if (fragmentCallbacks != null) {
            enableControls(false);
            fragmentCallbacks.enableProgressBar(true);
        }

/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fragmentCallbacks != null) {
                    fragmentCallbacks.enableProgressBar(false);
                }
                enableControls(true);
            }
        }, 3000);*/
        SignInUp signInUp = new SignInUp();
        signInUp.email = etEmail.getText().toString();
        signInUp.password = etPassword.getText().toString();
        signInUp.passwordConfirmed = etPasswordConfirmed.getText().toString();

        api.requestSignUp(signInUp, new Response.Listener<String>() {
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
                Tools.showToastCenter(activity, error.getLocalizedMessage());
                if (fragmentCallbacks != null) {
                    fragmentCallbacks.enableProgressBar(false);
                }
                enableControls(true);
            }
        });
    }

    private void openHome() {
        Intent openHome = new Intent(activity, AddRoomsActivity.class);
        startActivity(openHome);
        activity.finish();
    }

    private void enableControls(boolean enable) {
        btnOpenSignIn.setEnabled(enable);
        btnRegisterOk.setEnabled(enable);
        etEmail.setEnabled(enable);
        etPassword.setEnabled(enable);
        etPasswordConfirmed.setEnabled(enable);
    }
}
