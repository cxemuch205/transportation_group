package com.maker.transportationgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.maker.contenttools.GCMHelper;
import com.maker.contenttools.Interfaces.GCMHelperCallback;
import com.maker.contenttools.Interfaces.SignInUpCallbacks;
import com.maker.contenttools.Interfaces.SignInUpCallbacksAdapter;
import com.maker.contenttools.Tools;
import com.maker.transportationgroup.Fragments.SignInFragment;
import com.maker.transportationgroup.Fragments.SignUpFragment;
import io.fabric.sdk.android.Fabric;


public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        pb = (ProgressBar) findViewById(R.id.pb_load);

        GCMHelper.getInstance(this).initialUserDevice(new GCMHelperCallback() {
            @Override
            public void onInitSuccess() {

            }

            @Override
            public void onInitError() {

            }
        });

        if (!Tools.userIsRegistered(this)) {
            setupFragment(SignInFragment.TAG);
        } else {
            openHome();
        }
    }

    private SignInUpCallbacks fragmentSignInUpCallbacks = new SignInUpCallbacksAdapter() {
        @Override
        public void switchToSignIn() {
            super.switchToSignIn();
            setupFragment(SignInFragment.TAG);
        }

        @Override
        public void switchToSignUp() {
            super.switchToSignUp();
            setupFragment(SignUpFragment.TAG);
        }

        @Override
        public void enableProgressBar(final boolean enable) {
            super.enableProgressBar(enable);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (enable) {
                        pb.setVisibility(ProgressBar.VISIBLE);
                    } else {
                        pb.setVisibility(ProgressBar.GONE);
                    }
                }
            });
        }
    };

    private void openHome() {
        Intent openHome = new Intent(this, RoomsActivity.class);
        startActivity(openHome);
        finish();
    }

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
                    .commit();
            SignUpFragment.getInstance().setFragmentCallbacks(fragmentSignInUpCallbacks);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.checkPlayServices(this);
    }
}
