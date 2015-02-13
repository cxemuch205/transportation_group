package com.maker.transportationgroup;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.ProgressBar;

import com.maker.contenttools.Interfaces.SignInUpCallbacks;
import com.maker.contenttools.Interfaces.SignInUpCallbacksAdapter;
import com.maker.transportationgroup.Fragments.SignInFragment;
import com.maker.transportationgroup.Fragments.SignUpFragment;


public class LoginActivity extends ActionBarActivity {

    public static final String TAG = "LoginActivity";

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pb = (ProgressBar) findViewById(R.id.pb_load);

        setupFragment(SignInFragment.TAG);
    }

    private SignInUpCallbacks fragmentSignInUpCallbacks = new SignInUpCallbacksAdapter() {
        @Override
        public void switchToSignIn() {
            super.switchToSignIn();
        }

        @Override
        public void switchToSignUp() {
            super.switchToSignUp();
        }

        @Override
        public void enableProgressBar(boolean enable) {
            super.enableProgressBar(enable);
        }
    };

    private void setupFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (tag.equals(SignInFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SignInFragment.getInstance(), tag)
                    .commit();
            SignInFragment.getInstance().setFragmentCallbacks(fragmentSignInUpCallbacks);
        } else if(tag.equals(SignUpFragment.TAG)){
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SignUpFragment.getInstance(), tag)
                    .addToBackStack(tag)
                    .commit();
            SignUpFragment.getInstance().setFragmentCallbacks(fragmentSignInUpCallbacks);
        }
    }
}
