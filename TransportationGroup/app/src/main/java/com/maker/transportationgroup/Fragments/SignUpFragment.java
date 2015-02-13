package com.maker.transportationgroup.Fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maker.contenttools.Interfaces.SignInUpCallbacks;
import com.maker.transportationgroup.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
