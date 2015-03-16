package com.maker.contenttools;

import android.content.Context;
import android.util.Patterns;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Daniil on 13.02.2015.
 */
public class Tools {
    public static boolean isCorrectEmail(String email) {
        if ((email != null && email.length() == 0) || email == null) {
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        return true;
    }

    public static boolean isCorrectPassword(String password) {
        if ((password != null && password.length() == 0) || password == null) {
            return false;
        }
        if (password.length() < 5) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }
        return true;
    }

    public static void showToastCenter(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
