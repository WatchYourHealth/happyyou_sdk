package com.wyh.happyyousdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class CustomAlertDialog extends AlertDialog {

    public CustomAlertDialog(@NonNull Context context) {
        super(context);
        if (getWindow() != null) {
            if ((context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0) {
                // not debugable mode
                /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE);*/
            }
        }

    }
}
