package com.example.recoope_mobile.dialogs;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.enums.InvalidFormatLogin;
import com.example.recoope_mobile.enums.InvalidFormatRegister;

public class DialogUtils {

    private static final String LOG_TAG = "Register";

    public static void showCustomDialog(InvalidFormatRegister invalidFormatRegister, Context context) {
        if (context == null) {
            Log.e(LOG_TAG, "Context is null");
            return;
        }

        Dialog customDialog = new Dialog(context);
        customDialog.setContentView(R.layout.dialog_register);

        Log.d(LOG_TAG, "Custom Dialog is being set up");

        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(true);

        Window window = customDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);

            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.TOP | Gravity.END;
            layoutParams.x = 20;
            layoutParams.y = 20;
            window.setAttributes(layoutParams);

            Log.d(LOG_TAG, "Window configuration set");
        } else {
            Log.e(LOG_TAG, "Window is null");
        }

        TextView txtTitulo = customDialog.findViewById(R.id.message);
        if (txtTitulo != null) {
            txtTitulo.setText(invalidFormatRegister.getType());
        } else {
            Log.e(LOG_TAG, "TextView with ID 'message' not found");
        }

        try {
            customDialog.show();
            Log.d(LOG_TAG, "Dialog shown");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error showing dialog: " + e.getMessage(), e);
        }
    }


    public static void showCustomDialog(String message, Context context) {
        if (context == null) {
            Log.e(LOG_TAG, "Context is null");
            return;
        }

        Dialog customDialog = new Dialog(context);
        customDialog.setContentView(R.layout.fragment_feed);

        Log.d(LOG_TAG, "Custom Dialog is being set up");

        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(true);

        Window window = customDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);

            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.TOP | Gravity.END;
            layoutParams.x = 20;
            layoutParams.y = 20;
            window.setAttributes(layoutParams);

            Log.d(LOG_TAG, "Window configuration set");
        } else {
            Log.e(LOG_TAG, "Window is null");
        }

        TextView txtTitulo = customDialog.findViewById(R.id.message);
        if (txtTitulo != null) {
            txtTitulo.setText(message);
        } else {
            Log.e(LOG_TAG, "TextView with ID 'message' not found");
        }

        try {
            customDialog.show();
            Log.d(LOG_TAG, "Dialog shown");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error showing dialog: " + e.getMessage(), e);
        }
    }



}
