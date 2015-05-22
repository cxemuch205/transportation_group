package com.maker.contenttools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Interfaces.OnDialogListener;
import com.maker.contenttools.Models.TGGroup;
import com.maker.contenttools.Models.TGUser;

import java.util.ArrayList;

/**
 * Created by Daniil on 13.02.2015.
 */
public class Tools {

    public static final String TAG = "Tools";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

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

    public static boolean userIsRegistered(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(App.Prefs.NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(App.Prefs.USER_IS_REGISTERED, false);
    }

    public static void setUserIsRegistered(Context context, boolean userRegistered) {
        SharedPreferences prefs = context.getSharedPreferences(App.Prefs.NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(App.Prefs.USER_IS_REGISTERED, userRegistered).apply();
    }

    public static boolean checkPlayServices(Activity activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                activity.finish();
            }
            return false;
        }
        return true;
    }

    public static void setUserData(Context context, TGUser user) {
        SharedPreferences prefs = context.getSharedPreferences(App.Prefs.NAME, Context.MODE_PRIVATE);

        String userData = ApiParser.getGson().toJson(user, TGUser.getTypeToken());
        prefs.edit().putString(App.Prefs.USER_INFO, userData).apply();
    }

    public static TGUser getUserData(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(App.Prefs.NAME, Context.MODE_PRIVATE);

        String userData = prefs.getString(App.Prefs.USER_INFO, "");
        TGUser user = null;
        if (userData != null && !userData.isEmpty()) {
            user = ApiParser.getGson().fromJson(userData, TGUser.getTypeToken());
        }
        return user;
    }

    public static AlertDialog buildDialogAuthRoom(final Activity activity, final TGGroup room, final OnDialogListener dialogListener) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_auth_room, null);

        final TextView tvName = (TextView) dialogView.findViewById(R.id.tv_name);
        final EditText etPassword = (EditText) dialogView.findViewById(R.id.et_password);

        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogListener != null) {
                    if (etPassword.getText().length() > 0) {
                        Intent data = new Intent();
                        data.putExtra(App.Keys.PASSWORD, etPassword.getText().toString());
                        data.putExtra(App.Keys.ID, room.id);

                        dialogListener.onOK(data);
                        dialog.cancel();
                    } else {
                        showToastCenter(activity, activity.getString(R.string.empty_field));
                    }
                }
            }
        });

        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogListener != null) {
                    dialogListener.onCancel(null);
                }
                dialog.cancel();
            }
        });

        final AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                if (dialogListener != null) {
                    dialogListener.onShow(dialog);
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface d) {
                if (dialogListener != null) {
                    dialogListener.onDismiss(dialog);
                }
            }
        });

        if (dialogListener != null) {
            tvName.setText(String.format(activity.getString(R.string.title_dialog_auth_room),
                    room.name));
        }

        return dialog;
    }

    public static String convertArrayToString(ArrayList<String> fullMessages) {
        String result = "";
        for (String s : fullMessages) {
            result += (s+"\n");
        }
        return result;
    }

    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getDeviceId(Context context) {
        String deviceId = "000000000000000";

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return deviceId;
    }
}
