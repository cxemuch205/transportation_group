package com.maker.transportationgroup.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Interfaces.SignInUpCallbacks;
import com.maker.contenttools.Tools;
import com.maker.transportationgroup.R;
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
    private ActionBarActivity activity;
    private Api api;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (ActionBarActivity) activity;
        ((ActionBarActivity) activity).getSupportActionBar().setTitle(R.string.logged);
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
        btnRegister.setOnClickListener(clickRegisterListener);
    }

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fragmentCallbacks != null) {
                    fragmentCallbacks.enableProgressBar(false);
                }
                enableControls(true);
                if (email.equals("admin@gmail.com")
                        && password.equals("admin1")) {
                    openHome();
                }
            }
        }, 3000);
        //TODO: request to api for logging, after request disable progressbar

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
