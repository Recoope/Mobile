package com.example.recoope_mobile.dialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.recoope_mobile.R;
import com.example.recoope_mobile.activity.fragments.FeedFragment;
import com.example.recoope_mobile.enums.InvalidFormatLogin;
import com.example.recoope_mobile.enums.InvalidFormatRegister;
import com.example.recoope_mobile.utils.ButtonToggleManager;

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


    public static void showCustomDialog(InvalidFormatLogin invalidFormatLogin, Context context) {
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
            txtTitulo.setText(invalidFormatLogin.getType());
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
    public static void showFilterDialog(FeedFragment context, FilterDialogCallback filterDialogCallback) {
        if (context.getActivity() == null) {
            Log.e("CardFeed", "Context is null");
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context.getActivity());
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);

        Dialog customDialog = new Dialog(context.getActivity());
        customDialog.setContentView(dialogView);
        customDialog.setCancelable(true);
        customDialog.setCanceledOnTouchOutside(true);

        Window window = customDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);

            // Ajuste aqui para usar MATCH_PARENT na largura
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM; // Manter a gravidade se ainda quiser que apareça na parte inferior
            window.setAttributes(layoutParams);

            Log.d("CardFeed", "Window configuration set");
        } else {
            Log.e("CardFeed", "Window is null");
        }

        Log.d("CardFeed", "Custom Dialog is being set up");

        Button applyFilterButton = dialogView.findViewById(R.id.btFilterMaterialD);
        Button btGlassFilter = dialogView.findViewById(R.id.btGlassFilterD);
        Button btMetalFilter = dialogView.findViewById(R.id.btMetalFilterD);
        Button btPlasticFilter = dialogView.findViewById(R.id.btPlasticFilterD);
        Button btPaperFilter = dialogView.findViewById(R.id.btPaperFilterD);

        ButtonToggleManager toggleManager = new ButtonToggleManager(
                context.getContext(),
                R.color.recoope_primary_color,
                R.color.background_color
        );

        btPlasticFilter.setOnClickListener(v -> toggleManager.toggleButton(btPlasticFilter));
        btMetalFilter.setOnClickListener(v -> toggleManager.toggleButton(btMetalFilter));
        btGlassFilter.setOnClickListener(v -> toggleManager.toggleButton(btGlassFilter));
        btPaperFilter.setOnClickListener(v -> toggleManager.toggleButton(btPaperFilter));

        applyFilterButton.setOnClickListener(v -> {
            customDialog.dismiss();

            String selectedFilter = "";
            if (btPlasticFilter.isSelected()) {
                selectedFilter = "plastico";
            } else if (btMetalFilter.isSelected()) {
                selectedFilter = "metal";
            } else if (btGlassFilter.isSelected()) {
                selectedFilter = "vidro";
            } else if (btPaperFilter.isSelected()) {
                selectedFilter = "papel";
            }

            filterDialogCallback.onFilterSelected(selectedFilter);
        });

        customDialog.show();
    }


}
