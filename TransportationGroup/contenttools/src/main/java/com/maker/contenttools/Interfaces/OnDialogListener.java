package com.maker.contenttools.Interfaces;

import android.app.Dialog;
import android.content.Intent;

/**
 * Created by Daniil on 10-Apr-15.
 */
public interface OnDialogListener {

    void onOK(Intent data);

    void onCancel(Intent data);

    void onShow(Dialog dialog);

    void onDismiss(Dialog dialog);
}
