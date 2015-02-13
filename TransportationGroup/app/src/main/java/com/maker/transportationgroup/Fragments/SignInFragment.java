package com.maker.transportationgroup.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maker.contenttools.Interfaces.SignInUpCallbacks;
import com.maker.transportationgroup.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
